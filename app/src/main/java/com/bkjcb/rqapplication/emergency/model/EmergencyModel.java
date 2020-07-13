package com.bkjcb.rqapplication.emergency.model;

import java.io.Serializable;

/**
 * Created by DengShuai on 2020/7/8.
 * Description :
 */
public class EmergencyModel implements Serializable {

    /**
     * accidentid : 202006301111102564919
     * receivedate : 2020-06-30
     * completeaddress : 宝荻路500弄59号601室
     * memo : 110警号1312
     * state : 已销根
     * finisheddate : 2020-06-30
     * delflag : 0
     * report_party : 市应急联动中心
     * disposal_people : 燃气热线
     */

    private String accidentid;
    private String receivedate;
    private String completeaddress;
    private String memo;
    private String state;
    private String finisheddate;
    private String report_party;
    private String disposal_people;

    public String getAccidentid() {
        return accidentid;
    }

    public void setAccidentid(String accidentid) {
        this.accidentid = accidentid;
    }

    public String getReceivedate() {
        return receivedate;
    }

    public void setReceivedate(String receivedate) {
        this.receivedate = receivedate;
    }

    public String getCompleteaddress() {
        return completeaddress;
    }

    public void setCompleteaddress(String completeaddress) {
        this.completeaddress = completeaddress;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFinisheddate() {
        return finisheddate;
    }

    public void setFinisheddate(String finisheddate) {
        this.finisheddate = finisheddate;
    }

    public String getReport_party() {
        return report_party;
    }

    public void setReport_party(String report_party) {
        this.report_party = report_party;
    }

    public String getDisposal_people() {
        return disposal_people;
    }

    public void setDisposal_people(String disposal_people) {
        this.disposal_people = disposal_people;
    }
}
