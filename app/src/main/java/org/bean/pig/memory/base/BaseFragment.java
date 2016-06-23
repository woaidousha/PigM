package org.bean.pig.memory.base;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.bean.pig.memory.base.vm.DataBindingController;

/**
 * Created by liuyulong@yixin.im on 2016-6-23.
 */
public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment implements Face {

    private DataBindingController<T> mController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mController = DataBindingController.create(inflater, container, this);
        return mController.rootView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public DataBindingController getViewModel() {
        return mController;
    }

    @Override
    public T vm() {
        return mController.getDataBinding();
    }
}
