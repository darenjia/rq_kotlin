package com.bkjcb.rqapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.LatLng;

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

    public static class UserInfo implements Parcelable {

        /**
         * userGuid : feb05d79d41d452ab33ae54ead51b557
         * userName : 1-糕点房（娄国庆）
         * userAddress : 长宁区新泾镇街道新泾三村83号
         * userType : 非居民
         * unitName : 百斯特
         * areaCode : 310105102
         * areaJc : 长宁
         * streetJc : 新泾镇
         */

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

        protected UserInfo(Parcel in) {
            userGuid = in.readString();
            userName = in.readString();
            userAddress = in.readString();
            userType = in.readString();
            unitName = in.readString();
            areaCode = in.readString();
            areaJc = in.readString();
            streetJc = in.readString();
            qrCode = in.readInt();
            x = in.readString();
            y = in.readString();
            latLng = in.readParcelable(LatLng.class.getClassLoader());
        }

        public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
            @Override
            public UserInfo createFromParcel(Parcel in) {
                return new UserInfo(in);
            }

            @Override
            public UserInfo[] newArray(int size) {
                return new UserInfo[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.userGuid);
            dest.writeString(this.userName);
            dest.writeString(this.userAddress);
            dest.writeString(this.userType);
            dest.writeString(this.unitName);
            dest.writeString(this.areaCode);
            dest.writeString(this.areaJc);
            dest.writeString(this.streetJc);
            dest.writeInt(this.qrCode);
            dest.writeString(this.x);
            dest.writeString(this.y);
            dest.writeParcelable(this.latLng,flags);
        }

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

    }
}
