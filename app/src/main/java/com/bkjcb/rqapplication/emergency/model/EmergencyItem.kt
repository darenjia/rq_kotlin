package com.bkjcb.rqapplication.emergency.model

import com.bkjcb.rqapplication.datebase.ObjectBox
import io.objectbox.Box
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.io.Serializable

/**
 * Created by DengShuai on 2020/3/27.
 * Description :
 */
@Entity
data class EmergencyItem(@Id(assignable = true)
                         var id: Long = 0,
                         var userId: String? = null,
                         var reportingUnit: String? = null,
                         var qushu: String? = null,
                         var remark: String? = null,
                         var reportingDate: String? = null,
                         var accidentDate: String? = null,
                         var accidentAddress: String? = null,
                         var disposalPerson: String? = null,
                         var keyPerson: String? = null,
                         var reportingStaff: String? = null,
                         var contactPhone: String? = null,
                         var mainDescription: String? = null,
                         var phoneftp: String? = null,
                         var status: Int = 0,
                         var uuid: String? = null,
                         var systime: Long = 0) : Serializable
/**
 * userId : F7EE86DEA39B44BB95FDFE0A6A6B3319
 * reportingUnit : 报送单位
 * qushu : 黄浦
 * remark : 备注
 * reportingDate : 2020年3月12日15:08
 * accidentDate : 2020年3月12日15:08:28
 * accidentAddress : 事故地点
 * disposalPerson : 处置单位
 * keyPerson : 主要人员
 * reportingStaff : 报送人员
 * contactPhone : 联系方式
 * mainDescription : 主要描述
 * phoneftp : shiguxianchang/{uuid}
 */


