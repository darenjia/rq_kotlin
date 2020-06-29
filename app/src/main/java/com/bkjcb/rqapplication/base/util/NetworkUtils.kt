package com.bkjcb.rqapplication.base.util

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by BKJCB on 2017/3/16.
 * 网络工具
 * 判断网络是否可用
 * 判断网络状态
 */
object NetworkUtils {
    /***
     *
     * 此方法描述的是： 判断网络状态是否可用
     *
     * @return boolean 返回是否可用，true可用，false不可用
     */
    @JvmStatic
    fun isEnable(context: Context): Boolean {
        // 网络获得管理器
        val manager = context
                .applicationContext.getSystemService(
                        Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                ?: return false
        // 判断网络管理器是否为null
        // 获得网络连接信息
        val networkInfo = manager.activeNetworkInfo
        // 判断 netwokInfo是否为null networkInfo是否可用
        return !(networkInfo == null || !networkInfo.isAvailable)
        // 网络状态可用
    }
}