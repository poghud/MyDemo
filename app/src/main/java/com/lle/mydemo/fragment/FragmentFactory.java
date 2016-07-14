package com.lle.mydemo.fragment;

import com.lle.mydemo.base.BaseFragment;
import com.lle.mydemo.fragment.HomeFragment;
import com.lle.mydemo.fragment.NewFragment;
import com.lle.mydemo.fragment.ServiceFragment;
import com.lle.mydemo.fragment.SettingFragment;
import com.lle.mydemo.fragment.SubjectFragment;

import java.util.HashMap;
import java.util.Map;

public class FragmentFactory {
    private static Map<Integer, BaseFragment> mFragments = new HashMap<>();

    public static BaseFragment getFragment(int position) {
        BaseFragment fragment = mFragments.get(position);
        if(fragment == null){
            switch (position) {
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    fragment = new NewFragment();
                    break;
                case 2:
                    fragment = new ServiceFragment();
                    break;
                case 3:
                    fragment = new SubjectFragment();
                    break;
                case 4:
                    fragment = new SettingFragment();
                    break;
            }
            mFragments.put(position, fragment);
        }
        return fragment;
    }

}
