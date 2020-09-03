package com.bkjcb.rqapplication.Map.Bean;


import java.io.Serializable;


/**
 * Created by DengShuai on 2019/2/27.
 * Description :
 */
public class LocationPosition implements Serializable{
    //经度
    private double longitude;
    //纬度
    private double latitude;

    public LocationPosition() {
    }

    public LocationPosition(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

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
