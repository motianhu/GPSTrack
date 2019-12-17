package com.smona.http.config;

import android.content.Context;
import com.smona.http.wrapper.R;

public class LoadConfig {

    public static AppConfig appConfig = new AppConfig();

    public static void loadConfig(Context context) {
        appConfig.setApiUrl(context.getString(R.string.appUrl));
        appConfig.setApiKey(context.getString(R.string.apiKey));
        appConfig.setDebug(context.getResources().getBoolean(R.bool.debug));
        appConfig.setRoute(context.getResources().getBoolean(R.bool.route));
    }
}
