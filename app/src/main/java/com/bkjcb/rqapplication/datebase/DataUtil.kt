package com.bkjcb.rqapplication.datebase

import com.bkjcb.rqapplication.actionregister.model.ActionRegisterItem
import com.bkjcb.rqapplication.check.model.*
import com.bkjcb.rqapplication.emergency.model.EmergencyItem
import io.objectbox.Box
import io.objectbox.kotlin.boxFor

/**
 * Created by DengShuai on 2020/6/24.
 * Description :
 */
object DataUtil {
    fun getActionRegisterBox(): Box<ActionRegisterItem> {
        return ObjectBox.boxStore.boxFor();
    }

    fun getEmergencyItemBox(): Box<EmergencyItem> {
        return ObjectBox.boxStore.boxFor();
    }

    fun getCheckItemBox(): Box<CheckItem> = ObjectBox.boxStore.boxFor()
    fun getCheckResultItemBox(): Box<CheckResultItem> = ObjectBox.boxStore.boxFor()
    fun getApplianceCheckContentItemBox(): Box<ApplianceCheckContentItem> = ObjectBox.boxStore.boxFor()
    fun getApplianceCheckResultItemBox(): Box<ApplianceCheckResultItem> = ObjectBox.boxStore.boxFor()
    fun getContentItems(id: String): List<ApplianceCheckContentItem>? = getApplianceCheckContentItemBox().query().equal(ApplianceCheckContentItem_.cid, id).build().find()

}