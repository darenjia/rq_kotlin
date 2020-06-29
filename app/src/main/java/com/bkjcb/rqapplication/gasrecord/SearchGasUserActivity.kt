package com.bkjcb.rqapplication.gasrecord

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import com.bkjcb.rqapplication.base.BaseSimpleActivity
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.gasrecord.fragment.GasUserSearchFragment
import com.bkjcb.rqapplication.base.interfaces.OnPageButtonClickListener
import com.bkjcb.rqapplication.base.model.MediaFile
import com.bkjcb.rqapplication.treatmentdefect.model.UserInfoResult.UserInfo
import kotlinx.android.synthetic.main.with_bg_top_bar.*

/**
 * Created by DengShuai on 2020/5/12.
 * Description :
 */
class SearchGasUserActivity : BaseSimpleActivity() {
     override fun setLayoutID(): Int {
        return R.layout.activity_gas_user_add
    }

     override fun initView() {
        initTopBar("选择燃气用户",appbar)
    }

     override fun initData() {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.content_layout, GasUserSearchFragment.newInstance(object : OnPageButtonClickListener<UserInfo> {
                    override fun onClick(userInfo: UserInfo) {
                        val intent = Intent()
                        intent.putExtra("data", userInfo)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }

                    override fun onNext(list: List<MediaFile>?) {

                    }

                }, true))
                .commit()
    }

    override fun onBackPressed() {
        finish()
    }

    companion object {
        var CHOOSE_USER = 123
        fun toActivity(fragment: Fragment) {
            val intent = Intent(fragment.activity, SearchGasUserActivity::class.java)
            fragment.startActivityForResult(intent, CHOOSE_USER)
        }
    }
}