package com.bkjcb.rqapplication.treatmentdefect.model;

/**
 * Created by DengShuai on 2019/10/31.
 * Description :
 */
public class BottleSaleCheck{

    /**
     * unitName : 上液
     * saleTime : 2019-09-17 16:24:48
     * bottleCode : 12793326
     * checkPeople : 王海春
     * checkState : 一般隐患
     * mbsId : 1937251
     * mbcId : 1306660
     */

    private String unitName;
    private String saleTime;
    private String bottleCode;
    private String checkPeople;
    private String checkState;
    private String checkRemark;
    private int mbsId;
    private int mbcId;

    public String getCheckRemark() {
        return checkRemark;
    }

    public void setCheckRemark(String checkRemark) {
        this.checkRemark = checkRemark;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(String saleTime) {
        this.saleTime = saleTime;
    }

    public String getBottleCode() {
        return bottleCode;
    }

    public void setBottleCode(String bottleCode) {
        this.bottleCode = bottleCode;
    }

    public String getCheckPeople() {
        return checkPeople;
    }

    public void setCheckPeople(String checkPeople) {
        this.checkPeople = checkPeople;
    }

    public String getCheckState() {
        return checkState;
    }

    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    public int getMbsId() {
        return mbsId;
    }

    public void setMbsId(int mbsId) {
        this.mbsId = mbsId;
    }

    public int getMbcId() {
        return mbcId;
    }

    public void setMbcId(int mbcId) {
        this.mbcId = mbcId;
    }
}
