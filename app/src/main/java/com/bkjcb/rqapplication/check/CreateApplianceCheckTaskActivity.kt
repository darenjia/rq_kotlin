package com.bkjcb.rqapplication.check

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.bkjcb.rqapplication.base.BaseSimpleActivity
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.check.fragment.*
import com.bkjcb.rqapplication.check.model.CheckItem
import com.bkjcb.rqapplication.check.model.CheckStation
import com.bkjcb.rqapplication.base.datebase.DataUtil
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import kotlinx.android.synthetic.main.with_bg_top_bar.*

class CreateApplianceCheckTaskActivity : BaseSimpleActivity() {
    private lateinit var checkTypeFragment: ChooseApplianceCheckTypeFragment
    private var currentPage = 0
    private lateinit var stationFragment: ChooseCheckStationFragment
    private var barLayout: QMUITopBarLayout? = null
    private lateinit var checkItem: CheckItem
    private lateinit var infoFragment: ChooseApplianceCheckInfoFragment
    override fun setLayoutID(): Int {
        return R.layout.activity_create_check_task
    }

    override fun initView() {
        initTopBar("新建器具检查任务", appbar)
        updateTitle()
        checkTypeFragment = ChooseApplianceCheckTypeFragment.newInstance(object : ChooseCheckTypeFragment.OnChooseListener {
            override fun choose(type: String?) {
                checkItem.zhandianleixing = type
                changeView(1)
            }
        }, arrayOf("维修检查企业", "报警器企业", "销售企业", ""))
        stationFragment = ChooseCheckStationFragment.newInstance(object : ChooseCheckStationFragment.OnChooseListener {
            override fun choose(type: CheckStation?) {
                checkItem.beijiandanweiid = type?.guid
                checkItem.beijiandanwei = type?.qiyemingcheng
                changeView(2)
            }

            override fun back() {
                changeView(0)
            }
        }, 1)
        infoFragment = ChooseApplianceCheckInfoFragment.newInstance(object : ChooseCheckInfoFragment.OnChooseListener {
            override fun choose() {
                saveItem()
            }

            override fun back() {
                changeView(1)
            }
        })
    }

    override fun initData() {
        replaceView(checkTypeFragment)
        checkItem = CheckItem(1)
        checkTypeFragment.checkItem = checkItem
        stationFragment.checkItem = checkItem
        infoFragment.checkItem = checkItem
    }

    private fun replaceView(fragment: Fragment?) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.create_task_content, fragment!!)
                .commit()
    }

    override fun onBackPressed() {
        when (currentPage) {
            0 -> {
                super.onBackPressed()
            }
            1 -> {
                changeView(0)
            }
            else -> {
                changeView(1)
            }
        }
    }

    private fun updateTitle() {
        var title = "新建检查任务"
        title += when (currentPage) {
            0 -> {
                "(1/3)"
            }
            1 -> {
                "(2/3)"
            }
            else -> {
                "(2/3)"
            }
        }
        barLayout!!.setTitle(title)
    }

    private fun changeView(page: Int) {
        when (page) {
            0 -> {
                replaceView(checkTypeFragment)
            }
            1 -> {
                replaceView(stationFragment)
            }
            else -> {
                replaceView(infoFragment)
            }
        }
        currentPage = page
        updateTitle()
    }

    private fun saveItem() {
        val id: Long = DataUtil.getCheckItemBox().put(checkItem)
        showSnackbar(barLayout!!, "创建检查任务成功！")
        ApplianceCheckResultDetailActivity.toActivity(this, id)
        finish()
    }

    companion object {
        fun toActivity(context: Context) {
            val intent = Intent(context, CreateApplianceCheckTaskActivity::class.java)
            context.startActivity(intent)
        }
    }
}