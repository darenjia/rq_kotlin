package com.bkjcb.rqapplication.stationCheck.model;

import com.bkjcb.rqapplication.base.model.HttpResult;

import java.util.ArrayList;

/**
 * Created by DengShuai on 2020/1/6.
 * Description :
 */
public class CheckContentItemResult extends HttpResult {
    private ArrayList<CheckContentItem> datas;

    public ArrayList<CheckContentItem> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<CheckContentItem> datas) {
        this.datas = datas;
    }
}
