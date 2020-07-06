package com.bkjcb.rqapplication.stationCheck.retrofit;

import com.bkjcb.rqapplication.stationCheck.model.ApplianceCheckResult;
import com.bkjcb.rqapplication.base.model.HttpResult;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by DengShuai on 2020/1/6.
 * Description :
 */
public interface ApplianceCheckService {
    @GET("/rq/push/getunitname")
    Observable<ApplianceCheckResult> getUnitName(@Query("type") int type);

    @GET("/rq/push/getFixDic")
    Observable<ApplianceCheckResult> getFixCheckItem();

    @GET("/rq/push/getReport")
    Observable<ApplianceCheckResult> getReportCheckItem();

    @GET("/rq/push/getSaleCheck")
    Observable<ApplianceCheckResult> getSaleCheckItem();

    @POST("/rq/push/saveDailyCheck")
    @FormUrlEncoded
    Observable<HttpResult> saveDailyCheck(
            @Field("guid") String guid,
            @Field("checkMan") String userId,
            @Field("unitGUID") String unitID,
            @Field("checktime2") String time,
            @Field("Opinions") String Opinions,
            @Field("guidArry") String zdjcxid,
            @Field("tableJson") String jianchajilu,
            @Field("phoneftp") String ftp
    );

    @POST("/rq/push/saveAlarmDailyCheck")
    @FormUrlEncoded
    Observable<HttpResult> saveAlarmDailyCheck(
            @Field("guid") String guid,
            @Field("checkMan") String userId,
            @Field("unitGUID") String unitID,
            @Field("checktime2") String time,
            @Field("Opinions") String Opinions,
            @Field("guidArry") String zdjcxid,
            @Field("tableJson") String jianchajilu,
            @Field("phoneftp") String ftp
    );

    @POST("/rq/push/saveSaleDailyCheck")
    @FormUrlEncoded
    Observable<HttpResult> saveSaleDailyCheck(
            @Field("guid") String guid,
            @Field("checkMan") String userId,
            @Field("unitGUID") String unitID,
            @Field("checktime2") String time,
            @Field("Opinions") String Opinions,
            @Field("guidArry") String zdjcxid,
            @Field("tableJson") String jianchajilu,
            @Field("phoneftp") String ftp
    );
}
