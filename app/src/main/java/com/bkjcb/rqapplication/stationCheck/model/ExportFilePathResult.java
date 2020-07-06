package com.bkjcb.rqapplication.stationCheck.model;

import com.bkjcb.rqapplication.base.model.HttpResult;

/**
 * Created by DengShuai on 2020/5/22.
 * Description :
 */
public class ExportFilePathResult extends HttpResult {
    private String datas;

    public String getDatas() {
        return datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
    }
}
