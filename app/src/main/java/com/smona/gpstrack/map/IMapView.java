package com.smona.gpstrack.map;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.smona.gpstrack.map.listener.OnMapReadyListener;

public interface IMapView {
    IMapView buildMap();
    View getMapView(Context context);
    IMap getMap();
    void setMapReadyListener(OnMapReadyListener listener);
    void onCreate(Bundle bundle);
    void onResume();
    void onPause();
    void onSaveInstanceState(Bundle outState);
    void onDestroy();
}
