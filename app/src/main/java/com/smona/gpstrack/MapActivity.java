package com.smona.gpstrack;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.smona.base.ui.activity.BaseActivity;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.Constant;
import com.smona.gpstrack.util.SPUtils;
import com.smona.logger.Logger;

import java.util.Locale;

@Route(path = ARouterPath.PATH_TO_MAP)
public class MapActivity extends BaseActivity implements AMap.OnMyLocationChangeListener {

    private MapView mMapView;
    private AMap aMap;
    private boolean isEn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Logger.e("onCreate");

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
            aMap.animateCamera(CameraUpdateFactory.changeLatLng(ParamConstant.DEFAULT_POS));
            aMap.setMyLocationEnabled(true);
            String language = (String) SPUtils.get(Constant.SP_KEY_LANGUAGE, Constant.VALUE_LANGUAGE_ZH_CN);
            if (Constant.VALUE_LANGUAGE_EN.equals(language)) {
                aMap.setMapLanguage(AMap.ENGLISH);
            } else {
                aMap.setMapLanguage(AMap.CHINESE);
            }
        }

        findViewById(R.id.switchLanguage).setOnClickListener(view -> {
            String language = (String) SPUtils.get(Constant.SP_KEY_LANGUAGE, Constant.VALUE_LANGUAGE_EN);
            if (Constant.VALUE_LANGUAGE_EN.equals(language)) {
                SPUtils.put(Constant.SP_KEY_LANGUAGE, Constant.VALUE_LANGUAGE_ZH_CN);
                switchChinaLanguage();
            } else {
                SPUtils.put(Constant.SP_KEY_LANGUAGE, Constant.VALUE_LANGUAGE_EN);
                switchENLanguage();
            }

        });
        findViewById(R.id.openScan).setOnClickListener(view -> ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_SCAN));
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

        sendCloseAllActivity();
        ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_SPLASH);
    }

    private void sendCloseAllActivity() {
        Intent closeAllIntent = new Intent(ACTION_BASE_ACTIVITY);
        closeAllIntent.putExtra(ACTION_BASE_ACTIVITY_EXIT_KEY, ACTION_BASE_ACTIVITY_EXIT_VALUE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(closeAllIntent);
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

    @Override
    public void onMyLocationChange(Location location) {
        //location
    }
}
