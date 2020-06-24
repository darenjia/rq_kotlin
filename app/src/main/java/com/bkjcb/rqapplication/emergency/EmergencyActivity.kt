package com.bkjcb.rqapplication.emergency

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.actionregister.ActionRegisterActivity
import com.bkjcb.rqapplication.actionregister.adapter.ActionRegisterItemAdapter
import com.bkjcb.rqapplication.emergency.adapter.EmergencyItemAdapter
import com.bkjcb.rqapplication.emergency.model.EmergencyItem
import com.bkjcb.rqapplication.emergency.model.EmergencyItem_
import kotlinx.android.synthetic.main.activity_main_check.*

/**
 * Created by DengShuai on 2020/6/24.
 * Description :
 */
class EmergencyActivity : ActionRegisterActivity() {
    private var adapter: EmergencyItemAdapter? = null

    companion object {
        fun toActivity(context: Context) {
            val intent = Intent(context, EmergencyActivity::class.java);
            context.startActivity(intent);
        }
    }

    override fun createAdapter() {
        adapter = EmergencyItemAdapter(R.layout.item_checkadapter_view);
        check_list.adapter = adapter;
        check_list.layoutManager = LinearLayoutManager(this)
        adapter?.onItemClickListener = this
        adapter?.bindToRecyclerView(check_list)
    }

    override fun createNew(position: Int) {
        if (position >= 0) {
            CreateEmergencyActivity.ToActivity(this@EmergencyActivity, adapter?.getItem(position))
        } else {
            CreateEmergencyActivity.ToActivity(this@EmergencyActivity, null)
        }
    }

    override fun getTitleString(): String {
        return "事故现场"
    }

    private fun queryLocalData(): List<EmergencyItem?>? {
        return if (isShowAll) {
            EmergencyItem.getBox().all
        } else {
            EmergencyItem.getBox().query().notEqual(EmergencyItem_.status, 2).build().find()
        }
    }

    override fun showCheckList() {
        val list = queryLocalData()
        if (list != null && list.isNotEmpty()) {
            adapter?.setNewData(list)
        } else {
            adapter?.setNewData(null)
            adapter?.emptyView = createEmptyView()
        }
    }
}