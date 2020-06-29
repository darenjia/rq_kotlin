package com.bkjcb.rqapplication.check.fragment

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.check.model.CheckItem
import com.bkjcb.rqapplication.base.fragment.BaseSimpleFragment
import kotlinx.android.synthetic.main.fragment_check_type_view.*

/**
 * Created by DengShuai on 2019/12/30.
 * Description :
 */
open class ChooseCheckTypeFragment : BaseSimpleFragment(), View.OnClickListener {
    interface OnChooseListener {
        fun choose(type: String?)
    }

    private var listener: OnChooseListener? = null
    var checkItem: CheckItem? = null
    lateinit var typeString: Array<String>

    fun setListener(listener: OnChooseListener?) {
        this.listener = listener
    }

    override fun initResID() = R.layout.fragment_check_type_view

    override fun initView() {
        if (checkItem!!.type == 1) {
            title.text = "企业类型"
        }
        setTextString(check_type1, typeString[0])
        setTextString(check_type2, typeString[1])
        setTextString(check_type3, typeString[2])
        setTextString(check_type4, typeString[3])
        check_type1.setOnClickListener(this)
        check_type2.setOnClickListener(this)
        check_type3.setOnClickListener(this)
        check_type4.setOnClickListener(this)
    }

    override fun initData() {}
    override fun onStart() {
        super.onStart()
        if (checkItem != null && !TextUtils.isEmpty(checkItem!!.zhandianleixing)) {
            updateInfo(checkItem!!.zhandianleixing)
        }
    }

    private fun setTextString(textView: TextView, s: String) {
        if (TextUtils.isEmpty(s)) {
            textView.visibility = View.GONE
        } else {
            textView.text = s
        }
    }

    private fun changeTextColor(textView: TextView?, checked: Boolean = false) {
        if (textView != null) {
            textView.setTextColor(context!!.resources.getColor(if (checked) R.color.colorText else R.color.colorPrimary))
            textView.setBackgroundColor(context!!.resources.getColor(if (checked) R.color.colorPrimary else R.color.colorText))
        }
    }

   override fun onClick(v: View) {
        var name = ""
        when (v.id) {
            R.id.check_type1 -> name = typeString[0]
            R.id.check_type2 -> name = typeString[1]
            R.id.check_type3 -> name = typeString[2]
            R.id.check_type4 -> name = typeString[3]
            else -> {
            }
        }
        listener?.choose(name)
    }

    private fun updateInfo(s: String?) {
        when (s) {
            typeString[0] -> {
                changeTextColor(check_type1, true)
            }
            typeString[1] -> {
                changeTextColor(check_type2, true)
            }
            typeString[2] -> {
                changeTextColor(check_type3, true)
            }
            else -> {
                changeTextColor(check_type4, true)
            }
        }
    }

    companion object {
        fun newInstance(listener: OnChooseListener?, type: Array<String>): ChooseCheckTypeFragment {
            val fragment = ChooseCheckTypeFragment()
            fragment.setListener(listener)
            fragment.typeString = type
            return fragment
        }
    }
}