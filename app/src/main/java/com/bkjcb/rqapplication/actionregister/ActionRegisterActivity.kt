package com.bkjcb.rqapplication.actionregister;

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.bkjcb.rqapplication.BaseSimpleActivity
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.actionregister.adapter.ActionRegisterItemAdapter
import com.bkjcb.rqapplication.actionregister.database.DataUtil.getActionRegisterBox
import com.bkjcb.rqapplication.actionregister.model.ActionRegisterItem
import com.bkjcb.rqapplication.actionregister.model.ActionRegisterItem_
import com.chad.library.adapter.base.BaseQuickAdapter
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton
import kotlinx.android.synthetic.main.activity_main_check.*
import kotlinx.android.synthetic.main.with_bg_top_bar.*

open class ActionRegisterActivity : BaseSimpleActivity(), BaseQuickAdapter.OnItemClickListener {

    private var adapter: ActionRegisterItemAdapter? = null
    var isShowAll = false
    override fun setLayoutID(): Int {
        return R.layout.activity_main_check;
    }

    companion object {
        fun toActivity(context: Context) {
            val intent = Intent(context, ActionRegisterActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun initView() {
        initTopBar(getTitleString(), appbar)
        appbar.addRightImageButton(R.drawable.vector_drawable_create, R.id.top_right_button)
                .setOnClickListener {
                    createNew(-1)
                }
        appbar.addRightImageButton(R.drawable.vector_drawable_all, R.id.top_right_button1)
                .setOnClickListener { v ->
                    val button = v as QMUIAlphaImageButton
                    button.setImageResource(if (isShowAll) R.drawable.vector_drawable_sub else R.drawable.vector_drawable_all)
                    isShowAll = !isShowAll
                    Toast.makeText(this@ActionRegisterActivity, if (isShowAll) "显示未完成" else "显示全部", Toast.LENGTH_SHORT).show()
                    showCheckList()
                }
        createAdapter()
    }

    override fun initData() {
        getHideSetting()
        showCheckList()
    }

    open fun getTitleString(): String {
        return "立案"
    }

    fun getHideSetting() {
        isShowAll = getSharedPreferences()!!.getBoolean("hide_finished", true)
    }

    open fun createAdapter() {
        adapter = ActionRegisterItemAdapter(R.layout.item_checkadapter_view)
        check_list.adapter = adapter;
        check_list.layoutManager = LinearLayoutManager(this)
        adapter?.onItemClickListener = this
        adapter?.bindToRecyclerView(check_list)
    }

    open fun createNew(position: Int) {
        if (position >= 0) {
            CreateActionRegisterActivity.toActivity(this@ActionRegisterActivity, adapter?.getItem(position) as ActionRegisterItem)
        } else {
            CreateActionRegisterActivity.toActivity(this@ActionRegisterActivity, null)
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        createNew(position)
    }



    private fun queryLocalData(): List<ActionRegisterItem>? {
        return if (isShowAll) {
            getActionRegisterBox().all
        } else {
            getActionRegisterBox().query().notEqual(ActionRegisterItem_.status, 2).build().find();
        }
    }

    open fun showCheckList() {
        val list = queryLocalData()
        if (list != null && list.isNotEmpty()) {
            adapter?.setNewData(list)
        } else {
            adapter?.setNewData(null)
            adapter?.emptyView = createEmptyView()
        }
    }

    fun createEmptyView(): View? {
        val view = layoutInflater.inflate(R.layout.empty_textview_with_button, null)
        view.findViewById<View>(R.id.empty_button).setOnClickListener { createNew(-1) }
        return view
    }

    override fun onRestart() {
        super.onRestart()
        showCheckList()
    }
}