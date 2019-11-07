package com.smona.gpstrack.map;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.smona.gpstrack.R;
import com.smona.gpstrack.map.listener.OnMapReadyListener;

public class GaodeMapView implements IMapView {

    private MapView mapView;
    private MapGaode mapGaode;

    public static void initMap(AMap aMap, LatLng latLng) {

    }

    @Override
    public IMapView buildMap() {
        return this;
    }

    @Override
    public View getMapView(Context context) {
        mapView = (MapView) View.inflate(context, R.layout.amap_view, null);
        mapGaode = new MapGaode();
        mapGaode.initMap(mapView);
        return mapView;
    }

    @Override
    public IMap getMap() {
        return mapGaode;
    }

    @Override
    public void setMapReadyListener(OnMapReadyListener listener) {

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
