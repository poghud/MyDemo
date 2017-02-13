package com.lle.mydemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.WindowManager;
import android.widget.ImageView;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class ImageCache {

    private Map<Integer, SoftReference<Bitmap>> mImgCache = new HashMap<>();
    private static ImageCache mImageCache = new ImageCache();

    private ImageCache(){}

    public static ImageCache getImageCache(){
        return mImageCache;
    }

    public void setImage(int imgID, ImageView imageView){
        //获得屏幕高度和宽度
        WindowManager windowManager = (WindowManager) UiUtils.getContext().getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        int width = size.x;
        int height = size.y;

        SoftReference<Bitmap> softReference = mImgCache.get(imgID);
        if (softReference == null) {
//            Bitmap bitmap = BitmapFactory.decodeResource(UiUtils.getResource(), imgID);
            Bitmap bitmap = scaleBitmap(imgID, height, width);
            softReference = new SoftReference<>(bitmap);
            mImgCache.put(imgID, softReference);
            imageView.setImageBitmap(softReference.get());
        }else{
            Bitmap bitmap = softReference.get();
            if(bitmap != null){
                imageView.setImageBitmap(bitmap);
            }else {
                softReference = new SoftReference<>(scaleBitmap(imgID, height, width));
                imageView.setImageBitmap(softReference.get());
            }
        }
    }

    /**
     * 图片缩放
     * @param imgID 图片的资源ID
     * @param pixelH 想要缩放的高
     * @param pixelW 想要缩放的宽
     * @return 缩放后的图片
     */
    private Bitmap scaleBitmap(int imgID, float pixelH, float pixelW) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        // Get bitmap info, but notice that bitmap is null now
//        Bitmap bitmap = BitmapFactory.decodeFile(imgPath,newOpts);
        Bitmap bitmap = BitmapFactory.decodeResource(UiUtils.getResource(), imgID, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 想要缩放的目标尺寸
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        // 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了
//        bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        bitmap = BitmapFactory.decodeResource(UiUtils.getResource(), imgID, newOpts);
        return bitmap;
    }
}
