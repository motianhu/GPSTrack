package com.smona.gpstrack.map;

import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.db.table.Fence;
import com.smona.gpstrack.db.table.Location;
import com.smona.gpstrack.fence.bean.FenceBean;
import com.smona.gpstrack.map.search.AMapRouteSearch;
import com.smona.gpstrack.util.AMapUtil;
import com.smona.gpstrack.util.AppContext;
import com.smona.gpstrack.util.CommonUtils;

import java.util.List;


/**
 * 入参坐标都是WGS，使用时都需要转成CJS；出参需要转换成WGS
 */
public class MapGaode extends AMapRouteSearch implements AMap.OnMapClickListener {

    private Circle circle;
    private Marker pathMarker;

    void initMap(MapView mapView) {
        aMap = mapView.getMap();
        if (aMap == null) {
            return;
        }
        initMapObj(aMap, false);
    }

    public static void initMap(AMap aMap) {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);
        myLocationStyle.strokeWidth(0.01f);
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);
        myLocationStyle.strokeColor(Color.TRANSPARENT);
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation));
        aMap.setMyLocationStyle(myLocationStyle);
        initMapObj(aMap, true);
    }

    private static void initMapObj(AMap aMap, boolean isShowMy) {
        LatLng latLng = AMapUtil.wgsToCjg(AppContext.getAppContext(), ParamConstant.DEFAULT_POS.latitude, ParamConstant.DEFAULT_POS.longitude);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        aMap.setMyLocationEnabled(isShowMy);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
        if (ParamConstant.LOCALE_EN.equals(ConfigCenter.getInstance().getConfigInfo().getLocale())) {
            aMap.setMapLanguage(AMap.ENGLISH);
        } else {
            aMap.setMapLanguage(AMap.CHINESE);
        }
    }

    @Override
    public void animateCamera(double defaultLa, double defaultLo) {
        LatLng latLng = AMapUtil.wgsToCjg(AppContext.getAppContext(), defaultLa, defaultLo);
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
    }

    @Override
    public void setOnMapClickListener() {
        aMap.setOnMapClickListener(this);
    }

    //地图上选点
    @Override
    public void onAutoMapClick(FenceBean fenceBean) {
        boolean isLocation = CommonUtils.isInValidLatln(fenceBean.getLatitude(), fenceBean.getLongitude());
        if (isLocation) {
            fenceBean.setLatitude(GaodeLocationManager.getInstance().getLocation()[0]);
            fenceBean.setLongitude(GaodeLocationManager.getInstance().getLocation()[1]);
        }
        LatLng latLng = isLocation ? new LatLng(fenceBean.getLatitude(), fenceBean.getLongitude()) : AMapUtil.wgsToCjg(AppContext.getAppContext(), fenceBean.getLatitude(), fenceBean.getLongitude());
        circle = aMap.addCircle(new CircleOptions().
                center(latLng).
                fillColor(Fence.getFenceColor(fenceBean.getStatus())).
                radius(fenceBean.getRadius()).
                strokeWidth(1));
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
    }

    @Override
    public void setRadius(int radius) {
        if (circle != null) {
            circle.setRadius(radius);
        }
    }

    @Override
    public double getLatitude() {
        if (circle != null) {
            LatLng lng = AMapUtil.cgjToWGS(AppContext.getAppContext(), circle.getCenter());
            return lng.latitude;
        } else {
            return -1;
        }
    }

    @Override
    public double getLongitude() {
        if (circle != null) {
            LatLng lng = AMapUtil.cgjToWGS(AppContext.getAppContext(), circle.getCenter());
            return lng.longitude;
        } else {
            return -1;
        }
    }

    @Override
    public void drawMarker(double centerLa, double centerLo) {
        LatLng latLng = AMapUtil.wgsToCjg(AppContext.getAppContext(), centerLa, centerLo);
        if (pathMarker == null) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination));
            pathMarker = aMap.addMarker(markerOptions);
        } else {
            pathMarker.setPosition(latLng);
        }
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
    }

    @Override
    public void clear() {
        aMap.clear();
        pathMarker = null;
    }

    @Override
    public double[] getCurLocation() {
        return GaodeLocationManager.getInstance().getLocation();
    }

    @Override
    public void drawTrack(List<Location> points) {
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLUE).width(20);
        if (points.size() > 0) {
            // 起点
            Location p = points.get(0);
            LatLng latLng = AMapUtil.wgsToCjg(AppContext.getAppContext(), p.getLatitude(), p.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.amap_start));
            aMap.addMarker(markerOptions);
        }
        if (points.size() > 1) {
            // 终点
            Location p = points.get(points.size() - 1);
            LatLng latLng = AMapUtil.wgsToCjg(AppContext.getAppContext(), p.getLatitude(), p.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.amap_end));
            aMap.addMarker(markerOptions);
        }
        for (Location p : points) {
            LatLng latLng = AMapUtil.wgsToCjg(AppContext.getAppContext(), p.getLatitude(), p.getLongitude());
            polylineOptions.add(latLng);
            boundsBuilder.include(latLng);
        }
        aMap.addPolyline(polylineOptions);
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 30));
    }

    //地图上选点
    @Override
    public void onMapClick(LatLng latLng) {
        if (circle != null) {
            circle.setCenter(latLng);
        }
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
    }
}
