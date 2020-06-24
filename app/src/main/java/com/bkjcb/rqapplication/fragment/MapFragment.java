package com.bkjcb.rqapplication.fragment;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.animation.AlphaAnimation;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.AnimationSet;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.bkjcb.rqapplication.AddUserActivity;
import com.bkjcb.rqapplication.Constants;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.eventbus.MessageEvent;
import com.bkjcb.rqapplication.treatmentdefect.model.UserInfoResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Unbinder;

/**
 * Created by DengShuai on 2019/6/20.
 * Description :
 */
public class MapFragment extends BaseFragment implements View.OnClickListener, AMap.InfoWindowAdapter {

    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    protected static final int STROKE_COLOR = Color.argb(0, 3, 145, 255);
    protected static final int FILL_COLOR = Color.argb(0, 0, 0, 180);
    private MapView mapView;
    private View view;
    private AMap aMap;
    //private ImageView imageView;
    private boolean isFirst = true;
    private LatLng positonLatLng;
    private Point point;
    private String address = "";
    private LinearLayout btns;
    private String conditions;
    private Polyline newPolyLine;
    private TextView status;
    private int lastColor;
    private Marker breatheMarker;
    private List<Object> objects;
    private LinearLayout mBottomLayout;
    private TextView mRotueTimeDes;
    private Marker focusMarker;
    private Unbinder unbinder;
    private List<UserInfoResult.UserInfo> latLngs;

    public void setLatLngs(List<UserInfoResult.UserInfo> latLngs) {
        this.latLngs = latLngs;
        aMap.clear();
        addPointToMap();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_map, null);
            mapView = view.findViewById(R.id.map_view);
            mapView.onCreate(savedInstanceState);
            ImageView addBtn = view.findViewById(R.id.operation_layout);
            addBtn.setOnClickListener(this);
        }
        aMap = mapView.getMap();
        setUpMap("");
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {

    }

    private void setUpMap(String s) {
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(Constants.SHANGHAI, 18, 0, 0)));
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setLogoPosition(Gravity.START);
        uiSettings.setScaleControlsEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);
        //uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setZoomControlsEnabled(false);
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(1);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(FILL_COLOR);
        aMap.setInfoWindowAdapter(this);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        //myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);
      /*  aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (isFirst) {
                    //latLng = new LatLng(location.getLatitude(), location.getLongitude());
                  *//*  if (true) {
                    } else {
                        latLng = new LatLng(31.1988, 121.420531);
                    }*//*
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));
                    //getCurrentPosition(true);
                    isFirst = false;
                }
            }
        });*/
       /* aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                    if (drivingRouteOverlay != null) {
                        drivingRouteOverlay.removeFromMap();
                        mBottomLayout.setVisibility(View.GONE);
                        drivingRouteOverlay = null;
                    }
                }
            }
        });*/
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (focusMarker != null && focusMarker.isInfoWindowShown()) {
                    focusMarker.hideInfoWindow();
                }
                focusMarker = marker;
                return TextUtils.isEmpty(marker.getTitle());
            }
        });
       /* aMap.setOnMapLongClickListener(new AMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                positonLatLng = latLng;
                startBreatheAnimation(latLng, 1);
                startSelectLines();
            }
        });
        */
    }

    private void addMarker(UserInfoResult.UserInfo info) {
        if (info.getLatLng() == null) {
            return;
        }
        MarkerOptions options = new MarkerOptions();
        options.position(info.getLatLng());
        options.title(info.getUserName());
        options.snippet(info.getUserAddress());
        //options.icon(Utils.getBitmap(R.drawable.point_red));
        aMap.addMarker(options);
    }

    private void addLine(List<LatLng> list) {
        aMap.clear();
        PolylineOptions options = new PolylineOptions();
        options.width(6);
        options.color(Color.RED);
        options.addAll(list);
        aMap.addPolyline(options);
    }

    private void addPolygon(List<LatLng> list) {
        aMap.clear();
        PolygonOptions options = new PolygonOptions();
        options.fillColor(Color.RED);
        options.strokeColor(Color.RED);
        options.addAll(list);
        aMap.addPolygon(options);
    }

    private void getCurrentPosition(boolean isFirst) {
       /* if (!isFirst) {
            latLng = aMap.getCameraPosition().target;
        }*/
        //Logger.w(latLng.latitude + "::" + latLng.longitude);
    }


    public BitmapDescriptor getBitmap(int id) {
        return BitmapDescriptorFactory.fromResource(id);
    }


    private LatLng createNewLatLng(LatLng latLng) {
        return new LatLng(latLng.latitude, latLng.longitude + 0.0001);
    }


    private CircleOptions createOptions(LatLng latLng, double radius) {
        return new CircleOptions().center(latLng).radius(radius)
                .fillColor(Color.argb(90, 65, 191, 237))
                .strokeWidth(0);
    }

    private int getRGBColor(int r, int g, int b) {
        return Color.rgb(r, g, b);
    }


    @Override
    public void onClick(View v) {
        AddUserActivity.ToAddUserActivity(getContext());
    }

    private AnimationSet getAnimationSet(float range) {
        AnimationSet animationSet = new AnimationSet(true);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 0f);
        alphaAnimation.setDuration(2000);
        // 设置不断重复
        alphaAnimation.setRepeatCount(Animation.INFINITE);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1, range, 1, range);
        scaleAnimation.setDuration(2000);
        // 设置不断重复
        scaleAnimation.setRepeatCount(Animation.INFINITE);

        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setInterpolator(new LinearInterpolator());
        return animationSet;
    }

    private Animation getGrowAnimation() {
        Animation animation = new ScaleAnimation(0, 1, 0, 1);
        animation.setInterpolator(new AccelerateInterpolator());
        //整个移动所需要的时间
        animation.setDuration(1000);
        return animation;
    }

    @Override
    public View getInfoWindow(Marker marker) {

        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return initView(marker);
    }

    private View initView(Marker marker) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_infowindow, null);
        LinearLayout navigation = (LinearLayout) view.findViewById(R.id.navigation_LL);
        LinearLayout call = (LinearLayout) view.findViewById(R.id.call_LL);
        TextView nameTV = (TextView) view.findViewById(R.id.name);
        TextView addrTV = (TextView) view.findViewById(R.id.addr);

        nameTV.setText(marker.getTitle());
        addrTV.setText(marker.getSnippet());

        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                }
            }
        });
        //call.setOnClickListener(this);
        return view;
    }

    public void addPointToMap() {
        if (latLngs != null && latLngs.size() > 0) {
            for (UserInfoResult.UserInfo lng : latLngs) {
                addMarker(lng);
            }
        }
    }

}
