package com.lle.mydemo.fragment;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.lle.mydemo.base.BaseFragment;

public class TabFragment extends BaseFragment {

    private String mText;

    public void setText(String text) {
        mText = text;
    }

    @Override
    protected View initView() {
        TextView textView = new TextView(mContext);
        textView.setText(mText);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLUE);
        return textView;
    }

}
