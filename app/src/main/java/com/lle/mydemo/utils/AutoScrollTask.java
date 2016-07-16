package com.lle.mydemo.utils;

import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;

public class AutoScrollTask implements Runnable{
    private boolean flag;
    private ViewPager mViewPager;

    public AutoScrollTask(ViewPager viewPager){
        mViewPager = viewPager;
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        stop();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        start();
                        break;
                }
                return false;
            }
        });
    }

    public void start(){
        if (!flag) {
            UiUtils.removeCallbacks(this);
            flag = true;
            UiUtils.postDelayed(this, 3000);
        }
    }

    public void stop(){
        if(flag) {
            flag = false;
            UiUtils.removeCallbacks(this);
        }
    }

    @Override
    public void run() {
        if (flag) {
            UiUtils.removeCallbacks(this);
            int currentItem = mViewPager.getCurrentItem();
            mViewPager.setCurrentItem(++currentItem);
            UiUtils.postDelayed(this, 3000);
        }
    }
}
