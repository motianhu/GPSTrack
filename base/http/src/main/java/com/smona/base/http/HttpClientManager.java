package com.smona.base.http;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class HttpClientManager<T> implements IHttpClient<T> {

    private Context mContext;
    private static volatile HttpClientManager sInstance;
    private final Gson mGson= new GsonBuilder().disableHtmlEscaping().create();
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, HttpClient> mHttpClientMap = new HashMap<>();

    private HttpClientManager() {
    }

    public static HttpClientManager getInstance() {
        if (sInstance == null) {
            synchronized (HttpClientManager.class) {
                if (sInstance == null) {
                    sInstance = new HttpClientManager();
                }
            }
        }
        return sInstance;
    }

    public void init(Context context) {
        if (context == null) {
            return;
        }
        mContext = context.getApplicationContext();
    }

    /**
     * 获取缓存的Retrofit对象，如果没有缓存，则会返回null
     * 决定一个HttpClient 靠baseUrl和HttpConfig
     * @param baseUrl
     * @param config
     * @return
     */
    public HttpClient getCacheHttpClient(String baseUrl, HttpConfig config) {
        int key = getHttpClientKey(baseUrl, config);
        return mHttpClientMap.get(key);
    }

    /**
     * 缓存HttpClient
     *
     * @param baseUrl
     * @return
     */
    private void saveCacheHttpClient(HttpClient httpClient, String baseUrl, HttpConfig config) {
        if (mHttpClientMap.size() > HttpConstants.MAX_CACHE_SIZE) {
            mHttpClientMap.clear();
        }
        int key = getHttpClientKey(baseUrl, config);
        mHttpClientMap.put(key, httpClient);
    }

    private int getHttpClientKey(String baseUrl, HttpConfig config) {
        String url;
        String configToStr;
        if (TextUtils.isEmpty(baseUrl)) {
            url = "";
        }else {
            url = baseUrl;
        }
        if (config == null) {
            configToStr = "";
        }else {
            configToStr = config.createStrKey();
        }
        int key = (url + configToStr).hashCode();
        return key;
    }

    /**
     * 获取HttpClient，没有缓存则创建 然后缓存
     * @param baseUrl
     * @param httpConfig
     * @return
     */
    private HttpClient getHttpClientAndCache(String baseUrl, HttpConfig httpConfig) {
        HttpClient httpClient = getCacheHttpClient(baseUrl, httpConfig);
        if (httpClient == null) {
            httpClient = new HttpClient(mContext, baseUrl, httpConfig, mGson);
            saveCacheHttpClient(httpClient, baseUrl, httpConfig);
        }else {
            httpClient.updateConfit(httpConfig);
        }
        return httpClient;
    }


    /**
     * Post
     */
    @Override
    public int post(String baseUrl, String path, int httpKey, int tagHash, int retryTimes,
                    int retryDelayMillis, boolean onUiCallBack, HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.post(path, httpKey, tagHash, retryTimes, retryDelayMillis, onUiCallBack,
                    callback);

    }

    @Override
    public int postWithParamsMap(String baseUrl, String path, int httpKey, Map<String, String> params,
                                 int tagHash, int retryTimes, int retryDelayMillis,
                                 boolean onUiCallBack, HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.postWithParamsMap(path, httpKey, params, tagHash,
                    retryTimes, retryDelayMillis, onUiCallBack, callback);

    }

    @Override
    public int post(String baseUrl, String path, int httpKey, Object bodyJson, int tagHash,
                    int retryTimes, int retryDelayMillis, boolean onUiCallBack,
                    HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.post(path, httpKey, bodyJson, tagHash,
                    retryTimes, retryDelayMillis, onUiCallBack, callback);

    }

    @Override
    public int postWithHeaderMap(String baseUrl, String path, int httpKey, Map mapHeader,
                                 int tagHash, int retryTimes, int retryDelayMillis,
                                 boolean onUiCallBack, HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.postWithHeaderMap(path, httpKey, mapHeader, tagHash,
                    retryTimes, retryDelayMillis, onUiCallBack, callback);
    }

    @Override
    public int postParamsAndObj(String baseUrl, String path, int httpKey, Map<String, String> params,
                                Object bodyJson, int tagHash, int retryTimes, int retryDelayMillis,
                                boolean onUiCallBack, HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.postParamsAndObj(path, httpKey, params, bodyJson, tagHash,
                    retryTimes, retryDelayMillis, onUiCallBack, callback);
    }

    @Override
    public int post(String baseUrl, String path, int httpKey, Map<String, String> params,
                    Map<String, String> mapHeader, int tagHash, int retryTimes,
                    int retryDelayMillis, boolean onUiCallBack, HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.post(path, httpKey, params, mapHeader, tagHash,
                    retryTimes, retryDelayMillis, onUiCallBack, callback);
    }

    @Override
    public int postMapHeaderAndObj(String baseUrl, String path, int httpKey,
                                   Map<String, String> mapHeader, Object bodyJson, int tagHash,
                                   int retryTimes, int retryDelayMillis, boolean onUiCallBack,
                                   HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.postMapHeaderAndObj(path, httpKey, mapHeader, bodyJson, tagHash,
                    retryTimes, retryDelayMillis, onUiCallBack, callback);
    }

    @Override
    public int post(String baseUrl, String path, int httpKey, Map<String, String> params,
                    Map<String, String> mapHeader, Object bodyJson, int tagHash,
                    int retryTimes, int retryDelayMillis, boolean onUiCallBack, HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.post(path, httpKey, params, mapHeader, bodyJson, tagHash,
                    retryTimes, retryDelayMillis, onUiCallBack, callback);
    }

    /**
     * Get
     */
    @Override
    public int get(String baseUrl, String path, int httpKey, int tagHash, int retryTimes,
                   int retryDelayMillis, boolean onUiCallBack, HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.get(path, httpKey, tagHash, retryTimes, retryDelayMillis,
                    onUiCallBack, callback);
    }

    @Override
    public int getWithParamsMap(String baseUrl, String path, int httpKey, Map<String, String> params,
                                int tagHash, int retryTimes, int retryDelayMillis,
                                boolean onUiCallBack, HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.getWithParamsMap(path, httpKey, params, tagHash, retryTimes, retryDelayMillis,
                    onUiCallBack, callback);
    }

    @Override
    public int getWithHeaderMap(String baseUrl, String path, int httpKey, Map<String, String> mapHeader,
                                int tagHash, int retryTimes, int retryDelayMillis, boolean onUiCallBack,
                                HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.getWithHeaderMap(path, httpKey, mapHeader, tagHash, retryTimes, retryDelayMillis,
                    onUiCallBack, callback);
    }

    @Override
    public int get(String baseUrl, String path, int httpKey, Map<String, String> params,
                   Map<String, String> authHeader, int tagHash, int retryTimes, int retryDelayMillis,
                   boolean onUiCallBack, HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.get(path, httpKey, params, authHeader, tagHash,
                    retryTimes, retryDelayMillis, onUiCallBack, callback);
    }

    /**
     * Put
     */
    @Override
    public int put(String baseUrl, String path, int httpKey, int tagHash, int retryTimes,
                    int retryDelayMillis, boolean onUiCallBack, HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.put(path, httpKey, tagHash, retryTimes, retryDelayMillis, onUiCallBack,
                callback);

    }

    @Override
    public int putWithParamsMap(String baseUrl, String path, int httpKey, Map<String, String> params,
                                 int tagHash, int retryTimes, int retryDelayMillis,
                                 boolean onUiCallBack, HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.putWithParamsMap(path, httpKey, params, tagHash,
                retryTimes, retryDelayMillis, onUiCallBack, callback);

    }

    @Override
    public int put(String baseUrl, String path, int httpKey, Object bodyJson, int tagHash,
                    int retryTimes, int retryDelayMillis, boolean onUiCallBack,
                    HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.put(path, httpKey, bodyJson, tagHash,
                retryTimes, retryDelayMillis, onUiCallBack, callback);

    }

    @Override
    public int putWithHeaderMap(String baseUrl, String path, int httpKey, Map mapHeader,
                                 int tagHash, int retryTimes, int retryDelayMillis,
                                 boolean onUiCallBack, HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.putWithHeaderMap(path, httpKey, mapHeader, tagHash,
                retryTimes, retryDelayMillis, onUiCallBack, callback);
    }

    @Override
    public int putParamsAndObj(String baseUrl, String path, int httpKey, Map<String, String> params,
                                Object bodyJson, int tagHash, int retryTimes, int retryDelayMillis,
                                boolean onUiCallBack, HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.putParamsAndObj(path, httpKey, params, bodyJson, tagHash,
                retryTimes, retryDelayMillis, onUiCallBack, callback);
    }

    @Override
    public int put(String baseUrl, String path, int httpKey, Map<String, String> params,
                    Map<String, String> mapHeader, int tagHash, int retryTimes,
                    int retryDelayMillis, boolean onUiCallBack, HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.put(path, httpKey, params, mapHeader, tagHash,
                retryTimes, retryDelayMillis, onUiCallBack, callback);
    }

    @Override
    public int putMapHeaderAndObj(String baseUrl, String path, int httpKey,
                                   Map<String, String> mapHeader, Object bodyJson, int tagHash,
                                   int retryTimes, int retryDelayMillis, boolean onUiCallBack,
                                   HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.putMapHeaderAndObj(path, httpKey, mapHeader, bodyJson, tagHash,
                retryTimes, retryDelayMillis, onUiCallBack, callback);
    }

    @Override
    public int put(String baseUrl, String path, int httpKey, Map<String, String> params,
                    Map<String, String> mapHeader, Object bodyJson, int tagHash,
                    int retryTimes, int retryDelayMillis, boolean onUiCallBack, HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.put(path, httpKey, params, mapHeader, bodyJson, tagHash,
                retryTimes, retryDelayMillis, onUiCallBack, callback);
    }


    /**
     * Delete
     */
    @Override
    public int delete(String baseUrl, String path, int httpKey, int tagHash, int retryTimes,
                   int retryDelayMillis, boolean onUiCallBack, HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.delete(path, httpKey, tagHash, retryTimes, retryDelayMillis, onUiCallBack,
                callback);

    }

    @Override
    public int deleteWithParamsMap(String baseUrl, String path, int httpKey, Map<String, String> params,
                                int tagHash, int retryTimes, int retryDelayMillis,
                                boolean onUiCallBack, HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.deleteWithParamsMap(path, httpKey, params, tagHash,
                retryTimes, retryDelayMillis, onUiCallBack, callback);

    }

    @Override
    public int delete(String baseUrl, String path, int httpKey, Object bodyJson, int tagHash,
                   int retryTimes, int retryDelayMillis, boolean onUiCallBack,
                   HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.delete(path, httpKey, bodyJson, tagHash,
                retryTimes, retryDelayMillis, onUiCallBack, callback);

    }

    @Override
    public int deleteWithHeaderMap(String baseUrl, String path, int httpKey, Map mapHeader,
                                int tagHash, int retryTimes, int retryDelayMillis,
                                boolean onUiCallBack, HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.deleteWithHeaderMap(path, httpKey, mapHeader, tagHash,
                retryTimes, retryDelayMillis, onUiCallBack, callback);
    }

    @Override
    public int deleteParamsAndObj(String baseUrl, String path, int httpKey, Map<String, String> params,
                               Object bodyJson, int tagHash, int retryTimes, int retryDelayMillis,
                               boolean onUiCallBack, HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.deleteParamsAndObj(path, httpKey, params, bodyJson, tagHash,
                retryTimes, retryDelayMillis, onUiCallBack, callback);
    }

    @Override
    public int delete(String baseUrl, String path, int httpKey, Map<String, String> params,
                   Map<String, String> mapHeader, int tagHash, int retryTimes,
                   int retryDelayMillis, boolean onUiCallBack, HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.delete(path, httpKey, params, mapHeader, tagHash,
                retryTimes, retryDelayMillis, onUiCallBack, callback);
    }

    @Override
    public int deleteMapHeaderAndObj(String baseUrl, String path, int httpKey,
                                  Map<String, String> mapHeader, Object bodyJson, int tagHash,
                                  int retryTimes, int retryDelayMillis, boolean onUiCallBack,
                                  HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.deleteMapHeaderAndObj(path, httpKey, mapHeader, bodyJson, tagHash,
                retryTimes, retryDelayMillis, onUiCallBack, callback);
    }

    @Override
    public int delete(String baseUrl, String path, int httpKey, Map<String, String> params,
                   Map<String, String> mapHeader, Object bodyJson, int tagHash,
                   int retryTimes, int retryDelayMillis, boolean onUiCallBack, HttpConfig httpConfig, HttpCallBack<T> callback) {
        HttpClient httpClient = getHttpClientAndCache(baseUrl, httpConfig);
        return httpClient.delete(path, httpKey, params, mapHeader, bodyJson, tagHash,
                retryTimes, retryDelayMillis, onUiCallBack, callback);
    }
}
