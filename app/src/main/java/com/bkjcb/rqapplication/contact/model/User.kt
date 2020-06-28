package com.bkjcb.rqapplication.contact.model

import com.bkjcb.rqapplication.datebase.ObjectBox.getLevelBox
import com.bkjcb.rqapplication.datebase.ObjectBox.getUnitBox
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Transient

@Entity
class User : ContactBaseModel() {
    @JvmField
    @Id(assignable = true)
    var id: Long = 0

    /**
     * id : 1
     * unitid : 1
     * username : 朱成宇
     * loginname : 朱成宇
     * password : null
     * u_tel : 23113121
     * tel : 18916879517
     * duty : 设施管理处/处长
     * role_a : 1
     * role_b : 0
     * role_c : 0
     * role_d : 0
     * flag : 0
     */
    var uid = 0
    var unitid = 0
    var username: String? = null
    var loginname: String? = null
    var password: String? = null
    var u_tel: String? = null
    var tel: String? = null
    var duty: String? = null
    var role_a = 0
    var role_b = 0
    var role_c = 0
    var role_d = 0
    var flag = 0

    @Transient
    var unit: Unit? = null

    @Transient
    var level: Level? = null

    fun getUnit(): Unit? {
        if (unit == null) {
            unit = getUnitBox().query().equal(Unit_.uid, unitid.toLong()).build().findFirst()
        }
        return unit
    }

    fun setUnit(unit: Unit?): kotlin.Unit {
        this.unit = unit
    }

    fun getLevel(): Level {
        if (level == null) {
            level = getLevelBox().query().equal(Level_.uid, getUnit()!!.levelid).build().findFirst()
        }
        return level!!
    }

    fun setLevel(level: Level?): kotlin.Unit {
        this.level = level
    }

    fun init(): kotlin.Unit {
        if (unit == null) {
            unit = getUnitBox().query().equal(Unit_.uid, unitid.toLong()).build().findFirst()
        }
        if (level == null && unit != null) {
            level = getLevelBox().query().equal(Level_.uid, getUnit()!!.levelid).build().findFirst()
        }
    }
}