package com.bkjcb.rqapplication.gasrecord

import android.content.Context
import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import butterknife.BindView
import butterknife.OnClick
import com.bkjcb.rqapplication.BaseSimpleActivity
import com.bkjcb.rqapplication.MyApplication
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.SimpleBaseActivity
import com.bkjcb.rqapplication.gasrecord.AddNewGasUserActivity
import com.bkjcb.rqapplication.gasrecord.GasUserRecordActivity
import com.bkjcb.rqapplication.gasrecord.adapter.GasWorkRecordAdapter
import com.bkjcb.rqapplication.gasrecord.model.GasUserRecordResult
import com.bkjcb.rqapplication.gasrecord.model.GasUserRecordResult.GasUserRecord
import com.bkjcb.rqapplication.gasrecord.retrofit.GasService
import com.bkjcb.rqapplication.retrofit.NetworkApi
import com.bkjcb.rqapplication.view.CustomLoadMoreView
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_gas_check.*
import kotlinx.android.synthetic.main.search_layout_with_btn_view.*
import kotlinx.android.synthetic.main.with_bg_top_bar.*
import java.util.concurrent.TimeUnit

/**
 * Created by DengShuai on 2020/4/29.
 * Description :
 */
class GasUserRecordActivity : BaseSimpleActivity() {

    private lateinit var adapter: GasWorkRecordAdapter
    private var page = 0
    private var isLoadMore = false
    private val key = ""
    private var district: String? = null
    private var street = ""
    override fun setLayoutID(): Int {
        return R.layout.activity_gas_check
    }

    override fun initView() {
        initTopBar("一户一档",appbar)
        district = if (MyApplication.getUser().userleixing == "街镇用户" || MyApplication.getUser().userleixing == "区用户") MyApplication.getUser().area.area_name else ""
        street = if (MyApplication.getUser().userleixing == "街镇用户") MyApplication.getUser().areacode.street_jc else ""
        if (street != "") {
            appbar.addRightImageButton(R.drawable.vector_drawable_create, R.id.top_right_button)
                    .setOnClickListener { AddNewGasUserActivity.toActivity(this@GasUserRecordActivity) }
            appbar.addRightImageButton(R.drawable.vector_drawable_temp_list, R.id.top_right_button2)
                    .setOnClickListener { TempRecordActivity.ToActivity(this@GasUserRecordActivity) }
            /*  mAppbar.addRightImageButton(R.drawable.vector_drawable_setting, R.id.top_right_button1)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SettingActivity.ToActivity(GasUserRecordActivity.this);
                        }
                    });*/
        }
        refresh_layout.setColorSchemeColors(resources.getColor(R.color.colorAccent))
        adapter = GasWorkRecordAdapter(R.layout.item_record_adapter_view)
        check_list.layoutManager = LinearLayoutManager(this)
        check_list.adapter = adapter
        adapter.bindToRecyclerView(check_list)
        adapter.setLoadMoreView(CustomLoadMoreView())
        adapter.setOnItemClickListener { adapter, view, position ->
            val item = adapter.getItem(position) as GasUserRecord?
            if (street == "") {
                GasUserRecordDetailActivity.ToActivity(this@GasUserRecordActivity, item!!.yihuyidangid)
            } else {
                GasReviewActivity.ToActivity(this@GasUserRecordActivity, item!!.yihuyidangid, 1, item.yonghuming)
            }
        }
        adapter.setOnItemChildClickListener { adapter, view, position ->
            val item = adapter.getItem(position) as GasUserRecord?
            GasUserRecordDetailActivity.ToActivity(this@GasUserRecordActivity, item!!.yihuyidangid)
        }
    }

    override fun initData() {
        super.initData()
        queryRemoteData()
    }

    //@OnClick(R.id.station_search)
    fun onClick(v: View?) {
        /*  QMUIPopup popup = new QMUIPopup(this,QMUIPopup.DIRECTION_BOTTOM);
        popup.setContentView(R.layout.gas_record_filter_view);
        popup.show(mSearchImage);*/
    }

    /* protected void queryRemoteData() {
        adapter.setEnableLoadMore(false);
        disposable = NetworkApi.getService(GasService.class)
                .getWorkRecords(page, 20, district, street, key)
                .compose(RxJavaUtil.getObservableTransformer())
                .subscribe(new Consumer<GasUserRecordResult>() {
                    @Override
                    public void accept(GasUserRecordResult gasUserRecordResult) throws Exception {
                        showRefreshLayout(false);
                        if (gasUserRecordResult.pushState == 200) {
                            showCheckList(gasUserRecordResult.getDatas());
                            adapter.setEnableLoadMore(true);
                        } else {
                            showErrorView();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showErrorView();
                    }
                });
    }*/
    private fun queryRemoteData() {
        Observable.merge(Observable.create { emitter ->
            refresh_layout.setOnRefreshListener {
                isLoadMore = false
                emitter.onNext(station_name.text.toString())
            }
            adapter.setOnLoadMoreListener({
                isLoadMore = true
                emitter.onNext(station_name.text.toString())
            }, check_list)
            /*  mSearchView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        isLoadMore = false;
                        emitter.onNext(s.toString());
                    }
                });*/station_search_close.setOnClickListener { v: View? ->
            isLoadMore = false
            emitter.onNext(station_name.text.toString())
        }
        }, Observable.just(""))
                .debounce(200, TimeUnit.MILLISECONDS) //.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    if (!isLoadMore) {
                        refresh_layout.isRefreshing = true
                        page = 0
                    } else {
                        page = adapter.data.size
                    }
                }
                .observeOn(Schedulers.io())
                .flatMap { s ->
                    NetworkApi.getService(GasService::class.java)
                            .getWorkRecords(page, 20, district, street, s)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<GasUserRecordResult> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(result: GasUserRecordResult) {
                        if (refresh_layout.isRefreshing) {
                            refresh_layout.isRefreshing = false
                        }
                        if (result.pushState == 200) {
                            if (result.totalCount <= 20) {
                                adapter.setEnableLoadMore(false)
                                //adapter.setOnLoadMoreListener(null)
                            } else {
                                adapter.setEnableLoadMore(true)
                            }
                            showCheckList(result.datas)
                            if (adapter.data.size >= result.totalCount) {
                                adapter.loadMoreEnd()
                            }
                        } else {
                            showErrorView()
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (refresh_layout.isRefreshing) {
                            refresh_layout.isRefreshing = false
                        }
                        showErrorView()
                    }

                    override fun onComplete() {}
                })
    }

    protected fun showCheckList(list: List<GasUserRecord?>?) {
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
                adapter.setNewData(null)
                adapter.emptyView = createEmptyView(createClickListener())
            }
        }
    }

    protected fun createEmptyView(listener: View.OnClickListener?): View {
        val view = layoutInflater.inflate(R.layout.empty_textview_with_button, null)
        if (street != "") {
            view.findViewById<View>(R.id.empty_button).setOnClickListener(listener)
        } else {
            view.findViewById<View>(R.id.empty_button).visibility = View.GONE
        }
        return view
    }

    protected fun createClickListener(): View.OnClickListener {
        return View.OnClickListener { AddNewGasUserActivity.toActivity(this@GasUserRecordActivity) }
    }

    protected fun showErrorView() {
        if (isLoadMore) {
            adapter.loadMoreFail()
        } else {
            showRefreshLayout(false)
            adapter.setNewData(null)
            adapter.setEmptyView(R.layout.error_view, check_list.parent as ViewGroup)
        }
    }

    companion object {
        fun toActivity(context: Context) {
            /*  if (MyApplication.user.getAreacode() != null) {
            Intent intent = new Intent(context, GasUserRecordActivity.class);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "此功能暂只对街镇用户开放", Toast.LENGTH_SHORT).show();
        }*/
            val intent = Intent(context, GasUserRecordActivity::class.java)
            context.startActivity(intent)
        }
    }
}