package com.bkjcb.rqapplication.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.AMapGestureListener;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.util.LocationUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static com.bkjcb.rqapplication.fragment.MapFragment.FILL_COLOR;
import static com.bkjcb.rqapplication.fragment.MapFragment.STROKE_COLOR;

/**
 * Created by DengShuai on 2020/4/29.
 * Description :
 */
public class MapLocationFragment extends BaseSimpleFragment implements AMapGestureListener {

    @BindView(R.id.question_location_text)
    TextView mLocationText;
    private TextureMapView mapView;
    private AMap aMap;
    private LatLng latLng;
    private GeocodeSearch geocodeSearch;
    private AddressQueryListener listener;


    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setListener(AddressQueryListener listener) {
        this.listener = listener;
    }

    public interface AddressQueryListener {
        void onSuccess(String s, LatLng latLng);

        void onClick();
    }

    public static MapLocationFragment newInstance(AddressQueryListener listener) {
        MapLocationFragment fragment = new MapLocationFragment();
        fragment.setListener(listener);
        return fragment;
    }

    public static MapLocationFragment newInstance(LatLng latLng) {
        MapLocationFragment fragment = new MapLocationFragment();
        fragment.setLatLng(latLng);
        return fragment;
    }

    @Override
    public void setResId() {
        resId = R.layout.fragment_map_location;
    }

    @Override
    protected void initView() {
        setupMap();
        if (listener != null) {
            initReGeocode();
        } else {
            mLocationText.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.question_location_text)
    public void OnClick(View v) {
        listener.onClick();
    }

    @Override
    protected void initData() {
    }

    private void setupMap() {
        aMap = mapView.getMap();
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setLogoPosition(Gravity.START);
        if (latLng != null) {
            uiSettings.setAllGesturesEnabled(false);
            moveCameraTo(latLng);
            return;
        }
        uiSettings.setScaleControlsEnabled(true);
        //uiSettings.setAllGesturesEnabled(false);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setZoomControlsEnabled(false);
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.strokeColor(STROKE_COLOR);
        myLocationStyle.strokeWidth(1);
        myLocationStyle.radiusFillColor(FILL_COLOR);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setAMapGestureListener(this);
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (latLng == null) {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    moveCameraTo(latLng);
                    getCurrentPosition();
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMapView(savedInstanceState);
    }


    private void initMapView(Bundle savedInstanceState) {
        mapView = view.findViewById(R.id.question_map);
        mapView.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    public void moveCameraTo(LatLng latLng) {
        if (latLng != null) {
            aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                    latLng, 18, 0, 0)));
            MarkerOptions markerOptions=new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(LocationUtil.getMapIconBitmap(R.drawable.icon_location));
            aMap.addMarker(markerOptions);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    public void getAddress(LatLonPoint latLonPoint) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 20,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocodeSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }

    @Override
    public void onDoubleTap(float v, float v1) {

    }

    @Override
    public void onSingleTap(float v, float v1) {

    }

    @Override
    public void onFling(float v, float v1) {

    }

    @Override
    public void onScroll(float v, float v1) {

    }

    @Override
    public void onLongPress(float v, float v1) {

    }

    @Override
    public void onDown(float v, float v1) {

    }

    @Override
    public void onUp(float v, float v1) {

    }

    @Override
    public void onMapStable() {
        getCurrentPosition();
    }

    private void getCurrentPosition() {
        latLng = aMap.getCameraPosition().target;
        getAddress(new LatLonPoint(latLng.latitude, latLng.longitude));
    }

    private void initReGeocode() {
        geocodeSearch = new GeocodeSearch(getContext());
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult result, int i) {
                if (i == AMapException.CODE_AMAP_SUCCESS) {
                    if (result != null && result.getRegeocodeAddress() != null
                            && result.getRegeocodeAddress().getFormatAddress() != null) {
                        String addressName = result.getRegeocodeAddress().getFormatAddress();
                        addressName = addressName.replace("上海市", "");
                        listener.onSuccess(addressName, latLng);
                    } else {
                        listener.onSuccess("暂未获取到附件地址", latLng);
                    }
                } else {
                    listener.onSuccess("暂未获取到附件地址", latLng);
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
    }
}
