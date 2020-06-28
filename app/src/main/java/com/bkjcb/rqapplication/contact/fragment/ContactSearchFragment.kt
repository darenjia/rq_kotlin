package com.bkjcb.rqapplication.contact.fragment

import android.app.Activity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import co.lujun.androidtagview.TagView.OnTagClickListener
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.contact.ContactDepartmentActivity
import com.bkjcb.rqapplication.contact.adapter.ContactSearchResultAdapter
import com.bkjcb.rqapplication.contact.model.ContactBaseModel
import com.bkjcb.rqapplication.contact.model.Level
import com.bkjcb.rqapplication.contact.model.User
import com.bkjcb.rqapplication.datebase.ContactDataUtil.queryLevel
import com.bkjcb.rqapplication.datebase.ContactDataUtil.queryUser
import com.bkjcb.rqapplication.fragment.BaseSimpleFragment
import com.bkjcb.rqapplication.model.SearchKeyWord
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_contact_main.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by DengShuai on 2020/2/18.
 * Description :
 */
class ContactSearchFragment : BaseSimpleFragment(), BaseQuickAdapter.OnItemClickListener {

    private lateinit var adapter: ContactSearchResultAdapter
    var listener: OnClickListener? = null
    private var isSearchName = false

    fun initListener(listener: OnClickListener) {
        this.listener = listener
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        if (listener != null) {
            if (isSearchName) {
                listener?.onClick(adapter.getItem(position) as User)
            } else {
                ContactDepartmentActivity.toActivity(context!!, (adapter.getItem(position) as Level?)!!.departmentname)
            }
        }
    }

    interface OnClickListener {
        fun onClick(user: User)
    }

    override fun initResID(): Int = R.layout.fragment_contact_main

    override fun initView() {
        adapter = ContactSearchResultAdapter(R.layout.item_contact_view)
        contact_list.layoutManager = LinearLayoutManager(context)
        contact_list.adapter = adapter
        adapter.bindToRecyclerView(contact_list)
        adapter.onItemClickListener = this
        station_search_type.setAdapter(MaterialSpinnerAdapter(context, Arrays.asList("单位", "人名")))
        showTag()
    }

    private fun showTag() {
        val strings: MutableList<String> = ArrayList()
        val words = SearchKeyWord.getAll()
        for (s in words) {
            strings.add(s.key)
        }
        search_history_tag.tags = strings
    }

    override fun initData() {
        station_search_type.setOnItemSelectedListener { _, _, _, item -> isSearchName = item.toString() != "单位" }
        disposable = Observable.merge(Observable.create { emitter ->
            station_name.setOnClickListener { emitter.onNext(station_name.text.toString()) }
            search_history_tag.setOnTagClickListener(object : OnTagClickListener {
                override fun onTagClick(position: Int, text: String) {
                    emitter.onNext(text)
                }
                override fun onTagLongClick(position: Int, text: String) {}
                override fun onSelectedTagDrag(position: Int, text: String) {}
                override fun onTagCrossClick(position: Int) {}
            })
        }, Observable.create<String> { emitter ->
            station_name.setOnEditorActionListener { v, actionId, event ->
                if (v.id == R.id.station_name && actionId == EditorInfo.IME_ACTION_SEARCH) {
                    emitter.onNext(station_name.text.toString())
                }
                false
            }
        })
                .filter { s -> s.trim { it <= ' ' }.length > 0 }.debounce(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .map { s -> filter(s) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { list ->
                    val inputMethodManager = activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(view!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                    showResult(list)
                }
    }

    private fun filter(s: String): List<ContactBaseModel> {
        SearchKeyWord.save(s)
        val list: MutableList<ContactBaseModel> = ArrayList()
        if (isSearchName) {
            list.addAll(queryUser(s))
        } else {
            list.addAll(queryLevel(s))
        }
        return list
    }

    private fun showResult(list: List<ContactBaseModel>) {
        search_title.visibility = View.VISIBLE
        adapter.setNewData(list)
        contact_list.visibility = View.VISIBLE
        search_history.visibility = View.GONE
    }
}