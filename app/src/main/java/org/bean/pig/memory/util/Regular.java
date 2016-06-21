package org.bean.pig.memory.util;

import org.bean.pig.memory.App;
import org.bean.pig.memory.R;

import java.util.regex.Pattern;

/**
 * Created by liuyulong@yixin.im on 2016-6-21.
 */
public class Regular {

    public static Pattern audioFileName() {
        return Pattern.compile("^" + App.get().getString(R.string.default_audio_file_name) + "\\d*");
    }

}
