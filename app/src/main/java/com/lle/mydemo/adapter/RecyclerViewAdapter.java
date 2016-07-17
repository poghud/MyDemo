package com.lle.mydemo.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lle.mydemo.R;
import com.lle.mydemo.utils.UiUtils;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final OnItemClickListener mListener;
    private final List mList;
    private Map<Integer, SoftReference<Bitmap>> mImgCache = new HashMap<>();

    public RecyclerViewAdapter(List dataList, OnItemClickListener listener) {
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

        //        holder.mImageView.setImageResource((Integer) mList.get(position));

        int imgID = (Integer) mList.get(position);
        SoftReference<Bitmap> softReference = mImgCache.get(imgID);
        if (softReference == null) {
            Bitmap bitmap = BitmapFactory.decodeResource(UiUtils.getResource(), imgID);
            softReference = new SoftReference<>(bitmap);
            mImgCache.put(imgID, softReference);
            holder.mImageView.setImageBitmap(softReference.get());
        }else{
            Bitmap bitmap = softReference.get();
            if(bitmap != null){
                holder.mImageView.setImageBitmap(bitmap);
            }else {
                softReference = new SoftReference<>(BitmapFactory.decodeResource(UiUtils.getResource(), imgID));
                holder.mImageView.setImageBitmap(softReference.get());
            }
        }

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
        //        private TextView mTextView;
        private ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);

/*            mImageView = (ImageView) itemView.findViewById(R.id.iv_service);
            mTextView = (TextView) itemView.findViewById(R.id.tv_service);*/
            mImageView = (ImageView) itemView.findViewById(R.id.iv_staggeredgrid);

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
