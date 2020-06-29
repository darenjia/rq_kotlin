package com.bkjcb.rqapplication.gasrecord

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import com.bkjcb.rqapplication.MyApplication
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.gasrecord.fragment.GasRecordDetailFragment
import com.bkjcb.rqapplication.gasrecord.model.GasRecordModel
import com.bkjcb.rqapplication.gasrecord.retrofit.GasService
import com.bkjcb.rqapplication.interfaces.OnPageButtonClickListener
import com.bkjcb.rqapplication.model.HttpResult
import com.bkjcb.rqapplication.model.MediaFile
import com.bkjcb.rqapplication.retrofit.NetworkApi
import com.bkjcb.rqapplication.treatmentdefect.model.UserInfoResult.UserInfo
import com.bkjcb.rqapplication.util.Utils
import io.reactivex.Observable
import kotlinx.android.synthetic.main.with_bg_top_bar.*

/**
 * Created by DengShuai on 2020/5/13.
 * Description :
 */
class GasUserRecordChangeActivity : AddNewGasUserActivity() {
     override fun setLayoutID(): Int {
        return R.layout.activity_gas_user_add
    }

     override fun initView() {
        initTopBar("修改一户一档",appbar)
    }

     override fun initData() {
        model = intent.getSerializableExtra("data") as GasRecordModel
        model.setType(2)
        //model.phoneftp = "";
        model.jiandangriqi = Utils.obtainCurrentTime()
        if (!TextUtils.isEmpty(model.uid)) {
            model.yihuyidangid = model.uid
            remoteAddress = "yihuyidang/xinzeng/" + model.yihuyidangid + "/"
        }
        model.userId = MyApplication.getUser().userId
        var userInfo: UserInfo? = null
        if (!TextUtils.isEmpty(model.rquserid)) {
            userInfo = UserInfo()
            userInfo.userGuid = model.rquserid
            userInfo.userName = model.rqyonghuming
            userInfo.userAddress = model.rqdizhi
        }
        supportFragmentManager
                .beginTransaction()
                .add(R.id.content_layout, GasRecordDetailFragment.newInstance(object : OnPageButtonClickListener<String> {
                    override fun onClick(userInfo: String) {}
                    override fun onNext(list: List<MediaFile>?) {
                        submitData(list)
                    }
                }, userInfo, model))
                .commit()
    }

    override fun createSubmitObservable(): Observable<HttpResult> {
        return if (model.id > 0) NetworkApi.getService(GasService::class.java).saveUserInfo(getFiledMap(model)) else NetworkApi.getService(GasService::class.java).updateUserInfo(getFiledMap(model))
    }

    override fun submitSuccess() {
        if (model.id > 0) {
            GasRecordModel.getBox().remove(model)
        } else {
            setResult(Activity.RESULT_OK)
        }
        finish()
    }

    companion object {
        const val CHANGE_RECORD = 110
        fun toActivity(context: Activity, recordModel: GasRecordModel?) {
            val intent = Intent(context, GasUserRecordChangeActivity::class.java)
            intent.putExtra("data", recordModel)
            context.startActivityForResult(intent, CHANGE_RECORD)
        }
    }
}