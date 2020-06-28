package com.bkjcb.rqapplication.emergency.retrofit

import com.bkjcb.rqapplication.model.HttpResult
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by DengShuai on 2019/10/31.
 * Description :
 */
interface EmergencyService {
    @POST("/rq/push/SaveSiteInfo")
    @FormUrlEncoded
    fun submit(
            @Field("userId") userId: String?,
            @Field("reportingUnit") reportingUnit: String?,
            @Field("qushu") qushu: String?,
            @Field("remark") remark: String?,
            @Field("reportingDate") reportingDate: String?,
            @Field("accidentDate") accidentDate: String?,
            @Field("accidentAddress") accidentAddress: String?,
            @Field("disposalPerson") disposalPerson: String?,
            @Field("keyPerson") keyPerson: String?,
            @Field("reportingStaff") reportingStaff: String?,
            @Field("contactPhone") contactPhone: String?,
            @Field("mainDescription") mainDescription: String?,
            @Field("phoneftp") phoneftp: String?
    ): Observable<HttpResult>
}