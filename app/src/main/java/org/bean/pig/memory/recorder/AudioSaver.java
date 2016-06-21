package org.bean.pig.memory.recorder;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import butterknife.ButterKnife;
import org.bean.pig.memory.App;
import org.bean.pig.memory.R;
import org.bean.pig.memory.content.ContentStore;
import org.bean.pig.memory.util.FileUtil;
import org.bean.pig.memory.util.Regular;
import org.bean.pig.memory.util.ThreadUtil;
import org.bean.pig.memory.util.ToastUtil;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

import java.io.File;

/**
 * Created by liuyulong@yixin.im on 2016-6-20.
 */
public class AudioSaver {

    private Context mContext;
    private File temp;

    public AudioSaver(Context context, File temp) {
        this.mContext = context;
        this.temp = temp;
    }

    public static Observable<String> getDefaultFileName() {
        final String defaultName = App.get().getString(R.string.default_audio_file_name);
        return Observable.just(ContentStore.Folder.Audio.get())
                .compose(FileUtil.filterFileNames(Regular.audioFileName()))
                .compose(FileUtil.genFileName(defaultName));
    }

    public void save(String fileName) {
        FileUtil.move(temp.getAbsolutePath(), ContentStore.Folder.Audio.getPath(fileName + ".amr"))
                .compose(ThreadUtil.<Boolean>ioui())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        ToastUtil.show(R.string.save_success);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show(R.string.save_fail);
                    }

                    @Override
                    public void onNext(Boolean aLong) {
                    }
                });
    }

    public void save() {
        getDefaultFileName()
                .compose(ThreadUtil.<String>ioui())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String name) {
                        showSaveDialog(name);
                    }
                });

    }

    private void showSaveDialog(String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.save_audio_dialog_title);
        View view = LayoutInflater.from(App.get()).inflate(R.layout.save_name_edit, null);
        final EditText fileName = ButterKnife.findById(view, R.id.file_name);
        fileName.setText(name);
        builder.setView(view);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save(fileName.getText().toString());
            }
        });
        builder.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTemp();
            }
        });
        builder.show();
    }

    private void deleteTemp() {
        if (temp != null) {
            temp.delete();
        }
    }
}
