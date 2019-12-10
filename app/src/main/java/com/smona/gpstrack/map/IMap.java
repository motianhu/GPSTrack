package com.smona.gpstrack.map;

import com.smona.gpstrack.db.table.Location;
import com.smona.gpstrack.fence.bean.FenceBean;

import java.util.List;

public interface IMap {
    void animateCamera(double defaultLa, double defaultLo);
    void drawMarker(double centerLa, double centerLo);
    void clear();
    double[] getCurLocation();

    //navigation
    void initSearch(int type, double targetLa, double targetLo);
    void refreshRoute();//重新绘制线路
    void refreshDevice(double deviceLa, double deviceLo);//仅刷新设备位置
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
