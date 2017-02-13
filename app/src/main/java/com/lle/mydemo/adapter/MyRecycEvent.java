package com.lle.mydemo.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MotionEvent;
import android.view.View;

import com.lle.mydemo.activity.MainActivity;

public class MyRecycEvent {

    private static MyRecycEvent sMyRecycEvent = new MyRecycEvent();

    private MyRecycEvent() {
    }

    public static MyRecycEvent getInstance() {
        return sMyRecycEvent;
    }

    public void setMyRecyclerViewEvent(RecyclerView recyclerView, final MainActivity mainActivity) {
        //关联radioGroup
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //最后一行的position
            private int[] lastPositions;
            //最后一个的position
            private int lastVisibleItemPosition;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //拿到最后一个可见条目的position
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if(layoutManager instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager lm = (StaggeredGridLayoutManager) layoutManager;
                    if (lastPositions == null) {
                        lastPositions = new int[lm.getSpanCount()];
                    }
                    lm.findLastVisibleItemPositions(lastPositions);
                    lastVisibleItemPosition = findMax(lastPositions);
                }else if(layoutManager instanceof LinearLayoutManager){
                    LinearLayoutManager lm = (LinearLayoutManager) layoutManager;
                    lastVisibleItemPosition = lm.findLastVisibleItemPosition();
                }else if(layoutManager instanceof GridLayoutManager){
                    GridLayoutManager lm = (GridLayoutManager) layoutManager;
                    lastVisibleItemPosition = lm.findLastVisibleItemPosition();
                }else throw new RuntimeException("RecyclerView中LayoutManager的类型只能是StaggeredGridLayoutManager,LinearLayoutManager和GridLayoutManager这三种类型");

                if ((layoutManager.getChildCount() > 0 && lastVisibleItemPosition >= layoutManager.getItemCount() - 1)) {
                    //最后的条目可见时
                    mainActivity.setRadioGroupVisibility(View.GONE);
                } else {
                    mainActivity.setRadioGroupVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            public int findMax(int[] arr) {
                int max = arr[0];
                for (int i : arr) {
                    if (max < i)
                        max = i;
                }
                return max;
            }
        });
        //关联floatingActionButton
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
                        if (y < -10) {
                            if (mainActivity.getFab2().getVisibility() == View.VISIBLE) {
                                mainActivity.fabOutsideAnimator(mainActivity.getFab2(), 50);
                                mainActivity.getFab2().setVisibility(View.GONE);
                            }
                            mainActivity.getFab().hide();
                        } else if (y > 10) {
                            mainActivity.getFab().show();
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

}
