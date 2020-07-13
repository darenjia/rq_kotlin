package com.bkjcb.rqapplication.infoQuery.retrofit;

import com.bkjcb.rqapplication.base.model.SimpleHttpResult;
import com.bkjcb.rqapplication.infoQuery.model.AlarmFirmModel;
import com.bkjcb.rqapplication.infoQuery.model.InstallationFirmModel;
import com.bkjcb.rqapplication.infoQuery.model.SellFirmModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by DengShuai on 2020/7/6.
 * Description :
 */
public interface FirmService {
    @GET("/rq/push/getanzhuangweixiumx")
    Observable<SimpleHttpResult<List<InstallationFirmModel>>> getanzhuangweixiumx(@Query("guid") String guid);

    @GET("/rq/push/getxiaoshoumx")
    Observable<SimpleHttpResult<List<SellFirmModel>>> getxiaoshoumx(@Query("guid") String guid);

    @GET("/rq/push/getbaojingqimx")
    Observable<SimpleHttpResult<List<AlarmFirmModel>>> getbaojingqimx(@Query("guid") String guid);
}
