package com.bkjcb.rqapplication.check.fragment

import android.view.View
import kotlinx.android.synthetic.main.fragment_check_info_view.*

/**
 * Created by DengShuai on 2019/12/30.
 * Description :
 */
class ChooseApplianceCheckInfoFragment : ChooseCheckInfoFragment() {
    override fun initView() {
        super.initView()
        info_year.visibility = View.GONE
    }

    companion object {
        fun newInstance(listener: OnChooseListener?): ChooseApplianceCheckInfoFragment {
            val fragment = ChooseApplianceCheckInfoFragment()
            fragment.setListener(listener)
            return fragment
        }
    }
}