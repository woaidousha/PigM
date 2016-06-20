package org.bean.pig.memory.recorder.rx;

import android.media.MediaRecorder;

/**
 * Created by liuyulong@yixin.im on 2016-6-13.
 */
public class MediaRecorderEvent {

    MediaRecorder mr;
    int what;
    int extra;

    public MediaRecorderEvent(MediaRecorder mr, int what, int extra) {
        this.mr = mr;
        this.what = what;
        this.extra = extra;
    }

    public static MediaRecorderEvent create(MediaRecorder mr, int what, int extra) {
        return new MediaRecorderEvent(mr, what, extra);
    }

    public MediaRecorder getMr() {
        return mr;
    }

    public int getWhat() {
        return what;
    }

    public int getExtra() {
        return extra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediaRecorderEvent that = (MediaRecorderEvent) o;

        if (what != that.what) return false;
        if (extra != that.extra) return false;
        return mr != null ? mr.equals(that.mr) : that.mr == null;

    }

    @Override
    public int hashCode() {
        int result = mr != null ? mr.hashCode() : 0;
        result = 31 * result + what;
        result = 31 * result + extra;
        return result;
    }

    @Override
    public String toString() {
        return "MediaRecorderEvent{" +
                "mr=" + mr +
                ", what=" + what +
                ", extra=" + extra +
                '}';
    }
}
