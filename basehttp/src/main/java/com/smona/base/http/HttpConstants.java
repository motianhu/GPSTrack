package com.smona.base.http;

import okhttp3.MediaType;

public class HttpConstants {

    //JSON类型
    public static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
    //文本类型
    public static final MediaType TEXT_TYPE = MediaType.parse("text/plain");
    //表单类型
    public static final MediaType FORM_TYPE = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");

    //文件上传默认文件名格式
    public static final String FILE_NAME = "file\"; filename=\"";
    //文本上传默认文本信息格式
    public static final String FILE_DES = "file\"; filedes=\"";

    public static long SIZE_OF_CACHE = 50 * 1024 * 1024; // 50 MiB

    public static final String CACHE_CONTROL = "Cache-Control";

    public static final String HTTPS = "https";

    //默认下载相关
    public final static String DOWNLOAD_DIR = "/downlaod/";

    public final static int MAX_CACHE_SIZE = 100;

    public static final String LOG_TAG = "HttpLog";
}
