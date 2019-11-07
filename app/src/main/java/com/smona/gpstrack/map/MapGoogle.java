package com.smona.gpstrack.map;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.smona.gpstrack.db.table.Location;
import com.smona.gpstrack.map.search.GoogleRouteSearch;

import java.util.List;

public class MapGoogle extends GoogleRouteSearch implements IMap, GoogleMap.OnMapClickListener {

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
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(defaultLa, defaultLo)));
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
    public void initSearch(int type, double targetLa, double targetLo) {

    }

    @Override
    public void drawTrack(List<Location> points) {
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLUE).width(20);
        if (points.size() > 0) {
            // 起点
            Location p = points.get(0);
            LatLng latLng = new LatLng(p.getLatitude(), p.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            googleMap.addMarker(markerOptions);
        }
        if (points.size() > 1) {
            // 终点
            Location p = points.get(points.size() - 1);
            LatLng latLng = new LatLng(p.getLatitude(), p.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            googleMap.addMarker(markerOptions);
        }
        for (Location p : points) {
            LatLng latLng = new LatLng(p.getLatitude(), p.getLongitude());
            polylineOptions.add(latLng);
            boundsBuilder.include(latLng);
        }
        googleMap.addPolyline(polylineOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 17));
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
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
}
