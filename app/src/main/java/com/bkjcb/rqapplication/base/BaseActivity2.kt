package com.bkjcb.rqapplication.base

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.bkjcb.rqapplication.base.eventbus.MessageEvent
import com.bkjcb.rqapplication.base.util.ActivityManagerTool
import com.bkjcb.rqapplication.base.util.NetworkUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * Created by DengShuai on 2020/6/24.
 * Description :
 */
abstract class BaseActivity2 : AppCompatActivity() {
    private val PERMISSION_REQUEST = 0
    private val useEventBus: Boolean = false
    private val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CALL_PHONE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityManagerTool.getActivityManager().add(this)
        setContentView(setLayoutID())
        initView()
        initData()
    }

    abstract fun setLayoutID(): Int

    open fun initView() {}
    open fun initData() {}

    override fun onStart() {
        super.onStart()
        if (useEventBus) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onStop() {
        super.onStop()
        if (useEventBus) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityManagerTool.getActivityManager().removeActivity(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent?) {
        action(event)
    }

    open fun action(event: MessageEvent?) {}

    fun netIsEnable(): Boolean {
        return NetworkUtils.isEnable(this)
    }

    fun requestPermission() {
        checkPermissions(permissions)
    }

    /**
     * @param permissions
     * @since 2.5.0
     */
    private fun checkPermissions(permissions: Array<String>) {
        val needRequestPermissionList = findDeniedPermissions(permissions)
        if (null != needRequestPermissionList
                && needRequestPermissionList.isNotEmpty()) {
            ActivityCompat.requestPermissions(this,
                    needRequestPermissionList.toTypedArray(),
                    PERMISSION_REQUEST)
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private fun findDeniedPermissions(permissions: Array<String>): List<String>? {
        val needRequestPermissionList: MutableList<String> = ArrayList()
        for (perm in permissions) {
            if (ContextCompat.checkSelfPermission(this,
                            perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                            this, perm)) {
                needRequestPermissionList.add(perm)
            }
        }
        return needRequestPermissionList
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private fun verifyPermissions(grantResults: IntArray): Boolean {
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST) {
            if (!verifyPermissions(grantResults)) {
                showMissingPermissionDialog()
            }
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private fun showMissingPermissionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("提示")
        builder.setMessage("当前应用缺少必要权限。请点击\"设置\"-\"权限\"-打开所需权限。")
        // 拒绝, 退出应用
        builder.setNegativeButton("取消"
        ) { _, _ -> finish() }
        builder.setPositiveButton("设置"
        ) { _, _ -> startAppSettings() }
        builder.setCancelable(false)
        builder.show()
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private fun startAppSettings() {
        val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        startActivity(intent)
    }
}