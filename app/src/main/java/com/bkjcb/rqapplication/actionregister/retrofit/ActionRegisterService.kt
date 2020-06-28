package com.bkjcb.rqapplication.actionregister.retrofit

import com.bkjcb.rqapplication.actionregister.model.ActionRegisterItem
import com.bkjcb.rqapplication.model.HttpResult
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by DengShuai on 2020/6/24.
 * Description :
 */
interface ActionRegisterService {
    @POST("/rq/push/SaveOrEditRegister")
    fun submit(@Body item: ActionRegisterItem, @Query("userId") userID: String): Observable<HttpResult>
}