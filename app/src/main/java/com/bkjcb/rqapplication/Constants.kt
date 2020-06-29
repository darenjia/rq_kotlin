package com.bkjcb.rqapplication

import com.amap.api.maps.model.LatLng

/**
 * Created by DengShuai on 2019/10/22.
 * Description :
 */
object Constants {
    var BASE_URL = "http://183.194.249.186"
    //    public static String BASE_URL = "http://192.168.5.59:8099";
    //    public static String BASE_URL = "http://172.16.8.38:8080";
    val SHANGHAI = LatLng(31.176402, 121.447119)
    //public static final String FTP_HOST_DEFAULT = "172.29.43.21";
    const val FTP_HOST_DEFAULT = "183.194.249.187"
    const val FTP_HOST_PORT = 13000
    const val FTP_USER_DEFAULT = "RqAdmin"
    const val FTP_PASSWORD_DEFAULT = "123456"
    const val IMAGE_URL = "http://183.194.249.187:6080/"
}