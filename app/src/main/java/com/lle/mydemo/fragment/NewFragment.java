package com.lle.mydemo.fragment;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.lle.mydemo.R;
import com.lle.mydemo.activity.MainActivity;
import com.lle.mydemo.base.BaseFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class NewFragment extends BaseFragment {

    private volatile View self;

    private List<TabFragment> mList;
    private TabLayout mTabLayout;

    @Override
    protected View initView() {

        mList = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            TabFragment tabFragment = new TabFragment();
            tabFragment.setText("Tab" + i);
            mList.add(tabFragment);
        }

        if (this.self == null) {
            this.self = View.inflate(mContext, R.layout.fragment_news, null);
        }
        if (this.self.getParent() != null) {
            ViewGroup parent = (ViewGroup) this.self.getParent();
            parent.removeView(this.self);
        }
        //        this.self = View.inflate(mContext, R.layout.fragment_news, null);
        ViewPager vp = (ViewPager) this.self.findViewById(R.id.news_vp);
        mTabLayout = (TabLayout) this.self.findViewById(R.id.tabLayout);
        mTabLayout.setBackgroundColor(Color.rgb(63, 81, 181));

        //Fragment嵌套Fragment要用getChildFragmentManager
        vp.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mList.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "tab" + (position + 1);
            }
        });

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MainActivity mainActivity = (MainActivity) getActivity();
                switch (position) {
                    case 0:
                        setColor(mainActivity, Color.rgb(63, 81, 181));
                        break;
                    case 1:
                        setColor(mainActivity, Color.rgb(255, 215, 0));
                        break;
                    case 2:
                        setColor(mainActivity, Color.rgb(34, 139, 34));
                        break;
                    case 3:
                        setColor(mainActivity, Color.rgb(255, 127, 80));
                        break;
                    case 4:
                        setColor(mainActivity, Color.DKGRAY);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabLayout.setupWithViewPager(vp);

        return this.self;
    }

    private void setColor(MainActivity mainActivity, int color) {
        //        设置当完全CollapsingToolbarLayout折叠(收缩)后的背景颜色
        mainActivity.getCollapsingToolbarLayout().setContentScrimColor(color);
        mainActivity.getRadioGroup().setBackgroundColor(color);
        //侧滑菜单头部
        mainActivity.getNavigationView().getHeaderView(0).setBackgroundColor(colorBurn(color));
        mTabLayout.setBackgroundColor(color);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mainActivity.setColor(colorBurn(color));
            Window window = mainActivity.getWindow();
            if(!mainActivity.isBarExpanded())
                window.setStatusBarColor(colorBurn(color));
            window.setNavigationBarColor(colorBurn(color));

        }
    }

    /**
     * 颜色加深处理
     *
     * @param RGBValues
     *            RGB的值，由alpha（透明度）、red（红）、green（绿）、blue（蓝）构成，
     *            Android中我们一般使用它的16进制，
     *            例如："#FFAABBCC",最左边到最右每两个字母就是代表alpha（透明度）、
     *            red（红）、green（绿）、blue（蓝）。每种颜色值占一个字节(8位)，值域0~255
     *            所以下面使用移位的方法可以得到每种颜色的值，然后每种颜色值减小一下，在合成RGB颜色，颜色就会看起来深一些了
     * @return
     */
    private int colorBurn(int RGBValues) {
        int alpha = RGBValues >> 24;
        int red = RGBValues >> 16 & 0xFF;
        int green = RGBValues >> 8 & 0xFF;
        int blue = RGBValues & 0xFF;
        red = (int) Math.floor(red * (1 - 0.1));
        green = (int) Math.floor(green * (1 - 0.1));
        blue = (int) Math.floor(blue * (1 - 0.1));
        return Color.rgb(red, green, blue);
    }

    /**
     *java.lang.IllegalStateException: activity has been destroyed
     *如果你仔细查看Fragment的实现，你会看到当fragment进行到onDestroyView状态时，它会重置它的内部状态。
     *然而，它没有重置mChildFragmentManager，从而引发了上面的异常，这是当前版本support库的一个bug.
     */
    @SuppressWarnings("TryWithIdenticalCatches")
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
