package com.bkjcb.rqapplication.util;

import android.content.Context;

/**
 * Created by DengShuai on 2019/11/1.
 * Description :
 */
public class Utils {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
