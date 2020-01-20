package com.smona.gpstrack.splash;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BaseActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.BaseLanuageActivity;
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

/**
 * 启动页
 */
@Route(path = ARouterPath.PATH_TO_SPLASH)
@RuntimePermissions
public class SplashActivity extends BaseLanuageActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initData();
    }

    private void gotoMain() {
        mHandler.postDelayed(() -> {
            //是否启动过引导页
            int isGuide = (Integer) SPUtils.get(SPUtils.GUIDE_INFO, 0);
            if (isGuide == 0) {
                ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_GUIDE);
            } else {
                //读取持久化登录信息
                String loginInfo = (String) SPUtils.get(SPUtils.LOGIN_INFO, "");
                AccountInfo configParam = GsonUtil.jsonToObj(loginInfo, AccountInfo.class);
                //读取持久化配置信息
                String configStr = (String) SPUtils.get(SPUtils.CONFIG_INFO, "");
                ConfigInfo configInfo = GsonUtil.jsonToObj(configStr, ConfigInfo.class);
                //进行初始化
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
        Locale locale = Locale.ENGLISH;
        if(ParamConstant.LOCALE_ZH_CN.equals(language)) {
            locale = Locale.SIMPLIFIED_CHINESE;
        } else  if(ParamConstant.LOCALE_ZH_TW.equals(language)) {
            locale = Locale.TAIWAN;
        }
        setAppLanguage(locale);
    }

    protected void initData() {
        //申请权限
        SplashActivityPermissionsDispatcher.requestPermission1WithPermissionCheck(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    //读写存储器权限
    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void requestPermission1() {
        SplashActivityPermissionsDispatcher.requestPermission2WithPermissionCheck(this);
    }

    //访问定位权限
    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION})
    void requestPermission2() {
        gotoMain();
    }

    //拒绝存储器权限
    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onPermissionDenied1() {
        finish();
    }

    //拒绝定位权限
    @OnPermissionDenied({Manifest.permission.ACCESS_COARSE_LOCATION})
    void onPermissionDenied2() {
        finish();
    }
}
