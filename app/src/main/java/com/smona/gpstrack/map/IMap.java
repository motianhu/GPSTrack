package com.smona.gpstrack.map;

public interface IMap {
    void animateCamera(double defaultLa, double defaultLo);
    void drawMarker(double centerLa, double centerLo, int radius);
    void clear();

    ////电子围栏需要的接口///
    void setOnMapClickListener();
    void drawCircle(double centerLa, double centerLo, int radius);
    void onMapClick(double centerLa, double centerLo, int radius);
    void setRadius(int radius);
    double getLatitude();
    double getLongitude();
}
