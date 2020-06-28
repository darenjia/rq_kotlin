package com.bkjcb.rqapplication.retrofit

import com.bkjcb.rqapplication.model.HttpResult
import com.bkjcb.rqapplication.model.UserResult
import com.bkjcb.rqapplication.treatmentdefect.model.BottleResult
import com.bkjcb.rqapplication.treatmentdefect.model.UserInfoResult
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by DengShuai on 2019/10/24.
 * Description :
 */
interface DataService {
    @GET("/rq/push/getUserInfos")
    fun getUserInfos(@Query("areaCode") code: String?): Observable<UserInfoResult>

    @POST("/rq/push/getLoginUser")
    @FormUrlEncoded
    fun getLoginUser(@Field("userName") userName: String?, @Field("password") password: String?): Observable<UserResult>

    @POST("/rq/push/getLoginUser")
    @FormUrlEncoded
    fun getLogin(@Field("userName") userName: String?, @Field("password") password: String?): Call<String>

    @GET("/rq/push/getBottleData")
    fun getBottleData(@Query("userGuid") Guid: String?): Observable<BottleResult>

    @POST("/rq/push/saveTourCheck")
    @FormUrlEncoded
    fun saveTourCheck(@Field("userGuid") Guid: String?, @Field("tourPeople") name: String?, @Field("tourState") state: String?, @Field("tourRemark") remark: String?): Observable<HttpResult>

    @POST("/rq/push/handleUserNew")
    @FormUrlEncoded
    fun changeUserInfo(@Field("userGuid") Guid: String?, @Field("userName") name: String?, @Field("userAddress") address: String?): Observable<HttpResult>

    @POST("/rq/push/handleUserNew")
    @FormUrlEncoded
    fun changeUserInfo(@Field("areaCode") Guid: String?, @Field("userName") name: String?, @Field("userAddress") address: String?, @Field("userType") type: Int): Observable<HttpResult>
}