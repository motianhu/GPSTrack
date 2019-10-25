package com.smona.gpstrack;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.smona.base.http.HttpManager;
import com.smona.gpstrack.common.param.AccountCenter;
import com.smona.gpstrack.common.param.AccountInfo;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.db.DaoManager;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.AppContext;
import com.smona.gpstrack.util.GsonUtil;
import com.smona.gpstrack.util.SPUtils;
import com.smona.http.config.LoadConfig;
import com.smona.http.wrapper.FilterChains;
import com.smona.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 8/30/19 11:53 AM
 */
public class GPSTrackApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (!isMainProcess()) {
            return;
        }

        AppContext.setAppContext(this);
        LoadConfig.loadConfig();
        CrashReport.initCrashReport(this, "c29e76f4be", false);
        Logger.init(this);
        ARouterManager.init(this, true);
        HttpManager.init(this);
        FilterChains.getInstance().addAspectRouter(403, () -> ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_LOGIN));
        initDatabase();
        //异常crash时需要先加载本地缓存数据
        initLoginInfo();
    }

    private void initLoginInfo() {
        String loginInfo = (String) SPUtils.get(SPUtils.LOGIN_INFO, "");
        AccountInfo configParam = GsonUtil.jsonToObj(loginInfo, AccountInfo.class);
        if (configParam != null) {
            AccountCenter.getInstance().setAccountInfo(configParam);
            ConfigCenter.getInstance().setConfigInfo(configParam);
        }
    }

    /**
     * 初始化数据库
     */
    private void initDatabase() {
        DaoManager daoManager = DaoManager.getInstance();
        daoManager.setDebug(true);
    }

    protected boolean isMainProcess() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
