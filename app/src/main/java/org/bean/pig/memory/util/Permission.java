package org.bean.pig.memory.util;

import android.Manifest;
import com.tbruyelle.rxpermissions.RxPermissions;
import org.bean.pig.memory.App;
import rx.Observable;

/**
 * Created by liuyulong@yixin.im on 2016-6-13.
 */
public class Permission {

    public static Observable.Transformer<Object, Boolean> audio() {
        return RxPermissions.getInstance(App.get()).ensure(Manifest.permission.RECORD_AUDIO);
    }

}
