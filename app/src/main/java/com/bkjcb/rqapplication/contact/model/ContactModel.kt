package com.bkjcb.rqapplication.contact.model

import com.bkjcb.rqapplication.emergency.model.Emergency

/**
 * Created by DengShuai on 2020/2/18.
 * Description :
 */
class ContactModel {
    var emergency: List<Emergency>? = null
    var level: List<Level>? = null
    var user: List<User>? = null
    var unit: List<Unit>? = null

}