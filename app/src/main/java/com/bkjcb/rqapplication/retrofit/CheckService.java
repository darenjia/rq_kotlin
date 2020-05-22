package com.bkjcb.rqapplication.retrofit;

import com.bkjcb.rqapplication.model.CheckContentItemResult;
import com.bkjcb.rqapplication.model.CheckStationResult;
import com.bkjcb.rqapplication.model.ExportFilePathResult;
import com.bkjcb.rqapplication.model.HttpResult;

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
public interface CheckService {
    @GET("/rq/push/getCheckUnit")
    Observable<CheckStationResult> getCheckUnit(@Query("siteType") String type, @Query("type") String s);

    @GET("/rq/push/getCheckItem")
    Observable<CheckContentItemResult> getCheckItem(@Query("siteType") String type);

    @GET("/rq/push/zhandianjianchaExport")
    Observable<ExportFilePathResult> getExportPath(@Query("fileid") String id);

    @POST("/rq/push/saveCheckItem")
    @FormUrlEncoded
    Observable<HttpResult> saveCheckItem(@Field("userId") String userId,
                                         @Field("year") String year,
                                         @Field("zhandianleixing") String zhandianleixing,
                                         @Field("beijiandanweiid") String beijiandanweiid,
                                         @Field("beizhu") String beizhu,
                                         @Field("jianchariqi") String jianchariqi,
                                         @Field("jianchajieguo") String jianchajieguo,
                                         @Field("zdjcxid") String[] zdjcxid,
                                         @Field("jianchajilu") String[] jianchajilu,
                                         @Field("phoneftp") String path,
                                         @Field("fileid") String fileid
    );
}
