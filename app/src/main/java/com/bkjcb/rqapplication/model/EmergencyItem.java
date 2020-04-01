package com.bkjcb.rqapplication.model;

import com.bkjcb.rqapplication.datebase.ObjectBox;

import java.io.Serializable;

import io.objectbox.Box;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by DengShuai on 2020/3/27.
 * Description :
 */
@Entity
public class EmergencyItem implements Serializable {

    /**
     * userId : F7EE86DEA39B44BB95FDFE0A6A6B3319
     * reportingUnit : 报送单位
     * qushu : 黄浦
     * remark : 备注
     * reportingDate : 2020年3月12日15:08
     * accidentDate : 2020年3月12日15:08:28
     * accidentAddress : 事故地点
     * disposalPerson : 处置单位
     * keyPerson : 主要人员
     * reportingStaff : 报送人员
     * contactPhone : 联系方式
     * mainDescription : 主要描述
     * phoneftp : shiguxianchang/{uuid}
     */
    @Id(assignable = true)
    public long id;
    private String userId;
    private String reportingUnit;
    private String qushu;
    private String remark;
    private String reportingDate;
    private String accidentDate;
    private String accidentAddress;
    private String disposalPerson;
    private String keyPerson;
    private String reportingStaff;
    private String contactPhone;
    private String mainDescription;
    private String phoneftp;
    private int status;
    private String uuid;
    private long systime;

    public long getSystime() {
        return systime;
    }

    public void setSystime(long systime) {
        this.systime = systime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReportingUnit() {
        return reportingUnit;
    }

    public void setReportingUnit(String reportingUnit) {
        this.reportingUnit = reportingUnit;
    }

    public String getQushu() {
        return qushu;
    }

    public void setQushu(String qushu) {
        this.qushu = qushu;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(String reportingDate) {
        this.reportingDate = reportingDate;
    }

    public String getAccidentDate() {
        return accidentDate;
    }

    public void setAccidentDate(String accidentDate) {
        this.accidentDate = accidentDate;
    }

    public String getAccidentAddress() {
        return accidentAddress;
    }

    public void setAccidentAddress(String accidentAddress) {
        this.accidentAddress = accidentAddress;
    }

    public String getDisposalPerson() {
        return disposalPerson;
    }

    public void setDisposalPerson(String disposalPerson) {
        this.disposalPerson = disposalPerson;
    }

    public String getKeyPerson() {
        return keyPerson;
    }

    public void setKeyPerson(String keyPerson) {
        this.keyPerson = keyPerson;
    }

    public String getReportingStaff() {
        return reportingStaff;
    }

    public void setReportingStaff(String reportingStaff) {
        this.reportingStaff = reportingStaff;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getMainDescription() {
        return mainDescription;
    }

    public void setMainDescription(String mainDescription) {
        this.mainDescription = mainDescription;
    }

    public String getPhoneftp() {
        return phoneftp;
    }

    public void setPhoneftp(String phoneftp) {
        this.phoneftp = phoneftp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public static Box<EmergencyItem> getBox(){
        return ObjectBox.get().boxFor(EmergencyItem.class);
    }
}
