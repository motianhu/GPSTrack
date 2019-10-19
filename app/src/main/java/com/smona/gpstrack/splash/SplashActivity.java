package com.smona.gpstrack.splash;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BaseActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.param.ConfigParam;
import com.smona.gpstrack.common.param.ParamCenter;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.GsonUtil;
import com.smona.gpstrack.util.SPUtils;
import com.smona.logger.Logger;

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
            int isGuide = (Integer)SPUtils.get("user_guide", 0);
            if(isGuide == 0) {
                ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_GUIDE);
            }else {
                String loginInfo = (String) SPUtils.get("login_user", "");
                Logger.e("motianhu", "loginInfo=" + loginInfo);
                ConfigParam configParam = GsonUtil.jsonToObj(loginInfo, ConfigParam.class);
                if (configParam != null) {
                    ParamCenter.getInstance().setConfigParam(configParam);
                    ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_MAIN);
                } else {
                    ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_LOGIN);
                }
            }
            overridePendingTransition(0, 0);
            finish();
        }, (long) (3 * 1000));
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
