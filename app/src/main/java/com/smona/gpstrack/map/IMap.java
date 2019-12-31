package com.smona.gpstrack.map;

import com.smona.gpstrack.db.table.Location;
import com.smona.gpstrack.fence.bean.FenceBean;

import java.util.List;

public interface IMap {
    /**
     * 实时定位接口
     */
    //地图聚焦到坐标点
    void animateCamera(double defaultLa, double defaultLo);
    //绘制marker
    void drawMarker(double centerLa, double centerLo);
    //清除地图
    void clear();
    //获取当前位置坐标
    double[] getCurLocation();

    /**
     * 导航接口
     */
    //初始化导航
    void initSearch(int type, double targetLa, double targetLo);
    //重新绘制线路
    void refreshRoute();
    //仅刷新设备位置
    void refreshDevice(double deviceLa, double deviceLo);
    //移除导航
    void removeSearch();

    /**
     * 历史路线接口
     */
    //绘制路线
    void drawTrack(List<Location> points);

    /**
     * 电子围栏需要的接口
     */
    //监听地图点击
    void setOnMapClickListener();
    //初始化选点
    void onAutoMapClick(FenceBean fenceBean);
    //设置半径
    void setRadius(int radius);
    //获取经度纬度
    double getLatitude();
    double getLongitude();
}
