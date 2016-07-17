package com.lle.mydemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageHelper {
    private static LruCache<String, Bitmap> mCaches;
    //线程池
    public static ExecutorService mThreadPool = Executors.newFixedThreadPool(3);
    //存储控件和对应的future
//    private static Map<ImageView, Future> mFutureMap = new HashMap<>();
    //存储控件和对应的url标记
    private static Map<ImageView, String> mFlags = new HashMap<>();

    private final Context mContext;
    private File mFile;

    private Handler mHandler;

    public ImageHelper(Context context){
        mContext = context;

        if(mCaches == null) {
            //获取当前可用内存
            int maxSize = (int) (Runtime.getRuntime().freeMemory()/4);
            mCaches = new LruCache<String, Bitmap>(maxSize){
                // 用来记录添加进来的数据的内存大小
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount();
                }
            };
        }

//        Looper.prepare();
//        Looper.loop();
        mHandler = new Handler(mContext.getMainLooper());


    }

    public void display(ImageView iv, String url){
        //1.内存
        Bitmap bitmap = mCaches.get(url);
        if(bitmap != null){
            iv.setImageBitmap(bitmap);
            return;
        }

        //2.本地存储 sdcard/Android/data/包名：缓存目录
        //创建缓存存储目录
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            mFile = new File(Environment.getExternalStorageDirectory(), "/Android/data/" + mContext.getPackageName() + "/icon");
            if(!mFile.exists()){
                mFile.mkdirs();
            }
        }else{
            mFile = new File(mContext.getCacheDir(), "/icon");
            if(!mFile.exists()){
                mFile.mkdirs();
            }
        }
        //查找本地缓存是否有图片
        bitmap = getBitmapFromLocal(url);
        if(bitmap != null){
            iv.setImageBitmap(bitmap);
            return;
        }

        //3.网络加载
        loadFromNet(iv, url);
    }

    private void loadFromNet(ImageView iv, String urlPath) {
/*        //方案一
        Future f = mFutureMap.get(iv);
        if(f != null && !f.isDone() && !f.isCancelled()){
            //如果控件还有对应的任务在运行,干掉它
            f.cancel(true);//不一定执行,不靠谱
            f = null;
       }
        Future<?> future = mThreadPool.submit(new LoadImageTask(iv, urlPath));
        //将控件和任务关联起来
        mFutureMap.put(iv, future);*/

        //方案二
        mThreadPool.submit(new LoadImageTask(iv, urlPath));
        //存储标记
        mFlags.put(iv, urlPath);
    }

    private class LoadImageTask implements Runnable{

        private ImageView mIv;
        private String mUrlPath;

        public LoadImageTask(ImageView iv, String urlPath){
            mIv = iv;
            mUrlPath = urlPath;
        }

        @Override
        public void run() {
            //联网获取数据
            HttpURLConnection conn = null;
            try {
                URL url = new URL(mUrlPath);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                if(conn.getResponseCode() == 200){
                    InputStream is = conn.getInputStream();

                    // stream ---> bitmap
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    // 1. 存储到本地// bitmap--->file
                    write2Local(mUrlPath, bitmap);
                    // 2. 存储到内存
                    mCaches.put(mUrlPath, bitmap);
                    // 3.展示数据--->子线程中执行
                    // iv.setImageBitmap(bitmap);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            String newUrl = mFlags.get(mIv);
                            if(mUrlPath.equals(newUrl)){
                                //如果是最新的url则更新ui
                                display(mIv, mUrlPath);
                            }
                        }
                    });
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(conn != null) {
                    conn.disconnect();
                }
            }
        }
    }

    private void write2Local(String url, Bitmap bitmap) {
        // bitmap--->file
            FileOutputStream fos = null;
        try {
            String name = MD5Encoder.encode(url);
            fos = new FileOutputStream(new File(mContext.getCacheDir(), name));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(fos != null){
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap getBitmapFromLocal(String url) {
        try {
            String name = MD5Encoder.encode(url);
            File file = new File(mFile, name);
            if(!file.exists()){
                return null;
            }else{
                // decode---> 是否压缩的判断
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                if(bitmap != null){
                    //存到内存中
                    mCaches.put(url, bitmap);
                    return bitmap;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
