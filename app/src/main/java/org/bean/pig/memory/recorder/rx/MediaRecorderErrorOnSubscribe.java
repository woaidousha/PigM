package org.bean.pig.memory.recorder.rx;

import android.media.MediaRecorder;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

/**
 * Created by liuyulong@yixin.im on 2016-6-13.
 */
public class MediaRecorderErrorOnSubscribe implements Observable.OnSubscribe<MediaRecorderEvent> {

    private MediaRecorder mr;

    public MediaRecorderErrorOnSubscribe(MediaRecorder mr) {
        this.mr = mr;
    }

    @Override
    public void call(final Subscriber<? super MediaRecorderEvent> subscriber) {
        if (mr == null) {
            return;
        }
        MediaRecorder.OnErrorListener listener = new MediaRecorder.OnErrorListener() {
            @Override
            public void onError(MediaRecorder mr, int what, int extra) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(MediaRecorderEvent.create(mr, what, extra));
                }
            }
        };
        mr.setOnErrorListener(listener);
        subscriber.add(new MainThreadSubscription() {
            @Override
            protected void onUnsubscribe() {
                mr.setOnErrorListener(null);
            }
        });
    }
}
