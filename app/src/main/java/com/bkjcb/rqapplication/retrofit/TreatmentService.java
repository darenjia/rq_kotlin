package com.bkjcb.rqapplication.retrofit;

import com.bkjcb.rqapplication.model.DefectDetail;
import com.bkjcb.rqapplication.model.DefectTreatmentModel;
import com.bkjcb.rqapplication.model.HttpResult;
import com.bkjcb.rqapplication.model.TreatmentResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by DengShuai on 2020/6/16.
 * Description :
 */
public interface TreatmentService {
    @POST("rq/push/getYHQChuzhiList")
    @FormUrlEncoded
    Observable<TreatmentResult<DefectTreatmentModel>> getTreatmentList(@Field("type") int type,
                                                                       @Field("start") int start,
                                                                       @Field("limit") int limit,
                                                                       @Field("areaCode") String code,
                                                                       @Field("userNameORuserAddress") String key
    );
    @POST("rq/push/getYHQChuzhiObject")
    @FormUrlEncoded
    Observable<TreatmentResult<DefectDetail>> getTreatmentDefectDetail(@Field("mtfId")String id);

    @POST("rq/push/saveOrBackYHQChuzhi")
    @FormUrlEncoded
    Observable<HttpResult> saveTreatmentDefectResult(@Body DefectDetail result);

}
