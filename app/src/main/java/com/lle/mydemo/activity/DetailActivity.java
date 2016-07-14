package com.lle.mydemo.activity;


import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;

import com.lle.mydemo.R;
import com.lle.mydemo.base.BaseActivity;

/**
 * @项目名称: MyDemo
 * @包名: com.lle.mydemo.activity
 * @作者: 吴永乐
 *
 * @描述: TODO
 *
 * @创建时间: 2016-07-12 14:25 
 * @更新的时间:
 * @更新的描述: TODO
 *
 */
public class DetailActivity extends BaseActivity {
    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //设置工具栏标题
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.ctl_detail);
        collapsingToolbar.setTitle("详情页面");
        collapsingToolbar.setExpandedTitleColor(Color.GREEN);
        //折叠
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);

//        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_detail);
/*        final NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.ns_detail);
        nestedScrollView.post(new Runnable() {
            @Override
            public void run() {
                nestedScrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
            }
        });*/
    }
}
