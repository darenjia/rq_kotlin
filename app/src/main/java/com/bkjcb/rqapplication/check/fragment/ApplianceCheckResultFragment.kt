package com.bkjcb.rqapplication.check.fragment

import android.os.Bundle
import android.widget.TextView
import butterknife.BindView
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.check.model.CheckItem
import kotlinx.android.synthetic.main.fragment_appliance_check_result.*

/**
 * Created by DengShuai on 2020/2/21.
 * Description :
 */
class ApplianceCheckResultFragment : CheckResultFragment() {

    override fun initView() {
        super.initView()
        if (checkItem!!.zhandianleixing == "报警器企业") {
            info_remark_title.text = "检查综合意见及整改要求"
        }
    }

    override fun initData() {
        if (checkItem != null) {
            info_type.text = checkItem!!.zhandianleixing
            info_station.text = checkItem!!.beijiandanwei
            info_date.text = checkItem!!.jianchariqi
            info_remark.text = checkItem!!.beizhu
            info_name.text = checkItem!!.checkMan
        }
    }

    override fun initResID() = R.layout.fragment_appliance_check_result

    companion object {
        fun newInstance(checkItem: CheckItem?): ApplianceCheckResultFragment {
            val fragment = ApplianceCheckResultFragment()
            fragment.checkItem = checkItem
            return fragment
        }
    }
}