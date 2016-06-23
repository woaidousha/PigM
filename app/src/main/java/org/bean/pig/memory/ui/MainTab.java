package org.bean.pig.memory.ui;

import android.support.annotation.StringRes;
import org.bean.pig.memory.R;
import org.bean.pig.memory.base.BaseFragment;
import org.bean.pig.memory.ui.fragment.CalendarFragment;
import org.bean.pig.memory.ui.fragment.SizeFragment;
import org.bean.pig.memory.ui.fragment.TimeLineFragment;
import org.bean.pig.memory.ui.fragment.TipsFragment;

/**
 * Created by liuyulong@yixin.im on 2016-6-22.
 */
public enum MainTab {
    TIPS(0, TipsFragment.class),
    CALENDAR(1, CalendarFragment.class),
    TIMELINE(2, TimeLineFragment.class),
    SIZE(3, SizeFragment.class),
    ;

    public int index;
    public Class<? extends BaseFragment> clazz;

    MainTab(int index, Class clazz) {
        this.index = index;
        this.clazz = clazz;
    }


    public static MainTab fromTabIndex(int position) {
        for (MainTab value : MainTab.values()) {
            if (value.index == position) {
                return value;
            }
        }

        return null;
    }

    @StringRes
    public static int getTabTitle(MainTab tab) {
        switch (tab) {
            case TIPS:
                return R.string.main_tab_tips;
            case CALENDAR: {
                return R.string.main_tab_calendar;
            }
            case TIMELINE:
                return R.string.main_tab_timeline;
            case SIZE:
                return R.string.main_tab_size;
        }
        return 0;
    }
}
