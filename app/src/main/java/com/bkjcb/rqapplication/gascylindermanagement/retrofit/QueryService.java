package com.bkjcb.rqapplication.gascylindermanagement.retrofit;

import com.bkjcb.rqapplication.gascylindermanagement.model.CirculationInfo;
import com.bkjcb.rqapplication.gascylindermanagement.model.CompanyResult;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by DengShuai on 2019/10/31.
 * Description :
 */
public interface QueryService {
    /**
     * @Author: DS
     * @Description:查询气瓶信息
     * @Date: 9:09 2021/3/9
     * @Param: id 气瓶编号ID str解码内容或单位代码 type 1NFC 2自定义
     * @Return:
     **/

    @POST("/rq/push/getNFCJsonData")
    @FormUrlEncoded
    Observable<CirculationInfo> query(
            @Field("UIDStr") String id,
            @Field("inputListStr") String str,
            @Field("type") int type
    );

    @GET("rq/push/getBottleUnit")
    Observable<CompanyResult> queryCompanyList();
}
