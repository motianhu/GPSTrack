package com.smona.gpstrack.map;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

public class MapGoogle implements  IMap, GoogleMap.OnMapClickListener {

    private GoogleMap googleMap;
    private Circle circle;

    public void initMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(17));
    }

    @Override
    public void animateCamera(double defaultLa, double defaultLo) {
        if (googleMap == null) {
            return;
        }
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(defaultLa, defaultLo)));
    }

    @Override
    public void setOnMapClickListener() {
        googleMap.setOnMapClickListener(this);
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
    public void onMapClick(double centerLa, double centerLo, int radius) {
        drawCircle(new LatLng(centerLa, centerLo), radius);
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
            return circle.getCenter().latitude;
        } else {
            return -1;
        }
    }

    @Override
    public double getLongitude() {
        if (circle != null) {
            return circle.getCenter().longitude;
        } else {
            return -1;
        }
    }

    @Override
    public void drawMarker(double centerLa, double centerLo, int radius) {

    }

    @Override
    public void clear() {
        googleMap.clear();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        clear();
        int radius = 10;
        if (circle != null) {
            radius = (int) circle.getRadius();
        }
        drawCircle(latLng, radius);
    }

    private void drawCircle(LatLng latLng, int radius) {
        circle = googleMap.addCircle(new CircleOptions().
                center(latLng).
                fillColor(Color.argb(50, 1, 1, 1)).
                radius(radius).
                strokeWidth(1));
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }
}
