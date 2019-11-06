package com.smona.gpstrack.map;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.smona.gpstrack.R;

public class GaodeMapView implements IMapView {

    private MapView mapView;
    private MapGaode mapGaode;

    public static void initMap(AMap aMap, LatLng latLng) {


    }

    public static Circle drawFence(AMap aMap, LatLng latLng, int radius) {
        return aMap.addCircle(new CircleOptions().
                center(latLng).
                fillColor(Color.argb(50, 1, 1, 1)).
                radius(radius).
                strokeWidth(1));
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
