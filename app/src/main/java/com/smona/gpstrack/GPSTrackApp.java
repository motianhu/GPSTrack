package com.smona.gpstrack;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import com.smona.base.http.HttpManager;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.param.AccountCenter;
import com.smona.gpstrack.common.param.AccountInfo;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.common.param.ConfigInfo;
import com.smona.gpstrack.db.DaoManager;
import com.smona.gpstrack.thread.WorkHandlerManager;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.AppContext;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.gpstrack.util.GsonUtil;
import com.smona.gpstrack.util.SPUtils;
import com.smona.http.config.LoadConfig;
import com.smona.http.wrapper.FilterChains;
import com.smona.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * description:
 *  APP进程启动
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
        //加载配置信息
        LoadConfig.loadConfig(this);
        CrashReport.initCrashReport(this, "c29e76f4be", false);
        Logger.init(this);
        //初始化页面路由
        ARouterManager.init(this, true);
        //初始化网络库
        HttpManager.init(this);
        //统一Http过滤器，拦截403错误
        FilterChains.getInstance().addAspectRouter(403, () -> {
            //403退出的清除上一次账号数据
            SPUtils.put(SPUtils.LOGIN_INFO, "");
            SPUtils.put(SPUtils.CONFIG_INFO, "");
            WorkHandlerManager.getInstance().runOnWorkerThread(CommonUtils::clearAllCache);

            CommonUtils.sendCloseAllActivity(this);
            ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_LOGIN);});
        //初始化数据库
        initDatabase();
        //异常crash时需要先加载本地缓存数据
        initLoginInfo();
        //注册监听页面变化
        initAppForgroundListener();
    }

    //持久化的信息读取到内存
    private void initLoginInfo() {
        String configStr = (String) SPUtils.get(SPUtils.CONFIG_INFO, "");
        ConfigInfo configInfo = GsonUtil.jsonToObj(configStr, ConfigInfo.class);
        if (configInfo != null) {
            ConfigCenter.getInstance().setConfigInfo(configInfo);
            setAppLanguage(configInfo.getLocale());
        }

        String loginInfo = (String) SPUtils.get(SPUtils.LOGIN_INFO, "");
        AccountInfo configParam = GsonUtil.jsonToObj(loginInfo, AccountInfo.class);
        if (configParam != null) {
            AccountCenter.getInstance().setAccountInfo(configParam);
            if (configInfo == null) {
                ConfigCenter.getInstance().setConfigInfo(configParam);
                setAppLanguage(configParam.getLocale());
            }
        }
    }

    //切换语言
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

    /**
     * 初始化数据库
     */
    private void initDatabase() {
        DaoManager daoManager = DaoManager.getInstance();
        daoManager.setDebug(true);
    }

    /**
     * 非主进程
     * @return
     */
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

    private void initAppForgroundListener() {
        ForegroundCallbacks.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(createBaseContext(base));
    }


    public Context createBaseContext(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 8.0需要使用createConfigurationContext处理
            return updateResources(context);
        } else {
            return context;
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Context updateResources(Context context) {
        Resources resources = context.getResources();
        String language = CommonUtils.getSysLanuage();
        Locale locale = Locale.ENGLISH;
        if(ParamConstant.LOCALE_ZH_CN.equals(language)) {
            locale = Locale.SIMPLIFIED_CHINESE;
        } else  if(ParamConstant.LOCALE_ZH_TW.equals(language)) {
            locale = Locale.TAIWAN;
        }
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateResources(this);
    }
}
