package org.bean.pig.memory.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding.view.RxView;
import com.orhanobut.logger.Logger;
import org.bean.pig.memory.R;
import org.bean.pig.memory.base.BaseActivity;
import org.bean.pig.memory.databinding.ActivityMainBinding;
import org.bean.pig.memory.recorder.AudioRecorder;
import org.bean.pig.memory.ui.fragment.MainPagerAdapter;
import org.bean.pig.memory.util.Permission;
import org.bean.pig.memory.util.ToastUtil;
import rx.functions.Action1;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private AudioRecorder mRecorder;

    private PagerAdapter mPagerAdapter;

    @Override
    public int contentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void addListener() {

        RxView.clicks(vm().record).compose(Permission.audio()).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean grant) {
                Logger.d("grant:" + grant);
                if (!grant) {
                    ToastUtil.show(R.string.grant_record_fail);
                    return;
                }
                if (mRecorder.isRecording()) {
                    mRecorder.stop();
                } else {
                    mRecorder.start();
                }
            }
        });

        mRecorder.getStateSubject().subscribe(new Action1<AudioRecorder.State>() {
            @Override
            public void call(AudioRecorder.State state) {
                vm().setRecordstate(state);
            }
        });
    }

    public void init() {
        ButterKnife.bind(this);
        mRecorder = new AudioRecorder(this);

        mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());

        vm().mainTabPager.setAdapter(mPagerAdapter);
        vm().viewpagertab.setViewPager(vm().mainTabPager);

        addListener();
    }
}
