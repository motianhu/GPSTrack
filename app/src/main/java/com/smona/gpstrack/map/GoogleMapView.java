package com.smona.gpstrack.map;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.smona.gpstrack.R;
import com.smona.gpstrack.map.listener.OnMapReadyListener;

public class GoogleMapView implements IMapView, OnMapReadyCallback {

    private MapView mapView;
    private MapGoogle mapGoogle;
    private OnMapReadyListener onMapReadyListener;

    @Override
    public IMapView buildMap() {
        return this;
    }

    @Override
    public View getMapView(Context context) {
        mapView = (MapView) View.inflate(context, R.layout.google_view, null);
        mapView.getMapAsync(this);
        return mapView;
    }

    @Override
    public IMap getMap() {
        return mapGoogle;
    }

    @Override
    public void setMapReadyListener(OnMapReadyListener listener) {
        this.onMapReadyListener = listener;
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapGoogle = new MapGoogle();
        mapGoogle.initMap(googleMap);
        if(onMapReadyListener != null) {
            onMapReadyListener.onMapReady();
        }
    }
}
