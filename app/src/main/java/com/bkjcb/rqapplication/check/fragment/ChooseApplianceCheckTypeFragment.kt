package com.bkjcb.rqapplication.check.fragment

/**
 * Created by DengShuai on 2019/12/30.
 * Description :
 */
class ChooseApplianceCheckTypeFragment : ChooseCheckTypeFragment() {
    fun newInstance(listener: OnChooseListener?, type: Array<String>): ChooseApplianceCheckTypeFragment {
        val fragment = ChooseApplianceCheckTypeFragment()
        fragment.setListener(listener)
        fragment.typeString = type
        return fragment
    }
}