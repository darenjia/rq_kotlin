package com.bkjcb.rqapplication.gasrecord

import android.content.Context
import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bkjcb.rqapplication.base.BaseSimpleActivity
import com.bkjcb.rqapplication.MyApplication
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.gasrecord.adapter.TimeLineAdapter
import com.bkjcb.rqapplication.gasrecord.model.ReviewRecord
import com.bkjcb.rqapplication.gasrecord.retrofit.GasService
import com.bkjcb.rqapplication.base.retrofit.NetworkApi
import com.chad.library.adapter.base.BaseQuickAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_record_check.*
import kotlinx.android.synthetic.main.with_bg_top_bar.*

/**
 * Created by DengShuai on 2020/4/29.
 * Description :
 */
class ReviewRecordActivity : BaseSimpleActivity() {

    private lateinit var adapter: TimeLineAdapter
    private var id: String? = null
    private var name: String? = null
    private var isCanChange = false
    override fun setLayoutID(): Int {
        return R.layout.activity_record_check
    }

    override fun initView() {
        isCanChange = MyApplication.getUser().userleixing == "街镇用户"
        initTopBar("复查记录", appbar)
        if (isCanChange) {
            appbar.addRightTextButton("复查", R.id.top_right_button1)
                    .setOnClickListener(createClickListener())
        }
        adapter = TimeLineAdapter(R.layout.item_timeline)
        check_list.layoutManager = LinearLayoutManager(this)
        check_list.adapter = adapter
        adapter.bindToRecyclerView(check_list)
        initSwipeRefreshLayout(refresh_layout, SwipeRefreshLayout.OnRefreshListener {
            showRefreshLayout(true)
            queryRemoteData()
        })
        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position -> GasReviewActivity.toActivity(this@ReviewRecordActivity, adapter.getItem(position) as ReviewRecord?) }
    }

    override fun initData() {
        id = intent.getStringExtra("ID")
        name = intent.getStringExtra("Name")
        queryRemoteData()
    }

    protected fun queryRemoteData() {
        disposable = NetworkApi.getService(GasService::class.java)
                .getRecordList(id, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    showRefreshLayout(false)
                    if (result.pushState == 200) {
                        showResultList(result.data)
                    } else {
                        showError()
                    }
                }) {
                    showRefreshLayout(false)
                    showError()
                }
    }

    private fun showResultList(list: List<ReviewRecord>?) {
        if (list != null && list.isNotEmpty()) {
            adapter.setNewData(list)
        } else {
            adapter.setNewData(null)
            adapter.emptyView = createEmptyView(createClickListener())
        }
    }

    protected fun createEmptyView(listener: View.OnClickListener?): View {
        val view = layoutInflater.inflate(R.layout.empty_textview_with_button, null)
        if (isCanChange) {
            view.findViewById<View>(R.id.empty_button).setOnClickListener(listener)
        } else {
            view.findViewById<View>(R.id.empty_button).visibility = View.GONE
        }
        return view
    }

    private fun showError() {
        adapter.setNewData(null)
        adapter.setEmptyView(R.layout.error_view)
    }

    protected fun createClickListener(): View.OnClickListener {
        return View.OnClickListener { GasReviewActivity.toActivity(this@ReviewRecordActivity, id, 1, name) }
    }

    companion object {
         fun toActivity(context: Context, id: String?, name: String?) {
            val intent = Intent(context, ReviewRecordActivity::class.java)
            intent.putExtra("ID", id)
            intent.putExtra("Name", name)
            context.startActivity(intent)
        }
    }
}