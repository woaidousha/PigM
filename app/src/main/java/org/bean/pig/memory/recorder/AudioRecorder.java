package org.bean.pig.memory.recorder;

import android.content.Context;
import android.media.MediaRecorder;
import com.orhanobut.logger.Logger;
import org.bean.pig.memory.App;
import org.bean.pig.memory.recorder.rx.MediaRecorderEvent;
import org.bean.pig.memory.recorder.rx.RxMediaRecorder;
import org.bean.pig.memory.util.ThreadUtil;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.internal.util.SubscriptionList;
import rx.subjects.PublishSubject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by liuyulong@yixin.im on 2016-6-13.
 */
public class AudioRecorder {

    private static final int UPDATE_INTERVAL = 100;

    private Context mContext;

    private MediaRecorder mRecorder;
    private AtomicBoolean mRecording;
    private long mDuration;

    private PublishSubject<AudioRecorder> mUpdateSubject;
    private PublishSubject<State> mStateSubject;
    private SubscriptionList mSubscriptionList;

    public enum State {
        NONE,
        RECORDING
    }

    public AudioRecorder(Context context) {
        mContext = context;
        mRecording = new AtomicBoolean(false);
        mUpdateSubject = PublishSubject.create();
        mStateSubject = PublishSubject.create();
    }

    public File outputFile(boolean createNew) {
        App app = App.get();
        File file = new File(app.getCacheDir(), "temp_audio.amr");
        if (createNew && file.exists()) {
            file.delete();
        }
        return file;
    }

    public void init() {
        mSubscriptionList = new SubscriptionList();
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile(outputFile(true).getPath());
        Subscription s = RxMediaRecorder.info(mRecorder).subscribe(new Action1<MediaRecorderEvent>() {
            @Override
            public void call(MediaRecorderEvent mediaRecorderEvent) {
                stop(true);
            }
        });
        mSubscriptionList.add(s);
        s = RxMediaRecorder.error(mRecorder).subscribe(new Action1<MediaRecorderEvent>() {
            @Override
            public void call(MediaRecorderEvent mediaRecorderEvent) {
                stop(true);
            }
        });
        mSubscriptionList.add(s);
        mDuration = System.currentTimeMillis();
    }

    private void startUpdate() {
        Subscription s = Observable.interval(UPDATE_INTERVAL, TimeUnit.MILLISECONDS)
                .flatMap(new Func1<Long, Observable<AudioRecorder>>() {
                    @Override
                    public Observable<AudioRecorder> call(Long count) {
                        mDuration = count * UPDATE_INTERVAL;
                        return Observable.just(AudioRecorder.this);
                    }
                }).compose(ThreadUtil.<AudioRecorder>ioui()).subscribe(mUpdateSubject);
        mSubscriptionList.add(s);
    }

    public void start() {
        Logger.d("Start record " + mRecording.get());

        if (mRecording.get()) {
            return;
        }

        init();

        try {
            mRecording.set(true);
            mRecorder.prepare();
            mRecorder.start();
            startUpdate();
            updateState();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        stop(false);
    }

    private void stop(boolean error) {
        if (!mRecording.get()) {
            return;
        }
        mSubscriptionList.clear();
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        mRecording.set(false);

        updateState();

        if (error) {
            outputFile(false).delete();
        } else {
            save();
        }
    }

    private void save() {
        AudioSaver saver = new AudioSaver(mContext, outputFile(false));
        saver.save();
    }

    public boolean isRecording() {
        return mRecording.get();
    }

    public long duration() {
        return mDuration / 1000;
    }

    public PublishSubject<AudioRecorder> getUpdateSubject() {
        return mUpdateSubject;
    }

    public PublishSubject<State> getStateSubject() {
        return mStateSubject;
    }

    private void updateState() {
        mStateSubject.onNext(isRecording() ? State.RECORDING : State.NONE);
    }
}
