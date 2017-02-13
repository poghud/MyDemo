package com.lle.mydemo.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lle.mydemo.R;
import com.lle.mydemo.utils.ImageCache;
import com.lle.mydemo.utils.UiUtils;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    private final OnItemClickListener mListener;
    private final List mList;

    public GridAdapter(List dataList, OnItemClickListener listener) {
        mList = dataList;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(UiUtils.inflate(R.layout.item_recyc_grid));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        int imgID = (Integer) mList.get(position);
        ImageCache.getImageCache().setImage(imgID, holder.mImageView);

        holder.mTextView.setText("text" + position);
        holder.mTextView.setTextColor(Color.RED);
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextView;
        private ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.iv_cardview);
            mTextView = (TextView) itemView.findViewById(R.id.tv_cardview);

            if (mListener != null) {
                itemView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(v, getAdapterPosition());
        }
    }
}
