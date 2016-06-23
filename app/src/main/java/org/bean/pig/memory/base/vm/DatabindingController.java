package org.bean.pig.memory.base.vm;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.bean.pig.memory.base.Face;

/**
 * Created by liuyulong@yixin.im on 2016-6-23.
 */
public class DataBindingController<T extends ViewDataBinding> {

    private T mDataBinding;

    public DataBindingController(LayoutInflater inflater, ViewGroup root, Face face) {
        mDataBinding = DataBindingUtil.inflate(inflater, face.contentViewId(), root, false);
    }

    public static <T extends ViewDataBinding> DataBindingController<T> create(LayoutInflater inflater, ViewGroup root, Face face) {
        return new DataBindingController<>(inflater, root, face);
    }

    public View rootView() {
        return mDataBinding.getRoot();
    }

    public T getDataBinding() {
        return mDataBinding;
    }

}
