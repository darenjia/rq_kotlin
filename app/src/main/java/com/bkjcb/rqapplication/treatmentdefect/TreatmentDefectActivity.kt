package com.bkjcb.rqapplication.treatmentdefect

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast
import com.bkjcb.rqapplication.base.ftp.UploadTask
import com.bkjcb.rqapplication.base.model.HttpResult
import com.bkjcb.rqapplication.base.retrofit.NetworkApi
import com.bkjcb.rqapplication.treatmentdefect.fragment.TreatmentBackFragment
import com.bkjcb.rqapplication.treatmentdefect.fragment.TreatmentDefectFragment
import com.bkjcb.rqapplication.treatmentdefect.model.DefectDetail
import com.bkjcb.rqapplication.treatmentdefect.model.DefectTreatmentModel
import com.bkjcb.rqapplication.treatmentdefect.retrofit.TreatmentService
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_treatment.*
import kotlinx.android.synthetic.main.empty_view.*

class TreatmentDefectActivity : TreatmentDetailActivity() {
    private var type //true处置隐患 false退单
            = false
    private var fragment: TreatmentBackFragment? = null

    override fun setTitleString(): String {
        return if (type) "处置事件" else "任务退单"
    }

    override fun initView() {
        type = intent.getBooleanExtra("type", true)
        super.initView()
        if (model!!.flag == 0) {
            if (type) {
                info_operation.text = "提交"
                info_export.visibility = View.GONE
            } else {
                info_export.text = "退单"
                info_operation.visibility = View.GONE
            }
        } else {
            operate_layout.visibility = View.GONE
        }
        initEmptyView(empty_view)
    }

    override fun initData() {
        if (model!!.flag > 0) {
            obtainDefectDetail()
        } else {
            fragment = if (type) TreatmentDefectFragment.newInstance(model) else TreatmentBackFragment.newInstance(model)
            loadView()
        }
    }

    override fun createFragment(): Fragment? {
        return fragment
    }

    override fun handleClick(id: Int) {
        submitData()
    }

    private fun submitData() {
        val detail: DefectDetail? = fragment?.prepareSubmit();
        if (detail != null) {
            UploadTask.createUploadTask(detail.filePaths, detail.remotePath)
                    .flatMap { bool ->
                        if (bool) NetworkApi.getService(TreatmentService::class.java).saveTreatmentDefectResult(detail) else null
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<HttpResult> {
                        override fun onComplete() {
                        }

                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                            showLoading(true)
                        }

                        override fun onNext(t: HttpResult) {
                            showLoading(false)
                            if (t == null) {
                                showSnackbar(content, "文件上传失败,请重试")
                            } else {
                                if (t.pushState == 200) {
                                    Toast.makeText(this@TreatmentDefectActivity, "提交成功！", Toast.LENGTH_SHORT).show()
                                    setResult(100)
                                    finish()
                                } else {
                                    showSnackbar(content, "提交失败：" + t.pushMsg)
                                }
                            }
                        }

                        override fun onError(e: Throwable) {
                            showLoading(false)
                            showSnackbar(content, "提交错误：" + e.message)
                        }
                    })

        }
    }

    private fun showErrorView(tip: String?) {
        emptyView!!.show(false, "获取失败", tip, "重试") { obtainDefectDetail() }
    }

    private fun obtainDefectDetail() {
        emptyView!!.show(true, "请稍等", "正在获取数据", null, null)
        disposable = NetworkApi.getService(TreatmentService::class.java)
                .getTreatmentDefectDetail(model!!.mtfId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.isPushSuccess) {
                        emptyView!!.hide()
                        fragment = TreatmentDefectFragment.newInstance(model, result.datas)
                        loadView()
                    } else {
                        showErrorView(result.pushMsg)
                    }
                }, { throwable -> showErrorView(throwable.message) })
    }
    companion object{
        fun toActivity(context: Activity, model: DefectTreatmentModel?, type: Boolean) {
            val intent = Intent(context, TreatmentDefectActivity::class.java)
            intent.putExtra("data", model)
            intent.putExtra("type", type)
            context.startActivityForResult(intent, 100)
        }

        fun toActivity(context: Activity, model: DefectTreatmentModel?) {
            val intent = Intent(context, TreatmentDefectActivity::class.java)
            intent.putExtra("data", model)
            context.startActivity(intent)
        }
    }
}