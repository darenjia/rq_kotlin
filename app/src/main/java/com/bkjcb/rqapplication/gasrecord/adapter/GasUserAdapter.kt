package com.bkjcb.rqapplication.gasrecord.adapter

import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.gasrecord.model.GasUserRecordResult.GasUserRecord
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by DengShuai on 2019/10/22.
 * Description :
 */
class GasUserAdapter(layoutResId: Int) : BaseQuickAdapter<GasUserRecord, BaseViewHolder>(layoutResId) {
    override fun convert(helper: BaseViewHolder, item: GasUserRecord) {
        helper.setText(R.id.road_name, item.yonghuming)
                .setText(R.id.road_address, item.dizhi)
                .setVisible(R.id.code, false)
    }
}