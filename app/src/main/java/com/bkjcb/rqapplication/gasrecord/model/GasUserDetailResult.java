package com.bkjcb.rqapplication.gasrecord.model;

import com.bkjcb.rqapplication.model.HttpResult;

/**
 * Created by DengShuai on 2020/5/6.
 * Description :
 */
public class GasUserDetailResult extends HttpResult {
    private GasRecordModel datas;

    public GasRecordModel getDatas() {
        return datas;
    }

    public void setDatas(GasRecordModel datas) {
        this.datas = datas;
    }
}
