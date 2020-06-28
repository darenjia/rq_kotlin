package com.bkjcb.rqapplication.check.retrofit

import com.bkjcb.rqapplication.check.model.CheckContentItemResult
import com.bkjcb.rqapplication.check.model.CheckStationResult
import com.bkjcb.rqapplication.model.ExportFilePathResult
import com.bkjcb.rqapplication.model.HttpResult
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Created by DengShuai on 2020/1/6.
 * Description :
 */
interface CheckService {
    @GET("/rq/push/getCheckUnit")
    fun getCheckUnit(@Query("siteType") type: String?, @Query("type") s: String?): Observable<CheckStationResult>

    @GET("/rq/push/getCheckItem")
    fun getCheckItem(@Query("siteType") type: String?): Observable<CheckContentItemResult>

    @GET("/rq/push/zhandianjianchaExport")
    fun getExportPath(@Query("fileid") id: String?): Observable<ExportFilePathResult>

    @POST("/rq/push/saveCheckItem")
    @FormUrlEncoded
    fun saveCheckItem(@Field("userId") userId: String?,
                      @Field("year") year: String?,
                      @Field("zhandianleixing") zhandianleixing: String?,
                      @Field("beijiandanweiid") beijiandanweiid: String?,
                      @Field("beizhu") beizhu: String?,
                      @Field("jianchariqi") jianchariqi: String?,
                      @Field("jianchajieguo") jianchajieguo: String?,
                      @Field("zdjcxid") zdjcxid: Array<String?>?,
                      @Field("jianchajilu") jianchajilu: Array<String?>?,
                      @Field("phoneftp") path: String?,
                      @Field("fileid") fileid: String?
    ): Observable<HttpResult>
}