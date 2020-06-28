package com.bkjcb.rqapplication.model;

import java.io.Serializable;

/**
 * Created by DengShuai on 2020/6/10.
 * Description :
 */
public class DefectTreatmentModel implements Serializable {
    /**
     * CasesType : 2
     * UserName : 1-馒头店（何成春）
     * JzReasons : null
     * Qu : 上海/静安/临汾路街道
     * MbuGuid : 132e727acf5044809e7b8be45cdad623
     * ProcessTime : null
     * UserCode : 20180921000206
     * MtfId : 2c92448b6dd3d1f8016dd3d903c70722
     * UserAddress : 临汾路1312号
     * Flag : 0
     * ContactPhone : 13122397118
     * ProcessState : 2
     * UnitCode : 311512
     * UnitJc : 百斯特
     * IdCard : 342827197111145815
     * UserType : 1
     */

    private String CasesType;
    private String UserName;
    private String JzReasons;
    private String Qu;
    private String ProcessTime;
    private String UserCode;
    private String MtfId;
    private String UserAddress;
    private String ContactPhone;
    private String UnitCode;
    private String UnitJc;
    private String IdCard;
    private String Opinions;
    private String MbuGuid;
    private int UserType;
    private int Flag;
    private int ProcessState;

    public int getProcessState() {
        return ProcessState;
    }

    public void setProcessState(int processState) {
        ProcessState = processState;
    }

    public String getMbuGuid() {
        return MbuGuid;
    }

    public void setMbuGuid(String mbuGuid) {
        MbuGuid = mbuGuid;
    }

    public int getFlag() {
        return Flag;
    }

    public void setFlag(int flag) {
        Flag = flag;
    }

    public String getCasesType() {
        return CasesType;
    }

    public void setCasesType(String CasesType) {
        this.CasesType = CasesType;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getJzReasons() {
        return JzReasons;
    }

    public void setJzReasons(String JzReasons) {
        this.JzReasons = JzReasons;
    }

    public String getQu() {
        return Qu;
    }

    public void setQu(String Qu) {
        this.Qu = Qu;
    }

    public String getProcessTime() {
        return ProcessTime;
    }

    public void setProcessTime(String ProcessTime) {
        this.ProcessTime = ProcessTime;
    }

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String UserCode) {
        this.UserCode = UserCode;
    }

    public String getMtfId() {
        return MtfId;
    }

    public void setMtfId(String MtfId) {
        this.MtfId = MtfId;
    }

    public String getUserAddress() {
        return UserAddress;
    }

    public void setUserAddress(String UserAddress) {
        this.UserAddress = UserAddress;
    }

    public String getContactPhone() {
        return ContactPhone;
    }

    public void setContactPhone(String ContactPhone) {
        this.ContactPhone = ContactPhone;
    }

    public String getUnitCode() {
        return UnitCode;
    }

    public void setUnitCode(String UnitCode) {
        this.UnitCode = UnitCode;
    }

    public String getUnitJc() {
        return UnitJc;
    }

    public void setUnitJc(String UnitJc) {
        this.UnitJc = UnitJc;
    }

    public String getIdCard() {
        return IdCard;
    }

    public void setIdCard(String IdCard) {
        this.IdCard = IdCard;
    }

    public int getUserType() {
        return UserType;
    }

    public void setUserType(int UserType) {
        this.UserType = UserType;
    }

    public String getOpinions() {
        return Opinions;
    }

    public void setOpinions(String opinions) {
        Opinions = opinions;
    }
}
