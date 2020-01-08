package com.bkjcb.rqapplication.util;

import android.content.Context;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
}
