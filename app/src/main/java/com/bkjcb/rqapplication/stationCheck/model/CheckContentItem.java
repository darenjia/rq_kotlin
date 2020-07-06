package com.bkjcb.rqapplication.stationCheck.model;

/**
 * Created by DengShuai on 2019/12/24.
 * Description :
 */
public class CheckContentItem {

    /**
     * id : 476da99088c54e87af0bbd2dd29f6885
     * resultCount : 0
     * lastModifiedDate : null
     * markAsDeleted : false
     * zhandianleixing : 储配站
     * year : 2019
     * leibie : 经营条件
     * jianchaneirong : 有经过培训合格的专业技术人员和专业服务人员。
     * jianchayiju : 《条例》19条
     * jianchalanmu : 内业资料
     * xuhao : 3
     * jianchaxiangSet : []
     */
    private String id;
    private String zhandianleixing;
    private String year;
    private String leibie;
    private String jianchaneirong;
    private String jianchayiju;
    private String jianchalanmu;
    private int xuhao;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZhandianleixing() {
        return zhandianleixing;
    }

    public void setZhandianleixing(String zhandianleixing) {
        this.zhandianleixing = zhandianleixing;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLeibie() {
        return leibie;
    }

    public void setLeibie(String leibie) {
        this.leibie = leibie;
    }

    public String getJianchaneirong() {
        return jianchaneirong;
    }

    public void setJianchaneirong(String jianchaneirong) {
        this.jianchaneirong = jianchaneirong;
    }

    public String getJianchayiju() {
        return jianchayiju;
    }

    public void setJianchayiju(String jianchayiju) {
        this.jianchayiju = jianchayiju;
    }

    public String getJianchalanmu() {
        return jianchalanmu;
    }

    public void setJianchalanmu(String jianchalanmu) {
        this.jianchalanmu = jianchalanmu;
    }

    public int getXuhao() {
        return xuhao;
    }

    public void setXuhao(int xuhao) {
        this.xuhao = xuhao;
    }

    /*public static Box<CheckContentItem> getBox() {
        return ObjectBox.get().boxFor(CheckContentItem.class);
    }*/
}
