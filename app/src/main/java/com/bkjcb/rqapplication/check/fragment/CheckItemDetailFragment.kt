package com.bkjcb.rqapplication.check.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.check.model.CheckContentItem
import com.bkjcb.rqapplication.check.model.CheckResultItem
import com.bkjcb.rqapplication.check.model.CheckResultItem_
import com.bkjcb.rqapplication.base.datebase.DataUtil
import com.bkjcb.rqapplication.base.fragment.BaseLazyFragment
import kotlinx.android.synthetic.main.fragment_check_item_detail.*

/**
 * Created by DengShuai on 2020/1/7.
 * Description :
 */
class CheckItemDetailFragment : BaseLazyFragment() {
    private lateinit var contentItem: CheckContentItem
    private var uid //项目id
            : String? = null
    private var checkResultItem: CheckResultItem? = null
    private var type = 0 //1不可修改
    fun setType(type: Int) {
        this.type = type
    }

    fun setContentItem(contentItem: CheckContentItem) {
        this.contentItem = contentItem
    }

    fun setUid(uid: String?) {
        this.uid = uid
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_check_item_detail, null)
            item_type.text = contentItem.leibie
            item_content.text ="${contentItem.xuhao}、${contentItem.jianchaneirong}"
            item_basis.text = contentItem.jianchayiju
            item_section.text = contentItem.jianchalanmu!!.replace("<br/>", "/")
            if (type == 1) {
                item_record.isEnabled = false
            }
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        checkResultItem = queryLocalItem()
        if (checkResultItem != null) {
            item_record.setText(checkResultItem!!.jianchajilu)
        } else {
            checkResultItem = CheckResultItem(uid!!, contentItem.id!!)
        }
    }

    override fun onStop() {
        super.onStop()
        saveData()
    }

    private fun queryLocalItem(): CheckResultItem? {
        return DataUtil.getCheckResultItemBox().query().equal(CheckResultItem_.jianchaid, uid!!).and().equal(CheckResultItem_.jianchaxiangid, contentItem!!.id!!).build().findFirst()
    }

    private fun saveData() {
        checkResultItem!!.jianchajilu = item_record.text.toString()
        DataUtil.getCheckResultItemBox().put(checkResultItem!!)
    }

    companion object {
        fun newInstances(contentItem: CheckContentItem, id: String?): CheckItemDetailFragment {
            val fragment = CheckItemDetailFragment()
            fragment.setContentItem(contentItem)
            fragment.setUid(id)
            return fragment
        }

        fun newInstances(contentItem: CheckContentItem, id: String?, type: Int): CheckItemDetailFragment {
            val fragment = CheckItemDetailFragment()
            fragment.setContentItem(contentItem)
            fragment.setUid(id)
            fragment.setType(type)
            return fragment
        }
    }
}