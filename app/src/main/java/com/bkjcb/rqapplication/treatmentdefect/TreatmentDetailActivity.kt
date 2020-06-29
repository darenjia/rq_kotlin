package com.bkjcb.rqapplication.treatmentdefect

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import butterknife.BindView
import butterknife.OnClick
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.base.BaseSimpleActivity
import com.bkjcb.rqapplication.treatmentdefect.TreatmentDetailActivity
import com.bkjcb.rqapplication.treatmentdefect.fragment.TreatmentDetailFragment
import com.bkjcb.rqapplication.treatmentdefect.model.DefectTreatmentModel
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton
import kotlinx.android.synthetic.main.activity_detail_treatment.*
import kotlinx.android.synthetic.main.with_bg_top_bar.*

/**
 * Created by DengShuai on 2020/6/12.
 * Description :
 */
open class TreatmentDetailActivity : BaseSimpleActivity(), View.OnClickListener {

    protected var model: DefectTreatmentModel? = null
    override fun setLayoutID(): Int {
        return R.layout.activity_detail_treatment
    }

    override fun initView() {
        initTopBar(setTitleString(), appbar)
        model = intent.getSerializableExtra("data") as DefectTreatmentModel
        info_operation.setOnClickListener(this)
        info_export.setOnClickListener(this)

    }

    override fun initData() {
        loadView()
        if (model!!.flag > 0) {
            operate_layout.visibility = View.GONE
        }
    }

    protected open fun setTitleString(): String = "事件详情"

    protected fun loadView() {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.content, createFragment()!!)
                .commit()
    }

    protected open fun createFragment(): Fragment? {
        return TreatmentDetailFragment.newInstance(model)
    }

    //@OnClick(R.id.info_operation, R.id.info_export)
    override fun onClick(v: View) {
        handleClick(v.id)
    }

    protected open fun handleClick(id: Int) {
        TreatmentDefectActivity.toActivity(this@TreatmentDetailActivity, model, id == R.id.info_operation)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == 100) {
            operate_layout.visibility = View.GONE
            setResult(100)
        }
    }

    companion object {
        fun toActivity(context: Activity, model: DefectTreatmentModel?) {
            val intent = Intent(context, TreatmentDetailActivity::class.java)
            intent.putExtra("data", model)
            context.startActivityForResult(intent, 100)
        }
    }
}