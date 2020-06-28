package com.bkjcb.rqapplication.contact

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.contact.adapter.ContactItemAdapter
import com.bkjcb.rqapplication.contact.model.User
import com.bkjcb.rqapplication.datebase.ContactDataUtil.queryUserByDepartment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hss01248.dialog.StyledDialog
import kotlinx.android.synthetic.main.activity_contact_department.*
import kotlinx.android.synthetic.main.with_bg_top_bar.*

/**
 * Created by DengShuai on 2020/3/31.
 * Description :
 */
class ContactDepartmentActivity : ContactActivity(), BaseQuickAdapter.OnItemClickListener {
    private lateinit var keyWord: String
    private lateinit var itemAdapter: ContactItemAdapter
    override fun setLayoutID(): Int {
        return R.layout.activity_contact_department
    }

    override fun initView() {
        StyledDialog.init(this)
        keyWord = intent.getStringExtra("Key")
        initTopBar(keyWord,appbar)
        search_result_list.layoutManager = LinearLayoutManager(this)
        itemAdapter = ContactItemAdapter(R.layout.item_contact_view)
        search_result_list.adapter = itemAdapter
    }

    override fun initData() {
        itemAdapter.onItemClickListener = this
        itemAdapter.setNewData(queryUserByDepartment(keyWord))
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val user = adapter.getItem(position) as User?
        alertUserInfo(user!!)
    }

    companion object {
        fun toActivity(context: Context, key: String?) {
            val intent = Intent(context, ContactDepartmentActivity::class.java)
            intent.putExtra("Key", key)
            context.startActivity(intent)
        }
    }
}