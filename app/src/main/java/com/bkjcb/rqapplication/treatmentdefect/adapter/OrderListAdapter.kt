package com.bkjcb.rqapplication.treatmentdefect.adapter

import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.treatmentdefect.model.BottleSaleCheck
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by DengShuai on 2019/10/31.
 * Description :
 */
class OrderListAdapter(layoutResId: Int) : BaseQuickAdapter<BottleSaleCheck, BaseViewHolder>(layoutResId) {
    override fun convert(helper: BaseViewHolder, item: BottleSaleCheck) {
        helper.setText(R.id.order_id, item.bottleCode)
                .setText(R.id.order_company, item.unitName)
                .setText(R.id.order_person, item.checkPeople)
                .setText(R.id.order_time, item.saleTime)
    }

    fun showEmpty() {
        setEmptyView(R.layout.empty_textview)
    }

    fun showError() {
        setEmptyView(R.layout.error_view)
    }

    fun showLoading() {
        setEmptyView(R.layout.loading_view)
    }
}