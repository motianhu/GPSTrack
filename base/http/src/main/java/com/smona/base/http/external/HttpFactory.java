package com.smona.base.http.external;

import android.content.Context;
import android.util.Log;

import com.smona.base.http.internal.HttpClient;

public class HttpFactory {
    private static final String TAG = "HttpFactory";

    private static HttpConfig mConfig;
    private static Context mContext;

    static {
        mConfig = new HttpConfig();
    }

    public static void setHttpConfig(HttpConfig config) {
        if (config == null) {
            return;
        }
        mConfig = config;
    }

    public static void init(Context context) {
        mContext = context;
    }

    public void init(Context context, HttpConfig config) {
        if (config != null) {
            mConfig = config;
        }
    }

    public static HttpClient getClient(String baseUrl) {
        HttpClient result = null;
        try {
            result = new HttpClient(mContext, baseUrl, mConfig);
        } catch (Exception exp) {
            Log.e(TAG, exp.getStackTrace().toString());
        }

        return result;
    }
}
