package com.bkjcb.rqapplication.userRecord.retrofit;

import com.bkjcb.rqapplication.userRecord.model.GasCompanyResult;
import com.bkjcb.rqapplication.userRecord.model.GasStatisticData;
import com.bkjcb.rqapplication.userRecord.model.GasUserDetailResult;
import com.bkjcb.rqapplication.userRecord.model.GasUserRecordResult;
import com.bkjcb.rqapplication.base.model.HttpResult;
import com.bkjcb.rqapplication.userRecord.model.ReviewRecordResult;
import com.bkjcb.rqapplication.base.model.SimpleHttpResult;
import com.bkjcb.rqapplication.userRecord.model.UserInfoResult;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by DengShuai on 2019/10/24.
 * Description :
 */
public interface GasService {
    @POST("rq/push/saveYihuyidang")
    @FormUrlEncoded
    Observable<HttpResult> saveUserInfo(@Field("userId") String userId,
                                        @Field("suoshuqu") String suoshuqu,
                                        @Field("jiedao") String jiedao,
                                        @Field("jiandangriqi") String jiandangriqi,
                                        @Field("yonghuming") String yonghuming,
                                        @Field("fuzeren") String fuzeren,
                                        @Field("dizhi") String dizhi,
                                        @Field("dianhua") String dianhua,
                                        @Field("yingyezhizhao") String yingyezhizhao,
                                        @Field("faren") String faren,
                                        @Field("ranqiguanlizhidu") String ranqiguanlizhidu,
                                        @Field("gongqiqiye") String gongqiqiye,
                                        @Field("gongqiqiyeid") String gongqiqiyeid,
                                        @Field("yongqihetong") String yongqihetong,
                                        @Field("qiandingriqi") String qiandingriqi,
                                        @Field("tiaoyafa") String tiaoyafa,
                                        @Field("tiaoyafa_geshu") String tiaoyafa_geshu,
                                        @Field("lianjieguan") String lianjieguan,
                                        @Field("zaojuleixing_dafeng") String zaojuleixing_dafeng,
                                        @Field("zaojuleixing_gufeng") String zaojuleixing_gufeng,
                                        @Field("xihuobaohu") String xihuobaohu,
                                        @Field("xihuobaohu_geshu") String xihuobaohu_geshu,
                                        @Field("ranqixieloubaojinqi") String ranqixieloubaojinqi,
                                        @Field("qiyeanjianjilu") String qiyeanjianjilu,
                                        @Field("anjianriqi") String anjianriqi
    );

    @POST("rq/push/saveYihuyidang")
    @FormUrlEncoded
    Observable<HttpResult> saveUserInfo(@FieldMap HashMap<String, String> hashMap);

    @POST("rq/push/updateYihuyidang")
    @FormUrlEncoded
    Observable<HttpResult> updateUserInfo(@FieldMap HashMap<String, String> hashMap);

    @POST("rq/push/saveYihuyidangyinhuan")
    @FormUrlEncoded
    Observable<HttpResult> saveReviewInfo(@FieldMap HashMap<String, String> hashMap);

    @GET("rq/push/getComboList")
    Observable<GasCompanyResult> getComboList(@Query("type") String type);

    @POST("rq/push/getYihuyidangGongzuoList")
    @FormUrlEncoded
    Observable<GasUserRecordResult> getWorkRecords(@Field("start") int start, @Field("limit") int limit, @Field("suoshuqu") String qu,
                                                   @Field("jiedao") String jd, @Field("name") String key, @Field("tiaoyafa") String filter1,
                                                   @Field("xihuobaohu") String filter2, @Field("lianjieguan") String filter3);

    @POST("rq/push/getRqUserInfos")
    @FormUrlEncoded
    Observable<UserInfoResult> getUserInfo(@Field("start") int start, @Field("limit") int limit, @Field("areaJc") String areaJc, @Field("areaCode") String areaCode, @Field("name") String key);

    @POST("rq/push/getYihuyidangUserList")
    @FormUrlEncoded
    Observable<GasUserRecordResult> getUserList(@Field("name") String key);

    @POST("rq/push/getYihuyidang")
    @FormUrlEncoded
    Observable<GasUserDetailResult> getUserDetail(@Field("yihuyidangid") String id);

    @POST("rq/push/getYihuyidangFuchaList")
    @FormUrlEncoded
    Observable<ReviewRecordResult> getRecordList(@Field("yihuyidangid") String id, @Field("yihuyidangfuchaid") String rId);

    @GET("rq/push/getYihuyidangTongjifengmianList")
    Observable<SimpleHttpResult<List<GasStatisticData>>> getStatisticData(@Query("suoshuqu") String district);

    @GET("rq/push/getYihuyidangTongjifengmian")
    Observable<SimpleHttpResult<GasStatisticData>> getStatisticDataSimple(@Query("suoshuqu") String district,@Query("jiandangriqi") String date, @Query("jiedao") String street);

}
