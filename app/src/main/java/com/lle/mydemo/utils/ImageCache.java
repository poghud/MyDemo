package com.lle.mydemo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
//        int imgID = (Integer) mList.get(position);
        SoftReference<Bitmap> softReference = mImgCache.get(imgID);
        if (softReference == null) {
            Bitmap bitmap = BitmapFactory.decodeResource(UiUtils.getResource(), imgID);
            softReference = new SoftReference<>(bitmap);
            mImgCache.put(imgID, softReference);
            imageView.setImageBitmap(softReference.get());
        }else{
            Bitmap bitmap = softReference.get();
            if(bitmap != null){
                imageView.setImageBitmap(bitmap);
            }else {
                softReference = new SoftReference<>(BitmapFactory.decodeResource(UiUtils.getResource(), imgID));
                imageView.setImageBitmap(softReference.get());
            }
        }
    }
}
