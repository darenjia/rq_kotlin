package com.bkjcb.rqapplication.check.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.io.Serializable

/**
 * Created by DengShuai on 2019/12/23.
 * Description :
 */
@Entity
class CheckItem() : Serializable {
    @Id(assignable = true)
    var id: Long = 0
    var c_id: String? = null
    var zhandianleixing: String? = null
    var beijiandanweiid: String? = null
    var beijiandanwei: String? = null
    var jianchariqi: String? = null
    var jianchajieguo: String? = null
    var beizhu: String? = null
    var filePath: String? = null
    var checkMan: String? = null
    var year: String? = null
    var systime: Long = 0
    var status = 0
    var type //0站点检查 1器具检查
            = 0

    constructor(type: Int) : this() {
        this.type = type
    }
}