package org.bean.pig.memory.recorder.rx;

import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import rx.Observable;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Created by liuyulong@yixin.im on 2016-6-13.
 */
public class RxMediaRecorder {

    public static Observable<MediaRecorderEvent> info(@NonNull MediaRecorder mr) {
        checkNotNull(mr, "view == null");
        return Observable.create(new MediaRecorderInfoOnSubscribe(mr));
    }

    public static Observable<MediaRecorderEvent> error(@NonNull MediaRecorder mr) {
        checkNotNull(mr, "view == null");
        return Observable.create(new MediaRecorderErrorOnSubscribe(mr));
    }

}
