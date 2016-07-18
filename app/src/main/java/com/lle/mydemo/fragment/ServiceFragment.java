package com.lle.mydemo.fragment;

import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.lle.mydemo.R;
import com.lle.mydemo.activity.MainActivity;
import com.lle.mydemo.adapter.MyRecycEvent;
import com.lle.mydemo.adapter.RecyclerViewAdapter;
import com.lle.mydemo.adapter.SpacesItemDecoration;
import com.lle.mydemo.base.BaseFragment;
import com.lle.mydemo.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

public class ServiceFragment extends BaseFragment {

    @Override
    protected View initView() {
        View rootView = UiUtils.inflate(R.layout.fragment_service);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_service);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        List<Integer> list =  new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            list.add(R.drawable.icon_01 + i);
        }
        recyclerView.setAdapter(new RecyclerViewAdapter(list, new RecyclerViewAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View itemView, int position) {
                Snackbar.make(itemView, "pressed item " + position, Snackbar.LENGTH_SHORT).show();
            }
        }));
        //设置item之间的间隔
        SpacesItemDecoration decoration = new SpacesItemDecoration(5);
        recyclerView.addItemDecoration(decoration);

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
