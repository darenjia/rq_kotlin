package com.bkjcb.rqapplication.contact.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Unit {
    @Id(assignable = true)
    long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * id : 2
     * quxian : 0
     * address : 徐家汇路579号
     * tel : 65079898
     * fax : 53014925
     * zipcode : 200023
     * levelID : 400
     * isShow : 0
     * districtName :
     */

    private int uid;
    private String quxian;
    private String address;
    private String tel;
    private String fax;
    private String zipcode;
    private int levelid;
    private int isShow;
    private String districtName;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getQuxian() {
        return quxian;
    }

    public void setQuxian(String quxian) {
        this.quxian = quxian;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public int getLevelid() {
        return levelid;
    }

    public void setLevelid(int levelid) {
        this.levelid = levelid;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }
}