package com.bkjcb.rqapplication.datebase;

import android.content.Context;

import io.objectbox.BoxStore;

/**
 * Created by DengShuai on 2019/2/27.
 * Description :
 */
public class ObjectBox {
    private static BoxStore boxStore;

    public static void init(Context context) {
      /*  boxStore = MyObjectBox.builder()
                .androidContext(context.getApplicationContext())
                .build();
        if (BuildConfig.DEBUG) {
            boolean started = new AndroidObjectBrowser(boxStore).start(context);
            Log.i("ObjectBrowser", "Started: " + started);
        }*/
    }

    public static BoxStore get() { return boxStore; }
}
