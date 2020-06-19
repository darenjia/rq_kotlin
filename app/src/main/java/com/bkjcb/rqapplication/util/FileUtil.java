package com.bkjcb.rqapplication.util;

import com.bkjcb.rqapplication.MyApplication;

import java.io.File;

/**
 * Created by DengShuai on 2020/6/16.
 * Description :
 */
public class FileUtil {
    public static String getFileOutputPath(String s) {
        File file = new File(MyApplication.getContext().getExternalFilesDir(null).getAbsolutePath(), s);
        if (!file.exists() && file.mkdirs()) {
            return file.getAbsolutePath();
        }
        return MyApplication.getContext().getFilesDir().getAbsolutePath();
    }
}
