package com.bkjcb.rqapplication.gasrecord

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.bkjcb.rqapplication.MyApplication
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.gasrecord.GasUserRecordDetailActivity
import com.bkjcb.rqapplication.gasrecord.fragment.GasRecordDetailFragment
import com.bkjcb.rqapplication.gasrecord.model.GasRecordModel
import com.bkjcb.rqapplication.gasrecord.retrofit.GasService
import com.bkjcb.rqapplication.base.retrofit.NetworkApi
import com.bkjcb.rqapplication.treatmentdefect.model.UserInfoResult.UserInfo
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.with_bg_top_bar.*

/**
 * Created by DengShuai on 2020/4/29.
 * Description :
 */
class GasUserRecordDetailActivity : AddNewGasUserActivity() {
    private var id: String? = null
    private var name: String? = null
    private var needRefresh = false
    private var recordModel: GasRecordModel? = null
    override fun initView() {
        val isCanChange = MyApplication.getUser().userleixing == "街镇用户"
        initTopBar("一户一档详情", appbar)
        appbar.addRightTextButton("复查记录", R.id.top_right_button1)
                .setOnClickListener { ReviewRecordActivity.toActivity(this@GasUserRecordDetailActivity, id, name) }
        if (isCanChange) {
            appbar.addRightTextButton("修改", R.id.top_right_button)
                    .setOnClickListener { GasUserRecordChangeActivity.toActivity(this@GasUserRecordDetailActivity, recordModel) }
        }
    }

    override fun initData() {
        id = intent.getStringExtra("ID")
        queryRemoteDta(id)
    }

    override fun onStart() {
        super.onStart()
        if (needRefresh) {
            queryRemoteDta(id)
        }
    }

    private fun queryRemoteDta(id: String?) {
        disposable = NetworkApi.getService(GasService::class.java)
                .getUserDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.pushState == 200) {
                        needRefresh = false
                        recordModel = result.datas
                        if (recordModel != null) {
                            name = recordModel!!.yonghuming
                            showUserInfoDetail(createGasRecordDetailFragment(null, recordModel))
                        } else {
                            Toast.makeText(this@GasUserRecordDetailActivity, "未获取到一户一档信息，请稍后再试", Toast.LENGTH_LONG).show()
                        }
                    }
                }) { }
    }

    override fun createGasRecordDetailFragment(userInfo: UserInfo?, recordModel: GasRecordModel?): GasRecordDetailFragment? {
        return GasRecordDetailFragment.newInstance(userInfo, recordModel)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == GasUserRecordChangeActivity.CHANGE_RECORD) {
            needRefresh = true
        }
    }

    companion object {
        fun toActivity(context: Context, id: String?) {
            val intent = Intent(context, GasUserRecordDetailActivity::class.java)
            intent.putExtra("ID", id)
            context.startActivity(intent)
        }
    }
}