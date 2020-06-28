package com.bkjcb.rqapplication.check.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.check.model.CheckItem
import com.bkjcb.rqapplication.fragment.BaseLazyFragment
import kotlinx.android.synthetic.main.fragment_check_item_result.*

/**
 * Created by DengShuai on 2020/1/7.
 * Description :
 */
class CheckItemResultFragment : BaseLazyFragment(), RadioGroup.OnCheckedChangeListener {
    private var checkItem: CheckItem? = null
    private var mItemRecord: EditText? = null
    private var ok: RadioButton? = null
    private var failure: RadioButton? = null

    fun setCheckItem(checkItem: CheckItem?) {
        this.checkItem = checkItem
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_check_item_result, null)
            item_record.setText(checkItem!!.beizhu)
            if (!TextUtils.isEmpty(checkItem!!.jianchajieguo)) {
                if ("合格" == checkItem!!.jianchajieguo) {
                    check_result_radioGroup.check(R.id.check_result_radio_ok)
                    changeTextColor(true)
                } else {
                    check_result_radioGroup.check(R.id.check_result_radio_failure)
                    changeTextColor(false)
                }
            }
            check_result_radioGroup.setOnCheckedChangeListener(this)
        }
        return rootView
    }


    override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
        if (checkedId == R.id.check_result_radio_ok) {
            checkItem!!.jianchajieguo = "合格"
            changeTextColor(true)
        } else {
            checkItem!!.jianchajieguo = "不合格"
            changeTextColor(false)
        }
    }

    private fun changeTextColor(isOk: Boolean) {
        if (isOk) {
            check_result_radio_ok.setTextColor(resources.getColor(R.color.colorText))
            check_result_radio_failure.setTextColor(resources.getColor(R.color.colorSecondDrayText))
        } else {
            check_result_radio_ok.setTextColor(resources.getColor(R.color.colorSecondDrayText))
            check_result_radio_failure.setTextColor(resources.getColor(R.color.colorText))
        }
    }

    val remark: String
        get() =item_record.text.toString()

    companion object {
        fun newInstances(item: CheckItem?): CheckItemResultFragment {
            val fragment = CheckItemResultFragment()
            fragment.setCheckItem(item)
            return fragment
        }
    }
}