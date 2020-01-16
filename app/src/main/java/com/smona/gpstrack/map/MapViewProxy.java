package com.smona.gpstrack.map;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.map.listener.OnMapReadyListener;

/**
 * 地图代理类。外部不需要知道里面用的是什么地图。
 */
public class MapViewProxy implements IMapView {

    private IMapView mapView;

    @Override
    public IMapView buildMap() {
        if(ParamConstant.MAP_GOOGLE.equals(ConfigCenter.getInstance().getConfigInfo().getMapDefault())) {
            mapView = new GoogleMapView();
        } else {
            mapView = new GaodeMapView();
        }
        return mapView;
    }

    @Override
    public View getMapView(Context context) {
        return mapView.getMapView(context);
    }

    @Override
    public IMap getMap() {
        return mapView.getMap();
    }

    @Override
    public void setMapReadyListener(OnMapReadyListener listener) {
        mapView.setMapReadyListener(listener);
    }

    @Override
    public void onCreate(Bundle bundle) {
        mapView.onCreate(bundle);
    }

    @Override
    public void onResume() {
        mapView.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
    }
}
