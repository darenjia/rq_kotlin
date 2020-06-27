package com.bkjcb.rqapplication.datebase

import com.bkjcb.rqapplication.actionregister.model.ActionRegisterItem
import com.bkjcb.rqapplication.datebase.ObjectBox
import com.bkjcb.rqapplication.emergency.model.EmergencyItem
import io.objectbox.Box
import io.objectbox.kotlin.boxFor

/**
 * Created by DengShuai on 2020/6/24.
 * Description :
 */
object DataUtil {
    fun getActionRegisterBox(): Box<ActionRegisterItem> {
        return ObjectBox.get().boxFor();
    }

    fun getEmergencyItemBox(): Box<EmergencyItem> {
        return ObjectBox.get().boxFor();
    }
}