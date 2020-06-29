package com.bkjcb.rqapplication.gasrecord.adapter

import android.content.Context
import com.bkjcb.rqapplication.gasrecord.model.GasCompanyResult.GasCompany
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter

/**
 * Created by DengShuai on 2020/4/30.
 * Description :
 */
class GasCompanyAdapter(context: Context?, items: List<GasCompany?>?) : MaterialSpinnerAdapter<GasCompany?>(context, items) {
    override fun getItemText(position: Int): String {
        return getItem(position)!!.name
    }

    override fun getShowText(position: Int): String {
        return get(position)!!.name
    }
}