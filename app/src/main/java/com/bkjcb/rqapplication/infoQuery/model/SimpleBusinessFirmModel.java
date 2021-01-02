package com.bkjcb.rqapplication.infoQuery.model;

import java.io.Serializable;

/**
 * Created by DengShuai on 2020/12/30.
 * Description :
 */
public class SimpleBusinessFirmModel implements Serializable {

    private String qiyemingcheng;
    private String c_id;

    public String getQiyemingcheng() {
        return qiyemingcheng;
    }

    public void setQiyemingcheng(String qiyemingcheng) {
        this.qiyemingcheng = qiyemingcheng;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }
}
