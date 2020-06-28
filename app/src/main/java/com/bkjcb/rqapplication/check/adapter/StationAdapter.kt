package com.bkjcb.rqapplication.check.adapter

import com.bkjcb.rqapplication.MyApplication
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.check.model.CheckStation
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by DengShuai on 2019/12/30.
 * Description :
 */
class StationAdapter(layoutResId: Int) : BaseQuickAdapter<CheckStation, BaseViewHolder>(layoutResId) {
    private var type = 0

    constructor(layoutResId: Int, type: Int) : this(layoutResId) {
        this.type = type
    }

    override fun convert(helper: BaseViewHolder, item: CheckStation) {
        if (type == 0) {
            helper.setText(R.id.station_name, item.qiyemingcheng)
                    .setText(R.id.gas_station_name, item.gas_station)
            if (item.isChecked) {
                helper.setTextColor(R.id.station_name, getColor(R.color.colorText))
                        .setTextColor(R.id.gas_station_name, getColor(R.color.colorText))
                        .setBackgroundColor(R.id.station_layout, getColor(R.color.colorAccent))
            } else {
                helper.setTextColor(R.id.station_name, getColor(R.color.colorAccent))
                        .setTextColor(R.id.gas_station_name, getColor(R.color.colorSecondDrayText))
                        .setBackgroundColor(R.id.station_layout, getColor(R.color.colorText))
            }
        } else {
            helper.setText(R.id.station_name, item.qiyemingcheng)
                    .setGone(R.id.gas_station_name, false)
            if (item.isChecked) {
                helper.setTextColor(R.id.station_name, getColor(R.color.colorText))
                        .setBackgroundColor(R.id.station_layout, getColor(R.color.colorAccent))
            } else {
                helper.setTextColor(R.id.station_name, getColor(R.color.colorAccent))
                        .setBackgroundColor(R.id.station_layout, getColor(R.color.colorText))
            }
        }
    }

    private fun getColor(resID: Int): Int {
        return MyApplication.getContext().resources.getColor(resID)
    }
}