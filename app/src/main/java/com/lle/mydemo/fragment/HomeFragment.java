package com.lle.mydemo.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lle.mydemo.R;
import com.lle.mydemo.activity.MainActivity;
import com.lle.mydemo.base.BaseFragment;
import com.lle.mydemo.utils.UiUtils;

import java.util.ArrayList;

/**
 * @项目名称: MyDemo
 * @包名: com.lle.mydemo.fragment
 * @作者: 吴永乐
 *
 * @描述: TODO
 *
 * @创建时间: 2016-04-03 15:37 
 * @更新的时间:
 * @更新的描述: TODO
 *
 */
public class HomeFragment extends BaseFragment {
    private static final String TAG = "HomeFragment";
    int[] imgs = {R.drawable.twitter_icon_1, R.drawable.twitter_icon_2, R.drawable.twitter_icon_3, R.drawable.twitter_icon_4};
    private ArrayList<View> mList;
    private ViewPager mViewPager;
    private LinearLayout ll_container;
//    private List<ImageView> mPoints;
    private TextView mTv_title;
    private int	mSpace;				// 两个点之间的间距
    private ImageView mSelectedpoint;
    private AutoScrollTask mTask;
    private int mLeftMargin;

    @Override
    protected View initView() {
/*        TextView textView = new TextView(mContext);
        textView.setText("主页");
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLUE);*/

        FrameLayout frameLayout = (FrameLayout) UiUtils.inflate(R.layout.fragment_home);
        final Button button = (Button) frameLayout.findViewById(R.id.home_btn);
        mViewPager = (ViewPager) frameLayout.findViewById(R.id.home_vp);
        ll_container = (LinearLayout) frameLayout.findViewById(R.id.home_ll_pointcontainer);
        mTv_title = (TextView) frameLayout.findViewById(R.id.home_tv_title);
        mSelectedpoint = (ImageView) frameLayout.findViewById(R.id.home_iv_selected_point);
        mTv_title.setText("图片1");

        //初始化btn
        button.setText(((MainActivity) getActivity()).isHasAnim() ? "动画效果开关(ON)" : "动画效果开关(OFF)");
        button.setTextSize(20);
        button.setTextColor(Color.BLUE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("动画效果");
                builder.setMessage("是否开启页面切换动画效果?");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity) getActivity()).setHasAnim(true);
                        button.setText("动画效果开关(ON)");
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity) getActivity()).setHasAnim(false);
                        button.setText("动画效果开关(OFF)");
                    }
                });
                builder.create().show();
            }
        });

        //初始化viewpager中的图片和点
        mList = new ArrayList<>();
//        mPoints = new ArrayList();
        for (int i = 0; i < imgs.length; i++) {
            //初始化viewpager中的图片
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(imgs[i]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mList.add(imageView);
            //初始化图片对应的点
            ImageView indicator = new ImageView(mContext);
            indicator.setImageResource(R.drawable.point_normal_guide);
            int width = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources()
                            .getDisplayMetrics()) + .5f);
            int height = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources()
                            .getDisplayMetrics()) + .5f);
            LinearLayout.LayoutParams parmas = new LinearLayout.LayoutParams(width, height);
            if (i != 0) {
                parmas.leftMargin =
                        (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources()
                                .getDisplayMetrics()) + .5f);
            }
            ll_container.addView(indicator, parmas);

/*            ImageView iv = new ImageView(mContext);
            iv.setImageResource(i == 0 ? R.drawable.point_pressed_guide : R.drawable.point_normal_guide);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
            params.rightMargin = 8;
            iv.setLayoutParams(params);
            ll_container.addView(iv);
            mPoints.add(iv);*/
        }
        //设置viewpager的adapter
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return Integer.MAX_VALUE;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(mList.get(position % mList.size()));
                return mList.get(position % mList.size());
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
        //无限轮播
        int firstItem = (Integer.MAX_VALUE/2) - (Integer.MAX_VALUE / 2 % mList.size());
        mViewPager.setCurrentItem(firstItem);

        //自动轮播
        if(mTask == null) {
            mTask = new AutoScrollTask();
        }
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mTask.stop();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mTask.start();
                        break;
                }
                return false;
            }
        });
        mTask.start();

        return frameLayout;
    }

    @Override
    protected void initListener() {
        ll_container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // ll_container布局完成的时候
                // 两个点的间距
                if (mSpace == 0) {// 保存只需要初始化一次
                    mSpace = ll_container.getChildAt(1).getLeft() - ll_container.getChildAt(0).getLeft();
                }
                // 移除监听
                ll_container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        //viewpager的滑动监听事件
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 滚动的时候
                // positionOffset:移动的比值
                // positionOffsetPixels:移动的具体的像素值
                //                Log.i(TAG, "positionOffset:" + positionOffset + " positionOffsetPixels:" + positionOffsetPixels);
                if (position % mList.size() == mList.size() - 1) {
                    return;
                }
                int leftMargin = (int) (mSpace * (position % mList.size()) + mSpace * positionOffset + .5f);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mSelectedpoint.getLayoutParams();
                layoutParams.leftMargin = leftMargin;
                mSelectedpoint.setLayoutParams(layoutParams);

//                Log.i(TAG, "position:" + position % mList.size() + "--------------" + leftMargin);
            }
            @Override
            public void onPageSelected(int position) {
/*                       //先移除所有的点
                       ll_container.removeAllViews();
                       for (int i = 0; i < mPoints.size(); i++) {
                           mPoints.get(i).setImageResource(i == position ? R.drawable.point_pressed_guide : R.drawable.point_normal_guide);
                           ll_container.addView(mPoints.get(i));
                       }*/
                if (position % mList.size() == mList.size() - 1 || position % mList.size() == 0) {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mSelectedpoint.getLayoutParams();
                    layoutParams.leftMargin = mSpace * (position % mList.size());
                    mSelectedpoint.setLayoutParams(layoutParams);
                }
                mTv_title.setText("图片" + (position % mList.size() + 1));
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    //自动轮播任务
    class AutoScrollTask implements Runnable{
        private boolean flag;

        public void start(){
            if (!flag) {
                UiUtils.removeCallbacks(this);
                flag = true;
                UiUtils.postDelayed(this, 3000);
            }
        }

        public void stop(){
            if(flag) {
                flag = false;
                UiUtils.removeCallbacks(this);
            }
        }

        @Override
        public void run() {
            if (flag) {
                UiUtils.removeCallbacks(this);
                int currentItem = mViewPager.getCurrentItem();
                mViewPager.setCurrentItem(++currentItem);
                UiUtils.postDelayed(this, 3000);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mTask != null)
            mTask.start();
        if(mLeftMargin != 0)
            ((RelativeLayout.LayoutParams) mSelectedpoint.getLayoutParams()).leftMargin = mLeftMargin;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mTask != null)
            mTask.stop();
        mLeftMargin = ((RelativeLayout.LayoutParams) mSelectedpoint.getLayoutParams()).leftMargin;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTask = null;
    }
}
