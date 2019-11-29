package com.smona.gpstrack.map;

import android.app.Activity;

import com.smona.gpstrack.db.table.Location;
import com.smona.gpstrack.fence.bean.FenceBean;

import java.util.List;

public interface IMap {
    void animateCamera(double defaultLa, double defaultLo);
    void drawMarker(double centerLa, double centerLo);
    void clear();

    //navigate
    void initSearch(Activity activity, int type, double targetLa, double targetLo);
    void refreshSearch();
    void removeSearch();

    //history
    void drawTrack(List<Location> points);

    ////电子围栏需要的接口///
    void setOnMapClickListener();
    void onAutoMapClick(FenceBean fenceBean);//初始化选点
    void setRadius(int radius);
    double getLatitude();
    double getLongitude();
}
