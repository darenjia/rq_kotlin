package com.bkjcb.rqapplication.gasrecord

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.widget.Toast
import com.bkjcb.rqapplication.MyApplication
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.base.ftp.UploadTask
import com.bkjcb.rqapplication.gasrecord.fragment.GasReviewFragment
import com.bkjcb.rqapplication.gasrecord.fragment.GasUserSearch2Fragment
import com.bkjcb.rqapplication.gasrecord.model.GasUserRecordResult.GasUserRecord
import com.bkjcb.rqapplication.gasrecord.model.ReviewRecord
import com.bkjcb.rqapplication.gasrecord.retrofit.GasService
import com.bkjcb.rqapplication.base.interfaces.OnPageButtonClickListener
import com.bkjcb.rqapplication.base.model.MediaFile
import com.bkjcb.rqapplication.base.retrofit.NetworkApi
import com.bkjcb.rqapplication.base.util.Utils
import com.bkjcb.rqapplication.base.util.Utils.buildUUID
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_gas_user_add.*
import kotlinx.android.synthetic.main.with_bg_top_bar.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class GasReviewActivity : AddNewGasUserActivity() {
    private var record: ReviewRecord? = null
    private var id: String? = null


    override fun initView() {
        initEmptyView(empty_view)
    }

    override fun initData() {
        remoteAddress = "yihuyidang/fucha/" + buildUUID() + "/"
        id = intent.getStringExtra("ID")
        val type = intent.getIntExtra("Type", 0)
        val name = intent.getStringExtra("Name")
        record = intent.getSerializableExtra("data") as ReviewRecord
        if (type != 0 || TextUtils.isEmpty(id) && record == null) {
            initTopBar("新建复查记录", appbar)
            record = ReviewRecord(1)
            record!!.userId = MyApplication.getUser().userId
            record!!.yonghuming = name
            if (TextUtils.isEmpty(id)) {
                showUserListView()
            } else {
                record!!.yihuyidangid = id
                showUserInfoDetail(createFragment())
            }
        } else {
            initTopBar("复查记录详情", appbar)
            if (record == null) {
                initRecordData()
            } else {
                showUserInfoDetail(createFragment())
            }
        }
    }

    private fun createFragment(): Fragment? {
        return GasReviewFragment.newInstance(record, object : OnPageButtonClickListener<String?> {
            override fun onNext(list: List<MediaFile>?) {
                submitData(list)
            }

            override fun onClick(userInfo: String?) {
            }
        })
    }

    override fun showUserListView() {
        supportFragmentManager.beginTransaction()
                .add(R.id.content_layout, GasUserSearch2Fragment.newInstance(object : OnPageButtonClickListener<GasUserRecord> {
                    override fun onClick(userInfo: GasUserRecord) {
                        record!!.yihuyidangid = userInfo.yihuyidangid
                        showUserInfoDetail(createFragment())
                    }

                    override fun onNext(list: List<MediaFile>?) {}
                }))
                .commit()
    }

    override fun submitData(list: List<MediaFile>?) {
        showLoading(true)
        if (list != null && list.isNotEmpty()) {
            val path: MutableList<String> = ArrayList()
            val fileName: MutableList<String> = ArrayList()
            for (file in list) {
                path.add(file.path)
                fileName.add(remoteAddress + Utils.getFileName(file.path))
            }
            record!!.phoneftp = Utils.listToString(fileName)
            disposable = UploadTask.createUploadTask(path, remoteAddress)
                    .subscribeOn(Schedulers.io())
                    .flatMap {
                        if (it) NetworkApi.getService(GasService::class.java).saveReviewInfo(getFiledMap(record!!)) else null
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ httpResult ->
                        showLoading(false)
                        if (httpResult != null) {
                            if (httpResult.pushState == 200) {
                                finish()
                                Toast.makeText(this@GasReviewActivity, "提交成功！", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@GasReviewActivity, "提交失败！" + httpResult.pushMsg, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@GasReviewActivity, "文件上传失败，请稍后再试！", Toast.LENGTH_SHORT).show()
                        }
                    }, { throwable ->
                        showLoading(false)
                        Toast.makeText(this@GasReviewActivity, "提交失败！" + throwable.message, Toast.LENGTH_SHORT).show()
                    })
        }
    }

    private fun getFiledMap(model: ReviewRecord): HashMap<String, String>? {
        val map = HashMap<String, String>()
        val aClass: Class<out ReviewRecord> = model.javaClass
        val declaredFields = aClass.declaredFields
        for (filed in declaredFields) {
            try {
                map[filed.name] = valueOf(filed[model])
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
        return map
    }
    private fun initRecordData() {
        emptyView!!.show(true)
        disposable = NetworkApi.getService(GasService::class.java)
                .getRecordList("", id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.pushState == 200 && result.data.size > 0) {
                        record = result.data[0]
                        if (record != null) {
                            showUserInfoDetail(createFragment())
                            hideEmptyView()
                        } else {
                            showErrorView(null)
                        }
                    } else {
                        showErrorView(null)
                    }
                }, { showErrorView(null) })
    }
    companion object{
        fun toActivity(context: Context) {
            val intent = Intent(context, GasReviewActivity::class.java)
            context.startActivity(intent)
        }

        fun toActivity(context: Context, record: ReviewRecord?) {
            val intent = Intent(context, GasReviewActivity::class.java)
            intent.putExtra("data", record)
            context.startActivity(intent)
        }

        fun toActivity(context: Context, record: String?) {
            val intent = Intent(context, GasReviewActivity::class.java)
            intent.putExtra("ID", record)
            context.startActivity(intent)
        }

        fun toActivity(context: Context, record: String?, type: Int, name: String?) {
            val intent = Intent(context, GasReviewActivity::class.java)
            intent.putExtra("ID", record)
            intent.putExtra("Type", type)
            intent.putExtra("Name", name)
            context.startActivity(intent)
        }
    }
}