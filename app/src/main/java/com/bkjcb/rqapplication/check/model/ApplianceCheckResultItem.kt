package com.bkjcb.rqapplication.check.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * Created by DengShuai on 2019/12/24.
 * Description :
 */
@Entity
class ApplianceCheckResultItem() {
    @Id(assignable = true)
    var id: Long = 0
    var ischeck: String? = null
    var remark: String? = null
    var content: String? = null
    var jianchaxiangid: String? = null
    var jianchaid: String? = null

    constructor(jianchaid: String, jianchaxiangid: String) : this() {
        this.jianchaid = jianchaid
        this.jianchaxiangid = jianchaxiangid
    }
}