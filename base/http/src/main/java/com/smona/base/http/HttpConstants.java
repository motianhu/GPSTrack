package com.smona.base.http;

import okhttp3.MediaType;

public class HttpConstants {

    //JSON类型
    public static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
    //表单类型
    public static final MediaType FORM_TYPE = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");

    public static long SIZE_OF_CACHE = 50 * 1024 * 1024; // 50 MiB

    public static final String CACHE_CONTROL = "Cache-Control";

    public final static int MAX_CACHE_SIZE = 100;

    public static final String LOG_TAG = "HttpLog";
}
