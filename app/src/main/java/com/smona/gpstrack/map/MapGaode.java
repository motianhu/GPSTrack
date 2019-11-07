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
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.db.table.Location;
import com.smona.gpstrack.map.search.AMapRouteSearch;
import com.smona.gpstrack.util.AMapUtil;
import com.smona.gpstrack.util.AppContext;

import java.util.List;


/**
 * 入参坐标都是WGS，使用时都需要转成CJS；出参需要转换成WGS
 */
public class MapGaode extends AMapRouteSearch implements IMap, AMap.OnMapClickListener  {

    private Circle circle;

    void initMap(MapView mapView) {
        aMap = mapView.getMap();
        if (aMap == null) {
            return;
        }
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        animateCamera(ParamConstant.DEFAULT_POS_LA, ParamConstant.DEFAULT_POS_LO);
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
    public void onMapClick(double centerLa, double centerLo, int radius) {
        LatLng latLng = AMapUtil.wgsToCjg(AppContext.getAppContext(), centerLa, centerLo);
        drawCircle(latLng, radius);
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
    public void initSearch(int type, double targetLa, double targetLo) {
        super.initSearch(type, targetLa, targetLo);
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
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            aMap.addMarker(markerOptions);
        }
        if (points.size() > 1) {
            // 终点
            Location p = points.get(points.size() - 1);
            LatLng latLng = AMapUtil.wgsToCjg(AppContext.getAppContext(), p.getLatitude(), p.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
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

    @Override
    public void onMapClick(LatLng latLng) {
        aMap.clear();
        int radius = 10;
        if (circle != null) {
            radius = (int) circle.getRadius();
        }
        drawCircle(latLng, radius);
    }

    private void drawCircle(LatLng latLng, int radius) {
        circle = aMap.addCircle(new CircleOptions().
                center(latLng).
                fillColor(Color.argb(50, 1, 1, 1)).
                radius(radius).
                strokeWidth(1));
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
    }
}
