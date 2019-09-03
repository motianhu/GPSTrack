package com.smona.gpstrack;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.smona.base.ui.activity.BaseActivity;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;

import java.util.Locale;

@Route(path = ARouterPath.PATH_TO_MAIN)
public class MainActivity extends BaseActivity {

    private MapView mMapView;
    private AMap aMap;
    private boolean isEn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
            aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
            MyLocationStyle myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
            myLocationStyle.interval(2000);
            aMap.setMyLocationStyle(myLocationStyle);
            aMap.getUiSettings().setMyLocationButtonEnabled(true);
            aMap.setMyLocationEnabled(true);
        }

        findViewById(R.id.switchLanguage).setOnClickListener(view -> {
            if (isEn) {
                aMap.setMapLanguage(AMap.CHINESE);
                isEn = !isEn;
                switchChinaLanguage();
            } else {
                aMap.setMapLanguage(AMap.ENGLISH);
                isEn = !isEn;
                switchENLanguage();
            }

        });
        findViewById(R.id.openScan).setOnClickListener(view -> ARouterManager.getInstance().startARActivity(ARouterPath.PATH_TO_SCAN));
    }

    private void switchChinaLanguage() {
        switchLanguage(Locale.SIMPLIFIED_CHINESE);
    }

    private void switchENLanguage() {
        switchLanguage(Locale.ENGLISH);
    }

    private void switchLanguage(Locale locale) {
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, metrics);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
}
