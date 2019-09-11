package com.smona.gpstrack.util;

import android.app.Application;
import android.content.Context;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 7/24/19 8:26 AM
 */
public class AppContext {

    private static Context mAppContext;

    public static Context getAppContext() {
        return mAppContext;
    }

    public static void setAppContext(Context context) {
        if (!(context instanceof Application)) {
            throw new RuntimeException("Context is null or not  application");
        }
        mAppContext = context;
    }
}
