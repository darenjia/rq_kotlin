package com.bkjcb.rqapplication.check.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.check.model.ApplianceCheckContentItem
import com.bkjcb.rqapplication.check.model.ApplianceCheckResultItem
import com.bkjcb.rqapplication.check.model.ApplianceCheckResultItem_
import com.bkjcb.rqapplication.datebase.DataUtil
import com.bkjcb.rqapplication.fragment.BaseLazyFragment
import kotlinx.android.synthetic.main.fragment_check_item_detail.*

/**
 * Created by DengShuai on 2020/1/7.
 * Description :
 */
class AlarmCheckItemDetailFragment : BaseLazyFragment() {
    private lateinit var contentItem: ApplianceCheckContentItem
    private var uid //项目id
            : String? = null
    private var checkResultItem: ApplianceCheckResultItem? = null
    var isType = false

    fun setContentItem(contentItem: ApplianceCheckContentItem) {
        this.contentItem = contentItem
    }

    fun setUid(uid: String?) {
        this.uid = uid
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_check_item_detail, null)
            initView()
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        checkResultItem = queryLocalItem()
        if (checkResultItem != null) {
            item_record.setText(checkResultItem!!.content)
        } else {
            checkResultItem = ApplianceCheckResultItem(uid!!, contentItem.guid!!)
        }
    }

    override fun onStop() {
        super.onStop()
        saveData()
    }

    private fun saveData() {
        checkResultItem!!.content = item_record.text.toString()
        DataUtil.getApplianceCheckResultItemBox().put(checkResultItem!!)
    }

    private fun queryLocalItem(): ApplianceCheckResultItem? {
        return DataUtil.getApplianceCheckResultItemBox().query().equal(ApplianceCheckResultItem_.jianchaid, uid!!).and().equal(ApplianceCheckResultItem_.jianchaxiangid, contentItem.guid!!).build().findFirst()
    }

    private fun initView() {
        item_type.text = contentItem.cheaktype
        item_content.text = "${contentItem.xuhao}、${contentItem.cheakname}"
        item_basis.visibility = View.GONE
        item_section.visibility = View.GONE
        if (isType) {
            item_record.isEnabled = false
            item_record.hint = ""
        }
    }

    companion object {
        fun newInstances(contentItem: ApplianceCheckContentItem, id: String?): AlarmCheckItemDetailFragment {
            val fragment = AlarmCheckItemDetailFragment()
            fragment.setContentItem(contentItem)
            fragment.setUid(id)
            return fragment
        }

        fun newInstances(contentItem: ApplianceCheckContentItem, id: String?, type: Boolean): AlarmCheckItemDetailFragment {
            val fragment = AlarmCheckItemDetailFragment()
            fragment.setContentItem(contentItem)
            fragment.setUid(id)
            fragment.isType = type
            return fragment
        }
    }
}