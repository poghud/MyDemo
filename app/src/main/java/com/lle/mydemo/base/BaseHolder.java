package com.lle.mydemo.base;

import android.view.View;

/**
 * @项目名称: GooglePlay
 * @包名: com.itheima.googleplay.holder
 * @类名:
 * @作者: lle
 *
 * @描述: TODO
 *
 * @当前版本号: $Rev:
 * @更新人: $Author:
 * @更新的时间: $Date: 2015/12/4 0004 20:20
 * @更新的描述: TODO
 *
 */
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
