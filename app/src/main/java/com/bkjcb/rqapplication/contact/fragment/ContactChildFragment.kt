package com.bkjcb.rqapplication.contact.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import co.lujun.androidtagview.TagView.OnTagClickListener
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.contact.adapter.ContactItemAdapter
import com.bkjcb.rqapplication.contact.adapter.UnitAdapter
import com.bkjcb.rqapplication.contact.model.Level
import com.bkjcb.rqapplication.contact.model.User
import com.bkjcb.rqapplication.base.datebase.ContactDataUtil
import com.bkjcb.rqapplication.base.datebase.ContactDataUtil.queryLevel
import com.bkjcb.rqapplication.base.datebase.ContactDataUtil.queryUser
import com.bkjcb.rqapplication.base.fragment.BaseSimpleFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_contact_child.*
import java.util.*

/**
 * Created by DengShuai on 2017/9/15.
 */
class ContactChildFragment : BaseSimpleFragment() {

    private var fragmentType = 0
    private lateinit var unitAdapter: UnitAdapter
    private lateinit var itemAdapter: ContactItemAdapter
    private lateinit var strings: ArrayList<String>
    private var listener: OnClickListener? = null
    private var map: HashMap<String?, Level?>? = null
    override fun initResID(): Int {
        return R.layout.fragment_contact_child
    }

    interface OnClickListener {
        fun onClick(user: User?)
    }

    fun setFragmentType(fragmentType: Int) {
        this.fragmentType = fragmentType
    }

    fun setListener(listener: OnClickListener?) {
        this.listener = listener
    }

    override fun initView() {
        val layoutManager1 = FlexboxLayoutManager(context)
        layoutManager1.flexDirection = FlexDirection.ROW
        layoutManager1.justifyContent = JustifyContent.FLEX_START
        departmentListView.layoutManager = layoutManager1
        recycler_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun initData() {
        init()
        unitAdapter = UnitAdapter(R.layout.item_unit_adapter)
        when (fragmentType) {
            0 -> {
                unitAdapter.setNewData(getData(0, -1, -1, -1))
                departmentTitle.text = "市级单位"
                departmentTitle.setBackgroundColor(resources.getColor(R.color.color_type_1))
            }
            1 -> {
                unitAdapter.setNewData(initDistractData())
                departmentTitle.text = "区级单位"
                departmentTitle.setBackgroundColor(resources.getColor(R.color.color_type_2))
            }
            2 -> {
                unitAdapter.setNewData(getData(2, -1, -1, -1))
                departmentTitle.text = "企业单位"
                departmentTitle.setBackgroundColor(resources.getColor(R.color.color_type_0))
            }
        }
        itemAdapter = ContactItemAdapter(R.layout.item_contact_view)
        recycler_list.adapter = itemAdapter
        itemAdapter.bindToRecyclerView(recycler_list)
        departmentListView.adapter = unitAdapter
        setListener()
    }

    private fun init() {
        map = HashMap(5)
        tag_layout.removeAllTags()
        setGone(result_view)
        strings = ArrayList()
        tag_layout.tags = strings
        setVisible(departmentTitle)
        setVisible(departmentListView)
    }

    protected fun setListener() {
        itemAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position -> listener!!.onClick(adapter.getItem(position) as User?) }
        setDisposable()
    }

    private fun filterData(level: Level?) {
        if (level != null && level.level != -1) {
            when {
                level.kind1 == 0 -> {
                    unitAdapter.setNewData(getData(fragmentType, -1, -1, -1, level.quxian))
                }
                level.kind2 == 0 -> {
                    unitAdapter.setNewData(getData(fragmentType, level.kind1, -1, -1, level.quxian))
                }
                level.kind3 == 0 -> {
                    unitAdapter.setNewData(getData(fragmentType, level.kind1, level.kind2, -1, level.quxian))
                }
                else -> {
                    unitAdapter.setNewData(getData(fragmentType, level.kind1, level.kind2, level.kind3, level.quxian))
                }
            }
            if (!map!!.containsKey(level.departmentnamea)) {
                map!![level.departmentnamea] = level
                setStrings(level.departmentnamea)
            } else {
                val index = strings.indexOf(level.departmentnamea) + 1
                for (i in index until strings.size) {
                    map!!.remove(strings[i])
                }
                setStrings(null)
            }
           header.setLeftString(level.departmentnamea)
            departmentTitle.text = level.departmentnamea
        } else {
            when (fragmentType) {
                0 -> {
                    unitAdapter.setNewData(getData(0, -1, -1, -1))
                    departmentTitle.text = "市级单位"
                }
                1 -> {
                    unitAdapter.setNewData(initDistractData())
                    departmentTitle.text = "区级单位"
                }
                2 -> {
                    unitAdapter.setNewData(getData(2, -1, -1, -1))
                    departmentTitle.text = "企业单位"
                }
            }
            departmentTitle.visibility = View.VISIBLE
            strings.clear()
            tag_layout.removeAllTags()
            map!!.clear()
        }
    }

    private fun setDataAndView(users: List<User>) {
        if (users.isNotEmpty() || strings.size > 0) {
            setVisible(result_view)
            itemAdapter.setNewData(users)
            //            changeList(model.getDepartmentNameA());
           header.setRightString(itemAdapter.data.size.toString() + "")
        } else {
            setGone(result_view)
        }
    }

    private fun setDisposable() {
        disposable = Observable.create<Level> { emitter ->
            unitAdapter.setOnItemClickListener { adapter, view, position ->
                val level = adapter.getItem(position) as Level?
                emitter.onNext(level!!)
            }
            tag_layout.setOnTagClickListener(object : OnTagClickListener {
                override fun onTagClick(position: Int, text: String) {
                    if (position != 0) {
                        if (position != strings.size - 1) {
                            emitter.onNext(map!![text]!!)
                        }
                    } else {
                        emitter.onNext(Level(-1))
                    }
                }

                override fun onTagLongClick(position: Int, text: String) {}
                override fun onSelectedTagDrag(position: Int, text: String) {}
                override fun onTagCrossClick(position: Int) {}
            })
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { level -> filterData(level) }
                .observeOn(Schedulers.io())
                .map { level -> getUserData(level) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ users -> setDataAndView(users) }) { throwable -> Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show() }
    }

    private fun getData(level: Int, kind1: Int, kind2: Int, kind3: Int, quxian: String?): List<Level> {
        var qu = quxian
        if (quxian == "0") {
            qu = ""
        }
        return queryLevel(level, kind1, kind2, kind3, qu!!)
    }

    private fun getData(level: Int, kind1: Int, kind2: Int, kind3: Int): List<Level> {
        return queryLevel(level, kind1, kind2, kind3, "")
    }

    private fun initDistractData(): List<Level> = ContactDataUtil.obtainAllDistractName()

    private fun getUserData(level: Level): List<User> {
        return queryUser(level)
    }

    private fun changeList(s: String?) {
        if (s != null) {
            setHeaderView(s, itemAdapter.itemCount.toString())
        }
    }

    private fun setStrings(s: String?) {
        if (s == null) {
            if (strings.size > 0) {
                strings.removeAt(strings.size - 1)
            }
        } else {
            if (strings.size == 0) {
                strings.add(departmentTitle.text.toString())
                departmentTitle.visibility = View.GONE
            }
            strings.add(s)
        }
        tag_layout.tags = strings
    }

    private fun setGone(vararg views: View) {
        for (v in views) {
            v.visibility = View.GONE
        }
    }

    private fun setVisible(vararg views: View) {
        for (v in views) {
            v.visibility = View.VISIBLE
        }
    }

    private fun setHeaderView(name: String, num: String) {
       header.setLeftString(name)
       header.setRightString(num)
    }

    companion object {
        fun newInstance(type: Int, listener: OnClickListener?): ContactChildFragment {
            val fragment = ContactChildFragment()
            fragment.setFragmentType(type)
            fragment.setListener(listener)
            return fragment
        }
    }
}