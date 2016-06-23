package org.bean.pig.memory.widget.dialog;

import android.databinding.BindingAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Created by liuyulong@yixin.im on 2016-6-23.
 */
public class BindingUtil {

    @BindingAdapter({"view_pager_adapter"})
    public static void bindViewPagerAdapter(ViewPager viewPager, PagerAdapter adapter) {
        viewPager.setAdapter(adapter);
    }

}
