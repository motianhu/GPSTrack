package com.smona.gpstrack;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.smona.gpstrack.notify.NotifyCenter;
import com.smona.gpstrack.notify.event.ForgroudEvent;
import com.smona.logger.Logger;

import java.util.List;

/**
 * 应用前后台切换监听
 */
public class ForegroundCallbacks implements Application.ActivityLifecycleCallbacks {

    private static final long CHECK_DELAY = 600;
    private static ForegroundCallbacks instance;
    private Handler handler = new Handler();
    private Runnable check;

    public static void init(Application application) {
        if (instance == null) {
            instance = new ForegroundCallbacks();
            application.registerActivityLifecycleCallbacks(instance);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (check != null) {
            handler.removeCallbacks(check);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (check != null) {
            handler.removeCallbacks(check);
            check = null;
        }

        handler.postDelayed(check = () -> {
            boolean isForground = getRunningAppProcesses(activity);
            Logger.e("motianhu", "isForground: " + isForground);
            if (isForground) {
                return;
            }
            NotifyCenter.getInstance().postEvent(new ForgroudEvent());
        }, CHECK_DELAY);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (!activity.getClass().getName().equalsIgnoreCase(MainActivity.class.getName())) {
            return;
        }
        if (check != null) {
            handler.removeCallbacks(check);
            check = null;
        }
    }

    //判断当前进程是否在前台
    private static boolean getRunningAppProcesses(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return false;
        }
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess != null && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

}
