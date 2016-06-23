package org.bean.pig.memory.base;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import org.bean.pig.memory.base.vm.DataBindingController;

/**
 * Created by liuyulong@yixin.im on 2016-6-23.
 */
public abstract class BaseActivity<T extends ViewDataBinding> extends FragmentActivity implements Face {

    private DataBindingController<T> mController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mController = DataBindingController.create(getLayoutInflater(), null, this);
        setContentView(mController.rootView());
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
