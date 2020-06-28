package com.bkjcb.rqapplication.check.model

/**
 * Created by DengShuai on 2019/12/24.
 * Description :
 */
class CheckContentItem {
    /**
     * id : 476da99088c54e87af0bbd2dd29f6885
     * resultCount : 0
     * lastModifiedDate : null
     * markAsDeleted : false
     * zhandianleixing : 储配站
     * year : 2019
     * leibie : 经营条件
     * jianchaneirong : 有经过培训合格的专业技术人员和专业服务人员。
     * jianchayiju : 《条例》19条
     * jianchalanmu : 内业资料
     * xuhao : 3
     * jianchaxiangSet : []
     */
    var id: String? = null
    var zhandianleixing: String? = null
    var year: String? = null
    var leibie: String? = null
    var jianchaneirong: String? = null
    var jianchayiju: String? = null
    var jianchalanmu: String? = null

    /*public static Box<CheckContentItem> getBox() {
        return ObjectBox.get().boxFor(CheckContentItem.class);
    }*/
    var xuhao = 0

}