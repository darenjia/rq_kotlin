package com.bkjcb.rqapplication.actionRegister.retrofit;

import com.bkjcb.rqapplication.actionRegister.model.ActionRegisterItem;
import com.bkjcb.rqapplication.base.model.HttpResult;
import com.bkjcb.rqapplication.base.model.SimpleHttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by DengShuai on 2019/10/31.
 * Description :
 */
public interface ActionRegisterService {
    @POST("/rq/push/SaveOrEditRegister")
    Observable<HttpResult> submit(@Body ActionRegisterItem item, @Query("userId") String userID);

    @POST("/rq/push/getlianList")
    @FormUrlEncoded
    Observable<SimpleHttpResult<List<ActionRegisterItem>>> queryList(@Field("start") int start, @Field("limit") int limit, @Field("case_source") String from, @Field("crime_address") String address);
}
