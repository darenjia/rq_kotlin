package com.bkjcb.rqapplication.contact.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class Unit {
    @Id(assignable = true)
    var id: Long = 0

    /**
     * id : 2
     * quxian : 0
     * address : 徐家汇路579号
     * tel : 65079898
     * fax : 53014925
     * zipcode : 200023
     * levelID : 400
     * isShow : 0
     * districtName :
     */
    var uid = 0
    var quxian: String? = null
    var address: String? = null
    var tel: String? = null
    var fax: String? = null
    var zipcode: String? = null
    var levelid = 0
    var isShow = 0
    var districtName: String? = null

}