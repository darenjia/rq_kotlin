package com.bkjcb.rqapplication.check

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.bkjcb.rqapplication.BaseSimpleActivity
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.check.CheckResultDetailActivity
import com.bkjcb.rqapplication.check.fragment.ChooseCheckInfoFragment
import com.bkjcb.rqapplication.check.fragment.ChooseCheckStationFragment
import com.bkjcb.rqapplication.check.fragment.ChooseCheckTypeFragment
import com.bkjcb.rqapplication.check.model.CheckItem
import com.bkjcb.rqapplication.check.model.CheckStation
import com.bkjcb.rqapplication.datebase.DataUtil
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import kotlinx.android.synthetic.main.with_bg_top_bar.*

class CreateCheckTaskActivity : BaseSimpleActivity() {
    private lateinit var checkTypeFragment: ChooseCheckTypeFragment
    private var currentPage = 0
    private lateinit var stationFragment: ChooseCheckStationFragment
    private lateinit var infoFragment: ChooseCheckInfoFragment
    private lateinit var checkItem: CheckItem
    override fun setLayoutID(): Int {
        return R.layout.activity_create_check_task
    }

    override fun initView() {
        initTopBar("新建检查任务", appbar)
        updateTitle()
        checkTypeFragment = ChooseCheckTypeFragment.newInstance(object :ChooseCheckTypeFragment.OnChooseListener{
            override fun choose(type: String?) {
                checkItem.zhandianleixing = type
                changeView(1)
            }
        }, arrayOf("气化站", "储配站", "供应站", "加气站"))
        stationFragment = ChooseCheckStationFragment.newInstance(object : ChooseCheckStationFragment.OnChooseListener {
            override fun choose(type: CheckStation?) {
                checkItem.beijiandanweiid = type?.guid
                checkItem.beijiandanwei = type?.gas_station
                changeView(2)
            }

            override fun back() {
                changeView(0)
            }
        })
        infoFragment = ChooseCheckInfoFragment.newInstance(object : ChooseCheckInfoFragment.OnChooseListener {
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
        val item: CheckItem? = intent.getSerializableExtra("Data") as CheckItem
        checkItem = item ?: CheckItem()
        checkTypeFragment.checkItem = checkItem
        stationFragment.checkItem=checkItem
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
                "(3/3)"
            }
        }
        appbar.setTitle(title)
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
        showSnackbar(appbar, "创建检查任务成功！")
        //CheckDetailActivity.ToActivity(this,id);
        CheckResultDetailActivity.toActivity(this, id)
        finish()
    }

    companion object {
        fun toActivity(context: Context) {
            val intent = Intent(context, CreateCheckTaskActivity::class.java)
            context.startActivity(intent)
        }

        fun toActivity(context: Context, checkItem: CheckItem?) {
            val intent = Intent(context, CreateCheckTaskActivity::class.java)
            intent.putExtra("Data", checkItem)
            context.startActivity(intent)
        }
    }
}