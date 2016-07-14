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
 * @创建时间: 2016-04-03 15:41 
 * @更新的时间:
 * @更新的描述: TODO
 *
 */
public class SettingFragment extends BaseFragment {
    @Override
    protected View initView() {
        TextView textView = new TextView(mContext);
        textView.setText("页面5");
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLUE);
        return textView;
    }
}
