package com.bkjcb.rqapplication.check.model;

import com.bkjcb.rqapplication.model.HttpResult;

import java.util.List;

/**
 * Created by DengShuai on 2020/1/6.
 * Description :
 */
public class CheckStationResult extends HttpResult {
    private List<CheckStation> datas;

    public List<CheckStation> getDatas() {
        return datas;
    }

    public void setDatas(List<CheckStation> datas) {
        this.datas = datas;
    }
}
