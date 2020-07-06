package com.bkjcb.rqapplication.base.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.bkjcb.rqapplication.base.MyApplication;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.stationCheck.model.CheckItem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by DengShuai on 2019/11/1.
 * Description :
 */
public class Utils {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static String dateFormat(String format, Date date) {
        if (TextUtils.isEmpty(format)) {
            format = "yyyy-MM-dd";
        }
        return new SimpleDateFormat(format, Locale.CHINESE).format(date);
    }

    public static String dateFormat(String s) {
        if (TextUtils.isEmpty(s)) {
            return "";
        }
        Date date = null;
        try {
            date = new Date(Long.parseLong(s));
        } catch (NumberFormatException e) {
            //e.printStackTrace();
            return s;
        }
        String format = "yyyy-MM-dd";
        return new SimpleDateFormat(format, Locale.CHINESE).format(date);
    }

    public static int getColor(Context context, int position) {
        int[] colors = {R.color.color_type_0, R.color.color_type_1, R.color.color_type_2, R.color.color_type_3};
        return context.getResources().getColor(colors[position % 4]);
    }

    public static int getRandomColor(Context context) {
        int[] colors = {R.color.color_type_0, R.color.color_type_1, R.color.color_type_2, R.color.color_type_3};
        return context.getResources().getColor(colors[new Random().nextInt(15) % 4]);
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE).format(new Date(System.currentTimeMillis()));
    }

    public static String getFTPPath(CheckItem item) {
        StringBuilder path = new StringBuilder();
        if (item.type == 0) {
            path.append("zhandianjiancha/");
        } else {
            path.append("qijujiancha/");
        }
        switch (item.zhandianleixing) {
            case "气化站":
                path.append("qihuazhan");
                break;
            case "储配站":
                path.append("chupeizhan");
                break;
            case "供应站":
                path.append("gongyingzhan");
                break;
            case "加气站":
                path.append("jiaqizhan");
                break;
            case "维修检查企业":
                path.append("anzhuangweixiu");
                break;
            case "报警器企业":
                path.append("baojing");
                break;
            case "销售企业":
                path.append("xiaoshou");
                break;
            default:

        }
        path.append("/").append(item.c_id);
        return path.toString();
    }

    public static String getFileName(String path) {
        if (!TextUtils.isEmpty(path)) {
            return path.substring(path.lastIndexOf("/") + 1);
        }
        return "";
    }

    public static String getFileSuffix(String path) {
        if (!TextUtils.isEmpty(path)) {
            return path.substring(path.lastIndexOf("."));
        }
        return "";
    }

    public static Uri getImageContentUri(Context context, String filePath) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            File file = new File(filePath);
            if (file.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return Uri.parse("");
            }
        }
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static Uri getVideoContentUri(Context context, String filePath) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Video.Media._ID},
                MediaStore.Video.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (new File(filePath).exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Video.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static Uri getAudioContentUri(Context context, String filePath) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media._ID},
                MediaStore.Audio.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (new File(filePath).exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Audio.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static Calendar getCalendar() {
        return Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
    }

    public static BitmapDescriptor getMapIconBitmap(int id) {
        return BitmapDescriptorFactory.fromResource(id);

    }

    public static String listToString(List<String> list) {
        StringBuilder builder = new StringBuilder();
        for (String s : list) {
            builder.append(s).append(",");
        }
        return builder.substring(0, builder.length() - 1);
    }

    public static String getCurrentVersion() {
        PackageManager packageManager = MyApplication.getContext().getPackageManager();
        // getPackageName()是你当前类的包名
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(MyApplication.getContext().getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "未知";
    }
}
