package com.bkjcb.rqapplication.actionregister.adapter

import android.util.Log
import com.bkjcb.rqapplication.MyApplication
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.actionregister.model.ActionRegisterItem
import com.bkjcb.rqapplication.util.Utils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by DengShuai on 2020/6/24.
 * Description :
 */
class ActionRegisterItemAdapter(layoutId: Int) : BaseQuickAdapter<ActionRegisterItem, BaseViewHolder>(layoutId) {
    private var currentTime: Long = initCurrentTime()

    override fun convert(helper: BaseViewHolder?, item: ActionRegisterItem?) {
        if (helper!!.layoutPosition != 0) {
            if (isSampleDay(data[helper.layoutPosition - 1].systime, item!!.systime)) {
                helper.setGone(R.id.check_date, false)
            } else {
                helper.setGone(R.id.check_date, true)
            }
        }
        helper.setText(R.id.check_date, getTime(item!!.systime))
                .setText(R.id.check_time, item.crime_time)
                .setText(R.id.check_status, getStatus(item.status))
                .setText(R.id.check_name, item.crime_address)
                .setText(R.id.check_type, item.case_source)
                .setTextColor(R.id.check_status, getColors(item.status)) //.setText(R.id.check_operate, "")
                .setBackgroundColor(R.id.check_divider, Utils.getRandomColor(MyApplication.getContext()))

    }

    private fun getTime(time: Long): String? {
        Log.w("time", "$time:::$currentTime")
        return when {
            time - currentTime > 0 -> {
                "今天"
            }
            currentTime - time < 24 * 3600 -> {
                "昨天"
            }
            else -> {
                SimpleDateFormat("MM-dd", Locale.CHINESE).format(Date(time))
            }
        }
    }

    private fun getStatus(status: Int): String? {
        return when (status) {
            0, 1 -> "待提交"
            2 -> "已提交"
            else -> ""
        }
    }

    private fun getColors(type: Int): Int {
        return if (type == 2) getColor(R.color.colorMint)
        else getColor(R.color.colorAccent)
    }

    private fun getColor(resID: Int): Int {
        return MyApplication.getContext().resources.getColor(resID)
    }


    private fun isSampleDay(time1: Long, time2: Long): Boolean {
        val date1 = Date(time1)
        val date2 = Date(time2)
        return date1.month == date2.month && date1.date == date2.date
    }

    private fun initCurrentTime(): Long {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"))
        calendar[calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH], 0, 0] = 0
        return calendar.timeInMillis
    }
}