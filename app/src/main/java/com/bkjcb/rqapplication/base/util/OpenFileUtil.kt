package com.bkjcb.rqapplication.base.util

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import com.bkjcb.rqapplication.MyApplication
import com.bkjcb.rqapplication.base.util.Utils.getAudioContentUri
import com.bkjcb.rqapplication.base.util.Utils.getImageContentUri
import com.bkjcb.rqapplication.base.util.Utils.getVideoContentUri
import java.io.File
import java.util.*

object OpenFileUtil {
    fun openFile(filePath: String?): Intent? {
        val file = File(filePath)
        if (!file.exists()) return null
        /* 取得扩展名 */
        val end = file.name.substring(file.name.lastIndexOf(".") + 1, file.name.length).toLowerCase(Locale.getDefault())
        /* 依扩展名的类型决定MimeType */return if (end == "m4a" || end == "mp3" || end == "mid" || end == "xmf" || end == "ogg" || end == "wav") {
            getAudioFileIntent(filePath)
        } else if (end == "3gp" || end == "mp4") {
            getVideoFileIntent(filePath)
        } else if (end == "jpg" || end == "gif" || end == "png" || end == "jpeg" || end == "bmp") {
            getImageFileIntent(filePath)
        } else if (end == "apk") {
            getApkFileIntent(filePath)
        } else if (end == "ppt") {
            getPptFileIntent(filePath)
        } else if (end == "xls") {
            getExcelFileIntent(filePath)
        } else if (end == "doc") {
            getWordFileIntent(filePath)
        } else if (end == "pdf") {
            getPdfFileIntent(filePath)
        } else if (end == "chm") {
            getChmFileIntent(filePath)
        } else if (end == "txt") {
            getTextFileIntent(filePath, false)
        } else {
            getAllIntent(filePath)
        }
    }

    // Android获取一个用于打开APK文件的intent
    fun getAllIntent(param: String?): Intent {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Intent.ACTION_VIEW
        val uri = getUri(param)
        intent.setDataAndType(uri, "*/*")
        return intent
    }

    // Android获取一个用于打开APK文件的intent
    fun getApkFileIntent(param: String?): Intent {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Intent.ACTION_VIEW
        val uri = getUri(param)
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        return intent
    }

    // Android获取一个用于打开VIDEO文件的intent
    fun getVideoFileIntent(param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("oneshot", 0)
        intent.putExtra("configchange", 0)
        val uri = getVideoContentUri(MyApplication.getContext(), param!!)
        intent.setDataAndType(uri, "video/*")
        return intent
    }

    // Android获取一个用于打开AUDIO文件的intent
    fun getAudioFileIntent(param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("oneshot", 0)
        intent.putExtra("configchange", 0)
        val uri = getAudioContentUri(MyApplication.getContext(), param!!)
        intent.setDataAndType(uri, "audio/*")
        return intent
    }

    // Android获取一个用于打开Html文件的intent
    fun getHtmlFileIntent(param: String?): Intent {
        val uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build()
        val intent = Intent("android.intent.action.VIEW")
        intent.setDataAndType(uri, "text/html")
        return intent
    }

    // Android获取一个用于打开图片文件的intent
    fun getImageFileIntent(param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = getImageContentUri(MyApplication.getContext(), param!!)
        intent.setDataAndType(uri, "image/*")
        return intent
    }

    // Android获取一个用于打开PPT文件的intent
    fun getPptFileIntent(param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = getUri(param)
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
        return intent
    }

    // Android获取一个用于打开Excel文件的intent
    fun getExcelFileIntent(param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = getUri(param)
        intent.setDataAndType(uri, "application/vnd.ms-excel")
        return intent
    }

    // Android获取一个用于打开Word文件的intent
    fun getWordFileIntent(param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = getUri(param)
        intent.setDataAndType(uri, "application/msword")
        return intent
    }

    // Android获取一个用于打开CHM文件的intent
    fun getChmFileIntent(param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = getUri(param)
        intent.setDataAndType(uri, "application/x-chm")
        return intent
    }

    // Android获取一个用于打开文本文件的intent
    fun getTextFileIntent(param: String?, paramBoolean: Boolean): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (paramBoolean) {
            val uri1 = getUri(param)
            intent.setDataAndType(uri1, "text/plain")
        } else {
            val uri2 = getUri(param)
            intent.setDataAndType(uri2, "text/plain")
        }
        return intent
    }

    // Android获取一个用于打开PDF文件的intent
    fun getPdfFileIntent(param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = getUri(param)
        intent.setDataAndType(uri, "application/pdf")
        return intent
    }

    private fun getUri(file: String?): Uri? {
        var uri: Uri? = null
        uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //判断版本是否在7.0以上
            FileProvider.getUriForFile(MyApplication.getContext(),
                    MyApplication.getContext().packageName + ".fileprovider",
                    File(file))
        } else {
            Uri.parse(file)
        }
        return uri
    }
}