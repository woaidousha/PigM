package org.bean.pig.memory.util;

import android.text.TextUtils;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liuyulong@yixin.im on 2016-6-14.
 */
public class FileUtil {

    public static Observable<Long> copy(File src, File dest) {
        return copy(getPath(src), getPath(dest));
    }

    public static Observable<Long> copy(final String srcPath, final String dstPath) {
        return Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                if (TextUtils.isEmpty(srcPath) || TextUtils.isEmpty(dstPath)) {
                    Exceptions.propagate(new IllegalArgumentException("copy src or dest is empty"));
                }

                File source = new File(srcPath);
                if (!source.exists()) {
                    Exceptions.propagate(new IllegalArgumentException("copy src file is not exist"));
                }

                if (srcPath.equals(dstPath)) {
                    subscriber.onNext(source.length());
                    subscriber.onCompleted();
                    return;
                }

                FileChannel fcin = null;
                FileChannel fcout = null;
                try {
                    File destFile = create(dstPath);
                    fcin = new FileInputStream(source).getChannel();
                    fcout = new FileOutputStream(destFile).getChannel();
                    ByteBuffer tmpBuffer = ByteBuffer.allocateDirect(4096);
                    while (fcin.read(tmpBuffer) != -1) {
                        tmpBuffer.flip();
                        fcout.write(tmpBuffer);
                        tmpBuffer.clear();

                        // 如果外面cancel了这个操作,中断拷贝操作
                        if (subscriber != null && subscriber.isUnsubscribed()) {
                            destFile.delete();
                            return;
                        }
                    }
                    subscriber.onNext(source.length());
                    subscriber.onCompleted();
                    return;
                } catch (Exception e) {
                    Exceptions.propagate(e);
                } finally {
                    try {
                        if (fcin != null) {
                            fcin.close();
                        }
                        if (fcout != null) {
                            fcout.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Exceptions.propagate(new IllegalArgumentException("Save file fail with unknow error"));
                return;
            }
        }).compose(ThreadUtil.<Long>ioui());
    }

    public static Observable<Boolean> move(final File src, final File dest) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (src == null || !src.exists() || dest == null) {
                    throw new IllegalArgumentException("copy file src or dest is null");
                }

                if (dest.exists()) {
                    dest.delete();
                }

                try {
                    dest.createNewFile();
                } catch (IOException e) {
                    Exceptions.propagate(e);
                }

                subscriber.onNext(src.renameTo(dest.getAbsoluteFile()));
                subscriber.onCompleted();
            }
        });
    }

    public static Observable<Boolean> move(String src, String dest) {
        return move(new File(src), new File(dest));
    }

    public static File create(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }

        File f = new File(filePath);
        if (!f.getParentFile().exists()) {// 如果不存在上级文件夹
            f.getParentFile().mkdirs();
        }
        try {
            f.createNewFile();
            return f;
        } catch (IOException e) {
            if (f != null && f.exists()) {
                f.delete();
            }
            return null;
        }
    }

    public static String getPath(File file) {
        if (file == null) {
            return "";
        } else {
            return file.getAbsolutePath();
        }
    }

    public static Observable.Transformer<File, String[]> filterFileNames(final Pattern pattern) {
        final FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                Matcher matcher = pattern.matcher(filename);
                return matcher.find();
            }
        };
        return new Observable.Transformer<File, String[]>() {
            @Override
            public Observable<String[]> call(Observable<File> observable) {
                return observable.map(new Func1<File, String[]>() {
                    @Override
                    public String[] call(File file) {
                        return file.list(filter);
                    }
                });
            }
        };
    }

    public static Observable.Transformer<String[], String> genFileName(final String defaultName) {
        return new Observable.Transformer<String[], String>() {

            @Override
            public Observable<String> call(Observable<String[]> observable) {
                return observable.map(new Func1<String[], String>() {
                    @Override
                    public String call(String[] strings) {
                        if (strings == null || strings.length == 0) {
                            return defaultName;
                        }
                        Arrays.sort(strings, new Comparator<String>() {
                            @Override
                            public int compare(String lhs, String rhs) {
                                int ll = lhs.length();
                                int rl = rhs.length();
                                if (ll != rl) {
                                    return Integer.compare(ll, rl);
                                }
                                return lhs.compareTo(rhs);
                            }
                        });
                        String max = strings[strings.length - 1];
                        int end = max.lastIndexOf(".");
                        max = max.substring(0, end);
                        int index = 0;
                        if (!TextUtils.equals(defaultName, max)) {
                            index = Integer.parseInt(max.substring(defaultName.length()));
                        }
                        return defaultName + (index + 1);
                    }
                }).onErrorReturn(new Func1<Throwable, String>() {
                    @Override
                    public String call(Throwable throwable) {
                        return defaultName;
                    }
                });
            }
        };
    }
}
