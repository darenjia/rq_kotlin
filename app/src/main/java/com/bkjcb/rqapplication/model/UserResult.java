package com.bkjcb.rqapplication.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DengShuai on 2019/10/28.
 * Description :
 */
public class UserResult extends HttpResult{
    @SerializedName("datas")
    private User datas;

    public User getDatas() {
        return datas;
    }

    public void setDatas(User datas) {
        this.datas = datas;
    }

    public static class User {

        /**
         * userId : 40286f816d43aa6c016d43aaa06000dc
         * userName : gyz_cm
         * realName : 上海/崇明/港沿镇
         * contactPhone : 18173966842
         * userType : 街镇用户
         * areaCode : 310151108
         */

        private String userId;
        private String userName;
        private String realName;
        private String contactPhone;
        private String userType;
        private String areaCode;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getContactPhone() {
            return contactPhone;
        }

        public void setContactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }
    }
}
