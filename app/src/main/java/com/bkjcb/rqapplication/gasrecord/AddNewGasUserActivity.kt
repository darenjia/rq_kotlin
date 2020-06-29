package com.bkjcb.rqapplication.gasrecord

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.widget.Toast
import com.bkjcb.rqapplication.*
import com.bkjcb.rqapplication.ftp.UploadTask.createUploadTask
import com.bkjcb.rqapplication.gasrecord.AddNewGasUserActivity
import com.bkjcb.rqapplication.gasrecord.fragment.GasRecordDetailFragment
import com.bkjcb.rqapplication.gasrecord.fragment.GasUserSearchFragment.Companion.newInstance
import com.bkjcb.rqapplication.gasrecord.model.GasRecordModel
import com.bkjcb.rqapplication.gasrecord.retrofit.GasService
import com.bkjcb.rqapplication.interfaces.OnPageButtonClickListener
import com.bkjcb.rqapplication.model.HttpResult
import com.bkjcb.rqapplication.model.MediaFile
import com.bkjcb.rqapplication.retrofit.NetworkApi
import com.bkjcb.rqapplication.treatmentdefect.model.UserInfoResult.UserInfo
import com.bkjcb.rqapplication.util.Utils.buildUUID
import com.bkjcb.rqapplication.util.Utils.getFileName
import com.bkjcb.rqapplication.util.Utils.listToString
import com.bkjcb.rqapplication.util.Utils.obtainCurrentTime
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.with_bg_top_bar.*
import java.util.*

/**
 * Created by DengShuai on 2020/4/29.
 * Description :
 */
open class AddNewGasUserActivity : BaseSimpleActivity() {
    protected lateinit var model: GasRecordModel
    protected var remoteAddress = "yihuyidang/xinzeng/" + buildUUID() + "/"
    override fun setLayoutID(): Int {
        return R.layout.activity_gas_user_add
    }

    override fun initView() {
        initTopBar("添加一户一档",appbar)
    }

    override fun initData() {
        super.initData()
        model = GasRecordModel(1)
        model.userId = MyApplication.getUser().userId
        model.suoshuqu = MyApplication.getUser().areacode.area_jc
        model.jiedao = MyApplication.getUser().areacode.street_jc
        model.jiandangriqi = obtainCurrentTime()
        model.year = "2020"
        showUserListView()
    }

    protected open fun showUserListView() {
        val fragment = newInstance(object : OnPageButtonClickListener<UserInfo> {
            override fun onNext(list: List<MediaFile>?) {
                showUserInfoDetail(createGasRecordDetailFragment(null, model))
            }

            override fun onClick(userInfo: UserInfo) {
                showUserInfoDetail(createGasRecordDetailFragment(userInfo, model))
            }
        })
        supportFragmentManager.beginTransaction()
                .add(R.id.content_layout, fragment)
                .commit()
    }

    protected fun showUserInfoDetail(fragment: Fragment?) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.content_layout, fragment!!)
                .commit()
    }

    protected open fun createGasRecordDetailFragment(userInfo: UserInfo?, recordModel: GasRecordModel?): GasRecordDetailFragment? {
        return GasRecordDetailFragment.newInstance(object : OnPageButtonClickListener<UserInfo> {
            override fun onClick(userInfo: UserInfo) {}
            override fun onNext(list: List<MediaFile>?) {
                submitData(list)
            }
        }, userInfo, recordModel)
    }

    protected open fun submitData(list: List<MediaFile>?) {
        showLoading(true)
        if (list != null && list.isNotEmpty()) {
            val path: MutableList<String?> = ArrayList()
            val fileName: MutableList<String?> = ArrayList()
            for (file in list) {
                if (file.isLocal) {
                    path.add(file.path)
                    fileName.add(remoteAddress + getFileName(file.path))
                } else {
                    fileName.add(file.path.replace(Constants.IMAGE_URL, ""))
                }
            }
            model.phoneftp = listToString(fileName)
            disposable = createUploadTask(path, remoteAddress)
                    .subscribeOn(Schedulers.io())
                    .flatMap<HttpResult> { aBoolean -> if (aBoolean) createSubmitObservable() else null }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ httpResult ->
                        showLoading(false)
                        if (httpResult != null) {
                            if (httpResult.pushState == 200) {
                                submitSuccess()
                                Toast.makeText(this@AddNewGasUserActivity, "提交成功！", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@AddNewGasUserActivity, "提交失败！" + httpResult.pushMsg, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@AddNewGasUserActivity, "文件上传失败，稍后再试！", Toast.LENGTH_SHORT).show()
                        }
                    }) { throwable ->
                        showLoading(false)
                        Toast.makeText(this@AddNewGasUserActivity, "提交错误！" + throwable.message, Toast.LENGTH_SHORT).show()
                    }
        }
    }

    protected open fun createSubmitObservable(): Observable<HttpResult> {
        return NetworkApi.getService(GasService::class.java)
                .saveUserInfo(getFiledMap(model))
    }

    protected open fun submitSuccess() {
        finish()
    }

    protected fun getFiledMap(model: GasRecordModel): HashMap<String, String> {
        val map = HashMap<String, String>()
        val aClass: Class<out GasRecordModel> = model.javaClass
        val declaredFields = aClass.declaredFields
        for (filed in declaredFields) {
            try {
                map[filed.name] = valueOf(filed[model])
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
                break
            }
        }
        return map
    }

    fun valueOf(obj: Any?): String {
        return obj?.toString() ?: ""
    }

    companion object {
        fun toActivity(context: Context) {
            val intent = Intent(context, AddNewGasUserActivity::class.java)
            context.startActivity(intent)
        }
    }
}