package com.bkjcb.rqapplication.gasrecord.fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import butterknife.BindView
import butterknife.OnClick
import com.bkjcb.rqapplication.MyApplication
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.fragment.BaseSimpleFragment
import com.bkjcb.rqapplication.gasrecord.retrofit.GasService
import com.bkjcb.rqapplication.interfaces.OnPageButtonClickListener
import com.bkjcb.rqapplication.retrofit.NetworkApi
import com.bkjcb.rqapplication.treatmentdefect.adapter.AddressItemAdapter
import com.bkjcb.rqapplication.treatmentdefect.model.UserInfoResult
import com.bkjcb.rqapplication.treatmentdefect.model.UserInfoResult.UserInfo
import com.bkjcb.rqapplication.view.CustomLoadMoreView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_gas_search.*
import java.util.concurrent.TimeUnit

/**
 * Created by DengShuai on 2020/4/29.
 * Description :
 */
open class GasUserSearchFragment : BaseSimpleFragment() {

    private lateinit var adapter: AddressItemAdapter
    private var listener: OnPageButtonClickListener<UserInfo>? = null
    private var page = 0
    private var loadMore = false
    private var isHideAdd = false
    fun setHideAdd(hideAdd: Boolean) {
        isHideAdd = hideAdd
    }

    open fun setListener(listener: OnPageButtonClickListener<UserInfo>?) {
        this.listener = listener
    }

    @OnClick(R.id.btn_add)
    fun onClick(v: View?) {
        listener!!.onNext(null)
    }

    override fun initView() {
        address_list.layoutManager = LinearLayoutManager(context)
        adapter = AddressItemAdapter(R.layout.item_address_view)
        adapter.bindToRecyclerView(address_list)
        address_list.adapter = adapter
        adapter.setEnableLoadMore(true)
        adapter.setLoadMoreView(CustomLoadMoreView())
        adapter.setOnItemClickListener { adapter, view, position -> listener?.onClick(adapter.getItem(position) as UserInfo) }
        if (isHideAdd) {
            btn_add.visibility = View.GONE
        }
    }

    override fun initData() {
        //showLoadingView();
        //getData();
        setFilter()
    }

    private fun obtainData() {
        disposable = NetworkApi.getService(GasService::class.java)
                .getUserInfo(page, 20, MyApplication.getUser().area.area_name, "", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.getPushState() == 200) {
                        showResultList(result.datas)
                    } else {
                        showErrorView()
                        Toast.makeText(context, "获取数据失败！", Toast.LENGTH_SHORT).show()
                    }
                }) { showErrorView() }
    }

    protected fun showResultList(data: List<UserInfo?>?) {
        if (data != null && data.isNotEmpty()) {
            if (loadMore) {
                adapter.addData(data)
                adapter.loadMoreComplete()
            } else {
                adapter.setNewData(data)
            }
        } else {
            if (loadMore) {
                adapter.loadMoreEnd()
            } else {
                setEmptyList()
            }
        }
    }

    protected fun setEmptyList() {
        adapter.setNewData(null)
        adapter.setEmptyView(R.layout.empty_textview, address_list.parent as ViewGroup)
    }

    protected fun showErrorView() {
        if (loadMore) {
            adapter.loadMoreFail()
        } else {
            adapter.setNewData(null)
            adapter.setEmptyView(R.layout.error_view, address_list.parent as ViewGroup)
        }
    }

    protected fun showLoadingView() {
        adapter.setEnableLoadMore(false)
        adapter.setNewData(null)
        adapter.setEmptyView(R.layout.loading_view, address_list.parent as ViewGroup)
    }

    private fun setFilter() {
        Observable.merge(Observable.create { emitter ->
            search_view.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    loadMore = false
                    emitter.onNext(s.toString())
                    clear_btn.visibility = View.VISIBLE
                }
            })
            clear_btn.setOnClickListener {
                loadMore = false
                clear_btn.visibility = View.GONE
                search_view.setText("")
                emitter.onNext("")
            }
            adapter.setOnLoadMoreListener({
                loadMore = true
                page = adapter.data.size
                emitter.onNext(search_view.text.toString())
            }, address_list)
        }, Observable.just(""))
                .debounce(500, TimeUnit.MILLISECONDS) /* .filter(new Predicate<String>() {
                     @Override
                     public boolean test(String s) throws Exception {
                         return s.length() > 0;
                     }
                 })*/
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    if (!loadMore) {
                        showLoadingView()
                        page = 0
                    }
                }
                .observeOn(Schedulers.io())
                .flatMap { s ->
                    NetworkApi.getService(GasService::class.java)
                            .getUserInfo(page, 20, MyApplication.getUser().area.area_name, "", s)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserInfoResult> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                        showLoadingView()
                    }

                    override fun onNext(result: UserInfoResult) {
                        if (result.getPushState() == 200) {
                            showResultList(result.datas)
                        } else {
                            showErrorView()
                            Toast.makeText(context, "获取数据失败！", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onError(e: Throwable) {
                        showErrorView()
                    }

                    override fun onComplete() {}
                })
    }

    override fun initResID(): Int {
        return R.layout.fragment_gas_search
    }

    companion object {
        fun newInstance(listener: OnPageButtonClickListener<UserInfo>?): GasUserSearchFragment {
            val fragment = GasUserSearchFragment()
            fragment.setListener(listener)
            return fragment
        }

        @JvmStatic
        fun newInstance(listener: OnPageButtonClickListener<UserInfo>?, isHideAdd: Boolean): GasUserSearchFragment {
            val fragment = GasUserSearchFragment()
            fragment.setListener(listener)
            fragment.setHideAdd(isHideAdd)
            return fragment
        }
    }
}