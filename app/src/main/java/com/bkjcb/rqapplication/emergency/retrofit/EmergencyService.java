package com.bkjcb.rqapplication.emergency.retrofit;

import com.bkjcb.rqapplication.base.model.HttpResult;
import com.bkjcb.rqapplication.base.model.SimpleHttpResult;
import com.bkjcb.rqapplication.emergency.model.EmergencyItem;
import com.bkjcb.rqapplication.emergency.model.EmergencyModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by DengShuai on 2019/10/31.
 * Description :
 */
public interface EmergencyService {
    @POST("/rq/push/SaveSiteInfo")
    @FormUrlEncoded
    Observable<HttpResult> submit(
            @Field("userId") String userId,
            @Field("shiguid") String uuid,
            @Field("reportingUnit") String reportingUnit,
            @Field("qushu") String qushu,
            @Field("remark") String remark,
            @Field("reportingDate") String reportingDate,
            @Field("accidentDate") String accidentDate,
            @Field("accidentAddress") String accidentAddress,
            @Field("disposalPerson") String disposalPerson,
            @Field("keyPerson") String keyPerson,
            @Field("reportingStaff") String reportingStaff,
            @Field("contactPhone") String contactPhone,
            @Field("mainDescription") String mainDescription,
            @Field("phoneftp") String phoneftp
    );

    @POST("rq/push/getDnGasEvents")
    @FormUrlEncoded
    Observable<SimpleHttpResult<List<EmergencyModel>>> getGasAccident(@Field("state") String state);

    @POST("rq/push/getDangerxian")
    @FormUrlEncoded
    Observable<SimpleHttpResult<EmergencyItem>> getAccidentDetail(@Field("shiguid") String state);
}
