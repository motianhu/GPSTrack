package com.smona.logger.common;

import android.os.Environment;

import com.smona.logger.BuildConfig;

public class LogConstants {
    //日志文件默认目录
    public static final String DEFAULT_LOG_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + BuildConfig.packageNameSuffix + "/log";
    public static final String DEFAULT_LOG_TAG = "Log";//默认全局tag
    public static long LOG_WRITE_TIME_INTERVAL = 1000 * 30;//日志写入文件间隔
    public static int LOG_WRITE_NUM_THRESHOLD = 10;//条数阈值  达到该阈值才会写出
    public static final int LOG_FILE_KEEP_DAYS = 3;//文件默认保存时间  天数
    public static final int LOG_FILE_SIZE_THRESHOLD = 1024 * 1024 * 30;//日志文件大小控制
    public static final int JSON_INDENT = 4;
    public static final String APP_COMMON = "com.smona.gpstrack";
    public static final String APP_PREFIX = "com.smona";

}
