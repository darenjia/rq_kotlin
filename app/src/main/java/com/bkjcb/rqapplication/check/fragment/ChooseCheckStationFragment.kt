package com.bkjcb.rqapplication.check.fragment

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.OnClick
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.check.adapter.StationAdapter
import com.bkjcb.rqapplication.check.model.CheckItem
import com.bkjcb.rqapplication.check.model.CheckStation
import com.bkjcb.rqapplication.check.retrofit.CheckService
import com.bkjcb.rqapplication.fragment.BaseSimpleFragment
import com.bkjcb.rqapplication.retrofit.NetworkApi
import com.chad.library.adapter.base.BaseQuickAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_check_station_view.*
import kotlinx.android.synthetic.main.search_layout_view.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by DengShuai on 2019/12/30.
 * Description :
 */
class ChooseCheckStationFragment : BaseSimpleFragment(), BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var adapter: StationAdapter
    var checkItem: CheckItem? = null
    private var stationType: String? = ""
    private lateinit var checkStationList: List<CheckStation>
    private var editDisposbale: Disposable? = null
    private var checkType = 0
    fun setCheckType(checkType: Int) {
        this.checkType = checkType
    }


    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        listener?.choose(adapter.data[position] as CheckStation)
    }

    override fun onRefresh() {
        initStationData()
    }

    interface OnChooseListener {
        fun choose(type: CheckStation?)
        fun back()
    }

    private var listener: OnChooseListener? = null

    fun setListener(listener: OnChooseListener?) {
        this.listener = listener
    }


    override fun initResID() = R.layout.fragment_check_station_view

    override fun initView() {
        if (checkItem!!.type == 1) {
            title.text = "企业类型"
        }
        station_list.layoutManager = LinearLayoutManager(context)
        adapter = StationAdapter(R.layout.item_station_view, checkType)
        station_list.adapter = adapter
        adapter.bindToRecyclerView(station_list)
        refresh_layout.setColorSchemeColors(resources.getColor(R.color.colorAccent))
        refresh_layout.setOnRefreshListener(this)
        //@OnClick(R.id.station_search, R.id.station_search_close, R.id.station_back)
        station_back.setOnClickListener(View.OnClickListener {
            listener?.back()
        })
    }

    override fun initData() {
        adapter.onItemClickListener = this
        stationType = when (checkItem!!.zhandianleixing) {
            "维修检查企业" -> "安装维修"
            "报警器企业" -> "报警"
            "销售企业" -> "销售"
            else -> checkItem!!.zhandianleixing
        }
    }

    override fun onStart() {
        super.onStart()
        initStationData()
    }

    //adapter.setEmptyView(null);
    protected fun initStationData() {
        showRefreshing(true)
        disposable = NetworkApi.getService(CheckService::class.java)
                .getCheckUnit(stationType, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    showRefreshing(false)
                    //adapter.setEmptyView(null);
                }
                .subscribe({ checkStationResult ->
                    if (checkStationResult.pushState == 200 && checkStationResult.datas != null && checkStationResult.datas!!.isNotEmpty()) {
                        checkStationList = ArrayList(checkStationResult.datas!!)
                        if (!TextUtils.isEmpty(checkItem!!.beijiandanweiid)) {
                            for (station in checkStationList) {
                                if (station.guid == checkItem!!.beijiandanweiid) {
                                    station.isChecked = true
                                }
                            }
                        }
                        adapter.setNewData(checkStationList)
                        createEditTextDisposable()
                        station_name.text = station_name.text
                    } else {
                        setEmptyView()
                    }
                }) { setEmptyView() }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (editDisposbale != null && !editDisposbale!!.isDisposed) {
            editDisposbale!!.dispose()
        }
    }

    protected fun setEmptyView() {
        adapter.setEmptyView(R.layout.empty_textview, station_list.parent as ViewGroup)
    }

    protected fun showRefreshing(isShow: Boolean) {
        refresh_layout.isRefreshing = isShow
    }

    protected fun createEditTextDisposable() {
        editDisposbale = Observable.create<String> { emitter ->
            station_name.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    emitter.onNext(s.toString())
                }
            })
            station_search_close.setOnClickListener(View.OnClickListener {
                station_name.setText("")
                emitter.onNext("")
            })
        }.debounce(200, TimeUnit.MILLISECONDS) /* .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.trim().length() > 0;
                    }
                })*/
                .subscribeOn(Schedulers.computation())
                .map { s -> filterStation(s) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ strings ->
                    if (strings.size == 0) {
                        adapter.setEmptyView(R.layout.emptyview, station_list.parent as ViewGroup)
                        adapter.setNewData(null)
                    } else {
                        adapter.setNewData(strings)
                    }
                }, { throwable -> Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show() })
    }

    private fun filterStation(s: String): ArrayList<CheckStation> {
        val stations = ArrayList<CheckStation>()
        if (s.isNotEmpty()) {
            for (station in checkStationList) {
                if (!TextUtils.isEmpty(station.qiyemingcheng) && station.qiyemingcheng!!.contains(s) || !TextUtils.isEmpty(station.gas_station) && station.gas_station!!.contains(s)) {
                    stations.add(station)
                }
            }
        } else {
            stations.addAll(checkStationList)
        }
        return stations
    }

    companion object {
        fun newInstance(listener: OnChooseListener?): ChooseCheckStationFragment {
            val fragment = ChooseCheckStationFragment()
            fragment.setListener(listener)
            return fragment
        }

        fun newInstance(listener: OnChooseListener?, type: Int): ChooseCheckStationFragment {
            val fragment = ChooseCheckStationFragment()
            fragment.setListener(listener)
            fragment.setCheckType(type)
            return fragment
        }
    }
}