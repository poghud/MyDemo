package com.lle.mydemo.base;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lle.mydemo.utils.UiUtils;


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
 * @更新的时间: $Date: 2015/12/5 0005 19:18
 * @更新的描述: TODO
 *
 */
public class MoreHolder extends BaseHolder<Integer> {
    public static final int HAS_MORE = 0;//还有更多
    public static final int NO_MORE = 1;//没有数据了
    public static final int LOAD_ERROR = 2;//加载失败
    private FrameLayout mFrameLayout;
    private RelativeLayout mRl_loading;
    private RelativeLayout mRl_error;
    private final BaseAdapter mAdapter;

    public MoreHolder(BaseAdapter adapter) {
        mAdapter = adapter;
    }


    @Override
    protected View initView() {
/*        mFrameLayout = (FrameLayout) UiUtils.inflate(R.layout.load_more);
        mRl_loading = (RelativeLayout) mFrameLayout.findViewById(R.id.rl_more_loading);
        mRl_error = (RelativeLayout) mFrameLayout.findViewById(R.id.rl_more_error);*/
        return mFrameLayout;
    }

    @Override
    protected void refreshView(Integer integer) {
        mRl_loading.setVisibility(integer==HAS_MORE?View.VISIBLE:View.GONE);
        mRl_error.setVisibility(integer==LOAD_ERROR?View.VISIBLE:View.GONE);
        if(integer==NO_MORE){
            Toast.makeText(UiUtils.getContext(), "没有更多杰神了..", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View getContentView() {
        if(mAdapter.hasMore() && mData==HAS_MORE) {
            //加载数据
            mAdapter.loadMore();
        }else{
            mContentView.setVisibility(View.GONE);
        }
        return mContentView;
    }
}
