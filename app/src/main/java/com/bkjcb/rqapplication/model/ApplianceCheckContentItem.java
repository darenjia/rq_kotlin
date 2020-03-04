package com.bkjcb.rqapplication.model;

/**
 * Created by DengShuai on 2020/2/20.
 * Description :
 */
public class ApplianceCheckContentItem {

    /**
     * guid : f31335acf25546419d8477dfba44bbe3
     * unitname :
     * xuhao : 11
     * cheakname : 人员培训记录
     * cheaktype : 企业制度制定与执行
     * cheakdatail : 培训记录
     * cheakrecord : 技术培训：
     * cheakrecord2 : 其他培训：
     */

    private String guid;
    private String unitname;
    private int xuhao;
    private String cheakname;
    private String cheaktype;
    private String cheakdatail;
    private String cheakrecord;
    private String cheakrecord2;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getUnitname() {
        return unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    public int getXuhao() {
        return xuhao;
    }

    public void setXuhao(int xuhao) {
        this.xuhao = xuhao;
    }

    public String getCheakname() {
        return cheakname;
    }

    public void setCheakname(String cheakname) {
        this.cheakname = cheakname;
    }

    public String getCheaktype() {
        return cheaktype;
    }

    public void setCheaktype(String cheaktype) {
        this.cheaktype = cheaktype;
    }

    public String getCheakdatail() {
        return cheakdatail;
    }

    public void setCheakdatail(String cheakdatail) {
        this.cheakdatail = cheakdatail;
    }

    public String getCheakrecord() {
        return cheakrecord;
    }

    public void setCheakrecord(String cheakrecord) {
        this.cheakrecord = cheakrecord;
    }

    public String getCheakrecord2() {
        return cheakrecord2;
    }

    public void setCheakrecord2(String cheakrecord2) {
        this.cheakrecord2 = cheakrecord2;
    }
}
