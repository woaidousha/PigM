package org.bean.pig.memory.content;

import android.os.Environment;

import java.io.File;

/**
 * Created by liuyulong@yixin.im on 2016-6-20.
 */
public class ContentStore {

    public enum Folder {
        ROOT("PigM"),
        Audio("Audio"),
        Temp("Temp"),
        Log("Log");

        private String name;

        Folder(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public File get() {
            return new File(getPath());
        }

        public String getPath() {
            if (this == ROOT) {
                return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getName();
            } else {
                return ROOT.getPath() + "/" + getName();
            }
        }

        public String getPath(String fileName) {
            return getPath() + "/" + fileName;
        }
    }

}
