package org.bean.pig.memory.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding.view.RxView;
import com.orhanobut.logger.Logger;
import org.bean.pig.memory.R;
import org.bean.pig.memory.recorder.AudioRecorder;
import org.bean.pig.memory.util.Permission;
import org.bean.pig.memory.util.ToastUtil;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.duration)
    protected TextView mDuration;
    @Bind(R.id.start)
    protected ImageView mStart;
    @Bind(R.id.stop)
    protected ImageView mStop;
    private AudioRecorder mRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mRecorder = new AudioRecorder(this);
        RxView.clicks(mStart).compose(Permission.audio()).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean grant) {
                Logger.d("grant:" + grant);
                if (!grant) {
                    ToastUtil.show(R.string.grant_record_fail);
                    return;
                }
                mRecorder.start();
            }
        });
        RxView.clicks(mStop).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mRecorder.stop();
            }
        });
        addListener();
    }

    private void addListener() {
        mRecorder.getStateSubject().subscribe(new Action1<AudioRecorder.State>() {
            @Override
            public void call(AudioRecorder.State state) {
                updateState(state);
            }
        });
        mRecorder.getUpdateSubject().subscribe(new Action1<AudioRecorder>() {
            @Override
            public void call(AudioRecorder audioRecorder) {
                mDuration.setText(audioRecorder.duration() + "s");
            }
        });
    }

    public void updateState(AudioRecorder.State state) {
        mStart.setEnabled(state != AudioRecorder.State.RECORDING);
        mStop.setEnabled(state != AudioRecorder.State.NONE);
    }
}
