package com.bkjcb.rqapplication.userRecord.model;

import com.amap.api.maps.model.LatLng;
import com.bkjcb.rqapplication.base.model.HttpResult;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DengShuai on 2019/10/24.
 * Description :
 */
public class UserInfoResult extends HttpResult {
    private List<UserInfo> datas;

    public List<UserInfo> getDatas() {
        return datas;
    }

    public void setDatas(List<UserInfo> datas) {
        this.datas = datas;
    }

    private int totalCount;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public static class UserInfo implements Serializable {

        /**
         * userGuid : feb05d79d41d452ab33ae54ead51b557
         * userName : 1-糕点房（娄国庆）
         * userAddress : 长宁区新泾镇街道新泾三村83号
         * userType : 非居民
         * unitName : 百斯特
         * areaCode : 310105102
         * areaJc : 长宁
         * streetJc : 新泾镇
         * "mbu_id": 1048,
         * "yijiandang": "0"
         */

        private String mbu_id;
        private String yijiandang;
        private String userGuid;
        private String userName;
        private String userAddress;
        private String userType;
        private String unitName;
        private String areaCode;
        private String areaJc;
        private String streetJc;
        private int qrCode;
        private String x;
        private String y;
        private LatLng latLng;

        public LatLng getLatLng() {
            return latLng;
        }

        public void setLatLng(LatLng latLng) {
            this.latLng = latLng;
        }

        public int getQrCode() {
            return qrCode;
        }

        public void setQrCode(int qrCode) {
            this.qrCode = qrCode;
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }

        public String getUserGuid() {
            return userGuid;
        }

        public void setUserGuid(String userGuid) {
            this.userGuid = userGuid;
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

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getAreaJc() {
            return areaJc;
        }

        public void setAreaJc(String areaJc) {
            this.areaJc = areaJc;
        }

        public String getStreetJc() {
            return streetJc;
        }

        public void setStreetJc(String streetJc) {
            this.streetJc = streetJc;
        }

        public String getMbu_id() {
            return mbu_id;
        }

        public void setMbu_id(String mbu_id) {
            this.mbu_id = mbu_id;
        }

        public String getYijiandang() {
            return yijiandang;
        }

        public void setYijiandang(String yijiandang) {
            this.yijiandang = yijiandang;
        }


    }
}
