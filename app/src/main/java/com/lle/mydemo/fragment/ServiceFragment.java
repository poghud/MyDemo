package com.lle.mydemo.fragment;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MotionEvent;
import android.view.View;

import com.lle.mydemo.R;
import com.lle.mydemo.activity.MainActivity;
import com.lle.mydemo.adapter.RecyclerViewAdapter;
import com.lle.mydemo.adapter.SpacesItemDecoration;
import com.lle.mydemo.base.BaseFragment;
import com.lle.mydemo.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

public class ServiceFragment extends BaseFragment {

    private MainActivity mMainActivity;

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
        SpacesItemDecoration decoration=new SpacesItemDecoration(5);
        recyclerView.addItemDecoration(decoration);

        mMainActivity = (MainActivity) getActivity();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //最后一行的position
            private int[] lastPositions;
            //最后一个的position
            private int lastVisibleItemPosition;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

//                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                    if (lastPositions == null) {
                        lastPositions = new int[layoutManager.getSpanCount()];
                    }
                    layoutManager.findLastVisibleItemPositions(lastPositions);
                    lastVisibleItemPosition = findMax(lastPositions);
                    if ((layoutManager.getChildCount() > 0 && lastVisibleItemPosition >= layoutManager.getItemCount() - 1)) {
                        //最后的条目可见时
                        mMainActivity.setRadioGroupVisibility(View.GONE);
                    }
//                }
                else{
                    mMainActivity.setRadioGroupVisibility(View.VISIBLE);
                }
            }
            public int findMax(int[] arr){
                int max = arr[0];
                for(int i : arr){
                    if(max < i)
                        max = i;
                }
                return max;
            }
        });
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            private float downY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float moveY = event.getY();
                        float y = moveY - downY;
                        if (y < -5) {
                            if (mMainActivity.getFab2().getVisibility() == View.VISIBLE) {
                                mMainActivity.fabOutsideAnimator(mMainActivity.getFab2(), 50);
                                mMainActivity.getFab2().setVisibility(View.GONE);
                            }
                            mMainActivity.getFab().hide();
                        } else if (y > 5) {
                            mMainActivity.getFab().show();
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        return rootView;
    }


}
