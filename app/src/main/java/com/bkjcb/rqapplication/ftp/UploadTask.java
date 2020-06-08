package com.bkjcb.rqapplication.ftp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by DengShuai on 2019/4/7.
 * Description :
 */
public class UploadTask {

    public static Observable<Boolean> createUploadTask(List<String> paths, String remotepath, FtpUtils.UploadProgressListener listener) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                ArrayList<File> files = new ArrayList<>();
                if (paths.size() > 0) {
                    for (String path : paths) {
                        File file = new File(path);
                        if (file.exists()) {
                            files.add(file);
                        }
                    }
                    FtpUtils utils = new FtpUtils();
                    boolean isSuccess = utils.uploadMultiFile(files, remotepath, listener);
                    if (isSuccess) {
                        emitter.onNext(true);
                    } else {
                        emitter.onNext(false);
                    }
                } else {
                    emitter.onNext(true);
                }

            }
        });
    }
    public static Observable<Boolean> createUploadTask(List<String> paths, String remotepath) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                ArrayList<File> files = new ArrayList<>();
                if (paths.size() > 0) {
                    for (String path : paths) {
                        File file = new File(path);
                        if (file.exists()) {
                            files.add(file);
                        }
                    }
                    FtpUtils utils = new FtpUtils();
                    try {
                        boolean isSuccess = utils.uploadMultiFile(files, remotepath);
                        if (isSuccess) {
                            emitter.onNext(true);
                        } else {
                            emitter.onNext(false);
                        }
                    }catch (Exception e){
                        emitter.onError(e);
                    }

                } else {
                    emitter.onNext(true);
                }
            }
        });
    }
}
