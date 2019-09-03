package com.smona.gpstrack;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.smona.base.http.HttpManager;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.logger.Logger;

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
        if(!isMainProcess()) {
            return;
        }

        Logger.init(this);
        ARouterManager.init(this, true);
        HttpManager.init(this);
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
