package com.bkjcb.rqapplication.check.model;

import com.bkjcb.rqapplication.model.HttpResult;

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
