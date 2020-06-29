package com.bkjcb.rqapplication.base.util

import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import com.amap.api.maps.model.BitmapDescriptor
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.bkjcb.rqapplication.MyApplication
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.check.model.CheckItem
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by DengShuai on 2019/11/1.
 * Description :
 */
object Utils {
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun dateFormat(format: String?, date: Date?): String {
        var format = format
        if (TextUtils.isEmpty(format)) {
            format = "yyyy-MM-dd"
        }
        return SimpleDateFormat(format, Locale.CHINESE).format(date)
    }

    fun dateFormat(s: String): String {
        if (TextUtils.isEmpty(s)) {
            return ""
        }
        var date: Date? = null
        date = try {
            Date(s.toLong())
        } catch (e: NumberFormatException) {
            //e.printStackTrace();
            return s
        }
        val format = "yyyy-MM-dd"
        return SimpleDateFormat(format, Locale.CHINESE).format(date)
    }

    fun getColor(context: Context, position: Int): Int {
        val colors = intArrayOf(R.color.color_type_0, R.color.color_type_1, R.color.color_type_2, R.color.color_type_3)
        return context.resources.getColor(colors[position % 4])
    }

    fun getRandomColor(context: Context): Int {
        val colors = intArrayOf(R.color.color_type_0, R.color.color_type_1, R.color.color_type_2, R.color.color_type_3)
        return context.resources.getColor(colors[Random().nextInt(15) % 4])
    }

    fun obtainCurrentTime(): String = SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE).format(Date(System.currentTimeMillis()))

    fun getFTPPath(item: CheckItem): String {
        val path = StringBuilder()
        if (item.type == 0) {
            path.append("zhandianjiancha/")
        } else {
            path.append("qijujiancha/")
        }
        when (item.zhandianleixing) {
            "气化站" -> path.append("qihuazhan")
            "储配站" -> path.append("chupeizhan")
            "供应站" -> path.append("gongyingzhan")
            "加气站" -> path.append("jiaqizhan")
            "维修检查企业" -> path.append("anzhuangweixiu")
            "报警器企业" -> path.append("baojing")
            "销售企业" -> path.append("xiaoshou")
            else -> {
            }
        }
        path.append("/").append(item.c_id)
        return path.toString()
    }

    fun getFileName(path: String): String {
        return if (!TextUtils.isEmpty(path)) {
            path.substring(path.lastIndexOf("/") + 1)
        } else ""
    }

    fun getFileSuffix(path: String): String {
        return if (!TextUtils.isEmpty(path)) {
            path.substring(path.lastIndexOf("."))
        } else ""
    }

    fun getImageContentUri(context: Context, filePath: String): Uri? {
        val cursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Images.Media._ID),
                MediaStore.Images.Media.DATA + "=? ", arrayOf(filePath), null)
        return if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID))
            val baseUri = Uri.parse("content://media/external/images/media")
            cursor.close()
            Uri.withAppendedPath(baseUri, "" + id)
        } else {
            val file = File(filePath)
            if (file.exists()) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, filePath)
                context.contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            } else {
                Uri.parse("")
            }
        }
    }

    fun buildUUID(): String = UUID.randomUUID().toString().replace("-", "")

    fun getVideoContentUri(context: Context, filePath: String): Uri? {
        val cursor = context.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Video.Media._ID),
                MediaStore.Video.Media.DATA + "=? ", arrayOf(filePath), null)
        return if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID))
            val baseUri = Uri.parse("content://media/external/images/media")
            Uri.withAppendedPath(baseUri, "" + id)
        } else {
            if (File(filePath).exists()) {
                val values = ContentValues()
                values.put(MediaStore.Video.Media.DATA, filePath)
                context.contentResolver.insert(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
            } else {
                null
            }
        }
    }

    fun getAudioContentUri(context: Context, filePath: String): Uri? {
        val cursor = context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Audio.Media._ID),
                MediaStore.Audio.Media.DATA + "=? ", arrayOf(filePath), null)
        return if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID))
            val baseUri = Uri.parse("content://media/external/images/media")
            Uri.withAppendedPath(baseUri, "" + id)
        } else {
            if (File(filePath).exists()) {
                val values = ContentValues()
                values.put(MediaStore.Audio.Media.DATA, filePath)
                context.contentResolver.insert(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values)
            } else {
                null
            }
        }
    }

    fun obtainCalendar(): Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"))

    fun getMapIconBitmap(id: Int): BitmapDescriptor {
        return BitmapDescriptorFactory.fromResource(id)
    }

    fun listToString(list: List<String?>): String {
        val builder = StringBuilder()
        for (s in list) {
            builder.append(s).append(",")
        }
        return builder.substring(0, builder.length - 1)
    }

    // getPackageName()是你当前类的包名
    fun currentVersion(): String {
        val packageManager = MyApplication.getContext().packageManager
        // getPackageName()是你当前类的包名
        var packInfo: PackageInfo? = null
        try {
            packInfo = packageManager.getPackageInfo(MyApplication.getContext().packageName, 0)
            return packInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return "未知"
    }
}