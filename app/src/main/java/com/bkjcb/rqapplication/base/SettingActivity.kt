package com.bkjcb.rqapplication.base

import android.content.Context
import android.content.Intent
import android.view.View
import com.bkjcb.rqapplication.MyApplication
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.base.util.Utils
import com.luck.picture.lib.tools.PictureFileUtils
import com.tencent.bugly.beta.Beta
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.with_bg_top_bar.*

/**
 * Created by DengShuai on 2019/11/7.
 * Description :
 */
class SettingActivity : BaseSimpleActivity(), View.OnClickListener {

    override fun setLayoutID(): Int {
        return R.layout.activity_setting
    }

    override fun initView() {
        initTopBar("设置中心",appbar)
        val user = MyApplication.getUser()
        if (user != null) {
            user_name.text = user.real_name
            user_type.text = user.userleixing
            if (user.areacode != null) {
                user_comp.text = user.areacode.street_jc
            } else {
                user_comp.text = user.usercomp
            }
        }
        setting_hide_btn.switchIsChecked = getSharedPreferences().getBoolean("hide_finished", true)
        // @OnClick(R.id.introduction, R.id.clear, R.id.checkUpload, R.id.logout)
        introduction.setOnClickListener(this)
        checkUpload.setOnClickListener(this)
        logout.setOnClickListener(this)
    }

    override fun initData() {
        setting_hide_btn.setSwitchCheckedChangeListener { buttonView, isChecked ->  getSharedPreferences().edit().putBoolean("hide_finished", isChecked).apply() }
        checkUpload.setRightString("v " + Utils.currentVersion())
    }

   override fun onClick(v: View) {
        when (v.id) {
            R.id.introduction -> showSnackbar(user_name, "当前版本是:" + Utils.currentVersion())
            R.id.clear -> {
                PictureFileUtils.deleteCacheDirFile(this)
                showSnackbar(user_name, "缓存已清理！")
            }
            R.id.checkUpload -> Beta.checkUpgrade()
            R.id.logout -> {
                val intent = Intent(this@SettingActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            else -> {
            }
        }
    }

    companion object {
        fun toActivity(context: Context) {
            context.startActivity(Intent(context, SettingActivity::class.java))
        }
    }
}