package com.bkjcb.rqapplication.base.model;

/**
 * Created by DengShuai on 2020/7/1.
 * Description :
 */
public class SimpleHttpResult<T> extends HttpResult {
    private T datas;
    private int totalCount;

    public T getDatas() {
        return datas;
    }

    public void setDatas(T datas) {
        this.datas = datas;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
