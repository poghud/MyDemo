package com.lle.mydemo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.lle.mydemo.MyApplication;
import com.lle.mydemo.base.BaseActivity;


public class UiUtils {
	/**
	 * 获取到字符数组
	 * @param tabNames  字符数组的id
	 */
	public static String[] getStringArray(int tabNames) {
		return getResource().getStringArray(tabNames);
	}

	public static Resources getResource() {
		return MyApplication.getApplication().getResources();
	}

	/** dip转换px */
	public static int dip2px(int dip) {
		final float scale = getResource().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	/** pxz转换dip */

	public static int px2dip(int px) {
		final float scale = getResource().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	/**
	 * 让任务在主线程中运行
	 */
	public static void runOnMainThread(Runnable runnable){
		if(MyApplication.getMainTid() == android.os.Process.myTid()){
			runnable.run();
		}else{
			MyApplication.getHandler().post(runnable);
		}
	}

	public static Context getContext() {
		return MyApplication.getApplication();
	}

	public static Drawable getDrawable(int res){
		return getResource().getDrawable(res);
	}

	public static View inflate(int res){
		return View.inflate(getContext(), res, null);
	}


	public static void postDelayed(Runnable runnable, long delay) {
		MyApplication.getHandler().postDelayed(runnable, delay);
	}

	public static void post(Runnable runnable){
		MyApplication.getHandler().post(runnable);
	}

	public static void removeCallbacks(Runnable runnable){
		MyApplication.getHandler().removeCallbacks(runnable);
	}

	public static void startActivity(Intent intent) {
		//当前没有可见的Activity
		if(BaseActivity.mActivity == null){
			//是用广播,服务打开的activity,需要在新的任务栈中启动
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			getContext().startActivity(intent);
		}else{
			BaseActivity.mActivity.startActivity(intent);
		}
	}
}
