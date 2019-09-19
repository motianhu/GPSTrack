package com.smona.base.http;

import android.content.Context;

public class HttpManager {

    public static void init(Context context) {
        //初始化普通Http请求模块
        HttpClientManager.getInstance().init(context);
    }
}
