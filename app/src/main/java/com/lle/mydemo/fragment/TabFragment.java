package com.lle.mydemo.fragment;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.lle.mydemo.base.BaseFragment;

/**
 * @项目名称: MyDemo
 * @包名: com.lle.mydemo.fragment
 * @作者: 吴永乐
 *
 * @描述: TODO
 *
 * @创建时间: 2016-04-03 15:37 
 * @更新的时间:
 * @更新的描述: TODO
 *
 */
public class TabFragment extends BaseFragment {
    private TextView mTextView;

    public void setText(String text) {
        mTextView.setText(text);
    }

    @Override
    protected View initView() {
        mTextView = new TextView(mContext);
        mTextView.setTextSize(20);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextColor(Color.BLUE);
        return mTextView;
    }

}
