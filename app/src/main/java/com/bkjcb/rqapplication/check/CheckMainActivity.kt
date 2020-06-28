package com.bkjcb.rqapplication.check

import android.content.Context
import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import butterknife.BindView
import com.bkjcb.rqapplication.BaseSimpleActivity
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.check.CheckMainActivity
import com.bkjcb.rqapplication.check.adapter.CheckItemAdapter
import com.bkjcb.rqapplication.check.model.CheckItem
import com.bkjcb.rqapplication.check.model.CheckItem_
import com.bkjcb.rqapplication.datebase.DataUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import io.objectbox.kotlin.equal
import kotlinx.android.synthetic.main.activity_main_check.*
import kotlinx.android.synthetic.main.with_bg_top_bar.*

/**
 * Created by DengShuai on 2019/12/19.
 * Description :
 */
class CheckMainActivity : BaseSimpleActivity(), BaseQuickAdapter.OnItemClickListener {

    private lateinit var adapter: CheckItemAdapter
    private var isShowAll = false
    private var type = 0
    override fun setLayoutID(): Int {
        return R.layout.activity_main_check
    }

    override fun initView() {
        initTopBar("检查列表", appbar)
        appbar.addRightImageButton(R.drawable.vector_drawable_create, R.id.top_right_button)
                .setOnClickListener {
                    if (type == 0) {
                        CreateCheckTaskActivity.ToActivity(this@CheckMainActivity)
                    } else {
                        CreateApplianceCheckTaskActivity.ToActivity(this@CheckMainActivity)
                    }
                }
        appbar.addRightImageButton(R.drawable.vector_drawable_all, R.id.top_right_button1)
                .setOnClickListener { v ->
                    val button = v as QMUIAlphaImageButton
                    button.setImageResource(if (isShowAll) R.drawable.vector_drawable_sub else R.drawable.vector_drawable_all)
                    isShowAll = !isShowAll
                    Toast.makeText(this@CheckMainActivity, if (isShowAll) "显示未完成" else "显示全部", Toast.LENGTH_SHORT).show()
                    showCheckList()
                }
        //mAppbar.setBackgroundAlpha(0);
        adapter = CheckItemAdapter(R.layout.item_checkadapter_view)
        check_list.layoutManager = LinearLayoutManager(this)
        check_list.adapter = adapter
        adapter.bindToRecyclerView(check_list)
        initSwipeRefreshLayout(refresh_layout, SwipeRefreshLayout.OnRefreshListener {
            adapter.setNewData(queryLocalData())
            showRefreshLayout(false)
        })
    }

    override fun initData() {
        type = intent.getIntExtra("Type", 0)
        adapter.onItemClickListener = this
        initHideSetting()
        showCheckList()
    }

    private fun queryLocalData(): List<CheckItem> {
        return if (isShowAll) {
            DataUtil.getCheckItemBox().query().equal(CheckItem_.type, type).build().find()
        } else {
            DataUtil.getCheckItemBox().query().equal(CheckItem_.type, type).notEqual(CheckItem_.status, 3).build().find()
        }
    }

    private fun queryRemoteDta() {}
    protected fun initHideSetting() {
        isShowAll = getSharedPreferences()!!.getBoolean("hide_finished", true)
    }

    override fun onRestart() {
        super.onRestart()
        showCheckList()
    }

    protected fun showCheckList() {
        val list = queryLocalData()
        if (list.isNotEmpty()) {
            adapter.setNewData(list)
        } else {
            adapter.emptyView = createEmptyView(createClickListener())
        }
    }

    protected fun createClickListener(): View.OnClickListener {
        return if (type == 0) {
            View.OnClickListener { CreateCheckTaskActivity.ToActivity(this@CheckMainActivity) }
        } else {
            View.OnClickListener { CreateApplianceCheckTaskActivity.ToActivity(this@CheckMainActivity) }
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        if (type == 0) {
            CheckResultDetailActivity.ToActivity(this, adapter.getItem(position) as CheckItem?)
        } else {
            ApplianceCheckResultDetailActivity.ToActivity(this, adapter.getItem(position) as CheckItem?)
        }
    }

    private fun createEmptyView(listener: View.OnClickListener): View {
        val view = layoutInflater.inflate(R.layout.empty_textview_with_button, null)
        view.findViewById<View>(R.id.empty_button).setOnClickListener(listener)
        return view
    }

    companion object {
        fun toActivity(context: Context) {
            val intent = Intent(context, CheckMainActivity::class.java)
            context.startActivity(intent)
        }

        fun toActivity(context: Context, type: Int) {
            val intent = Intent(context, CheckMainActivity::class.java)
            intent.putExtra("Type", type)
            context.startActivity(intent)
        }
    }
}