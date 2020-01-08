package com.bkjcb.rqapplication.retrofit;

import com.bkjcb.rqapplication.model.CheckContentItemResult;
import com.bkjcb.rqapplication.model.CheckStationResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by DengShuai on 2020/1/6.
 * Description :
 */
public interface CheckService {
    @GET("/rq/push/getCheckUnit")
    Observable<CheckStationResult> getCheckUnit(@Query("siteType")String type);
    @GET("/rq/push/getCheckItem")
    Observable<CheckContentItemResult> getCheckItem(@Query("siteType")String type);
}
