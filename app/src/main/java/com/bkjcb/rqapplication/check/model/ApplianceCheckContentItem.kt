package com.bkjcb.rqapplication.check.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * Created by DengShuai on 2020/2/20.
 * Description :
 */
@Entity
class ApplianceCheckContentItem {
    /**
     * guid : f31335acf25546419d8477dfba44bbe3
     * unitname :
     * xuhao : 11
     * cheakname : 人员培训记录
     * cheaktype : 企业制度制定与执行
     * cheakdatail : 培训记录
     * cheakrecord : 技术培训：
     * cheakrecord2 : 其他培训：
     */
    @Id(assignable = true)
    var id: Long = 0
    var guid: String? = null
    var unitname: String? = null
    var xuhao = 0
    var cheakname: String? = null
    var cheaktype: String? = null
    var cheakdatail: String? = null
    var cheakrecord: String? = null
    var cheakrecord2: String? = null
    var isChecked = false
    var cid: String? = null

}