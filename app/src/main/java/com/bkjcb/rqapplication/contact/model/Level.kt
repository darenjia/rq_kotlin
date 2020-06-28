package com.bkjcb.rqapplication.contact.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class Level : ContactBaseModel {
    @Id(assignable = true)
    var id: Long = 0

    /**
     * id : 1
     * quxian : 10
     * departmentname : 浦东新区环保局
     * departmentnamea : 浦东新区环保局
     * level : 1
     * kind1 : 1
     * kind2 : 0
     * kind3 : 0
     * flag : 0
     * districtname : 浦东新区
     */
    constructor() {}
    constructor(level: Int) {
        this.level = level
    }

    constructor(quxian: String?, departmentnamea: String?, level: Int, districtname: String?) {
        this.quxian = quxian
        this.departmentnamea = departmentnamea
        this.level = level
        this.districtname = districtname
    }

    var uid = 0
    var quxian: String? = null
    var departmentname: String? = null
    var departmentnamea: String? = null
    var level = 0
    var kind1 = 0
    var kind2 = 0
    var kind3 = 0
    var flag = 0
    var districtname: String? = null

}