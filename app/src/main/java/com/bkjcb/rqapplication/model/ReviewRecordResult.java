package com.bkjcb.rqapplication.model;

import java.util.List;

/**
 * Created by DengShuai on 2020/5/6.
 * Description :
 */
public class ReviewRecordResult extends HttpResult {
    private List<ReviewRecord> datas;

    public List<ReviewRecord> getData() {
        return datas;
    }

    public void setData(List<ReviewRecord> data) {
        this.datas = data;
    }
}
