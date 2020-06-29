package com.bkjcb.rqapplication.gasrecord

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bkjcb.rqapplication.base.BaseSimpleActivity
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.gasrecord.adapter.GasTempRecordAdapter
import com.bkjcb.rqapplication.gasrecord.model.GasRecordModel
import com.hss01248.dialog.StyledDialog
import com.hss01248.dialog.interfaces.MyDialogListener
import kotlinx.android.synthetic.main.activity_main_check.*
import kotlinx.android.synthetic.main.with_bg_top_bar.*

/**
 * Created by DengShuai on 2020/4/29.
 * Description :
 */
class TempRecordActivity : BaseSimpleActivity() {

    private lateinit var adapter: GasTempRecordAdapter
    private var item: GasRecordModel? = null
    override fun setLayoutID(): Int {
        return R.layout.activity_main_check
    }

    override fun initView() {
        initTopBar("一户一档(暂存)", appbar)
        refresh_layout.isEnabled = false
        adapter = GasTempRecordAdapter(R.layout.item_temp_record_adapter_view)
        check_list.layoutManager = LinearLayoutManager(this)
        check_list.adapter = adapter
        adapter.bindToRecyclerView(check_list)
        adapter.setOnItemClickListener { adapter, view, position ->
            item = adapter.getItem(position) as GasRecordModel?
            GasUserRecordChangeActivity.toActivity(this@TempRecordActivity, item)
        }
        adapter.setOnItemChildClickListener { adapter, view, position ->
            item = adapter.getItem(position) as GasRecordModel?
            showDeleteTip()
        }
    }

    override fun onStart() {
        super.onStart()
        StyledDialog.init(this)
        queryLocalData()
    }

    private fun queryLocalData() {
        showCheckList(GasRecordModel.all())
    }

    protected fun showCheckList(list: List<GasRecordModel?>?) {
        if (list != null && list.isNotEmpty()) {
            adapter.setNewData(list)
        } else {
            adapter.setNewData(null)
            adapter.emptyView = createEmptyView()
        }
    }

    protected fun createEmptyView(): View {
        val view = layoutInflater.inflate(R.layout.empty_textview_with_button, null)
        view.findViewById<View>(R.id.empty_button).visibility = View.GONE
        return view
    }

    private fun showDeleteTip() {
        StyledDialog.buildIosAlert("警告", "当前记录尚未提交，是否删除？", object : MyDialogListener() {
            override fun onFirst() {
                deleteRecord()
                showSnackbar(refresh_layout, "删除成功！")
                queryLocalData()
            }

            override fun onSecond() {}
        }).setBtnText("删除", "取消").show()
    }

    private fun deleteRecord() {
        GasRecordModel.remove(item)
    }

    companion object {
        fun toActivity(context: Context) {
            val intent = Intent(context, TempRecordActivity::class.java)
            context.startActivity(intent)
        }
    }
}