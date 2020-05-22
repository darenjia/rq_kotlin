package com.bkjcb.rqapplication;

import android.app.Application;
import android.content.Context;

import com.bkjcb.rqapplication.datebase.ObjectBox;
import com.bkjcb.rqapplication.model.UserResult;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.tencent.bugly.Bugly;

/**
 * Created by DengShuai on 2019/10/14.
 * Description :
 */
public class MyApplication extends Application {
    private static Context context;
    public static UserResult.User user;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        initObjectBox();
        initLogger();
        Bugly.init(getApplicationContext(), "b89a5bbe17", BuildConfig.DEBUG);
    }

    private void initObjectBox() {
        ObjectBox.init(this);
    }
    public static Context getContext() {
        return context;
    }

    private void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                //.methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                //.logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("BK_TAG")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }
}
