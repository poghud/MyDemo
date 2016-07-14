package com.lle.mydemo.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @项目名称: MyDemo
 * @包名: com.lle.mydemo.adapter
 * @作者: 吴永乐
 *
 * @描述: TODO
 *
 * @创建时间: 2016-07-14 19:08 
 * @更新的时间:
 * @更新的描述: TODO
 *
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = space;
        }
    }
}
