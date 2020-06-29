package com.bkjcb.rqapplication.treatmentdefect

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import butterknife.BindView
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.base.BaseSimpleActivity
import com.bkjcb.rqapplication.base.adapter.ViewPagerAdapter
import com.bkjcb.rqapplication.treatmentdefect.fragment.DefectTreatmentFragment
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.QMUITabSegment
import kotlinx.android.synthetic.main.activity_main_defect.*
import kotlinx.android.synthetic.main.with_bg_top_bar.*
import java.util.*

/**
 * Created by DengShuai on 2020/6/10.
 * Description :
 */
class DefectTreatmentMainActivity : BaseSimpleActivity() {

    private lateinit var fragments: MutableList<Fragment>
    override fun setLayoutID(): Int {
        return R.layout.activity_main_defect
    }

    override fun initView() {
        initTopBar("事件处置",appbar)
    }

    override fun initData() {
        initTab()
        addTab("待处置")
        addTab("已处置")
        fragments = ArrayList()
        fragments.add(DefectTreatmentFragment.newInstance(1))
        fragments.add(DefectTreatmentFragment.newInstance(2))
        val adapter = ViewPagerAdapter(supportFragmentManager, fragments)
        content_pager.adapter = adapter
    }

    private fun initTab() {
        val space = QMUIDisplayHelper.dp2px(this, 16)
       tabSegment.setHasIndicator(true)
       tabSegment.setIndicatorWidthAdjustContent(false)
       tabSegment.mode = QMUITabSegment.MODE_FIXED
       tabSegment.setItemSpaceInScrollMode(space)
       tabSegment.setupWithViewPager(content_pager, false)
       tabSegment.setDefaultSelectedColor(resources.getColor(R.color.color_type_0))
       tabSegment.setDefaultNormalColor(resources.getColor(R.color.colorSecondDrayText))
    }

    private fun addTab(title: String) {
        val tab = QMUITabSegment.Tab(title)
        //tab.setTextColor(getResources().getColor(R.color.colorText), getResources().getColor(R.color.colorYellowWhite));
       tabSegment.addTab(tab)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == 100) {
            refreshData()
        }
    }

    private fun refreshData() {
        for (fragment in fragments) {
            (fragment as DefectTreatmentFragment).refresh()
        }
    }

    companion object {
        fun toActivity(context: Context) {
            val intent = Intent(context, DefectTreatmentMainActivity::class.java)
            context.startActivity(intent)
        }
    }
}