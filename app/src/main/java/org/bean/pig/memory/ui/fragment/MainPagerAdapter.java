package org.bean.pig.memory.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import org.bean.pig.memory.App;
import org.bean.pig.memory.base.BaseFragment;
import org.bean.pig.memory.ui.MainTab;

import java.util.List;

/**
 * Created by liuyulong@yixin.im on 2016-6-22.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    private final BaseFragment[] fragments;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new BaseFragment[MainTab.values().length];
        for (MainTab tab : MainTab.values()) {
            try {
                BaseFragment fragment = null;

                List<Fragment> fs = fm.getFragments();
                if (fs != null) {
                    for (Fragment f : fs) {
                        if (f != null && f.getClass() == tab.clazz) {
                            fragment = (BaseFragment) f;
                            break;
                        }
                    }
                }

                if (fragment == null) {
                    fragment = tab.clazz.newInstance();
                }

                fragments[tab.index] = fragment;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getCount() {
        return MainTab.values().length;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        MainTab tab = MainTab.fromTabIndex(position);

        int resId = tab != null ? MainTab.getTabTitle(tab) : 0;

        return resId != 0 ? App.get().getText(resId) : "";
    }
}
