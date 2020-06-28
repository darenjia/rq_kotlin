package com.bkjcb.rqapplication.contact.fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.contact.ContactDetailActivity
import com.bkjcb.rqapplication.datebase.ContactDataUtil
import com.bkjcb.rqapplication.emergency.adapter.EmergencyAdapter
import com.bkjcb.rqapplication.emergency.model.Emergency
import com.bkjcb.rqapplication.fragment.BaseSimpleFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hss01248.dialog.StyledDialog
import com.hss01248.dialog.interfaces.MyDialogListener
import kotlinx.android.synthetic.main.fragment_contact_first.*

/**
 * Created by DengShuai on 2020/3/3.
 * Description :
 */
class ContactFirstFragment : BaseSimpleFragment(), BaseQuickAdapter.OnItemClickListener, View.OnClickListener {


    private var listener: OnClickListener? = null

    fun setListener(listener: OnClickListener?) {
        this.listener = listener
    }

    override fun initResID(): Int {
        return R.layout.fragment_contact_first
    }

    interface OnClickListener {
        fun onClick(tel: String?)
    }

    override fun initView() {
        StyledDialog.init(context)
        recycler.layoutManager = LinearLayoutManager(context)
        val adapter = EmergencyAdapter(R.layout.item_emergency_view)
        recycler.adapter = adapter
        adapter.setNewData(initEmergencyData())
        adapter.onItemClickListener = this
    }

    override fun initData() {
        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
        btn3.setOnClickListener(this)
    }
    private fun initEmergencyData(): List<Emergency> = ContactDataUtil.obtainAllEmergency()


    override fun onClick(v: View) {
        var type = 0
        when (v.id) {
            R.id.btn1 -> type = 2
            R.id.btn3 -> type = 1
        }
        ContactDetailActivity.toActivity(context!!, type)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val emergency = adapter.getItem(position) as Emergency?
        if (emergency != null) {
            showDialog(emergency.tel)
        }
    }

    private fun showDialog(tel: String?) {
        StyledDialog.buildIosAlert("是否拨打此号码", tel, object : MyDialogListener() {
            override fun onFirst() {
                listener!!.onClick(tel)
            }

            override fun onSecond() {}
        }).setBtnText("拨打", "取消").show()
    }

    companion object {
        fun newInstance(listener: OnClickListener?): ContactFirstFragment {
            val fragment = ContactFirstFragment()
            fragment.setListener(listener)
            return fragment
        }
    }
}