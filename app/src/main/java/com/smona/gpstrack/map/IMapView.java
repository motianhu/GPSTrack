package com.smona.gpstrack.map;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.smona.gpstrack.map.listener.OnMapReadyListener;

/**
 * 地图View的封装接口
 */
public interface IMapView {
    //构建地图View
    IMapView buildMap();
    //获取地图View
    View getMapView(Context context);
    //获取地图实际操作类
    IMap getMap();
    //设置地图Ready的回调
    void setMapReadyListener(OnMapReadyListener listener);
    /**
     * 地图固定的五个方法
     */
    void onCreate(Bundle bundle);
    void onResume();
    void onPause();
    void onSaveInstanceState(Bundle outState);
    void onDestroy();
}
