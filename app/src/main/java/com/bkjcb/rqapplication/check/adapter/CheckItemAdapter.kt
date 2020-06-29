package com.bkjcb.rqapplication.check.adapter

import android.util.Log
import com.bkjcb.rqapplication.MyApplication
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.check.model.CheckItem
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by DengShuai on 2019/12/23.
 * Description :
 */
class CheckItemAdapter(layoutResId: Int) : BaseQuickAdapter<CheckItem, BaseViewHolder>(layoutResId) {
    private var currentTime: Long = 0
    override fun convert(helper: BaseViewHolder, item: CheckItem) {
        if (helper.layoutPosition != 0) {
            val preItem = data[helper.layoutPosition - 1]
            if (isSampleDay(preItem.systime, item.systime)) {
                helper.setGone(R.id.check_date, false)
            } else {
                helper.setGone(R.id.check_date, true)
            }
        }
        helper.setText(R.id.check_date, getTime(item.systime))
                .setText(R.id.check_time, item.jianchariqi)
                .setText(R.id.check_status, getStatus(item.status))
                .setText(R.id.check_name, item.beijiandanwei)
                .setText(R.id.check_type, "#" + item.zhandianleixing)
                .setTextColor(R.id.check_status, getColors(item.status))
                .setBackgroundColor(R.id.check_divider, getColor(item.zhandianleixing!!))
    }

    private fun getTime(time: Long): String {
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

    private fun getStatus(status: Int): String {
        return when (status) {
            0 -> "待检查"
            1 -> "检查中"
            2 -> "待上传"
            3 -> "已完成"
            else -> ""
        }
    }

    private fun getColor(type: String): Int {
        return when (type) {
            "供应站", "维修检查企业" -> getColor(R.color.colorMint)
            "储配站", "报警器企业" -> getColor(R.color.colorGrizzlyBear)
            "加气站", "销售企业" -> getColor(R.color.colorTextThird)
            "气化站" -> getColor(R.color.colorYellow)
            else -> getColor(R.color.colorAccent)
        }
    }

    private fun getColor(resID: Int): Int {
        return MyApplication.getContext().resources.getColor(resID)
    }

    private fun getColors(type: Int): Int {
        return if (type == 3) {
            getColor(R.color.colorMint)
        } else getColor(R.color.colorAccent)
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

    init {
        currentTime = initCurrentTime()
    }
}