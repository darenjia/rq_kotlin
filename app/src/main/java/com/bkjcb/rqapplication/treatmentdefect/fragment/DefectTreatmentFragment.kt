package com.bkjcb.rqapplication.treatmentdefect.fragment

import android.app.Activity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.bkjcb.rqapplication.MyApplication
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.base.fragment.BaseSimpleFragment
import com.bkjcb.rqapplication.base.interfaces.OnTextChangeListener
import com.bkjcb.rqapplication.base.retrofit.NetworkApi
import com.bkjcb.rqapplication.treatmentdefect.TreatmentDetailActivity
import com.bkjcb.rqapplication.treatmentdefect.adapter.DefectTreatmentItemAdapter
import com.bkjcb.rqapplication.treatmentdefect.model.DefectTreatmentModel
import com.bkjcb.rqapplication.treatmentdefect.retrofit.TreatmentService
import com.chad.library.adapter.base.BaseQuickAdapter
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_defect_treatment.*
import kotlinx.android.synthetic.main.search_layout_with_btn_view.*
import java.util.concurrent.TimeUnit

/**
 * Created by DengShuai on 2020/6/10.
 * Description :
 */
class DefectTreatmentFragment : BaseSimpleFragment(), BaseQuickAdapter.OnItemClickListener {
   
    private lateinit var adapter: DefectTreatmentItemAdapter
    private var isLoadMore = false
    private var currentCount = 0
    private var type = 1
    private val code = MyApplication.getUser().areacode.area_code
    private var tempListener: OnTextChangeListener? = null
    fun setType(type: Int) {
        this.type = type
    }

    override fun initView() {
        adapter = DefectTreatmentItemAdapter(R.layout.item_defect_treatment)
        adapter.bindToRecyclerView(defect_content_layout)
        defect_content_layout.layoutManager = LinearLayoutManager(context)
        defect_content_layout.adapter = adapter
       refresh_layout.setColorSchemeColors(resources.getColor(R.color.colorAccent))
    }

    override fun initData() {
        adapter.onItemClickListener = this
        initDisposable()
    }

    private fun initDisposable() {
        disposable = Observable.merge(Observable.create { emitter ->
            station_search_close.setOnClickListener {
                hideSoftInput()
                isLoadMore = false
                emitter.onNext(station_name.text.toString())
            }
            adapter.setOnLoadMoreListener({
                isLoadMore = true
                emitter.onNext(station_name.text.toString())
            }, defect_content_layout)
           refresh_layout.setOnRefreshListener {
                isLoadMore = false
                emitter.onNext(station_name.text.toString())
            }
            tempListener = object : OnTextChangeListener {
                override fun textChange(value: String?) {
                    Logger.d("准备刷新")
                    isLoadMore = false
                    emitter.onNext(station_name.text.toString())
                }
            }
        }, Observable.just(""))
                .debounce(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    if (!isLoadMore) {
                       refresh_layout.isRefreshing = true
                        currentCount = 0
                    } else {
                        currentCount = adapter.data.size
                    }
                }
                .observeOn(Schedulers.io())
                .flatMap { s -> NetworkApi.getService(TreatmentService::class.java).getTreatmentList(type, currentCount, 20, code, s) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ treatmentResult ->
                   refresh_layout.isRefreshing = false
                    if (treatmentResult.isPushSuccess) {
                        if (20 <= treatmentResult.totalCount) {
                            adapter.setEnableLoadMore(true)
                        } else {
                            adapter.setEnableLoadMore(false)
                            //adapter.setOnLoadMoreListener(null);
                        }
                        showResultList(treatmentResult.datas)
                    } else {
                        Toast.makeText(context, "获取数据失败：" + treatmentResult.pushMsg, Toast.LENGTH_SHORT).show()
                        showErrorView()
                    }
                }, { throwable ->
                   refresh_layout.isRefreshing = false
                    Toast.makeText(context, "获取数据错误：" + throwable.message, Toast.LENGTH_SHORT).show()
                    showErrorView()
                })
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        TreatmentDetailActivity.toActivity(activity!!, adapter.getItem(position) as DefectTreatmentModel?)
    }

    private fun hideSoftInput() {
        val inputMethodManager = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isActive) {
            inputMethodManager.hideSoftInputFromWindow(station_name.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    protected fun showResultList(list: List<DefectTreatmentModel?>?) {
        if (list != null && list.isNotEmpty()) {
            if (isLoadMore) {
                adapter.addData(list)
                adapter.loadMoreComplete()
            } else {
                adapter.setNewData(list)
                //adapter.loadMoreEnd();
            }
        } else {
            if (isLoadMore) {
                adapter.loadMoreEnd()
            } else {
                adapter.showEmptyView()
            }
        }
    }

    protected fun showErrorView() {
        if (isLoadMore) {
            adapter.loadMoreFail()
        } else {
            adapter.showErrorView()
        }
    }

    fun refresh() {
        if (tempListener != null) {
            tempListener!!.textChange("")
        }
    }

    override fun initResID(): Int {
        return R.layout.fragment_defect_treatment
    }

    companion object {
        fun newInstance(type: Int): DefectTreatmentFragment {
            val fragment = DefectTreatmentFragment()
            fragment.setType(type)
            return fragment
        }
    }
}