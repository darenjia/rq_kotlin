package com.bkjcb.rqapplication.treatmentdefect.retrofit

import com.bkjcb.rqapplication.base.model.HttpResult
import com.bkjcb.rqapplication.treatmentdefect.model.DefectDetail
import com.bkjcb.rqapplication.treatmentdefect.model.DefectDetailResult
import com.bkjcb.rqapplication.treatmentdefect.model.DefectTreatmentModel
import com.bkjcb.rqapplication.treatmentdefect.model.TreatmentResult
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by DengShuai on 2020/6/16.
 * Description :
 */
interface TreatmentService {
    @POST("rq/push/getYHQChuzhiList")
    @FormUrlEncoded
    fun getTreatmentList(@Field("type") type: Int,
                         @Field("start") start: Int,
                         @Field("limit") limit: Int,
                         @Field("areaCode") code: String?,
                         @Field("userNameORuserAddress") key: String?
    ): Observable<TreatmentResult<DefectTreatmentModel>>

    @POST("rq/push/getYHQChuzhiObject")
    @FormUrlEncoded
    fun getTreatmentDefectDetail(@Field("mtfId") id: String?): Observable<DefectDetailResult>

    @POST("rq/push/saveOrBackYHQChuzhi")
    fun saveTreatmentDefectResult(@Body result: DefectDetail?): Observable<HttpResult>
}