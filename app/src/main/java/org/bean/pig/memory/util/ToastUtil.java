package org.bean.pig.memory.util;

import android.widget.Toast;
import org.bean.pig.memory.App;

/**
 * Created by liuyulong@yixin.im on 2016-6-20.
 */
public class ToastUtil {

    public static void show(String message) {
        Toast.makeText(App.get(), message, Toast.LENGTH_SHORT).show();
    }

    public static void show(int message) {
        Toast.makeText(App.get(), message, Toast.LENGTH_SHORT).show();
    }

}
