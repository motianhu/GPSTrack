package com.smona.gpstrack.map;

import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.map.listener.OnMapClickListener;
import com.smona.gpstrack.util.AMapUtil;
import com.smona.gpstrack.util.AppContext;


/**
 * 入参坐标都是WGS，使用时都需要转成CJS；出参需要转换成WGS
 */
public class MapGaode implements IMap, AMap.OnMapClickListener {

    private AMap aMap;
    private Circle circle;

    void initMap(MapView mapView) {
        aMap = mapView.getMap();
        if (aMap == null) {
            return;
        }
        aMap.moveCamera(CameraUpdateFactory.zoomTo(19));
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
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

    @Override
    public void drawCircle(double centerLa, double centerLo, int radius) {
        if (aMap == null) {
            return;
        }
        LatLng latLng = AMapUtil.wgsToCjg(AppContext.getAppContext(), centerLa, centerLo);
        aMap.addCircle(new CircleOptions().
                center(latLng).
                fillColor(Color.argb(50, 1, 1, 1)).
                radius(radius).
                strokeWidth(1));
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
    public void drawMarker(double centerLa, double centerLo, int radius) {

    }

    @Override
    public void clear() {
        aMap.clear();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        aMap.clear();
        int radius = 10;
        if (circle != null) {
            radius = (int) circle.getRadius();
        }
        circle = aMap.addCircle(new CircleOptions().
                center(latLng).
                fillColor(Color.argb(50, 1, 1, 1)).
                radius(radius).
                strokeWidth(1));
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
    }
}
