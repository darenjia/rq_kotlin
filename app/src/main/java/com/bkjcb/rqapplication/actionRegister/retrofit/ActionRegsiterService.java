package com.bkjcb.rqapplication.actionRegister.retrofit;

import com.bkjcb.rqapplication.actionRegister.model.ActionRegisterItem;
import com.bkjcb.rqapplication.base.model.HttpResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by DengShuai on 2019/10/31.
 * Description :
 */
public interface ActionRegsiterService {
    @POST("/rq/push/SaveOrEditRegister")
    Observable<HttpResult> submit(@Body ActionRegisterItem item, @Query("userId") String userID);
}
