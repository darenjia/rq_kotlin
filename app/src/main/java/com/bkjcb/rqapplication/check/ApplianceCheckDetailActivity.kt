package com.bkjcb.rqapplication.check

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.widget.Toast
import com.bkjcb.rqapplication.adapter.ViewPagerAdapter
import com.bkjcb.rqapplication.check.fragment.ApplianceCheckItemDetailFragment
import com.bkjcb.rqapplication.check.model.ApplianceCheckContentItem
import com.bkjcb.rqapplication.check.model.ApplianceCheckResultItem
import com.bkjcb.rqapplication.check.model.ApplianceCheckResultItem_
import com.bkjcb.rqapplication.check.model.CheckItem
import com.bkjcb.rqapplication.check.retrofit.ApplianceCheckService
import com.bkjcb.rqapplication.datebase.DataUtil
import com.bkjcb.rqapplication.retrofit.NetworkApi.Companion.getService
import com.hss01248.dialog.StyledDialog
import com.hss01248.dialog.interfaces.MyDialogListener
import io.objectbox.Box
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_check.*
import java.util.*

/**
 * Created by DengShuai on 2019/12/19.
 * Description :
 */
open class ApplianceCheckDetailActivity : CheckDetailActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StyledDialog.init(this)
    }

    override fun saveResult() {
        /*if (TextUtils.isEmpty(checkItem.jianchajieguo)) {

        } else {

        }*/
        var fragment: ApplianceCheckItemDetailFragment
        if (fragmentList[check_detail_pager.currentItem] is ApplianceCheckItemDetailFragment) {
            fragment = fragmentList[check_detail_pager.currentItem] as ApplianceCheckItemDetailFragment
            fragment.saveData()
        }
        var pager = 0
        if (verify().also { pager = it } == -1) {
            createRemarkDialog()
        } else {
            check_detail_pager.setCurrentItem(pager, true)
            fragment = fragmentList[pager] as ApplianceCheckItemDetailFragment
            fragment.scrollToBottom()
            showSnackbar(check_detail_pager, "请填写检查内容记录")
        }
        /* */
    }

    override fun initCheckContent() {
        val list: List<ApplianceCheckContentItem>? = DataUtil.getContentItems(checkItem!!.c_id!!)
        if (list != null && list.isNotEmpty()) {
            initCheckData(list)
            initImageListView()
            hideEmptyView()
        } else {
            initDataFromNet()
        }
    }

    protected open fun initDataFromNet() {
        disposable = getService(ApplianceCheckService::class.java).fixCheckItem()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.pushState == 200 && result.datas != null && result.datas!!.size > 0) {
                        saveCheckContent(result.datas!!)
                        initCheckData(result.datas!!)
                        initImageListView()
                        hideEmptyView()
                    } else {
                        getDateFail(result.pushMsg)
                    }
                }) { throwable -> getDateFail(throwable.message) }
    }

    protected fun saveCheckContent(datas: List<ApplianceCheckContentItem>) {
        for (item in datas) {
            item.cid = checkItem!!.c_id
        }
        DataUtil.getApplianceCheckContentItemBox().put(datas)
    }

    fun initCheckData(datas: List<ApplianceCheckContentItem>) {
        fragmentList = ArrayList()
        contents = ArrayList()
        for (item in datas) {
            fragmentList.add(createFragment(item, checkItem!!.c_id)!!)
            saveResultItem(checkItem!!.c_id!!, item.guid!!)
            contents.add("${item.xuhao}.${item.cheakname}")
        }
        val adapter = ViewPagerAdapter(supportFragmentManager, fragmentList)
        check_detail_pager.offscreenPageLimit = 1
        check_detail_pager.addOnPageChangeListener(this)
        check_detail_pager.adapter = adapter
        updateCurrentPageNumber()
    }

    protected open fun createFragment(item: ApplianceCheckContentItem, id: String?): Fragment? {
        return ApplianceCheckItemDetailFragment.newInstances(item, id, checkItem!!.status == 3)
    }

    override fun saveResultItem(cid: String, uid: String) {
        if (DataUtil.getApplianceCheckResultItemBox().query().equal(ApplianceCheckResultItem_.jianchaid, cid).and().equal(ApplianceCheckResultItem_.jianchaxiangid, uid).build().count() == 0L) {
            val resultItem = ApplianceCheckResultItem(cid, uid)
            resultItem.content = ""
            resultItem.remark = ""
            resultItem.ischeck = "1"
            DataUtil.getApplianceCheckResultItemBox().put(resultItem)
        }
    }

    private fun createRemarkDialog() {
        var content: String? = ""
        if (!TextUtils.isEmpty(checkItem!!.beizhu)) {
            content = checkItem!!.beizhu
        }
        StyledDialog.buildNormalInput("检查综合意见", "请输入", null, content, null, object : MyDialogListener() {
            override fun onFirst() {
                //closeActivity(true);
                showFinishTipDialog()
            }

            override fun onSecond() {
                closeActivity(false)
            }

            override fun onGetInput(input1: CharSequence, input2: CharSequence) {
                super.onGetInput(input1, input2)
                saveData(input1.toString())
            }
        }).setBtnText("保存并提交", "保存")
                .show()
    }

    private fun saveData(data: String?) {
        if (data != null) {
            checkItem!!.beizhu = data
        }
    }

    override fun closeActivity(isSubmit: Boolean) {
        if (isSubmit) {
            checkItem!!.status = 2
        }
        saveDate()
        Toast.makeText(this, "检查完成！", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun verify(): Int {
        var resultItem: ApplianceCheckResultItem?
        val contentItems: List<ApplianceCheckContentItem>? = DataUtil.getContentItems(checkItem!!.c_id!!)
        if (contentItems != null) {
            for (i in contentItems.indices) {
                val item = contentItems[i]
                if (queryResult(item.guid!!).also { resultItem = it } != null) {
                    if (resultItem != null && resultItem!!.ischeck == "0") {
                        if ((!TextUtils.isEmpty(item.cheakrecord) || !TextUtils.isEmpty(item.cheakrecord2)) && TextUtils.isEmpty(resultItem!!.content) && TextUtils.isEmpty(resultItem!!.remark)) {
                            return i
                        }
                    }
                }
            }
        }
        return -1
    }

    private fun queryResult(id: String): ApplianceCheckResultItem? {
        return DataUtil.getApplianceCheckResultItemBox().query().equal(ApplianceCheckResultItem_.jianchaid, checkItem!!.c_id!!).equal(ApplianceCheckResultItem_.jianchaxiangid, id).build().findUnique()
    }

    companion object {
        fun toActivity(context: Context, checkItem: CheckItem?) {
            val intent = Intent(context, ApplianceCheckDetailActivity::class.java)
            intent.putExtra("data", checkItem)
            context.startActivity(intent)
        }

        fun toActivity(context: Context, id: Long) {
            val intent = Intent(context, ApplianceCheckDetailActivity::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
    }
}