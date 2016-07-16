package com.lle.mydemo.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.lle.mydemo.R;
import com.lle.mydemo.base.BaseFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class NewFragment extends BaseFragment {

    private volatile View self;

    private List<TabFragment> mList;

    @Override
    protected View initView() {

        mList = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            TabFragment tabFragment = new TabFragment();
            tabFragment.setText("Tab" + i);
            mList.add(tabFragment);
        }

        if(this.self == null) {
            this.self = View.inflate(mContext, R.layout.fragment_news, null);
        }
        if (this.self.getParent() != null) {
            ViewGroup parent = (ViewGroup) this.self.getParent();
            parent.removeView(this.self);
        }
//        this.self = View.inflate(mContext, R.layout.fragment_news, null);
        ViewPager vp = (ViewPager) this.self.findViewById(R.id.news_vp);
        TabLayout tabLayout = (TabLayout) this.self.findViewById(R.id.tabLayout);

        //Fragment嵌套Fragment要用getChildFragmentManager
        vp.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
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

        tabLayout.setupWithViewPager(vp);

        return this.self;
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
