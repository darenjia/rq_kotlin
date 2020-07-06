package com.bkjcb.rqapplication.treatment.model;

import com.bkjcb.rqapplication.model.HttpResult;

/**
 * Created by DengShuai on 2020/6/22.
 * Description :
 */
public class DefectDetailResult extends HttpResult {
    private DefectDetail datas;
    private boolean pushSuccess;

    public DefectDetail getDatas() {
        return datas;
    }

    public void setDatas(DefectDetail datas) {
        this.datas = datas;
    }

    public boolean isPushSuccess() {
        return pushSuccess;
    }

    public void setPushSuccess(boolean pushSuccess) {
        this.pushSuccess = pushSuccess;
    }
}
