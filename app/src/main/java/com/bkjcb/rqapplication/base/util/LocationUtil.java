package com.bkjcb.rqapplication.base.util;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.orhanobut.logger.Logger;

/**
 * Created by DengShuai on 2019/2/15.
 * Description :
 */
public class LocationUtil {
    private static LocationUtil locationUtil;

    private AMapLocationClient mapLocationClient;

    public interface LocationInterface {
        void onLocationSuccess(AMapLocation location);

        void onLocationError(AMapLocation location);
    }

    public static LocationUtil getInstance() {
        if (locationUtil == null) {
            locationUtil = new LocationUtil();
        }
        return locationUtil;
    }

    private LocationInterface locationInterface;

    //使用高德定位SDK辅助定位
    public void setLocationSetting(Context context, LocationInterface location) {
        locationInterface = location;
        mapLocationClient = new AMapLocationClient(context);
        //声明AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = null;
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(2000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);
        //给定位客户端对象设置定位参数
        mapLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mapLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        Logger.i("定位成功！");
                        locationInterface.onLocationSuccess(aMapLocation);
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                        locationInterface.onLocationError(aMapLocation);
                    }
                }
            }
        });
    }

    public void startLoaction() {
        if (mapLocationClient != null) {
            mapLocationClient.startLocation();
        }
    }

    public void stopLocation() {
        if (mapLocationClient != null) {
            mapLocationClient.stopLocation();
        }
    }

    public void destoryLocation() {
        if (mapLocationClient != null) {
            mapLocationClient.onDestroy();
        }
    }

    public void getCurrentLocation(Context context, LocationInterface location) {
        locationInterface = location;
        mapLocationClient = new AMapLocationClient(context);
        //声明AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = null;
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        mLocationOption.setOnceLocation(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        //给定位客户端对象设置定位参数
        mapLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mapLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        Logger.i("定位成功！");
                        locationInterface.onLocationSuccess(aMapLocation);
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Logger.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                        locationInterface.onLocationError(aMapLocation);
                    }
                }
            }
        });
        mapLocationClient.startLocation();
    }
    public static BitmapDescriptor getMapIconBitmap(int id) {
        return BitmapDescriptorFactory.fromResource(id);

    }
}
