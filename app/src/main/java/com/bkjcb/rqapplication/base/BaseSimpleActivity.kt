package com.bkjcb.rqapplication.base

import android.content.Context
import android.content.SharedPreferences
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import android.widget.TextView
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.base.view.EmptyView
import com.qmuiteam.qmui.widget.QMUIEmptyView
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialog.CustomDialogBuilder
import io.reactivex.disposables.Disposable

/**
 * Created by DengShuai on 2020/6/24.
 * Description :
 */
abstract class BaseSimpleActivity : BaseActivity2() {
    var emptyView: QMUIEmptyView? = null
    var disposable: Disposable? = null

    var refreshLayout: SwipeRefreshLayout? = null
    private var tipDialog: QMUIDialog? = null

    fun initEmptyView(emptyView: QMUIEmptyView) {
        this.emptyView = emptyView
    }

    fun initTopBar(title: String, barLayout: QMUITopBarLayout) {
        barLayout.setTitle(title)
        barLayout.addLeftBackImageButton().setOnClickListener { finish() }
    }

    fun initTopBar(title: String, alpha: Int, barLayout: QMUITopBarLayout) {
        initTopBar(title, barLayout)
        barLayout.setBackgroundAlpha(alpha)
    }

    fun initTopBar(title: String, listener: View.OnClickListener, barLayout: QMUITopBarLayout) {
        barLayout.setTitle(title)
        barLayout.addLeftBackImageButton().setOnClickListener(listener)
    }

    fun showNoNetworkView(emptyView: EmptyView, listener: View.OnClickListener) {
        emptyView.show("加载失败", "请检查网络是否可用")
        emptyView.show(false, "加载失败", "请检查网络是否可用", "重试", listener)
    }

    fun checkNetwork() {
        if (!netIsEnable()) {
            emptyView?.show(false, "加载失败", "请检查网络是否可用", "重试") { checkNetwork() }
        } else {
            emptyView?.hide()
            networkIsOk()
        }
    }

    protected open fun networkIsOk() {}

    fun showEmptyView(listener: View.OnClickListener?) {
        if (listener != null) {
            emptyView?.show(false, "无数据", "当前记录为空,请刷新试试", "刷新", listener)
        } else {
            emptyView?.show("无数据", "当前记录为空,请刷新试试")
        }
    }

    fun showErrorView(detailText: String?, listener: View.OnClickListener?) {
        if (listener != null) {
            emptyView?.show(false, "失败", detailText, "重试", listener)
        } else {
            emptyView?.show("失败", detailText)
        }
    }

    fun hideEmptyView() {
        emptyView?.hide()
    }

    fun showLoadingView() {
        emptyView?.show(true, "请稍等", "数据加载中", null, null)
    }

    fun showErrorView(listener: View.OnClickListener?) {
        if (listener != null) {
            emptyView?.show(false, "失败", "获取数据错误", "重试", listener)
        } else {
            emptyView?.show("失败", "获取数据错误")
        }
    }

    fun hasData(list: List<*>?, isError: Boolean, listener: View.OnClickListener?) {
        if (list != null && list.isNotEmpty() && !isError) {
            emptyView?.hide()
        } else if (!isError) {
            showEmptyView(listener)
        } else {
            showErrorView(listener)
        }
    }

    fun initSwipeRefreshLayout(listener: SwipeRefreshLayout.OnRefreshListener) {
        refreshLayout?.setColorSchemeColors(resources.getColor(R.color.colorAccent))
        refreshLayout?.setOnRefreshListener(listener)
    }

    fun initSwipeRefreshLayout(refreshLayout: SwipeRefreshLayout, listener: SwipeRefreshLayout.OnRefreshListener) {
        initRefreshLayout(refreshLayout)
        initSwipeRefreshLayout(listener)
    }

    private fun initRefreshLayout(refreshLayout: SwipeRefreshLayout) {
        this.refreshLayout = refreshLayout
    }

    fun showRefreshLayout(isShow: Boolean) {
        refreshLayout?.isRefreshing = isShow
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelDisposable()
    }

    protected open fun cancelDisposable() {
        disposable?.dispose()
    }

    fun getSharedPreferences(): SharedPreferences {
        return getSharedPreferences("user", Context.MODE_PRIVATE)
    }

    fun showSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    fun showLoading(isShow: Boolean) {
        if (tipDialog == null) {
            createDialog()
            tipDialog?.setCanceledOnTouchOutside(false)
            tipDialog?.setCancelable(false)
        }
        if (isShow) {
            tipDialog?.show()
        } else {
            tipDialog?.dismiss()
        }
    }

    private fun createDialog() {
        tipDialog = CustomDialogBuilder(this)
                .setLayout(R.layout.dialog_view)
                .setTitle("请稍等")
                .addAction("取消") { dialog, index ->
                    cancelDisposable()
                    dialog.dismiss()
                }.create()
    }

    fun createDialogView(tip: String): View? {
        val view = View.inflate(this, R.layout.dialog_view, null)
        val text = view.findViewById<TextView>(R.id.dialog_text)
        text.text = tip
        return view
    }

}