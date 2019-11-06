package com.smona.gpstrack.map;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

public class MapGoogle implements  IMap {

    private GoogleMap googleMap;

    public void initMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    public void animateCamera(double defaultLa, double defaultLo) {
        if (googleMap == null) {
            return;
        }
    }

    @Override
    public void setOnMapClickListener() {

    }

    @Override
    public void drawCircle(double centerLa, double centerLo, int radius) {
        if (googleMap == null) {
            return;
        }
        LatLng latLng = new LatLng(centerLa, centerLo);
        googleMap.addCircle(new CircleOptions().
                center(latLng).
                fillColor(Color.argb(50, 1, 1, 1)).
                radius(radius).
                strokeWidth(1));
    }

    @Override
    public void setRadius(int radius) {
        
    }

    @Override
    public double getLatitude() {
        return 0;
    }

    @Override
    public double getLongitude() {
        return 0;
    }

    @Override
    public void drawMarker(double centerLa, double centerLo, int radius) {

    }

    @Override
    public void clear() {

    }
}
