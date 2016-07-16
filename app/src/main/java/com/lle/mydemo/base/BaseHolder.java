package com.lle.mydemo.base;

import android.view.View;

public abstract class BaseHolder<Data> {
    protected View mContentView;
    protected Data mData;
    
    public BaseHolder(){
        mContentView = initView();
        mContentView.setTag(this);
    }

    protected abstract View initView();

    protected abstract void refreshView(Data data);

    public View getContentView() {
        return mContentView;
    }

    public void setData(Data data) {
        mData = data;
        refreshView(data);
    }

}
