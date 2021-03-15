package com.bkjcb.rqapplication.gascylindermanagement.model;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by DengShuai on 2021/3/8.
 * Description :
 */
public class CirculationInfo {

    private boolean success;
    private List<ReturnValueBean> returnValue;
    private String message;
    private DataBean data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<ReturnValueBean> getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(List<ReturnValueBean> returnValue) {
        this.returnValue = returnValue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private String name;
        private String madedate;
        private String gpno;
        private String madeName;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMadedate() {
            return madedate;
        }

        public void setMadedate(String madedate) {
            this.madedate = madedate;
        }

        public String getGpno() {
            return gpno;
        }

        public void setGpno(String gpno) {
            this.gpno = gpno;
        }

        public String getMadeName() {
            return madeName;
        }

        public void setMadeName(String madeName) {
            this.madeName = madeName;
        }
    }

    public static class ReturnValueBean {

        private String opType;
        private String plateNumber;
        private String userCode;
        private String userName;
        private String userAddress;
        private String cDateTime;
        private int cNumber;
        private String operator;
        private String station;
        private String customername;
        private String faultName;

        public String getOpType() {
            return opType;
        }

        public void setOpType(String opType) {
            this.opType = opType;
        }

        public String getPlateNumber() {
            return plateNumber;
        }

        public void setPlateNumber(String plateNumber) {
            this.plateNumber = plateNumber;
        }

        public Object getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserAddress() {
            return userAddress;
        }

        public void setUserAddress(String userAddress) {
            this.userAddress = userAddress;
        }

        public String getCDateTime() {
            return cDateTime;
        }

        public void setCDateTime(String cDateTime) {
            this.cDateTime = cDateTime;
        }

        public int getCNumber() {
            return cNumber;
        }

        public void setCNumber(int cNumber) {
            this.cNumber = cNumber;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getStation() {
            return station;
        }

        public void setStation(String station) {
            this.station = station;
        }

        public String getCustomername() {
            return customername;
        }

        public void setCustomername(String customername) {
            this.customername = customername;
        }

        public String getFaultName() {
            return faultName;
        }

        public void setFaultName(String faultName) {
            this.faultName = faultName;
        }

        @Override
        public String toString() {
            StringBuilder builder=new StringBuilder();
            if(!TextUtils.isEmpty(plateNumber)){
                builder.append(plateNumber).append("\t\t");
            }
            if(!TextUtils.isEmpty(userCode)){
                builder.append(userCode).append("\t\t");
            }
            if(!TextUtils.isEmpty(userName)){
                builder.append(userName).append("\t\t");
            }
            if(!TextUtils.isEmpty(userAddress)){
                builder.append(userAddress).append("\t\t");
            }
            if(!TextUtils.isEmpty(station)){
                builder.append(station).append("\t\t");
            }
            if(!TextUtils.isEmpty(faultName)){
                builder.append(faultName).append("\t\t");
            }
            return builder.toString();
        }
    }
}
