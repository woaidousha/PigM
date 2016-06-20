package org.bean.pig.memory;

import android.app.Application;

/**
 * Created by liuyulong@yixin.im on 2016-6-13.
 */
public class App extends Application {

    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static App get() {
        return sInstance;
    }
}
