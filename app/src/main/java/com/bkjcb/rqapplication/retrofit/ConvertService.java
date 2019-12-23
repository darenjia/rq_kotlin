package com.bkjcb.rqapplication.retrofit;

import com.bkjcb.rqapplication.model.ConvertResult;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by DengShuai on 2019/10/31.
 * Description :
 */
public interface ConvertService {
    @FormUrlEncoded
    @POST("cn/Convert/convertPoint")
    Observable<ConvertResult> convert(@Field("from") String start, @Field("to") String end, @Field("locations") String locations);
}
