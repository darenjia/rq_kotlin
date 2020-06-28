package com.bkjcb.rqapplication.emergency.adapter

import android.text.TextUtils
import com.allen.library.SuperTextView
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.emergency.model.Emergency
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by DengShuai on 2017/8/28.
 */
class EmergencyAdapter(layoutResId: Int) : BaseQuickAdapter<Emergency, BaseViewHolder>(layoutResId) {
    override fun convert(holder: BaseViewHolder, emergency: Emergency) {
        val tv = holder.getView<SuperTextView>(R.id.third_content)
        tv.setLeftString(emergency.tel)
                .setRightString(emergency.unit)
        if (!TextUtils.isEmpty(emergency.area) && !TextUtils.isEmpty(emergency.remarks)) {
            tv.setCenterString(emergency.area + "(" + emergency.remarks + ")")
        } else if (!TextUtils.isEmpty(emergency.area) && TextUtils.isEmpty(emergency.remarks)) {
            tv.setCenterString(emergency.area)
        } else {
            tv.setCenterString("")
        }
    }
}