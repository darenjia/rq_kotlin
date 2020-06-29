package com.bkjcb.rqapplication;

import android.app.Application;
import android.content.Context;

import com.bkjcb.rqapplication.base.datebase.ObjectBox;
import com.bkjcb.rqapplication.base.model.UserResult;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.io.IOException;
import java.io.ObjectInputStream;

import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by DengShuai on 2019/10/14.
 * Description :
 */
public class MyApplication extends Application {
    private static Context context;
    private static UserResult.User user;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        initObjectBox();
        initLogger();
        //Bugly.init(getApplicationContext(), "b89a5bbe17", BuildConfig.DEBUG);
        try {
            RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    if (throwable instanceof UndeliverableException) {
                        //throwable = throwable.getCause();
                    }else {

                    }
                }
            });
        }catch (Exception e){
            Logger.e(e.getMessage());
        }
    }

    private void initObjectBox() {
        ObjectBox.INSTANCE.init(this);
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

    public static UserResult.User getUser() {
        if (user == null) {
            initUser();
        }
        return user;
    }

    private static void initUser() {
        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(context.openFileInput("CacheUser"));
            UserResult.User user = (UserResult.User) is.readObject();
            if (user != null) {
                setUser(user);
            }
            is.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void setUser(UserResult.User user) {
        MyApplication.user = user;
    }
}
