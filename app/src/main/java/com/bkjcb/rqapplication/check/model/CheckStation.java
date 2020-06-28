package com.bkjcb.rqapplication.check.model;


/**
 * Created by DengShuai on 2019/12/30.
 * Description :
 */
public class CheckStation {
    /**
     * qiyemingcheng : 上海浦东海光燃气有限公司
     * guid : 794574bcf5164e2f8d170adeb69676fa
     * license_code : 2010022020069
     * gas_station : 上海浦东海光有限公司储配站
     */

    private String qiyemingcheng;
    private String guid;
    private String license_code;
    private String gas_station;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public CheckStation() {
    }

    public String getQiyemingcheng() {
        return qiyemingcheng;
    }

    public void setQiyemingcheng(String qiyemingcheng) {
        this.qiyemingcheng = qiyemingcheng;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getLicense_code() {
        return license_code;
    }

    public void setLicense_code(String license_code) {
        this.license_code = license_code;
    }

    public String getGas_station() {
        return gas_station;
    }

    public void setGas_station(String gas_station) {
        this.gas_station = gas_station;
    }
}
