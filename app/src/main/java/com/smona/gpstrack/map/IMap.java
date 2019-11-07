package com.smona.gpstrack.map;

import com.smona.gpstrack.db.table.Location;

import java.util.List;

public interface IMap {
    void animateCamera(double defaultLa, double defaultLo);
    void drawMarker(double centerLa, double centerLo, int radius);
    void clear();

    //history
    void drawTrack(List<Location> points);

    ////电子围栏需要的接口///
    void setOnMapClickListener();
    void drawCircle(double centerLa, double centerLo, int radius);
    void onMapClick(double centerLa, double centerLo, int radius);
    void setRadius(int radius);
    double getLatitude();
    double getLongitude();
}
