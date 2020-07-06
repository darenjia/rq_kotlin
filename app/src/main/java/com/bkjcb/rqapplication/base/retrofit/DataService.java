package com.bkjcb.rqapplication.base.retrofit;

import com.bkjcb.rqapplication.base.model.BottleResult;
import com.bkjcb.rqapplication.base.model.HttpResult;
import com.bkjcb.rqapplication.userRecord.model.UserInfoResult;
import com.bkjcb.rqapplication.base.model.UserResult;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by DengShuai on 2019/10/24.
 * Description :
 */
public interface DataService {
    @GET("/rq/push/getUserInfos")
    Observable<UserInfoResult> getUserInfos(@Query("areaCode")String code);
    @POST("/rq/push/getLoginUser")
    @FormUrlEncoded
    Observable<UserResult> getLoginUser(@Field("userName") String userName, @Field("password")String password); @POST("/rq/push/getLoginUser")
    @FormUrlEncoded
    Call<String> getLogin(@Field("userName") String userName, @Field("password")String password);
    @GET("/rq/push/getBottleData")
    Observable<BottleResult> getBottleData(@Query("userGuid")String Guid);
    @POST("/rq/push/saveTourCheck")
    @FormUrlEncoded
    Observable<HttpResult> saveTourCheck(@Field("userGuid") String Guid,@Field("tourPeople") String name,@Field("tourState") String state,@Field("tourRemark") String remark);

    @POST("/rq/push/handleUserNew")
    @FormUrlEncoded
    Observable<HttpResult> changeUserInfo(@Field("userGuid") String Guid,@Field("userName") String name,@Field("userAddress") String address);

    @POST("/rq/push/handleUserNew")
    @FormUrlEncoded
    Observable<HttpResult> changeUserInfo(@Field("areaCode") String Guid,@Field("userName") String name,@Field("userAddress") String address,@Field("userType")int type);
}
