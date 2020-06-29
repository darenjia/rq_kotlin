package com.bkjcb.rqapplication.treatmentdefect.adapter

import android.content.Context
import android.view.View
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.treatmentdefect.model.DefectTreatmentModel
import com.bkjcb.rqapplication.base.view.CustomLoadMoreView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by DengShuai on 2020/6/10.
 * Description :
 */
class DefectTreatmentItemAdapter(layoutResId: Int) : BaseQuickAdapter<DefectTreatmentModel, BaseViewHolder>(layoutResId) {
    override fun convert(helper: BaseViewHolder, item: DefectTreatmentModel) {
        helper.setText(R.id.treatment_type, item.processTime)
                .setText(R.id.treatment_name, item.userName)
                .setText(R.id.treatment_address, item.userAddress)
                .setText(R.id.treatment_time, item.casesType)
                .setText(R.id.treatment_opinion, item.opinions)
                .setGone(R.id.treatment_opinion, item.flag == 0)
        if (item.flag > 0) {
            helper.setText(R.id.treatment_time, obtainStatus(item.processState))
                    .setTextColor(R.id.treatment_time, mContext.resources.getColor(R.color.colorMint))
        }
    }

    private fun obtainStatus(status: Int): String {
        return if (status > 4) {
            "处置完成"
        } else if (status > 2) {
            "已结案"
        } else {
            "退单"
        }
    }

    fun showErrorView(context: Context?, listener: View.OnClickListener?) {
        val view = View.inflate(context, R.layout.error_view, null)
        view.setOnClickListener(listener)
        setNewData(null)
        emptyView = view
    }

    fun showErrorView() {
        setNewData(null)
        setEmptyView(R.layout.error_view)
    }

    fun showLoadingView() {
        setNewData(null)
        setEmptyView(R.layout.loading_view)
    }

    fun showEmptyView() {
        setNewData(null)
        setEmptyView(R.layout.empty_textview)
    }

    init {
        setLoadMoreView(CustomLoadMoreView())
    }
}