package com.lle.mydemo.base;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lle.mydemo.R;
import com.lle.mydemo.utils.UiUtils;

public class MoreHolder extends BaseHolder<Integer> {
    public static final int HAS_MORE = 0;//还有更多
    public static final int NO_MORE = 1;//没有数据了
    public static final int LOAD_ERROR = 2;//加载失败
    private RelativeLayout mRl_loading;
    private RelativeLayout mRl_error;
    private final BaseAdapter mAdapter;

    public MoreHolder(BaseAdapter adapter) {
        mAdapter = adapter;
    }


    @Override
    protected View initView() {
        FrameLayout frameLayout = (FrameLayout) UiUtils.inflate(R.layout.moreholder);
        mRl_loading = (RelativeLayout) frameLayout.findViewById(R.id.rl_loading_moreholder);
        mRl_error = (RelativeLayout) frameLayout.findViewById(R.id.rl_error_moreholder);
        return frameLayout;
    }

    @Override
    protected void refreshView(Integer integer) {
        mRl_loading.setVisibility(integer == HAS_MORE ? View.VISIBLE : View.GONE);
        mRl_error.setVisibility(integer == LOAD_ERROR ? View.VISIBLE : View.GONE);
        if (integer == NO_MORE) {
            Toast.makeText(UiUtils.getContext(), "没有更多数据了..", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View getContentView() {
        if (mAdapter.hasMore() && mData == HAS_MORE) {
            //加载数据
            mAdapter.loadMore();
        } else {
            mContentView.setVisibility(View.GONE);
        }
        return mContentView;
    }
}
