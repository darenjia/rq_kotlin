package com.bkjcb.rqapplication.contact.fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import butterknife.BindView
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.contact.adapter.ContactItemAdapter
import com.bkjcb.rqapplication.contact.model.User
import com.bkjcb.rqapplication.datebase.ContactDataUtil
import com.bkjcb.rqapplication.datebase.ContactDataUtil.init
import com.bkjcb.rqapplication.fragment.BaseSimpleFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.qmuiteam.qmui.widget.QMUIEmptyView
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.fragment_contact_main.*

/**
 * Created by DengShuai on 2020/2/18.
 * Description :
 */
class ContactMainFragment : BaseSimpleFragment(), BaseQuickAdapter.OnItemClickListener {


    private lateinit var adapter: ContactItemAdapter
    var listener: OnClickListener? = null

    fun initListener(listener: OnClickListener?) {
        this.listener = listener
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        if (listener != null) {
            listener!!.onClick(adapter.getItem(position) as User)
        }
    }

    override fun initResID(): Int {
        return R.layout.fragment_contact_main
    }

    interface OnClickListener {
        fun onClick(user: User)
    }

    override fun initView() {
        empty_view.show(true, "请稍后", "正在初始化数据", null, null)
        adapter = ContactItemAdapter(R.layout.item_contact_view)
        contact_list.layoutManager = LinearLayoutManager(context)
        contact_list.adapter = adapter
        adapter.bindToRecyclerView(contact_list)
        adapter.onItemClickListener = this
    }

    override fun initData() {
        disposable = Observable.create<List<User>>(ObservableOnSubscribe<List<User>?> { emitter ->
            ContactDataUtil.init(context!!)
            emitter.onNext(ContactDataUtil.obtainAllUsers())
        }).subscribeOn(Schedulers.computation())
                .map { list ->
                    for (user in list) {
                        user.init()
                    }
                    list
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { list ->
                    empty_view.hide()
                    adapter.setNewData(list)
                }
    }
}