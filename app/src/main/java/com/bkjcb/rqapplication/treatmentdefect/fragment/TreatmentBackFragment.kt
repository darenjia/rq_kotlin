package com.bkjcb.rqapplication.treatmentdefect.fragment

import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import com.bkjcb.rqapplication.MyApplication
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.fragment.BaseSimpleFragment
import com.bkjcb.rqapplication.treatmentdefect.model.DefectDetail
import com.bkjcb.rqapplication.treatmentdefect.model.DefectTreatmentModel
import kotlinx.android.synthetic.main.fragment_treatment_back.*

/**
 * Created by DengShuai on 2020/6/12.
 * Description :
 */
open class TreatmentBackFragment : BaseSimpleFragment() {

    protected var model: DefectTreatmentModel? = null
    var result: DefectDetail? = null
    protected var isCanChange = true

    override fun initView() {
        if (model != null) {
            info_time.text = model!!.processTime
            info_accident_type.text = model!!.casesType
            info_opinion.text = model!!.opinions
            info_name.text = model!!.userName
            info_address.text = model!!.userAddress
        }
    }

    override fun initData() {
        if (result == null) {
            result = DefectDetail()
            result!!.userId = MyApplication.getUser().userId
            result!!.mtfId = model!!.mtfId
        }
    }

    protected open fun collectParams() {
        result!!.jzReasons = getText(record_remark)
        result!!.type = "2"
        result!!.remotePath = ""
    }

    protected open fun verifyData(): Boolean {
        return verify(record_remark, result!!.jzReasons, "请填写退单理由！")
    }

    protected fun verify(view: View?, value: String?, tip: String?): Boolean {
        if (TextUtils.isEmpty(value)) {
            view?.requestFocus()
            Toast.makeText(context, tip, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    protected fun getText(view: TextView?): String {
        return view?.text.toString()
    }

    protected fun setText(view: TextView, value: String?) {
        view.text = value
        view.isEnabled = isCanChange
    }

    fun prepareSubmit(): DefectDetail? {
        collectParams()
        return if (verifyData()) result else null
    }

    override fun initResID(): Int {
        return R.layout.fragment_treatment_back
    }

    companion object {
        fun newInstance(model: DefectTreatmentModel?): TreatmentBackFragment {
            val fragment = TreatmentBackFragment()
            fragment.model = model
            return fragment
        }
    }
}