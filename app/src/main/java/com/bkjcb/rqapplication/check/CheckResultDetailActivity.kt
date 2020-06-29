package com.bkjcb.rqapplication.check

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.View
import com.bkjcb.rqapplication.base.BaseSimpleActivity
import com.bkjcb.rqapplication.Constants
import com.bkjcb.rqapplication.MyApplication
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.check.fragment.ApplianceCheckResultFragment
import com.bkjcb.rqapplication.check.fragment.CheckResultFragment
import com.bkjcb.rqapplication.check.model.CheckItem
import com.bkjcb.rqapplication.check.model.CheckResultItem
import com.bkjcb.rqapplication.check.model.CheckResultItem_
import com.bkjcb.rqapplication.check.retrofit.CheckService
import com.bkjcb.rqapplication.base.datebase.DataUtil
import com.bkjcb.rqapplication.base.ftp.FtpUtils.UploadProgressListener
import com.bkjcb.rqapplication.base.ftp.UploadTask.createUploadTask
import com.bkjcb.rqapplication.base.retrofit.NetworkApi.Companion.getService
import com.bkjcb.rqapplication.base.util.Utils.getFTPPath
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_result_check.*
import kotlinx.android.synthetic.main.with_bg_top_bar.*
import java.util.*

/**
 * Created by DengShuai on 2019/12/19.
 * Description :
 */
open class CheckResultDetailActivity : BaseSimpleActivity(), View.OnClickListener {
    protected lateinit var checkItem: CheckItem
    private lateinit var fragment: CheckResultFragment
    override fun setLayoutID(): Int {
        return R.layout.activity_detail_result_check
    }

    override fun initView() {
        initTopBar("检查信息", appbar)
        info_operation.setOnClickListener(this)
        info_export.setOnClickListener(this)
    }

    override fun initData() {
        val item: CheckItem? = intent.getSerializableExtra("data") as CheckItem
        checkItem = if (item == null) {
            val id = intent.getLongExtra("id", 0)
            DataUtil.getCheckItemBox().get(id)
        } else {
            item
        }
        fragment = if (checkItem.type == 0) {
            CheckResultFragment.newInstance(checkItem)
        } else {
            ApplianceCheckResultFragment.newInstance(checkItem)
        }
        showCheckDetail()
        //initTextValue();
        info_export.visibility = if (checkItem.status == 3) View.VISIBLE else View.GONE
        info_operation.text = getOperation(checkItem.status)
    }

    private fun showCheckDetail() {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.content, fragment)
                .commit()
    }

    override fun onRestart() {
        super.onRestart()
        initTextValue()
        fragment.updateData(checkItem)
    }

    private fun initTextValue() {
        checkItem = DataUtil.getCheckItemBox().get(checkItem.id)
        info_operation.text = getOperation(checkItem.status)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.info_operation) {
            if (checkItem.status == 2) {
                submitResult()
            } else {
                toCheckContentActivity()
            }
        } else if (v.id == R.id.info_export) {
            initExportFilePath()
        }
    }

    protected open fun toCheckContentActivity() {
        CheckDetailActivity.toActivity(this, checkItem.id)
    }

    private fun getOperation(status: Int): String {
        return when (status) {
            0 -> "开始检查"
            1 -> "继续检查"
            2 -> "上传提交"
            3 -> "查看详情"
            else -> ""
        }
    }

    private fun initFiles(): List<String?> = if (TextUtils.isEmpty(checkItem.filePath)) {
        ArrayList()
    } else checkItem.filePath!!.split(",")

    protected open fun submitResult() {
        showLoading(true)
        val list = queryResult()
        disposable = createUploadTask(initFiles(), getFTPPath(checkItem), UploadProgressListener { currentStep, uploadSize, size, file -> }).subscribeOn(Schedulers.io())
                .flatMap { aBoolean ->
                    if (aBoolean) getService(CheckService::class.java).saveCheckItem(
                            MyApplication.getUser().userId,
                            checkItem.year,
                            checkItem.zhandianleixing,
                            checkItem.beijiandanweiid,
                            checkItem.beizhu,
                            checkItem.jianchariqi,
                            checkItem.jianchajieguo,
                            getItemsID(list),
                            getItemsResult(list),
                            getFTPPath(checkItem),
                            checkItem.c_id
                    ) else null
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.pushState == 200) {
                        updateTaskStatus()
                    }
                    showTipInfo(result.pushMsg)
                    showLoading(false)
                }) { throwable ->
                    showLoading(false)
                    showTipInfo("上传失败！" + throwable.message)
                }
    }

    protected fun updateTaskStatus() {
        checkItem.status = 3
        DataUtil.getCheckItemBox().put(checkItem)
        initTextValue()
    }

    protected fun showTipInfo(info: String?) {
        showSnackbar(info_operation, info!!)
    }

    private fun getItemsID(list: List<CheckResultItem>): Array<String?> {
        val strings = arrayOfNulls<String>(list.size)
        for (i in list.indices) {
            strings[i] = list[i].jianchaxiangid
        }
        return strings
    }

    private fun getItemsResult(list: List<CheckResultItem>): Array<String?> {
        val strings = arrayOfNulls<String>(list.size)
        for (i in list.indices) {
            strings[i] = if (TextUtils.isEmpty(list[i].jianchajilu)) "" else list[i].jianchajilu
        }
        return strings
    }

    private fun queryResult(): List<CheckResultItem> {
        return DataUtil.getCheckResultItemBox().query().equal(CheckResultItem_.jianchaid, checkItem.c_id!!).build().find()
    }

    private fun initExportFilePath() {
        showLoading(true)
        disposable = getService(CheckService::class.java)
                .getExportPath(checkItem.c_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ exportFilePathResult ->
                    showLoading(false)
                    if (exportFilePathResult.pushState == 200) {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(Constants.BASE_URL + "/rq/push/downloadfile?downloadfile=" + exportFilePathResult.datas)
                        startActivity(intent)
                    } else {
                        showSnackbar(info_export, "生成文件失败！" + exportFilePathResult.pushMsg)
                    }
                }) { throwable ->
                    showLoading(false)
                    showSnackbar(info_export, "获取文件地址失败！" + throwable.message)
                }
    }

    companion object {
        fun toActivity(context: Context, checkItem: CheckItem?) {
            val intent = Intent(context, CheckResultDetailActivity::class.java)
            intent.putExtra("data", checkItem)
            context.startActivity(intent)
        }

        fun toActivity(context: Context, id: Long) {
            val intent = Intent(context, CheckResultDetailActivity::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
    }
}