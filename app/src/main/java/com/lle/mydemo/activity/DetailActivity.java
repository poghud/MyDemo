package com.lle.mydemo.activity;


import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;

import com.lle.mydemo.R;
import com.lle.mydemo.base.BaseActivity;

public class DetailActivity extends BaseActivity {

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //设置工具栏标题
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.ctl_detail);
        assert collapsingToolbar != null;
        collapsingToolbar.setTitle("详情页面");
        collapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
        //折叠
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
    }
}
