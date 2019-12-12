package com.smona.gpstrack.map;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.smona.gpstrack.R;
import com.smona.gpstrack.db.table.Fence;
import com.smona.gpstrack.db.table.Location;
import com.smona.gpstrack.fence.bean.FenceBean;
import com.smona.gpstrack.map.search.GoogleRouteSearch;
import com.smona.gpstrack.util.CommonUtils;

import java.util.List;

public class MapGoogle extends GoogleRouteSearch implements GoogleMap.OnMapClickListener {

    private Circle circle;
    private Marker pathMarker;

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
    public void onAutoMapClick(FenceBean fenceBean) {
        boolean isLocation = CommonUtils.isInValidLatln(fenceBean.getLatitude(), fenceBean.getLongitude());
        if (isLocation) {
            fenceBean.setLatitude(GoogleLocationManager.getInstance().getLocation()[0]);
            fenceBean.setLongitude(GoogleLocationManager.getInstance().getLocation()[1]);
        }
        LatLng latLng = new LatLng(fenceBean.getLatitude(), fenceBean.getLongitude());
        circle = googleMap.addCircle(new CircleOptions().
                center(latLng).
                fillColor(Fence.getFenceColor(fenceBean.getStatus())).
                radius(fenceBean.getRadius()).
                strokeWidth(1));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
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
    public void drawMarker(double centerLa, double centerLo) {
        LatLng latLng = new LatLng(centerLa, centerLo);
        if(pathMarker == null) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination));
            pathMarker = googleMap.addMarker(markerOptions);
        } else {
            pathMarker.setPosition(latLng);
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void clear() {
        googleMap.clear();
        pathMarker = null;
    }

    @Override
    public double[] getCurLocation() {
        return GoogleLocationManager.getInstance().getLocation();
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
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.amap_start));
            googleMap.addMarker(markerOptions);
        }
        if (points.size() > 1) {
            // 终点
            Location p = points.get(points.size() - 1);
            LatLng latLng = new LatLng(p.getLatitude(), p.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.amap_end));
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

    ////地图上选点
    @Override
    public void onMapClick(LatLng latLng) {
        if (circle != null) {
            circle.setCenter(latLng);
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
}
