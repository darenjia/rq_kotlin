package com.bkjcb.rqapplication.base

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.bkjcb.rqapplication.MyApplication
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.base.retrofit.DataService
import com.bkjcb.rqapplication.base.retrofit.NetworkApi
import com.bkjcb.rqapplication.base.util.ActivityManagerTool
import com.bkjcb.rqapplication.base.util.MD5Util
import com.bkjcb.rqapplication.base.util.Utils
import com.bkjcb.rqapplication.base.util.Utils.dip2px
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import java.io.IOException
import java.io.ObjectOutputStream

/**
 * Created by DengShuai on 2019/10/22.
 * Description :
 */
class LoginActivity : BaseSimpleActivity(), View.OnClickListener {


    var tipDialog: QMUITipDialog? = null

    override fun setLayoutID(): Int {
        return R.layout.activity_login
    }

    override fun initView() {
        resetRadioButtonImage(R.drawable.municipal_bg, login_type1)
        resetRadioButtonImage(R.drawable.district_bg, login_type2)
        resetRadioButtonImage(R.drawable.street_bg, login_type3)
        login_type.setOnCheckedChangeListener { group, checkedId -> checked(checkedId) }
        sign_in_button.setOnClickListener(this)
    }

    private fun checked(checkedId: Int) {
        var tipText = ""
        when (checkedId) {
            R.id.login_type1 -> {
                tipText = "当前为市级用户"
                login_type1.setTextColor(getTextColor(R.color.colorApplication))
                login_type2.setTextColor(getTextColor(R.color.colorDivider))
                login_type3.setTextColor(getTextColor(R.color.colorDivider))
            }
            R.id.login_type2 -> {
                tipText = "当前为区级用户"
                login_type1.setTextColor(getTextColor(R.color.colorDivider))
                login_type2.setTextColor(getTextColor(R.color.colorApplication))
                login_type3.setTextColor(getTextColor(R.color.colorDivider))
            }
            R.id.login_type3 -> {
                tipText = "当前为街镇用户"
                login_type1.setTextColor(getTextColor(R.color.colorDivider))
                login_type2.setTextColor(getTextColor(R.color.colorDivider))
                login_type3.setTextColor(getTextColor(R.color.colorApplication))
            }
            else -> {
            }
        }
        user_tip.text = tipText
    }

    private fun getTextColor(color: Int): Int {
        return resources.getColor(color)
    }

    private fun resetRadioButtonImage(drawableId: Int, radioButton: RadioButton?) {
        val drawable_news = resources.getDrawable(drawableId)
        drawable_news.setBounds(0, 0, dip2px(this, 48f), dip2px(this, 48f))
        radioButton!!.setCompoundDrawables(null, drawable_news, null, null)
    }

    override fun initData() {
        super.initData()
        ActivityManagerTool.getActivityManager().finishAll(this)
        val isRemember = getSharedPreferences().getBoolean("remember", false)
        if (isRemember) {
            box_password.isChecked = true
            username.setText(getSharedPreferences().getString("name", ""))
            password.setText(getSharedPreferences().getString("password", ""))
            val level = getSharedPreferences().getString("level", "")
            if (!TextUtils.isEmpty(level)) {
                when {
                    level!!.contains("市") -> {
                        login_type1.toggle()
                        checked(R.id.login_type1)
                    }
                    level.contains("区") -> {
                        login_type2.toggle()
                        checked(R.id.login_type2)
                    }
                    else -> {
                        login_type3.toggle()
                        checked(R.id.login_type3)
                    }
                }
            }
        }
        requestPermission()
        app_version.text = java.lang.String.format("版本号：V %s", Utils.currentVersion())
    }

    override fun onClick(v: View?) {
        val user = username.text.toString()
        val password = password.text.toString()
        if (user.trim { it <= ' ' }.isEmpty()) {
            showSnackbar("用户名不能为空！")
            return
        }
        if (password.trim { it <= ' ' }.isEmpty()) {
            showSnackbar("请输入密码！")
            return
        }
        login(user, password)
    }

    private fun login(name: String, password: String) {
        showLoading()
        disposable = NetworkApi.getService(DataService::class.java)
                .getLoginUser(name, MD5Util.encode(password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    tipDialog!!.dismiss()
                    if (result.pushState == 200) {
                        MyApplication.setUser(result.datas)
                        loginSuccess()
                    } else {
                        tipDialog!!.dismiss()
                        Toast.makeText(this@LoginActivity, "用户名或密码错误！", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    tipDialog!!.dismiss()
                    Toast.makeText(this@LoginActivity, "登录失败，请检查网络！", Toast.LENGTH_SHORT).show()
                }
        //loginSuccess();
    }

    private fun showLoading() {
        if (tipDialog == null) {
            tipDialog = QMUITipDialog.Builder(this)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                    .setTipWord("正在登录")
                    .create()
        }
        tipDialog!!.show()
    }

    private fun loginSuccess() {
        if (box_password.isChecked) {
            getSharedPreferences("user", Context.MODE_PRIVATE).edit()
                    .putString("name", username.text.toString())
                    .putString("password", password.text.toString())
                    .putBoolean("remember", true)
                    .putString("level", MyApplication.getUser().userleixing)
                    .apply()
        }
        try {
            val os = ObjectOutputStream(openFileOutput("CacheUser", Context.MODE_PRIVATE))
            os.writeObject(MyApplication.getUser())
            os.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        //        if (MyApplication.getUser().getUserleixing().equals("街镇用户")) {
//            GasUserRecordActivity.ToActivity(LoginActivity.this);
//        } else {
//        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//        startActivity(intent);
//        }
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showSnackbar(message: String) {
        showSnackbar(username, message)
    }

    override fun onBackPressed() {
        if (tipDialog != null && tipDialog!!.isShowing) {
            tipDialog!!.dismiss()
            showSnackbar("登录已取消")
            cancelDisposable()
        } else {
            super.onBackPressed()
        }
    }
}