package com.bkjcb.rqapplication.Map;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.bkjcb.rqapplication.Constants;
import com.bkjcb.rqapplication.Map.Bean.EquipmentLine;
import com.bkjcb.rqapplication.Map.Bean.EquipmentPoint;
import com.bkjcb.rqapplication.Map.Bean.GuanxianDetail;
import com.bkjcb.rqapplication.Map.Bean.GuanxianResult;
import com.bkjcb.rqapplication.Map.Bean.LocationPosition;
import com.bkjcb.rqapplication.Map.Bean.MapData;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.fragment.BaseFragment;
import com.bkjcb.rqapplication.base.model.ConvertResult;
import com.bkjcb.rqapplication.base.retrofit.ConvertService;
import com.hss01248.dialog.StyledDialog;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DengShuai on 2019/6/20.
 * Description :
 */
public class MapFragment extends BaseFragment implements View.OnClickListener {

    protected final int STROKE_COLOR = Color.argb(0, 3, 145, 255);
    protected final int FILL_COLOR = Color.argb(0, 0, 0, 180);
    private MapView mapView;
    private View view;
    private AMap aMap;
    private boolean isFirst = true;
    private LatLng positionLatLng;
    private Polyline newPolyLine;
    private int lastColor;
    private Retrofit retrofit;
    private ArcGisSearchUtil util;
    private Button searchBtn;
    private Map<Polyline, String> map;
    private Retrofit retrofit2;
    private ViewHolder holder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_map, null);
            mapView = view.findViewById(R.id.map_view);
            mapView.onCreate(savedInstanceState);
            searchBtn = view.findViewById(R.id.info_operation);
            searchBtn.setOnClickListener(this);
            StyledDialog.init(getContext());
            initArcGisUtil();
        }
        aMap = mapView.getMap();
        setUpMap();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
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

    private void setUpMap() {
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(Constants.JS, 18, 0, 0)));
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setLogoPosition(Gravity.START);
        uiSettings.setScaleControlsEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setZoomControlsEnabled(false);
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.strokeColor(STROKE_COLOR);
        myLocationStyle.strokeWidth(1);
        myLocationStyle.radiusFillColor(FILL_COLOR);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(5000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
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
        aMap.setOnMapLongClickListener(new AMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                positionLatLng = latLng;
                addMarker(latLng);
                searchBtn.setVisibility(View.VISIBLE);
            }
        });
        aMap.setOnPolylineClickListener(new AMap.OnPolylineClickListener() {
            @Override
            public void onPolylineClick(Polyline polyline) {
                lastColor = polyline.getColor();
                if (newPolyLine != null) {
                    if (newPolyLine.getId().equals(polyline.getId())) {
                        return;
                    }
                    newPolyLine.setColor(lastColor);
                }
                newPolyLine = polyline;
                polyline.setColor(R.color.colorSecondDrayText);
                String code = map.get(polyline);
                findDetail(code);
            }
        });
    }

    private void findDetail(String code) {
        if (!TextUtils.isEmpty(code)) {
            if (retrofit2 == null) {
                retrofit2 = new Retrofit
                        .Builder()
                        .baseUrl("http://120.253.250.135:13001/")
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            disposable = retrofit2.create(ConvertService.class)
                    .getGuanXianDetailResult(code)
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<GuanxianResult>() {
                        @Override
                        public void accept(GuanxianResult result) throws Exception {
                            if (result.isSuccess()) {
                                alertUserInfo(result.getTongzhi());
                            } else {
                                Toast.makeText(getContext(), "查询失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Toast.makeText(getContext(), "查询错误", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void addMarker(LatLng latLng) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
//        options.icon(Utils.getBitmap(R.drawable.point_red));
        aMap.addMarker(options);
    }

    public BitmapDescriptor getBitmap(int id) {
        return BitmapDescriptorFactory.fromResource(id);
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
        Toast.makeText(getContext(), "正在查询周边管线", Toast.LENGTH_SHORT).show();
        aMap.clear();
        transformCoordinate(positionLatLng);
        if (map == null) {
            map = new HashMap<>(20);
        } else {
            map.clear();
        }
        v.setVisibility(View.GONE);
    }

    private void transformCoordinate(LatLng latLng) {
        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl("http://47.103.63.36:8084/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        disposable = retrofit.create(ConvertService.class)
                .convert("gcj02", "sh", latLng.longitude + "," + latLng.latitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ConvertResult>() {
                    @Override
                    public void accept(ConvertResult convertResult) throws Exception {
                        if (convertResult.isSuccess() && convertResult.getData().size() > 0) {
                            LocationPosition position = convertResult.getData().get(0);
                            util.search(position.getLongitude(), position.getLatitude());
                        } else {
                            Toast.makeText(getContext(), convertResult.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initArcGisUtil() {
        util = new ArcGisSearchUtil(new ArcGisSearchUtil.SearchStatus() {
            @Override
            public void searchSuccess(ArrayList<MapData> locationPositions, int type) {
                if (locationPositions.size() > 0) {
                    convertPosition(locationPositions);
                }
            }

            @Override
            public void searchFailed() {
                Logger.w("查询失败");
            }
        });
    }

    //上海转高德
    private void convertPosition(final List<MapData> positions) {
        StringBuilder locations = new StringBuilder();
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getType() == MapData.LINE) {
                List<EquipmentPoint> list = ((EquipmentLine) positions.get(i)).getLine();
                for (int j = 0; j < list.size(); j++) {
                    locations.append(getPositionString(list.get(j)));
                }

            }
        }
        disposable = retrofit.create(ConvertService.class)
                .convert("sh", "gcj02", locations.substring(0, locations.length() - 1))
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<ConvertResult>() {
                    @Override
                    public void accept(ConvertResult result) throws Exception {
                        if (result.isSuccess()) {
                            showMarker(result.getData(), positions);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });
    }

    private String getPositionString(EquipmentPoint point) {
        StringBuilder builder = new StringBuilder();
        LocationPosition position = point.getPosition();
        builder.append(position.getLongitude()).append(",").append(position.getLatitude()).append("|");
        return builder.toString();
    }

    private void showMarker(List<LocationPosition> s, List<MapData> data) {
        EquipmentPoint point;
        if (s.size() == 0) {
            return;
        }
        int z = 0;
        EquipmentLine line;
        for (int i = 0; i < data.size(); i++) {
            line = (EquipmentLine) data.get(i);
            for (int j = 0; j < line.getLine().size(); j++) {
                point = line.getLine().get(j);
                point.setPosition(s.get(z++));
            }
        }
        addPolyline(data);

    }

    private void addPolyline(List<MapData> lines) {
        if (lines == null) {
            return;
        }
        for (int i = 0; i < lines.size(); i++) {
            EquipmentLine line = (EquipmentLine) lines.get(i);
            PolylineOptions options = getLineOptions(line);
            Polyline polyline = aMap.addPolyline(options);
            map.put(polyline, line.getEquimentCode());
        }

    }

    private PolylineOptions getLineOptions(EquipmentLine line) {
        PolylineOptions options = new PolylineOptions();
        for (EquipmentPoint position : line.getLine()) {
            options.add(new LatLng(position.getPosition().getLatitude(), position.getPosition().getLongitude()));
        }
        options.width(12);
        switch (line.getName()) {
            case "燃气":
                options.color(getRGBColor(3));
                break;
            case "排水":
                options.color(getRGBColor(1));
                break;
            case "工业":
                options.color(getRGBColor(2));
                break;
            case "其他":
                options.color(getRGBColor(4));
                break;
            case "热力":
                options.color(getRGBColor(5));
                break;
            case "电力":
                options.color(getRGBColor(6));
                break;
            case "给水":
                options.color(getRGBColor(7));
                break;
            case "通信":
                options.color(getRGBColor(8));
                break;
            default:
                options.color(getRGBColor(0));
                break;
        }
        return options;
    }

    private int getRGBColor(int type) {
        if (type == 1) {
            return getRGBColor(153, 102, 51);
        } else if (type == 2) {
            return getRGBColor(93, 156, 236);
        } else if (type == 3) {
            return getRGBColor(255, 153, 204);
        } else if (type == 4) {
            return getRGBColor(232, 206, 77);
        } else if (type == 5) {
            return getRGBColor(46, 204, 113);
        } else if (type == 6) {
            return getRGBColor(255, 51, 0);
        } else if (type == 7) {
            return getRGBColor(102, 102, 255);
        } else if (type == 8) {
            return getRGBColor(51, 204, 51);
        } else if (type == 9) {
            return getRGBColor(18, 33, 121);
        } else {
            return getRGBColor(153, 153, 153);
        }

    }

    protected void alertUserInfo(GuanxianDetail detail) {
        StyledDialog.buildCustomBottomSheet(initDialogView(detail))
                .setBackground(getResources().getColor(R.color.colorAlpha))
                .setBottomSheetDialogMaxHeightPercent(0.4f).show();
    }

    private View initDialogView(GuanxianDetail detail) {
        if (holder == null) {
            View view = getLayoutInflater().inflate(R.layout.view_piple_line_info, null);
            holder = new ViewHolder(view);
        }
        holder.mInfoId.setText(detail.getDistrict());
        holder.mInfoCredit.setText(detail.getPipeMATER());
        holder.mInfoProduct.setText(detail.getPipeSHAPE());
        holder.mInfoProductAddress.setText(detail.getRoad());
        holder.mInfoCode.setText(detail.getWidth() + " mm");
        holder.mInfoSale.setText(detail.getHigh() + " mm");
        holder.mInfoSaleAddress.setText(detail.getLength() + " m");
        holder.mInfoTel.setText(detail.getEnbed());
        holder.mInfoType.setText(detail.getPipeTYPE());
        return holder.view;
    }

    class ViewHolder {
        View view;
        TextView mInfoId;
        TextView mInfoCredit;
        TextView mInfoProduct;
        TextView mInfoProductAddress;
        TextView mInfoCode;
        TextView mInfoSale;
        TextView mInfoSaleAddress;
        TextView mInfoTel;
        TextView mInfoType;

        ViewHolder(View view) {
            this.view = view;
            this.mInfoId = (TextView) view.findViewById(R.id.info_id);
            this.mInfoCredit = (TextView) view.findViewById(R.id.info_credit);
            this.mInfoProduct = (TextView) view.findViewById(R.id.info_product);
            this.mInfoProductAddress = (TextView) view.findViewById(R.id.info_product_address);
            this.mInfoCode = (TextView) view.findViewById(R.id.info_code);
            this.mInfoSale = (TextView) view.findViewById(R.id.info_sale);
            this.mInfoSaleAddress = (TextView) view.findViewById(R.id.info_sale_address);
            this.mInfoTel = (TextView) view.findViewById(R.id.info_tel);
            this.mInfoType=view.findViewById(R.id.info_type);
        }
    }
}
