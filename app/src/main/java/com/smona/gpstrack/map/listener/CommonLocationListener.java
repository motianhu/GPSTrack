package com.smona.gpstrack.map.listener;

public interface CommonLocationListener {
    int CLICK_LOCATION = 0;
    int AUTO_LOCATION = 1;
    void onLocation(int type, double la,double lo);
}
