package com.bkjcb.rqapplication.treatmentdefect.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.amap.api.maps.AMap
import com.amap.api.maps.AMap.OnMyLocationChangeListener
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.fragment.BaseSimpleFragment
import com.bkjcb.rqapplication.fragment.MapFragment
import com.bkjcb.rqapplication.util.LocationUtil
import kotlinx.android.synthetic.main.fragment_map_location.*

/**
 * Created by DengShuai on 2020/4/29.
 * Description :
 */
class MapLocationFragment : BaseSimpleFragment(), AMapGestureListener {
     private val STROKE_COLOR = Color.argb(0, 3, 145, 255)
     private val FILL_COLOR = Color.argb(0, 0, 0, 180)
    private lateinit var aMap: AMap
    private var latLng: LatLng? = null
    private var geocodeSearch: GeocodeSearch? = null
    private var listener: AddressQueryListener? = null
    private var isHideButton: Boolean = false

    private fun setIsHideButton(isHideButton: Boolean) {
        this.isHideButton = isHideButton
    }

    fun setLatLng(latLng: LatLng?) {
        this.latLng = latLng
    }

    fun setListener(listener: AddressQueryListener?) {
        this.listener = listener
    }

    override fun initResID(): Int {
        return R.layout.fragment_map_location
    }

    interface AddressQueryListener {
        fun onSuccess(s: String?, latLng: LatLng?)
        fun onClick()
    }

    override fun initView() {
        setupMap()
        if (listener != null) {
            initReGeocode()
        }
        if (latLng != null || isHideButton) {
            question_location_text.visibility = View.GONE
        }
        question_location_text.setOnClickListener {
            listener?.onClick()
        }
    }


    override fun initData() {}
    private fun setupMap() {
        aMap = question_map.map
        val uiSettings = aMap.getUiSettings()
        uiSettings.logoPosition = Gravity.START
        if (latLng != null) {
            uiSettings.setAllGesturesEnabled(false)
            moveCameraTo(latLng)
            return
        }
        uiSettings.isScaleControlsEnabled = true
        //uiSettings.setAllGesturesEnabled(false);
        uiSettings.isMyLocationButtonEnabled = true
        uiSettings.isZoomControlsEnabled = false
        val myLocationStyle: MyLocationStyle = MyLocationStyle()
        myLocationStyle.strokeColor(STROKE_COLOR)
        myLocationStyle.strokeWidth(1f)
        myLocationStyle.radiusFillColor(FILL_COLOR)
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER) //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.myLocationStyle = myLocationStyle //设置定位蓝点的Style
        aMap.isMyLocationEnabled = true // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setAMapGestureListener(this)
        aMap.setOnMyLocationChangeListener(OnMyLocationChangeListener { location ->
            if (latLng == null) {
                latLng = LatLng(location.latitude, location.longitude)
                moveCameraTo(latLng)
                takeCurrentPosition()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMapView(savedInstanceState)
    }

    private fun initMapView(savedInstanceState: Bundle?) {
        //mapView = contentView?.findViewById(R.id.question_map)
        question_map.onCreate(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        if (question_map != null) {
            question_map.onPause()
        }
    }

    private fun moveCameraTo(latLng: LatLng?) {
        if (latLng != null) {
            aMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition(
                    latLng, 18f, 0f, 0f)))
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            markerOptions.icon(LocationUtil.getMapIconBitmap(R.drawable.icon_location))
            aMap.addMarker(markerOptions)
        }
    }

    override fun onResume() {
        super.onResume()
        if (question_map != null) {
            question_map.onResume()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (question_map != null) {
            question_map.onDestroy()
        }
    }

    fun getAddress(latLonPoint: LatLonPoint?) {
        val query = RegeocodeQuery(latLonPoint, 20f,
                GeocodeSearch.AMAP) // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocodeSearch!!.getFromLocationAsyn(query) // 设置异步逆地理编码请求
    }

    override fun onDoubleTap(v: Float, v1: Float) {}
    override fun onSingleTap(v: Float, v1: Float) {}
    override fun onFling(v: Float, v1: Float) {}
    override fun onScroll(v: Float, v1: Float) {}
    override fun onLongPress(v: Float, v1: Float) {}
    override fun onDown(v: Float, v1: Float) {}
    override fun onUp(v: Float, v1: Float) {}
    override fun onMapStable() {
        takeCurrentPosition()
    }

    private fun takeCurrentPosition() {
        latLng = aMap.cameraPosition.target
        getAddress(LatLonPoint(latLng!!.latitude, latLng!!.longitude))
    }

    private fun initReGeocode() {
        geocodeSearch = GeocodeSearch(context)
        geocodeSearch!!.setOnGeocodeSearchListener(object : OnGeocodeSearchListener {
            override fun onRegeocodeSearched(result: RegeocodeResult, i: Int) {
                if (i == AMapException.CODE_AMAP_SUCCESS) {
                    if (result != null && result.regeocodeAddress != null && result.regeocodeAddress.formatAddress != null) {
                        var addressName = result.regeocodeAddress.formatAddress
                        addressName = addressName.replace("上海市", "")
                        listener!!.onSuccess(addressName, latLng)
                    } else {
                        listener!!.onSuccess("暂未获取到附件地址", latLng)
                    }
                } else {
                    listener!!.onSuccess("暂未获取到附件地址", latLng)
                }
            }

            override fun onGeocodeSearched(geocodeResult: GeocodeResult, i: Int) {}
        })
    }

    companion object {
        fun newInstance(listener: AddressQueryListener?): MapLocationFragment {
            val fragment = MapLocationFragment()
            fragment.setListener(listener)
            return fragment
        }

        fun newInstance(listener: AddressQueryListener?, isHideButton: Boolean): MapLocationFragment {
            val fragment = MapLocationFragment()
            fragment.setListener(listener)
            fragment.setIsHideButton(isHideButton)
            return fragment
        }

        fun newInstance(latLng: LatLng?): MapLocationFragment {
            val fragment = MapLocationFragment()
            fragment.setLatLng(latLng)
            return fragment
        }
    }
}