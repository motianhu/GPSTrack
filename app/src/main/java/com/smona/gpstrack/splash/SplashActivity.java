package com.smona.gpstrack.splash;

import android.Manifest;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BaseActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.param.AccountInfo;
import com.smona.gpstrack.common.param.AccountCenter;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.common.param.ConfigInfo;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.GsonUtil;
import com.smona.gpstrack.util.SPUtils;

import java.util.Locale;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@Route(path = ARouterPath.PATH_TO_SPLASH)
@RuntimePermissions
public class SplashActivity extends BaseActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initData();
    }

    private void gotoMain() {
        mHandler.postDelayed(() -> {
            int isGuide = (Integer) SPUtils.get(SPUtils.GUIDE_INFO, 0);
            if (isGuide == 0) {
                ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_GUIDE);
            } else {
                String loginInfo = (String) SPUtils.get(SPUtils.LOGIN_INFO, "");
                AccountInfo configParam = GsonUtil.jsonToObj(loginInfo, AccountInfo.class);
                String configStr = (String) SPUtils.get(SPUtils.CONFIG_INFO, "");
                ConfigInfo configInfo = GsonUtil.jsonToObj(configStr, ConfigInfo.class);
                if (configParam != null) {
                    AccountCenter.getInstance().setAccountInfo(configParam);
                    if(configInfo == null) {
                        ConfigCenter.getInstance().setConfigInfo(configParam);
                        setAppLanguage(configParam.getLocale());
                    } else {
                        ConfigCenter.getInstance().setConfigInfo(configInfo);
                        setAppLanguage(configInfo.getLocale());
                    }
                    ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_MAIN);
                } else {
                    ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_LOGIN);
                }
            }
            overridePendingTransition(0, 0);
            finish();
        }, (long) (3 * 1000));
    }

    private void setAppLanguage(String language) {
        Locale locale = Locale.SIMPLIFIED_CHINESE;
        if(ParamConstant.LOCALE_EN.equals(language)) {
            locale = Locale.ENGLISH;
        } else  if(ParamConstant.LOCALE_ZH_TW.equals(language)) {
            locale = Locale.TAIWAN;
        }
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, metrics);
    }

    protected void initData() {
        SplashActivityPermissionsDispatcher.requestPermission1WithPermissionCheck(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void requestPermission1() {
        SplashActivityPermissionsDispatcher.requestPermission2WithPermissionCheck(this);
    }

    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION})
    void requestPermission2() {
        SplashActivityPermissionsDispatcher.requestPermission3WithPermissionCheck(this);
    }

    @NeedsPermission({Manifest.permission.READ_PHONE_STATE})
    void requestPermission3() {
        gotoMain();
    }

    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onPermissionDenied1() {
        finish();
    }

    @OnPermissionDenied({Manifest.permission.ACCESS_COARSE_LOCATION})
    void onPermissionDenied2() {
        finish();
    }

    @OnPermissionDenied({Manifest.permission.READ_PHONE_STATE})
    void onPermissionDenied3() {
        finish();
    }
}
