package com.bkjcb.rqapplication.datebase

import android.content.Context
import com.bkjcb.rqapplication.contact.model.*
import com.bkjcb.rqapplication.datebase.ObjectBox.getEmergencyBox
import com.bkjcb.rqapplication.datebase.ObjectBox.getLevelBox
import com.bkjcb.rqapplication.datebase.ObjectBox.getUnitBox
import com.bkjcb.rqapplication.datebase.ObjectBox.getUserBox
import com.bkjcb.rqapplication.emergency.model.Emergency
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader
import java.util.*

/**
 * Created by DengShuai on 2020/2/18.
 * Description :
 */
object ContactDataUtil {
    fun init(context: Context) {
        if (getUserBox().isEmpty) {
            val gson = Gson()
            val reader: Reader = InputStreamReader(context.assets.open("database.json"))
            val contactModel = gson.fromJson(reader, ContactModel::class.java)
            if (contactModel != null) {
                getEmergencyBox().put(contactModel.emergency)
                getLevelBox().put(contactModel.level)
                getUserBox().put(contactModel.user)
                getUnitBox().put(contactModel.unit)
            }
        }
    }

    fun obtainAllUsers(): List<User> = getUserBox().all

    fun obtainAllEmergency(): List<Emergency> = getEmergencyBox().all

    fun obtainAllDistractName(): ArrayList<Level> {
            val strings = ArrayList<Level>(16)
            strings.add(Level("1", "宝山区", 1, "宝山区"))
            strings.add(Level("2", "崇明区", 1, "崇明区"))
            strings.add(Level("3", "奉贤区", 1, "奉贤区"))
            strings.add(Level("4", "虹口区", 1, "虹口区"))
            strings.add(Level("5", "黄浦区", 1, "黄浦区"))
            strings.add(Level("6", "嘉定区", 1, "嘉定区"))
            strings.add(Level("7", "金山区", 1, "金山区"))
            strings.add(Level("8", "静安区", 1, "静安区"))
            strings.add(Level("9", "闵行区", 1, "闵行区"))
            strings.add(Level("10", "浦东新区", 1, "浦东新区"))
            strings.add(Level("11", "普陀区", 1, "普陀区"))
            strings.add(Level("12", "青浦区", 1, "青浦区"))
            strings.add(Level("13", "松江区", 1, "松江区"))
            strings.add(Level("14", "徐汇区", 1, "徐汇区"))
            strings.add(Level("15", "杨浦区", 1, "杨浦区"))
            strings.add(Level("16", "长宁区", 1, "长宁区"))
            return strings
        }

    fun queryLevel(level: Int, kind1: Int, kind2: Int, kind3: Int, quxian: String): List<Level> {
        val builder = getLevelBox().query()
        builder.equal(Level_.level, level.toLong())
        if (level == 1 && "" != quxian) {
            builder.equal(Level_.quxian, quxian)
        }
        if (kind1 == -1) {
            builder.notEqual(Level_.kind1, 0).equal(Level_.kind2, 0).equal(Level_.kind3, 0)
        } else if (kind2 != -1 && kind3 == -1) {
            //sql.append("and Kind1 = ? and Kind2 = ? AND Kind3 != 0");
            builder.equal(Level_.kind1, kind1.toLong()).equal(Level_.kind2, kind2.toLong()).notEqual(Level_.kind3, 0)
        } else if (kind3 != -1) {
            //sql.append("and Kind1 = ? and Kind2 = ? AND Kind3 = ?");
            builder.equal(Level_.kind1, kind1.toLong()).equal(Level_.kind2, kind2.toLong()).equal(Level_.kind3, kind3.toLong())
        } else {
            //sql.append("and Kind1 = ? AND Kind2 != 0 AND Kind3 = 0");
            builder.equal(Level_.kind1, kind1.toLong()).and().notEqual(Level_.kind2, 0).and().equal(Level_.kind3, 0)
        }
        return builder.build().find()
    }

    fun queryUser(level: Level?): List<User> {
        if (level == null || level.level == -1) {
            return ArrayList()
        }
        val levelIds = getLevelBox().query().equal(Level_.uid, level.uid.toLong()).build().property(Level_.uid).distinct().findInts()
        val unitIds = getUnitBox().query().`in`(Unit_.levelid, levelIds).build().property(Unit_.uid).distinct().findInts()
        return getUserBox().query().`in`(User_.unitid, unitIds).build().find()
    }

    fun queryUser(key: String): List<User> {
        return getUserBox().query().contains(User_.username, key).build().find()
    }

    fun queryUserByDepartment(key: String): List<User> {
        val levelIds = getLevelBox().query().equal(Level_.departmentname, key).build().property(Level_.uid).distinct().findInts()
        val unitIds = getUnitBox().query().`in`(Unit_.levelid, levelIds).build().property(Unit_.uid).distinct().findInts()
        return getUserBox().query().`in`(User_.unitid, unitIds).build().find()
    }

    fun queryLevel(s: String): List<Level> {
        return getLevelBox().query().contains(Level_.departmentname, s).or().contains(Level_.departmentnamea, s).build().find()
    }
}