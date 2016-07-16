package com.lle.mydemo.base;


import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lle.mydemo.manager.ThreadManager;
import com.lle.mydemo.utils.UiUtils;

import java.util.List;

public abstract class BaseAdapter<Data> extends android.widget.BaseAdapter implements AdapterView.OnItemClickListener {
    private static final int PAGERSIZE = 20;
    protected List<Data> mList;
    public static final int ITEM_DEFAULT = 0;
    public static final int ITEM_MORE = 1;
//    public static final int ITEM_TITLE =2;
    private final ListView mLv;

    public BaseAdapter(List<Data> list, ListView listView){
        mList = list;
        //条目点击事件
        listView.setOnItemClickListener(this);
        mLv = listView;
    }

/*
    public void setData(List<Data> list){
        mList = list;
    }
*/

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(mList != null){
            return position == mList.size() ? ITEM_MORE : getItemType(position);
        }
        return super.getItemViewType(position);
    }

    /**
     *如果listview有更多的类型的条目,重写此方法
     * @param position 条目所在的位置
     * @return 指定位置view的类型
     */
    @SuppressWarnings("UnusedParameters")
    public int getItemType(int position) {
        return ITEM_DEFAULT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
/*        BaseHolder<Data> holder;
        if(convertView == null){
            holder = getholder();
        }else{
            holder = (BaseHolder) convertView.getTag();
        }

        Data data = mList.get(position);
        holder.setData(data);*/

        BaseHolder holder;
        switch (getItemViewType(position)){
            case ITEM_MORE:
                if(convertView == null){
                    holder = getMoreHolder();
                }else{
                    holder = (MoreHolder) convertView.getTag();
                }
                break;
            default :
                if(convertView == null){
                    holder = getholder();
                }else{
                    holder = (BaseHolder) convertView.getTag();
                }
                break;
        }

        //noinspection unchecked
        holder.setData(position == mList.size() ? MoreHolder.HAS_MORE : mList.get(position));

        //  如果当前Holder 恰好是MoreHolder  证明MoreHOlder已经显示
        return holder.getContentView();
    }

    private MoreHolder mHolder;

    private BaseHolder<Integer> getMoreHolder() {
        if(mHolder!=null){
            return mHolder;
        }else{
            mHolder = new MoreHolder(this);
            return mHolder;
        }
    }

    @Override
    public int getCount() {
        if(mList != null){
            return mList.size() + 1;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(mList != null && position < mList.size()){
            return mList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void loadMore(){
        ThreadManager.getThreadManager().createLongTimePool().execute(new Runnable() {
            @Override
            public void run() {
                final List<Data> newData;
                try {
                    newData = onLoad();
                    UiUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (newData == null) {
                                mHolder.setData(MoreHolder.LOAD_ERROR);//
                            } else if (newData.size() < PAGERSIZE) {
                                mHolder.setData(MoreHolder.NO_MORE);
                            } else {
                                // 成功了
                                mHolder.setData(MoreHolder.HAS_MORE);
                                mList.addAll(newData);//  给listView之前的集合添加一个新的集合
                                notifyDataSetChanged();// 刷新界面
                            }

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    UiUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mHolder.setData(MoreHolder.LOAD_ERROR);
                        }
                    });
                }

            }
        });
    }

    /**
     * 加载更多的抽象方法
     * @return 数据集3
     */
    protected List<Data> onLoad() throws Exception{
        return null;
    }

    protected abstract BaseHolder<Data> getholder();

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int headerViewsCount = mLv.getHeaderViewsCount();
        position -= headerViewsCount;
        if(mList != null && position == mList.size()){
            //加载更多的条目
            return;
        }
        //条目点击事件的具体实现可重写这个方法
        onInnerItemClick(position);
    }

    /**
     * 条目点击事件的具体实现可重写这个方法
     * @param position 被点击的位置
     */
    @SuppressWarnings("UnusedParameters")
    public void onInnerItemClick(int position){}

    /**
     * 如果不想显示加载更多的视图,可重写此方法
     * @return true:显示加载更多   false:不显示加载更多
     */
    public boolean hasMore(){
        return true;
    }
}
