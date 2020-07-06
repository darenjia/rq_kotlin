package com.bkjcb.rqapplication.datebase;

import android.content.Context;
import android.util.Log;

import com.bkjcb.rqapplication.BuildConfig;
import com.bkjcb.rqapplication.model.Emergency;
import com.bkjcb.rqapplication.contactBook.model.Level;
import com.bkjcb.rqapplication.model.MyObjectBox;
import com.bkjcb.rqapplication.contactBook.model.Unit;
import com.bkjcb.rqapplication.contactBook.model.User;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;

/**
 * Created by DengShuai on 2019/2/27.
 * Description :
 */
public class ObjectBox {
    private static BoxStore boxStore;
    private static Box<Emergency> emergencyBox;
    private static Box<Unit> unitBox;
    private static Box<Level> levelBox;
    private static Box<User> userBox;

    public static void init(Context context) {
        boxStore = MyObjectBox.builder()
                .androidContext(context.getApplicationContext())
                .build();
        if (BuildConfig.DEBUG) {
            boolean started = new AndroidObjectBrowser(boxStore).start(context);
            Log.i("ObjectBrowser", "Started: " + started);
        }
    }

    public static BoxStore get() {
        return boxStore;
    }

    public static Box<Emergency> getEmergencyBox() {
        if (emergencyBox == null) {
            emergencyBox = boxStore.boxFor(Emergency.class);
        }
        return emergencyBox;
    }

    public static Box<Unit> getUnitBox() {
        if (unitBox == null) {
            unitBox = boxStore.boxFor(Unit.class);
        }
        return unitBox;
    }

    public static Box<Level> getLevelBox() {
        if (levelBox == null) {
            levelBox = boxStore.boxFor(Level.class);
        }
        return levelBox;
    }

    public static Box<User> getUserBox() {
        if (userBox == null) {
            userBox = boxStore.boxFor(User.class);
        }
        return userBox;
    }

}
