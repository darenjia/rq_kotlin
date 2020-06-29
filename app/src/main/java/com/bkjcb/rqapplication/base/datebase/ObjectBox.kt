package com.bkjcb.rqapplication.base.datebase

import android.content.Context
import android.util.Log
import com.bkjcb.rqapplication.BuildConfig
import com.bkjcb.rqapplication.MyObjectBox
import com.bkjcb.rqapplication.contact.model.Level
import com.bkjcb.rqapplication.contact.model.Unit
import com.bkjcb.rqapplication.contact.model.User
import com.bkjcb.rqapplication.emergency.model.Emergency
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser

/**
 * Created by DengShuai on 2019/2/27.
 * Description :
 */
object ObjectBox {
    lateinit var boxStore: BoxStore
        private set

    fun getEmergencyBox(): Box<Emergency> = boxStore.boxFor(Emergency::class.java)
    fun getUnitBox(): Box<Unit> = boxStore.boxFor(Unit::class.java)
    fun getLevelBox(): Box<Level> = boxStore.boxFor(Level::class.java)
    fun getUserBox(): Box<User> = boxStore.boxFor(User::class.java)
    fun init(context: Context) {
        boxStore = MyObjectBox.builder()
                .androidContext(context.applicationContext)
                .build()
        if (BuildConfig.DEBUG) {
            val started = AndroidObjectBrowser(boxStore).start(context)
            Log.i("ObjectBrowser", "Started: $started")
        }
    }


}