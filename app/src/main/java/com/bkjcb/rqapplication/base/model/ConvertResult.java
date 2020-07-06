package com.bkjcb.rqapplication.base.model;

import java.util.List;

/**
 * Created by DengShuai on 2019/3/26.
 * Description :
 */
public class ConvertResult {
    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private List<LocationPosition> data;

    public List<LocationPosition> getData() {
        return data;
    }

    public void setData(List<LocationPosition> data) {
        this.data = data;
    }

    public static class LocationPosition {
        private double longitude;
        //纬度
        private double latitude;

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }
    }
}
