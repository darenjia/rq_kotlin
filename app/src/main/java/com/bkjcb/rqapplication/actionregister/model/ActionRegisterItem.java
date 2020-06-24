package com.bkjcb.rqapplication.actionregister.model;

import com.bkjcb.rqapplication.datebase.ObjectBox;

import java.io.Serializable;

import io.objectbox.Box;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by DengShuai on 2020/3/5.
 * Description :
 */
@Entity
public class ActionRegisterItem implements Serializable {
    @Id(assignable = true)
    public long id;


    /**
     * wid : 40286f817089a1a8017089f50d9d0004
     * lid : 40286f817089a1a8017089f50d9d0005
     * userId : 40286f816e598c60016e598ca2110002
     * case_source : 案件来源
     * zi : 字
     * di : 第
     * hao : 号
     * crime_time : 2020-02-26
     * crime_address : 案发地点
     * party : 当事人
     * party_address : 当事人联系地址
     * party_phone : 当事人联系电话
     * reporter : 举报人
     * reporter_address : 举报人联系地址
     * reporter_phone : 举报人联系电话
     * case_introduction : 案情简介
     * undertaker : 承办人
     * undertaker_time : 2020-02-27
     * undertaker_opinion : 承办人意见
     */

    private String uuid;
    private String wid;
    private String lid;
    private String userId;
    private String case_source;
    private String zi;
    private String di;
    private String hao;
    private String crime_time;
    private String crime_address;
    private String party;
    private String party_address;
    private String party_phone;
    private String reporter;
    private String reporter_address;
    private String reporter_phone;
    private String case_introduction;
    private String undertaker;
    private String undertaker_time;
    private String undertaker_opinion;
    private long systime;
    private int status;
    private String phoneftp;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPhoneftp() {
        return phoneftp;
    }

    public void setPhoneftp(String phoneftp) {
        this.phoneftp = phoneftp;
    }

    public long getSystime() {
        return systime;
    }

    public void setSystime(long systime) {
        this.systime = systime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCase_source() {
        return case_source;
    }

    public void setCase_source(String case_source) {
        this.case_source = case_source;
    }

    public String getZi() {
        return zi;
    }

    public void setZi(String zi) {
        this.zi = zi;
    }

    public String getDi() {
        return di;
    }

    public void setDi(String di) {
        this.di = di;
    }

    public String getHao() {
        return hao;
    }

    public void setHao(String hao) {
        this.hao = hao;
    }

    public String getCrime_time() {
        return crime_time;
    }

    public void setCrime_time(String crime_time) {
        this.crime_time = crime_time;
    }

    public String getCrime_address() {
        return crime_address;
    }

    public void setCrime_address(String crime_address) {
        this.crime_address = crime_address;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getParty_address() {
        return party_address;
    }

    public void setParty_address(String party_address) {
        this.party_address = party_address;
    }

    public String getParty_phone() {
        return party_phone;
    }

    public void setParty_phone(String party_phone) {
        this.party_phone = party_phone;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getReporter_address() {
        return reporter_address;
    }

    public void setReporter_address(String reporter_address) {
        this.reporter_address = reporter_address;
    }

    public String getReporter_phone() {
        return reporter_phone;
    }

    public void setReporter_phone(String reporter_phone) {
        this.reporter_phone = reporter_phone;
    }

    public String getCase_introduction() {
        return case_introduction;
    }

    public void setCase_introduction(String case_introduction) {
        this.case_introduction = case_introduction;
    }

    public String getUndertaker() {
        return undertaker;
    }

    public void setUndertaker(String undertaker) {
        this.undertaker = undertaker;
    }

    public String getUndertaker_time() {
        return undertaker_time;
    }

    public void setUndertaker_time(String undertaker_time) {
        this.undertaker_time = undertaker_time;
    }

    public String getUndertaker_opinion() {
        return undertaker_opinion;
    }

    public void setUndertaker_opinion(String undertaker_opinion) {
        this.undertaker_opinion = undertaker_opinion;
    }

    public static Box<ActionRegisterItem> getBox() {
        return ObjectBox.get().boxFor(ActionRegisterItem.class);
    }
}
