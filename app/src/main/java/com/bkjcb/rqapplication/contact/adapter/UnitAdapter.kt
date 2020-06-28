package com.bkjcb.rqapplication.contact.adapter

import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.contact.model.Level
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by DengShuai on 2020/3/4.
 * Description :
 */
class UnitAdapter(layoutResId: Int) : BaseQuickAdapter<Level, BaseViewHolder>(layoutResId) {

    override fun convert(helper: BaseViewHolder, item: Level) {
        helper.setText(R.id.view_text, item.departmentnamea)
    }
}