package com.bkjcb.rqapplication.treatmentdefect.model;

import com.bkjcb.rqapplication.model.HttpResult;

import java.util.List;

/**
 * Created by DengShuai on 2020/6/16.
 * Description :
 */
public class TreatmentResult<T> extends HttpResult {
    private List<T> datas;
    private int totalCount;
    private boolean pushSuccess;

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isPushSuccess() {
        return pushSuccess;
    }

    public void setPushSuccess(boolean pushSuccess) {
        this.pushSuccess = pushSuccess;
    }
}
