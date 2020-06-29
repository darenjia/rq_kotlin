package com.bkjcb.rqapplication.base.util

import com.bkjcb.rqapplication.MyApplication
import java.io.File

/**
 * Created by DengShuai on 2020/6/16.
 * Description :
 */
object FileUtil {
    fun getFileOutputPath(s: String?): String {
        val file = File(MyApplication.getContext().getExternalFilesDir(null)?.absolutePath, s)
        return if (!file.exists() && file.mkdirs()) {
            file.absolutePath
        } else MyApplication.getContext().filesDir.absolutePath
    }
}