package org.bean.pig.memory.base;

import android.databinding.ViewDataBinding;
import org.bean.pig.memory.base.vm.DataBindingController;

/**
 * Created by liuyulong@yixin.im on 2016-6-23.
 */
public interface Face<T extends ViewDataBinding> {
    int contentViewId();
    DataBindingController<T> getViewModel();
    T vm();
}
