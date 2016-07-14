package com.lle.mydemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lle.mydemo.R;
import com.lle.mydemo.utils.UiUtils;

import java.util.List;

/**
 * @项目名称: MyDemo
 * @包名: com.lle.mydemo.adapter
 * @作者: 吴永乐
 *
 * @描述: TODO
 *
 * @创建时间: 2016-07-14 17:42 
 * @更新的时间:
 * @更新的描述: TODO
 *
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final OnItemClickListener mListener;
    private final List mList;

    public RecyclerViewAdapter(List dataList, OnItemClickListener listener){
        mList = dataList;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new ViewHolder(UiUtils.inflate(R.layout.item_service));
        return new ViewHolder(UiUtils.inflate(R.layout.item_staggeredgrid));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
/*        holder.mImageView.setImageResource(R.mipmap.ic_icon);
        holder.mTextView.setText("position : " + position);
        holder.mTextView.setTextSize(18);*/
        holder.mImageView.setImageResource((Integer) mList.get(position));
    }

    @Override
    public int getItemCount() {
        if(mList != null)
            return mList.size();
        return 0;
    }

    public interface OnItemClickListener{
         void onItemClick(View itemView, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextView;
        private ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);

/*            mImageView = (ImageView) itemView.findViewById(R.id.iv_service);
            mTextView = (TextView) itemView.findViewById(R.id.tv_service);*/
            mImageView = (ImageView) itemView.findViewById(R.id.iv_staggeredgrid);

            if(mListener != null){
                itemView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(v, getAdapterPosition());
        }
    }
}
