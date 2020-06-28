package com.bkjcb.rqapplication.check

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.bkjcb.rqapplication.check.model.ApplianceCheckResultItem
import com.bkjcb.rqapplication.check.model.ApplianceCheckResultItem_
import com.bkjcb.rqapplication.check.model.CheckItem
import com.bkjcb.rqapplication.check.retrofit.ApplianceCheckService
import com.bkjcb.rqapplication.datebase.DataUtil
import com.bkjcb.rqapplication.ftp.FtpUtils.UploadProgressListener
import com.bkjcb.rqapplication.ftp.UploadTask.createUploadTask
import com.bkjcb.rqapplication.retrofit.NetworkApi.Companion.getService
import com.bkjcb.rqapplication.util.Utils.getFTPPath
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by DengShuai on 2019/12/19.
 * Description :
 */
class ApplianceCheckResultDetailActivity : CheckResultDetailActivity() {
    override fun submitResult() {
        showLoading(true)
        val list = queryResult()
        val paths: MutableList<String?> = ArrayList()
        if (!TextUtils.isEmpty(checkItem.filePath)) {
            paths.addAll(checkItem.filePath!!.split(",".toRegex()))
        }
        disposable = createUploadTask(paths, getFTPPath(checkItem), UploadProgressListener { currentStep, uploadSize, size, file -> }).subscribeOn(Schedulers.io())
                .flatMap { aBoolean ->
                    if (aBoolean) {
                        val service = getService(ApplianceCheckService::class.java)
                        when (checkItem.zhandianleixing) {
                            "维修检查企业" -> service.saveDailyCheck(
                                    null,
                                    checkItem.checkMan,
                                    checkItem.beijiandanweiid,
                                    checkItem.jianchariqi,
                                    checkItem.beizhu,
                                    getItemsID(list),
                                    getItemsResult(list),
                                    getFTPPath(checkItem))
                            "报警器企业" -> service.saveAlarmDailyCheck(
                                    null,
                                    checkItem.checkMan,
                                    checkItem.beijiandanweiid,
                                    checkItem.jianchariqi,
                                    checkItem.beizhu,
                                    getItemsID(list),
                                    getItemsResultRecord(list),
                                    getFTPPath(checkItem))
                            "销售企业" -> service.saveSaleDailyCheck(
                                    null,
                                    checkItem.checkMan,
                                    checkItem.beijiandanweiid,
                                    checkItem.jianchariqi,
                                    checkItem.beizhu,
                                    getItemsID(list),
                                    getItemsResult(list),
                                    getFTPPath(checkItem))
                            else -> null
                        }
                    } else {
                        null
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.pushState == 200) {
                        updateTaskStatus()
                    }
                    showTipInfo( result.pushMsg)
                    showLoading(false)
                }) { throwable ->
                    showLoading(false)
                    showTipInfo("上传失败！" + throwable.message)
                }
    }

    override fun toCheckContentActivity() {
        when (checkItem.zhandianleixing) {
            "维修检查企业" -> ApplianceCheckDetailActivity.ToActivity(this, checkItem.id)
            "报警器企业" -> AlarmCheckDetailActivity.ToActivity(this, checkItem.id)
            "销售企业" -> SellCheckDetailActivity.ToActivity(this, checkItem.id)
        }
    }

    private fun getItemsID(list: List<ApplianceCheckResultItem>): String {
        val builder = StringBuilder()
        for (i in list.indices) {
            builder.append(list[i].jianchaxiangid).append(",")
        }
        return builder.substring(0, builder.length - 1)
    }

    private fun getItemsResult(list: List<ApplianceCheckResultItem>): String {
        return  Gson().toJson(list)
    }

    private fun getItemsResultRecord(list: List<ApplianceCheckResultItem>): String {
        val builder = StringBuilder()
        for (i in list.indices) {
            if (TextUtils.isEmpty(list[i].content)) {
                builder.append("").append(",")
            } else {
                builder.append(list[i].content).append(",")
            }
        }
        return builder.substring(0, builder.length - 1)
    }

    private fun queryResult(): List<ApplianceCheckResultItem> {
        return DataUtil.getApplianceCheckResultItemBox().query().equal(ApplianceCheckResultItem_.jianchaid, checkItem.c_id!!).build().find()
    }

    companion object {
         fun toActivity(context: Context, checkItem: CheckItem?) {
            val intent = Intent(context, ApplianceCheckResultDetailActivity::class.java)
            intent.putExtra("data", checkItem)
            context.startActivity(intent)
        }

        fun toActivity(context: Context, id: Long) {
            val intent = Intent(context, ApplianceCheckResultDetailActivity::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
    }
}