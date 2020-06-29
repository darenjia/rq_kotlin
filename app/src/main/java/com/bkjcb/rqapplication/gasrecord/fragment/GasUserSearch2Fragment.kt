package com.bkjcb.rqapplication.gasrecord.fragment

import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.gasrecord.adapter.GasUserAdapter
import com.bkjcb.rqapplication.gasrecord.model.GasUserRecordResult
import com.bkjcb.rqapplication.gasrecord.model.GasUserRecordResult.GasUserRecord
import com.bkjcb.rqapplication.gasrecord.retrofit.GasService
import com.bkjcb.rqapplication.base.interfaces.OnPageButtonClickListener
import com.bkjcb.rqapplication.base.retrofit.NetworkApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_gas_search.*
import java.util.concurrent.TimeUnit

/**
 * Created by DengShuai on 2020/4/29.
 * Description :
 */
class GasUserSearch2Fragment : GasUserSearchFragment() {
    private lateinit var adapter: GasUserAdapter
    private var clickListener: OnPageButtonClickListener<GasUserRecord>? = null

    override fun initView() {
        btn_add.visibility = View.GONE
        address_list.layoutManager = LinearLayoutManager(context)
        adapter = GasUserAdapter(R.layout.item_address_view)
        adapter.bindToRecyclerView(address_list)
        address_list.adapter = adapter
        adapter.setOnItemClickListener { adapter, view, position -> clickListener?.onClick(adapter.getItem(position) as GasUserRecord) }
    }

     override fun initData() {
        obtainData()
    }

    private fun obtainData() {
            disposable = NetworkApi.getService(GasService::class.java)
                    .getUserList("")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        if (result.pushState == 200) {
                            showResult(result.datas)
                            setFilter()
                        } else {
                            setEmptyList()
                            Toast.makeText(context, "获取数据失败！", Toast.LENGTH_SHORT).show()
                        }
                    }) { throwable ->
                        Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()
                        setEmptyList()
                    }
        }

    private fun setFilter() {
        disposable = Observable.create<String> { emitter ->
            search_view.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    emitter.onNext(s.toString())
                    clear_btn.visibility = View.VISIBLE
                }
            })
            clear_btn.setOnClickListener(View.OnClickListener {
                clear_btn.visibility = View.GONE
                search_view.setText("")
                emitter.onNext("")
            })
        }.debounce(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .filter { s -> s.isNotEmpty() }
                .doAfterNext { showLoading() }
                .flatMap { s ->
                    NetworkApi.getService(GasService::class.java)
                            .getUserList(s)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.pushState == 200) {
                        showResult(result.datas)
                    }
                }) { setEmptyList() }
    }

    private fun showResult(list: List<GasUserRecord>?) {
        if (list != null && list.isNotEmpty()) {
            adapter.setNewData(list)
        } else {
            setEmptyList()
        }
    }

    private fun showLoading() {
        adapter.setNewData(null)
        adapter.setEmptyView(R.layout.loading_view, address_list.parent as ViewGroup)
    }

    companion object {
        fun newInstance(listener: OnPageButtonClickListener<GasUserRecord>?): GasUserSearch2Fragment {
            val fragment = GasUserSearch2Fragment()
            fragment.clickListener = listener
            return fragment
        }
    }
}