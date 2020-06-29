package com.bkjcb.rqapplication.check.fragment

import android.view.View
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.check.CreateCheckTaskActivity
import com.bkjcb.rqapplication.check.model.CheckItem
import com.bkjcb.rqapplication.base.fragment.BaseSimpleFragment
import kotlinx.android.synthetic.main.fragment_check_result.*

/**
 * Created by DengShuai on 2020/2/21.
 * Description :
 */
open class CheckResultFragment : BaseSimpleFragment() {
    protected var checkItem: CheckItem? = null

    fun updateData(checkItem: CheckItem?) {
        this.checkItem = checkItem
        initData()
    }


    override fun initView() {
        if (checkItem!!.status > 0) {
            info_edit.visibility = View.GONE
        }
        info_edit.setOnClickListener {
            CreateCheckTaskActivity.toActivity(context!!, checkItem)
        }
    }

    override fun initData() {
        if (checkItem != null) {
            info_type.text = checkItem!!.zhandianleixing
            info_station.text = checkItem!!.beijiandanwei
            info_year.text = checkItem!!.year
            info_date.text = checkItem!!.jianchariqi
            info_remark.text = checkItem!!.beizhu
            info_result.text = checkItem!!.jianchajieguo
            info_name.text = checkItem!!.checkMan
        }
    }

    override fun initResID() = R.layout.fragment_check_result

    companion object {
        fun newInstance(checkItem: CheckItem?): CheckResultFragment {
            val fragment = CheckResultFragment()
            fragment.checkItem = checkItem
            return fragment
        }
    }
}