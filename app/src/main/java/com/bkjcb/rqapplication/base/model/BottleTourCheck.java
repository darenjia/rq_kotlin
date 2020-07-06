package com.bkjcb.rqapplication.base.model;

/**
 * Created by DengShuai on 2019/10/31.
 * Description :
 */
public class BottleTourCheck {


    /**
     * mbtId : 00000ee5938c44199227a32f8a5e5be1
     * userGuid : 9d91e00f01df48be9df4714c90269b01
     * tourTime : 2019-10-17 14:55:24
     * tourPeople : 赵嘉良
     * tourState : 正常
     * tourRemark : 巡检正常
     */

    private String mbtId;
    private String userGuid;
    private String tourTime;
    private String tourPeople;
    private String tourState;
    private String tourRemark;

    public String getMbtId() {
        return mbtId;
    }

    public void setMbtId(String mbtId) {
        this.mbtId = mbtId;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public String getTourTime() {
        return tourTime;
    }

    public void setTourTime(String tourTime) {
        this.tourTime = tourTime;
    }

    public String getTourPeople() {
        return tourPeople;
    }

    public void setTourPeople(String tourPeople) {
        this.tourPeople = tourPeople;
    }

    public String getTourState() {
        return tourState;
    }

    public void setTourState(String tourState) {
        this.tourState = tourState;
    }

    public String getTourRemark() {
        return tourRemark;
    }

    public void setTourRemark(String tourRemark) {
        this.tourRemark = tourRemark;
    }
}
