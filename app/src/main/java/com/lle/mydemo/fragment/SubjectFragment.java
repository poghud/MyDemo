package com.lle.mydemo.fragment;

import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lle.mydemo.R;
import com.lle.mydemo.activity.MainActivity;
import com.lle.mydemo.adapter.GridAdapter;
import com.lle.mydemo.adapter.MyRecycEvent;
import com.lle.mydemo.base.BaseFragment;
import com.lle.mydemo.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

public class SubjectFragment extends BaseFragment {
    @Override
    protected View initView() {
        View rootView = UiUtils.inflate(R.layout.fragment_service);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_service);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            list.add(R.mipmap.icon_item);
        }
        recyclerView.setAdapter(new GridAdapter(list, new GridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Snackbar.make(itemView, "pressed " + position, Snackbar.LENGTH_SHORT).show();
            }
        }));

        //设置item之间的间隔
//        SpacesItemDecoration decoration = new SpacesItemDecoration(5);
/*        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = 1;
                if (parent.getChildLayoutPosition(view) % 2 == 0) {
                    outRect.left = 0;
                    outRect.right = 1;
                }
            }
        });*/

        MainActivity mainActivity = (MainActivity) getActivity();
        MyRecycEvent.getInstance().setMyRecyclerViewEvent(recyclerView, mainActivity);

        //下拉刷新
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_service);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UiUtils.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //停止刷新
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        return rootView;
    }
}
