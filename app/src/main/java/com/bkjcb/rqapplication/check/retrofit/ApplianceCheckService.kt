package com.bkjcb.rqapplication.check.retrofit

import com.bkjcb.rqapplication.check.model.ApplianceCheckResult
import com.bkjcb.rqapplication.model.HttpResult
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Created by DengShuai on 2020/1/6.
 * Description :
 */
interface ApplianceCheckService {
    @GET("/rq/push/getunitname")
    fun getUnitName(@Query("type") type: Int): Observable<ApplianceCheckResult>

    @GET("/rq/push/getFixDic")
    fun fixCheckItem(): Observable<ApplianceCheckResult>

    @GET("/rq/push/getReport")
    fun reportCheckItem(): Observable<ApplianceCheckResult>

    @GET("/rq/push/getSaleCheck")
    fun saleCheckItem(): Observable<ApplianceCheckResult>

    @POST("/rq/push/saveDailyCheck")
    @FormUrlEncoded
    fun saveDailyCheck(
            @Field("guid") guid: String?,
            @Field("checkMan") userId: String?,
            @Field("unitGUID") unitID: String?,
            @Field("checktime2") time: String?,
            @Field("Opinions") Opinions: String?,
            @Field("guidArry") zdjcxid: String?,
            @Field("tableJson") jianchajilu: String?,
            @Field("phoneftp") ftp: String?
    ): Observable<HttpResult>

    @POST("/rq/push/saveAlarmDailyCheck")
    @FormUrlEncoded
    fun saveAlarmDailyCheck(
            @Field("guid") guid: String?,
            @Field("checkMan") userId: String?,
            @Field("unitGUID") unitID: String?,
            @Field("checktime2") time: String?,
            @Field("Opinions") Opinions: String?,
            @Field("guidArry") zdjcxid: String?,
            @Field("tableJson") jianchajilu: String?,
            @Field("phoneftp") ftp: String?
    ): Observable<HttpResult>

    @POST("/rq/push/saveSaleDailyCheck")
    @FormUrlEncoded
    fun saveSaleDailyCheck(
            @Field("guid") guid: String?,
            @Field("checkMan") userId: String?,
            @Field("unitGUID") unitID: String?,
            @Field("checktime2") time: String?,
            @Field("Opinions") Opinions: String?,
            @Field("guidArry") zdjcxid: String?,
            @Field("tableJson") jianchajilu: String?,
            @Field("phoneftp") ftp: String?
    ): Observable<HttpResult>
}