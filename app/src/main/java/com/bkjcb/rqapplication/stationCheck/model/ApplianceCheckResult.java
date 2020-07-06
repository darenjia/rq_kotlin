package com.bkjcb.rqapplication.stationCheck.model;

import com.bkjcb.rqapplication.base.model.HttpResult;

import java.util.List;

/**
 * Created by DengShuai on 2020/2/20.
 * Description :
 */
public class ApplianceCheckResult extends HttpResult {
    private List<ApplianceCheckContentItem> datas;

    public List<ApplianceCheckContentItem> getDatas() {
        return datas;
    }

    public void setDatas(List<ApplianceCheckContentItem> datas) {
        this.datas = datas;
    }
}