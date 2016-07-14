package com.lle.mydemo.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {
    // 管理运行的所有的activity
    public final static List<BaseActivity> mActivities = new LinkedList<>();
    //存储当前可见的activity
    public static BaseActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        synchronized (mActivities) {
            mActivities.add(this);
        }
        initView();
        initData();
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        synchronized (mActivities) {
            mActivities.remove(this);
        }
    }

    @Override
    protected void onResume() {
        mActivity = this;
        super.onResume();
    }

    @Override
    protected void onPause() {
        mActivity = null;
        super.onPause();
    }

    public void killAll() {
        // 复制了一份mActivities 集合
        List<BaseActivity> copy;
        synchronized (mActivities) {
            copy = new LinkedList<>(mActivities);
        }
        for (BaseActivity activity : copy) {
            activity.finish();
        }
        // 杀死当前的进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    protected void setListener() {
    }

    protected void initView() {
    }

    protected void initData() {
    }
}
