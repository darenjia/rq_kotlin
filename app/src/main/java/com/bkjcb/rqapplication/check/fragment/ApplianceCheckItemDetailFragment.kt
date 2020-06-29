package com.bkjcb.rqapplication.check.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.check.model.ApplianceCheckContentItem
import com.bkjcb.rqapplication.check.model.ApplianceCheckResultItem
import com.bkjcb.rqapplication.check.model.ApplianceCheckResultItem_
import com.bkjcb.rqapplication.base.datebase.DataUtil
import com.bkjcb.rqapplication.base.fragment.BaseLazyFragment
import kotlinx.android.synthetic.main.fragment_appliance_check_item_detail.*

/**
 * Created by DengShuai on 2020/1/7.
 * Description :
 */
class ApplianceCheckItemDetailFragment : BaseLazyFragment(), RadioGroup.OnCheckedChangeListener {
    private lateinit var contentItem: ApplianceCheckContentItem
    private var uid //项目id
            : String? = null
    private var checkResultItem: ApplianceCheckResultItem? = null
    private var type = false
    fun setType(type: Boolean) {
        this.type = type
    }

    fun setContentItem(contentItem: ApplianceCheckContentItem) {
        this.contentItem = contentItem
    }

    fun setUid(uid: String?) {
        this.uid = uid
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_appliance_check_item_detail, null)
            initView()
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        checkResultItem = queryLocalItem()
        if (checkResultItem != null) {
            item_record.setText(checkResultItem!!.content)
            item_remark.setText(checkResultItem!!.remark)
            if (!TextUtils.isEmpty(checkResultItem!!.ischeck)) {
                if ("1" == checkResultItem!!.ischeck) {
                    check_result_radioGroup.check(R.id.check_result_radio_ok)
                    changeTextColor(true)
                } else if ("0" == checkResultItem!!.ischeck) {
                    check_result_radioGroup.check(R.id.check_result_radio_failure)
                    changeTextColor(false)
                }
            }
        } else {
            checkResultItem = ApplianceCheckResultItem(uid!!, contentItem.guid!!)
        }
    }

    private fun queryLocalItem(): ApplianceCheckResultItem? {
        return DataUtil.getApplianceCheckResultItemBox().query().equal(ApplianceCheckResultItem_.jianchaid, uid!!).and().equal(ApplianceCheckResultItem_.jianchaxiangid, contentItem.guid!!).build().findFirst()
    }

    private fun initView() {
        item_type.text = contentItem.cheaktype
        item_section.text = contentItem.cheakdatail
        item_content.text = "${contentItem.xuhao}、${contentItem.cheakname}"
        if (!TextUtils.isEmpty(contentItem.cheakrecord)) {
            item_record1.visibility = View.VISIBLE
            item_record_title.text = contentItem.cheakrecord
        }
        if (!TextUtils.isEmpty(contentItem.cheakrecord2)) {
            item_record2.visibility = View.VISIBLE
            item_remark_title.text = contentItem.cheakrecord2
        }
        if (type) {
            item_record.isEnabled = false
            item_record.hint = ""
            item_remark.isEnabled = false
            item_remark.hint = ""
            check_result_radio_failure.isEnabled = false
            check_result_radio_ok.isEnabled = false
        } else {
            check_result_radioGroup.setOnCheckedChangeListener(this)
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

    override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
        if (checkedId == R.id.check_result_radio_ok) {
            checkResultItem!!.ischeck = "1"
            changeTextColor(true)
        } else {
            checkResultItem!!.ischeck = "0"
            changeTextColor(false)
        }
    }

    override fun onPause() {
        super.onPause()
        saveData()
    }

    fun saveData() {
        checkResultItem!!.content = item_record.text.toString()
        checkResultItem!!.remark = item_remark.text.toString()
        DataUtil.getApplianceCheckResultItemBox().put(checkResultItem!!)
    }

    fun verify(): Boolean {
        return check_result_radio_failure.isChecked && (item_record!!.visibility == View.GONE || item_record.text.isNotEmpty() || item_remark.visibility == View.GONE || item_remark.text.isNotEmpty())
    }

    fun scrollToBottom() {
        check_content.fullScroll(ScrollView.FOCUS_DOWN)
    }

    companion object {
        fun newInstances(contentItem: ApplianceCheckContentItem, id: String?): ApplianceCheckItemDetailFragment {
            val fragment = ApplianceCheckItemDetailFragment()
            fragment.setContentItem(contentItem)
            fragment.setUid(id)
            return fragment
        }

        fun newInstances(contentItem: ApplianceCheckContentItem, id: String?, type: Boolean): ApplianceCheckItemDetailFragment {
            val fragment = ApplianceCheckItemDetailFragment()
            fragment.setContentItem(contentItem)
            fragment.setUid(id)
            fragment.setType(type)
            return fragment
        }
    }
}