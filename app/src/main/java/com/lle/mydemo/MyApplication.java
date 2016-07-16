package com.lle.mydemo;

import android.app.Application;
import android.os.Handler;

public class MyApplication extends Application implements Thread.UncaughtExceptionHandler {

    private static Application sApplication;
    private static int sMainTid;
    private static Handler sHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        sApplication = this;
        sMainTid = android.os.Process.myTid();
        sHandler = new Handler();
    }

    public static Application getApplication() {
        return sApplication;
    }

    public static int getMainTid() {
        return sMainTid;
    }

    public static Handler getHandler() {
        return sHandler;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
    }
}
