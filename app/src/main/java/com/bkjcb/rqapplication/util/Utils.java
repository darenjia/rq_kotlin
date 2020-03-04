package com.bkjcb.rqapplication.util;

import android.content.Context;
import android.text.TextUtils;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.model.CheckItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

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
                path.append("qiju");
                break;
            case "报警器企业":
                path.append("baojin");
                break;
            case "销售企业":
                path.append("xiaoshou");
                break;
            default:

        }
        path.append("/").append(item.c_id).append("/");
        return path.toString();
    }
}
