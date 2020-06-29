package com.bkjcb.rqapplication.gasrecord.adapter

import android.text.TextUtils
import com.bkjcb.rqapplication.MyApplication
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.gasrecord.model.GasUserRecordResult.GasUserRecord
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by DengShuai on 2020/5/6.
 * Description :
 */
class GasWorkRecordAdapter(layoutResId: Int) : BaseQuickAdapter<GasUserRecord, BaseViewHolder>(layoutResId) {
    override fun convert(helper: BaseViewHolder, item: GasUserRecord) {
        helper.setText(R.id.check_time, "建档日期：" + item.jiandangriqi)
                .setText(R.id.check_name, item.yonghuming)
                .setText(R.id.check_operate, item.dizhi)
                .setBackgroundColor(R.id.check_divider, getColor(item.rquserid))
                .addOnClickListener(R.id.check_type)
    }

    private fun getColor(type: String): Int {
        return if (!TextUtils.isEmpty(type)) {
            getColor(R.color.colorMint)
        } else {
            getColor(R.color.colorTextThird)
        }
    }

    private fun getColor(resID: Int): Int {
        return MyApplication.getContext().resources.getColor(resID)
    }

    private fun getColors(type: String): Int {
        return if (type == "0") {
            getColor(R.color.colorMint)
        } else getColor(R.color.colorAccent)
    }
}