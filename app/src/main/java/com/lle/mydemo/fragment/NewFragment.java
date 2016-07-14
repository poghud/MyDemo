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

/**
 * @项目名称: MyDemo
 * @包名: com.lle.mydemo.fragment
 * @作者: 吴永乐
 *
 * @描述: TODO
 *
 * @创建时间: 2016-04-03 15:40 
 * @更新的时间:
 * @更新的描述: TODO
 *
 */
public class NewFragment extends BaseFragment {

    private volatile View self;

    private List<TabFragment> mList;

    @Override
    protected View initView() {
/*        TextView textView = new TextView(mContext);
        textView.setText("新闻");
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLUE);*/

        mList = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            TabFragment tabFragment = new TabFragment();
            tabFragment.setText("Tab" + i);
            mList.add(tabFragment);
        }

        if(this.self == null) {
            this.self = View.inflate(mContext, R.layout.news_tab, null);
        }
        if (this.self.getParent() != null) {
            ViewGroup parent = (ViewGroup) this.self.getParent();
            parent.removeView(this.self);
        }
//        this.self = View.inflate(mContext, R.layout.news_tab, null);
        ViewPager vp = (ViewPager) this.self.findViewById(R.id.news_vp);
        TabLayout tabLayout = (TabLayout) this.self.findViewById(R.id.tabLayout);

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
