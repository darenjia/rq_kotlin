package com.bkjcb.rqapplication.retrofit

import com.bkjcb.rqapplication.model.ConvertResult
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by DengShuai on 2019/10/31.
 * Description :
 */
interface ConvertService {
    @FormUrlEncoded
    @POST("cn/Convert/convertPoint")
    fun convert(@Field("from") start: String?, @Field("to") end: String?, @Field("locations") locations: String?): Observable<ConvertResult?>?
}