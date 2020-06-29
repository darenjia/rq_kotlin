package com.bkjcb.rqapplication.gasrecord.adapter

import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.gasrecord.model.GasRecordModel
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by DengShuai on 2020/5/6.
 * Description :
 */
class GasTempRecordAdapter(layoutResId: Int) : BaseQuickAdapter<GasRecordModel, BaseViewHolder>(layoutResId) {
    override fun convert(helper: BaseViewHolder, item: GasRecordModel) {
        helper.setText(R.id.check_time, "建档日期：" + item.jiandangriqi)
                .setText(R.id.check_name, item.yonghuming)
                .setText(R.id.check_operate, item.dizhi)
                .addOnClickListener(R.id.check_type)
    }
}