package com.smona.base.http.builder;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.smona.base.http.HttpCallBack;
import com.smona.base.http.HttpClient;
import com.smona.base.http.HttpClientManager;
import com.smona.base.http.HttpConfig;
import com.smona.base.http.HttpConstants;
import com.smona.base.http.HttpMethod;
import com.smona.base.http.utils.HttpUtils;
import com.smona.base.http.utils.MD5;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.RequestBody;


public abstract class CommonBuilder<T> {

    private String TAG = "CommonBuilder";

    protected Map<String, String> mHttpParams;
    protected Map<String, String> mHttpHeader;


    protected abstract String getPath();

    protected abstract String getBaseUrl();

    protected abstract @HttpMethod.IMethed
    String getMethod();

    private Object mBodyObj;
    protected HttpConfig mHttpCustomConfig;
    protected int mRetryTimes;
    protected int mRetryDelayMillis;
    protected int mKey = -1;
    protected Object mTag;

    protected int mAppId = -1;
    protected String mAppKey = "";

    /**
     * 设置Rxjava重试次数和延迟时间,默认失败不重试
     *
     * @param retryTimes
     * @param retryDelayMillis 毫秒
     * @return
     */
    public CommonBuilder<T> setRetryTimes(int retryTimes, int retryDelayMillis) {
        mRetryTimes = retryTimes;
        mRetryDelayMillis = retryDelayMillis;
        return this;
    }


    /**
     * 批量添加http查询参数
     */
    public CommonBuilder<T> addParamsMap(Map<String, String> params) {
        if (mHttpParams == null) {
            mHttpParams = new HashMap<>();
        }
        if (params != null) {
            mHttpParams.putAll(params);
        }
        return this;
    }

    /**
     * 逐个添加http查询参数
     */
    public CommonBuilder<T> addParam(String key, String value) {
        if (mHttpParams == null) {
            mHttpParams = new HashMap<>();
        }
        if (!TextUtils.isEmpty(key)) {
            mHttpParams.put(key, value);
        }
        return this;
    }

    /**
     * 覆盖http查询参数，注意，两次调用该方法添加参数，后来添加的会将前面添加的清空
     */
    public CommonBuilder<T> setParamsMap(Map<String, String> params) {
        mHttpParams = params;
        return this;
    }

    /**
     * bodyObj会被Gson转换为Json数据，可以直接传JsonObject对象
     *
     * @param bodyObj
     * @return
     */
    public CommonBuilder<T> addBodyObj(Object bodyObj) {
        mBodyObj = bodyObj;
        return this;
    }

    /**
     * 以map形式添加表单类型数据
     *
     * @param mapValue
     * @return
     */
    public CommonBuilder<T> addFormDataMap(Map<String, String> mapValue) {
        if (mapValue == null || mapValue.size() <= 0) {
            Log.e(HttpConstants.LOG_TAG, "CommonBuilder：Error! input addFormDataMap mapValue = " + mapValue);
            return this;
        }
        StringBuilder sb = new StringBuilder();
        for (String key : mapValue.keySet()) {
            sb.append(key + "=" + mapValue.get(key) + "&");
        }
        String content = sb.toString();
        content = content.substring(0, content.length() - 1);
        RequestBody requestBody = RequestBody.create(HttpConstants.FORM_TYPE, content);
        mBodyObj = requestBody;
        return this;
    }

    /**
     * 以map的形式添加Body，map将转换为JsonObject
     *
     * @param mapValue
     * @return
     */
    public CommonBuilder<T> addBodyMap(Map<String, String> mapValue) {
        if (mapValue == null) {
            Log.e(HttpConstants.LOG_TAG, "CommonBuilder：Error! input addBodyMap mapValue == null");
            return this;
        }
        JsonObject jsonObject = new JsonObject();

        for (String key : mapValue.keySet()) {
            try {
                jsonObject.addProperty(key, mapValue.get(key));
            } catch (Exception e) {
                Log.e(HttpConstants.LOG_TAG, "Exception: " + e);
                jsonObject = null;
            }
        }

        mBodyObj = jsonObject;
        return this;
    }

    /**
     * 完全覆盖除默认Header以外的Header
     *
     * @param mapValue
     * @return
     */
    public CommonBuilder<T> setHeader(Map<String, String> mapValue) {
        if (mapValue == null) {
            Log.e(HttpConstants.LOG_TAG, "CommonBuilder：Error! input setHeader mapValue = " + mapValue);
            return this;
        }
        mHttpHeader = mapValue;
        return this;
    }

    /**
     * 批量添加除默认Header以外的Header信息，不覆盖
     *
     * @param mapValue
     * @return
     */
    public CommonBuilder<T> addHeader(Map<String, String> mapValue) {
        if (mapValue == null || mapValue.size() <= 0) {
            Log.i(HttpConstants.LOG_TAG, "CommonBuilder：Error! input addHeader mapValue = " + mapValue);
            return this;
        }
        if (mHttpHeader == null) {
            mHttpHeader = new HashMap<>();
        }

        mHttpHeader.putAll(mapValue);
        return this;
    }

    /**
     * 逐条添加除默认Header以外的Header信息，不覆盖
     *
     * @param key
     * @param value
     * @return
     */
    public CommonBuilder<T> addHeader(String key, String value) {
        if (TextUtils.isEmpty(key)) {
            Log.e(HttpConstants.LOG_TAG, "CommonBuilder：Error! input addHeader key = " + key);
            return this;
        }
        if (mHttpHeader == null) {
            mHttpHeader = new HashMap<>();
        }

        mHttpHeader.put(key, value);
        return this;
    }

    /**
     * 设置HttpConfig信息，里面包含默认Header信息以及超时和缓存配置
     *
     * @param httpConfig
     * @return
     */
    public CommonBuilder<T> setHttpCustomConfig(HttpConfig httpConfig) {
        mHttpCustomConfig = httpConfig;
        return this;
    }

    /**
     * Function: getParams()
     * NOTE: 该方法可以被重写。如果不重写，则默认使用addJsonQuery()调用时设置的参数。如果重写，则是
     * 添加通用参数，需要创建新的Map<String, String>，在添加通用参数的同时，将mHttpParams中的参数
     * 也填写进去，切不可直接在mHttpParams中直接添加通用参数并返回。
     */
    protected Map<String, String> getParams() {
        return mHttpParams;
    }

    /**
     * 创建一个请求，回调默认在主线程
     *
     * @param callback
     * @return key -1为判断条件失败，并未成功发起请求
     */
    final public int build(HttpCallBack<T> callback) {
        return build(true, callback);
    }

    /**
     * 创建一个请求，并指定回调是否在UI线程
     *
     * @param onUiCallBack
     * @param callback
     * @return key -1为判断条件失败，并未成功发起请求
     */
    final public int build(boolean onUiCallBack, HttpCallBack<T> callback) {
        return request(onUiCallBack, callback);
    }

    /**
     * @param onUiCallBack
     * @param callback
     * @return key -1为判断条件失败，并未成功发起请求
     */
    protected int request(boolean onUiCallBack, HttpCallBack<T> callback) {

        int httpKey = HttpUtils.getHttpKey(getPath(), mHttpHeader, getParams(), mBodyObj, mHttpCustomConfig);
        int key = -1;
        switch (getMethod()) {
            case HttpMethod.POST:
                key = processPostQuest(onUiCallBack, callback, httpKey);
                break;
            case HttpMethod.GET:
                key = processGetQuest(onUiCallBack, callback, httpKey);
                break;
        }
        registerLifecycle();
        mKey = key;
        return key;
    }

    /**
     *
     */
    private void registerLifecycle() {
        if (mTag == null) {
            Log.e(HttpConstants.LOG_TAG, "CommonBuilder: registerLifecycle, mTag == null");
            return;
        }
        LifecycleOwner owner;
        if (mTag instanceof LifecycleOwner) {
            HttpClient cacheHttpClient = HttpClientManager.getInstance().getCacheHttpClient(getBaseUrl(), mHttpCustomConfig);
            if (cacheHttpClient == null) {
                Log.e(HttpConstants.LOG_TAG, "CommonBuilder: registerLifecycle, cacheHttpClient == null");
                return;
            }
            Log.i(HttpConstants.LOG_TAG, "CommonBuilder: registerLifecycle, Success!");
            owner = (LifecycleOwner) mTag;
            owner.getLifecycle().addObserver(cacheHttpClient);
        }
    }

    private int processPostQuest(boolean onUiCallBack, HttpCallBack<T> callback, int httpKey) {
        HttpClientManager clientManager = HttpClientManager.getInstance();
        boolean paramsEmpty = getParams() == null;
        boolean bodyObjEmpty = mBodyObj == null;
        boolean mapHeaderEmpty = (mHttpHeader == null || mHttpHeader.size() <= 0);

        String realPath = getRealPath();
        if (paramsEmpty && bodyObjEmpty && mapHeaderEmpty) {
            return clientManager.post(getBaseUrl(), realPath, httpKey, getTagHash(), mRetryTimes, mRetryDelayMillis,
                    onUiCallBack, mHttpCustomConfig, callback);
        } else if (!paramsEmpty && bodyObjEmpty && mapHeaderEmpty) {
            return clientManager.postWithParamsMap(getBaseUrl(), realPath, httpKey, getParams(), getTagHash(),
                    mRetryTimes, mRetryDelayMillis, onUiCallBack, mHttpCustomConfig, callback);
        } else if (paramsEmpty && !bodyObjEmpty && mapHeaderEmpty) {
            return clientManager.post(getBaseUrl(), realPath, httpKey, mBodyObj, getTagHash(),
                    mRetryTimes, mRetryDelayMillis, onUiCallBack, mHttpCustomConfig, callback);
        } else if (paramsEmpty && bodyObjEmpty && !mapHeaderEmpty) {
            return clientManager.postWithHeaderMap(getBaseUrl(), realPath, httpKey, mHttpHeader, getTagHash(),
                    mRetryTimes, mRetryDelayMillis, onUiCallBack, mHttpCustomConfig, callback);
        } else if (!paramsEmpty && !bodyObjEmpty && mapHeaderEmpty) {
            return clientManager.postParamsAndObj(getBaseUrl(), realPath, httpKey, getParams(), mBodyObj, getTagHash(),
                    mRetryTimes, mRetryDelayMillis, onUiCallBack, mHttpCustomConfig, callback);
        } else if (!paramsEmpty && bodyObjEmpty && !mapHeaderEmpty) {
            return clientManager.post(getBaseUrl(), realPath, httpKey, getParams(), mHttpHeader, getTagHash(),
                    mRetryTimes, mRetryDelayMillis, onUiCallBack, mHttpCustomConfig, callback);
        } else if (paramsEmpty && !bodyObjEmpty && !mapHeaderEmpty) {
            return clientManager.postMapHeaderAndObj(getBaseUrl(), realPath, httpKey, mHttpHeader, mBodyObj,
                    getTagHash(), mRetryTimes, mRetryDelayMillis, onUiCallBack, mHttpCustomConfig, callback);
        } else if (!paramsEmpty && !bodyObjEmpty && !mapHeaderEmpty) {
            return clientManager.post(getBaseUrl(), realPath, httpKey, getParams(), mHttpHeader, mBodyObj,
                    getTagHash(), mRetryTimes, mRetryDelayMillis, onUiCallBack, mHttpCustomConfig, callback);
        } else {
            Log.e(HttpConstants.LOG_TAG, "CommonBuilder：Error! processPostQuest : not support request !");
            return -1;
        }
    }

    private int processGetQuest(boolean onUiCallBack, HttpCallBack<T> callback, int httpKey) {
        HttpClientManager clientManager = HttpClientManager.getInstance();
        boolean paramsEmpty = getParams() == null;
        boolean mapHeaderEmpty = (mHttpHeader == null || mHttpHeader.size() <= 0);

        String realPath = getRealPath();
        if (paramsEmpty && mapHeaderEmpty) {
            //验证Ok
            return clientManager.get(getBaseUrl(), realPath, httpKey, getTagHash(), mRetryTimes, mRetryDelayMillis,
                    onUiCallBack, mHttpCustomConfig, callback);
        } else if (!paramsEmpty && mapHeaderEmpty) {
            //验证Ok
            return clientManager.getWithParamsMap(getBaseUrl(), realPath, httpKey, getParams(), getTagHash(),
                    mRetryTimes, mRetryDelayMillis, onUiCallBack, mHttpCustomConfig, callback);
        } else if (paramsEmpty && !mapHeaderEmpty) {
            //
            return clientManager.getWithHeaderMap(getBaseUrl(), realPath, httpKey, mHttpHeader, getTagHash(),
                    mRetryTimes, mRetryDelayMillis, onUiCallBack, mHttpCustomConfig, callback);
        } else if (!paramsEmpty && !mapHeaderEmpty) {
            //
            return clientManager.get(getBaseUrl(), realPath, httpKey, getParams(), mHttpHeader, getTagHash(),
                    mRetryTimes, mRetryDelayMillis, onUiCallBack, mHttpCustomConfig, callback);
        } else {
            Log.e(HttpConstants.LOG_TAG, "CommonBuilder：Error! processGetQuest : not support request !");
            return -1;
        }
    }

    protected int getTagHash() {
        return TAG.hashCode();
    }

    /**
     * 获取真正的请求path
     * 如果设置了Appid，则做一些特殊处理：拼接appid ，timestamp，sig
     *
     * @return
     */
    private String getRealPath() {
        if (mAppId >= 0) {
            long currentTimeMillis = System.currentTimeMillis();
            String body = "";
            if (getMethod().equals(HttpMethod.POST) && mBodyObj != null) {
                body = new GsonBuilder().disableHtmlEscaping().create().toJson(mBodyObj);
            }
            Map<String, String> sigMap = new HashMap<>();
            if (mHttpParams != null) {
                sigMap.putAll(mHttpParams);
            }
            sigMap.put("appid", mAppId + "");
            sigMap.put("body", body);
            sigMap.put("timestamp", currentTimeMillis + "");
            sigMap = sortMapByKey(sigMap);

            StringBuilder sb = new StringBuilder();
            for (String key : sigMap.keySet()) {
                sb.append("&" + key + "=" + sigMap.get(key));
            }
            String query_str = sb.toString();
            query_str = query_str.substring(1, query_str.length());
            String tmp_sig = query_str + "&appkey=" + mAppKey;
            String sig = MD5.strMD5(tmp_sig).toUpperCase();
            String path = getPath() + "?appid=" + mAppId
                    + "&timestamp=" + currentTimeMillis
                    + "&sig=" + sig;
            return path;
        } else {
            return getPath();
        }

    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<String, String>(
                new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }


    class MapKeyComparator implements Comparator<String> {

        @Override
        public int compare(String str1, String str2) {

            return str1.compareTo(str2);
        }
    }

    /**
     * 上层主动取消请求
     */
    public boolean dispose() {
        return HttpClientManager.getInstance().dispose(getBaseUrl(), getTagHash(), mKey, mHttpCustomConfig);
    }
}
