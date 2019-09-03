package com.smona.base.http.external;

public class HttpConfig {

    public final static int DEFAULT_CONNECT_TIME_OUT = 10;
    public final static int DEFAULT_READ_TIME_OUT = 10;
    public final static int DEFAULT_WRITE_TIME_OUT = 10;
    private final static int NET_WORK_CACHE_TIMEOUT = 10;//有网缓存，默认5秒
    private final static int NO_NET_WORK_CACHE_TIMEOUT = 60 * 60 * 24; //离线缓存，默认一天

    private int mConnectTimeout = DEFAULT_CONNECT_TIME_OUT;
    private int mReadTimeout = DEFAULT_READ_TIME_OUT;
    private int mWriteTimeout = DEFAULT_WRITE_TIME_OUT;
    private int mNetWorkCacheTimeout = NET_WORK_CACHE_TIMEOUT;
    private int mNoNetWorkCacheTimeout = NO_NET_WORK_CACHE_TIMEOUT;

    private boolean mConnectedCache = true; //是否需要有网络时的缓存
    private boolean mNotConnectedCache = true; // 是否需要离线缓存

    public void setConnectTimeout(int time){
        mConnectTimeout = time;
    }

    public int getConnectTimeout(){
        return mConnectTimeout;
    }

    public void setReadTimeout(int time){
        mReadTimeout = time;
    }

    public int getReadTimeout(){
        return mReadTimeout;
    }

    public void setWriteTimeout(int time){
        mWriteTimeout = time;
    }

    public int getWriteTimeout(){
        return mWriteTimeout;
    }

    public void setNetWorkCacheTimeout(int time){
        mNetWorkCacheTimeout = time;
    }

    public int getNetworkCacheTimeout(){
        return mNetWorkCacheTimeout;
    }

    public void setNoNetWorkCacheTimeout(int time){
        mNoNetWorkCacheTimeout = time;
    }

    public int getNoNetWorkCacheTimeout(){
        return mNoNetWorkCacheTimeout;
    }

    private static final int DEFAULT_RETRY_COUNT = 1;

    private static final int DEFAULT_RETRY_DELAY_MILLIS = 10 * 1000;

    private int mRetryCount = DEFAULT_RETRY_COUNT;
    private int mRetryDelayMillis = DEFAULT_RETRY_DELAY_MILLIS;

    public void setRetryCount(){

    }

    public void setRetryDelayMillis(){

    }

    public int getRetryCount(){
        return mRetryCount;
    }

    public int getRetryDelayMillis(){
        return mRetryDelayMillis;
    }

}
