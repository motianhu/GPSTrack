package com.smona.base.http;

import android.annotation.SuppressLint;
import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.smona.base.http.converter.ConvertFactory;
import com.smona.base.http.ssl.SslContextFactory;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

@SuppressLint("RestrictedApi")
public class HttpClient<T> implements GenericLifecycleObserver {

    private Context mContext;
    private Retrofit mCurrentRetrofit;
    private HttpServices mCurrentServices;
    private Gson mGson;
    //由tagKey 和httpKey 共同维护的mDisposableCache
    @SuppressLint("UseSparseArrays")
    private final HashMap<Integer, List<Pair<Integer, Disposable>>> mDisposableCache = new HashMap<>();

    private String mBaseUrl = "";
    private HttpConfig mHttpConfig;

    public HttpClient(Context context, String baseUrl, HttpConfig httpConfig, Gson gson) {
        mContext = context;
        if (!TextUtils.isEmpty(baseUrl)) {
            mBaseUrl = baseUrl;
        }
        mGson = gson;
        init(httpConfig);
    }

    /**
     * 更新配置
     */
    public void updateConfit(HttpConfig httpConfig) {
        mHttpConfig = httpConfig;
    }

    private void init(HttpConfig httpConfig) {
        OkHttpClient client = getOkHttpClient(httpConfig);
        try {
            mCurrentRetrofit = new Retrofit.Builder()
                    .client(client)
                    .addConverterFactory(new ConvertFactory(mGson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(mBaseUrl)
                    .build();

        } catch (Exception e) {
            mCurrentRetrofit = null;
            Log.e(HttpConstants.LOG_TAG, "Exception: " + e);
        }

        if (mCurrentRetrofit != null) {
            mCurrentServices = mCurrentRetrofit.create(HttpServices.class);
        }
    }

    private OkHttpClient getOkHttpClient(HttpConfig httpConfig) {
        if (mContext == null) {
            Log.e(HttpConstants.LOG_TAG, "HttpClient: getOkHttpClient, mContext == null");
            return null;
        }

        int connectTimeout;
        int readTimeout;
        int writeTimeout;
        if (httpConfig == null) {
            connectTimeout = HttpConfig.CONNECT_TIME_OUT_DEFAULT;
            readTimeout = HttpConfig.READ_TIME_OUT_DEFAULT;
            writeTimeout = HttpConfig.WRITE_TIME_OUT_DEFAULT;
        } else {
            connectTimeout = httpConfig.getConnectTimeout();
            readTimeout = httpConfig.getReadTimeout();
            writeTimeout = httpConfig.getWriteTimeout();
        }

        //缓存路径
        String cacheFile = mContext.getCacheDir() + "/retrofit";
        Cache cache = new Cache(new File(cacheFile), HttpConstants.SIZE_OF_CACHE);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .addInterceptor(getHttpInterceptor(httpConfig))
                .addInterceptor(getLogInterceptor())
                .cache(cache);

        //测试用  跳过所有认证
        if (mBaseUrl.startsWith(HttpConstants.HTTPS)) {
            //SSLSocketFactory sslSocketFactory = new SslContextFactory().getSslSocket(mContext).getSocketFactory();
            //builder.sslSocketFactory(sslSocketFactory);
            builder.sslSocketFactory(new SslContextFactory().createSSLSocketFactory())
                    .hostnameVerifier((hostname, session) -> true);
        }
        return builder.build();
    }

    /**
     * Log interceptor
     *
     * @return
     */
    private Interceptor getLogInterceptor() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (message != null && message.length() > 8 * 1024) {
                    Log.i(HttpConstants.LOG_TAG, "HttpClient: request or response data =  " + message.length());
                } else {
                    Log.i(HttpConstants.LOG_TAG, "HttpClient: request or response data =  " + message);
                }
            }
        });
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logInterceptor;
    }

    private Interceptor getHttpInterceptor(HttpConfig config) {
        mHttpConfig = config;
        return chain -> {
            Request okHttpRequest = chain.request();
            Request.Builder builder;
            builder = okHttpRequest.newBuilder();
            HttpConfig realConfig;
            if (mHttpConfig == null) {
                realConfig = HttpConfig.create(true);
            } else {
                realConfig = mHttpConfig;
            }

            if (!realConfig.getHeaders().isEmpty()) {
                Set<Map.Entry<String, String>> entrySet = realConfig.getHeaders().entrySet();
                for (Map.Entry<String, String> entry : entrySet) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }

            boolean networkAvailable = isNetworkAvailable(mContext);
            if (networkAvailable && realConfig.isNeedNetWorkCache()) { //有网络连接，看是否有配置，没配置，则走默认不缓存
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(realConfig.getNetWorkCacheTimeout(), TimeUnit.SECONDS)
                        .build();

                builder.addHeader(HttpConstants.CACHE_CONTROL, cacheControl.toString());
            } else if (!networkAvailable && realConfig.isNeedNoNetWorkCache()) { //离线缓存
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(realConfig.getNoNetWorkCacheTimeout(), TimeUnit.SECONDS)
                        .build();

                builder.addHeader(HttpConstants.CACHE_CONTROL, cacheControl.toString());
            }
            okHttpRequest = builder.build();
            return chain.proceed(okHttpRequest);
        };
    }

    /**
     * 检查网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);

        if (manager == null) {
            return false;
        }

        NetworkInfo networkinfo = manager.getActiveNetworkInfo();

        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }

        return true;
    }


    /**
     post
     */
    public int post(String path, int httpKey, int tagHash, int retryTimes,
                    int retryDelayMillis, boolean onUiCallBack, HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }
        Observable<Response<String>> observable = mCurrentServices.post(path);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }


    public int postWithParamsMap(String path, int httpKey, Map<String, String> params,
                                 int tagHash, int retryTimes, int retryDelayMillis,
                                 boolean onUiCallBack, HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }
        Observable<Response<String>> observable = mCurrentServices.postWithParamsMap(path, params);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }


    public int post(String path, int httpKey, Object bodyJson, int tagHash,
                    int retryTimes, int retryDelayMillis, boolean onUiCallBack,
                    HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }
        Observable<Response<String>> observable = mCurrentServices.post(path, bodyJson);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }


    public int postWithHeaderMap(String path, int httpKey, Map mapHeader,
                                 int tagHash, int retryTimes, int retryDelayMillis,
                                 boolean onUiCallBack, HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }
        Observable<Response<String>> observable = mCurrentServices.postWithHeaderMap(path, mapHeader);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }


    public int postParamsAndObj(String path, int httpKey, Map<String, String> params,
                                Object bodyJson, int tagHash, int retryTimes, int retryDelayMillis,
                                boolean onUiCallBack, HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }
        Observable<Response<String>> observable = mCurrentServices.post(path, params, bodyJson);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }


    public int post(String path, int httpKey, Map<String, String> params,
                    Map<String, String> mapHeader, int tagHash, int retryTimes,
                    int retryDelayMillis, boolean onUiCallBack, HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }
        Observable<Response<String>> observable = mCurrentServices.post(path, params, mapHeader);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }

    public int postMapHeaderAndObj(String path, int httpKey,
                                   Map<String, String> mapHeader, Object bodyJson, int tagHash,
                                   int retryTimes, int retryDelayMillis, boolean onUiCallBack,
                                   HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }

        Observable<Response<String>> observable = mCurrentServices.post(path, bodyJson, mapHeader);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }

    public int post(String path, int httpKey, Map<String, String> params,
                    Map<String, String> mapHeader, Object bodyJson, int tagHash,
                    int retryTimes, int retryDelayMillis, boolean onUiCallBack, HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }

        Observable<Response<String>> observable = mCurrentServices.post(path, params, bodyJson, mapHeader);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }

    /**
      get
     */
    public int get(String path, int httpKey, int tagHash, int retryTimes,
                   int retryDelayMillis, boolean onUiCallBack, HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }

        Observable<Response<String>> observable = mCurrentServices.get(path);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }

    public int getWithParamsMap(String path, int httpKey, Map<String, String> params,
                                int tagHash, int retryTimes, int retryDelayMillis,
                                boolean onUiCallBack, HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }

        Observable<Response<String>> observable = mCurrentServices.getWithParamsMap(path, params);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis, onUiCallBack, callback);
    }

    public int getWithHeaderMap(String path, int httpKey, Map<String, String> mapHeader,
                                int tagHash, int retryTimes, int retryDelayMillis, boolean onUiCallBack,
                                HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }

        Observable<Response<String>> observable = mCurrentServices.getWithHeaderMap(path, mapHeader);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }

    public int get(String path, int httpKey, Map<String, String> params,
                   Map<String, String> authHeader, int tagHash, int retryTimes, int retryDelayMillis,
                   boolean onUiCallBack, HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }

        Observable<Response<String>> observable = mCurrentServices.get(path, params, authHeader);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }


    /**
     put
     */
    public int put(String path, int httpKey, int tagHash, int retryTimes,
                    int retryDelayMillis, boolean onUiCallBack, HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }
        Observable<Response<String>> observable = mCurrentServices.put(path);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }


    public int putWithParamsMap(String path, int httpKey, Map<String, String> params,
                                 int tagHash, int retryTimes, int retryDelayMillis,
                                 boolean onUiCallBack, HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }
        Observable<Response<String>> observable = mCurrentServices.putWithParamsMap(path, params);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }


    public int put(String path, int httpKey, Object bodyJson, int tagHash,
                    int retryTimes, int retryDelayMillis, boolean onUiCallBack,
                    HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }
        Observable<Response<String>> observable = mCurrentServices.put(path, bodyJson);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }


    public int putWithHeaderMap(String path, int httpKey, Map mapHeader,
                                 int tagHash, int retryTimes, int retryDelayMillis,
                                 boolean onUiCallBack, HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }
        Observable<Response<String>> observable = mCurrentServices.putWithHeaderMap(path, mapHeader);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }


    public int putParamsAndObj(String path, int httpKey, Map<String, String> params,
                                Object bodyJson, int tagHash, int retryTimes, int retryDelayMillis,
                                boolean onUiCallBack, HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }
        Observable<Response<String>> observable = mCurrentServices.put(path, params, bodyJson);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }


    public int put(String path, int httpKey, Map<String, String> params,
                    Map<String, String> mapHeader, int tagHash, int retryTimes,
                    int retryDelayMillis, boolean onUiCallBack, HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }
        Observable<Response<String>> observable = mCurrentServices.put(path, params, mapHeader);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }

    public int putMapHeaderAndObj(String path, int httpKey,
                                   Map<String, String> mapHeader, Object bodyJson, int tagHash,
                                   int retryTimes, int retryDelayMillis, boolean onUiCallBack,
                                   HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }

        Observable<Response<String>> observable = mCurrentServices.put(path, bodyJson, mapHeader);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }

    public int put(String path, int httpKey, Map<String, String> params,
                    Map<String, String> mapHeader, Object bodyJson, int tagHash,
                    int retryTimes, int retryDelayMillis, boolean onUiCallBack, HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }

        Observable<Response<String>> observable = mCurrentServices.put(path, params, bodyJson, mapHeader);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }


    private int doSubscribe(final int httpKey, final int tagHash,
                            Observable<Response<String>> observable,
                            int retryTimes, int retryDelayMillis,
                            final boolean onUiCallBack, final HttpCallBack<T> callback) {
        Log.i(HttpConstants.LOG_TAG, "HttpClient: doSubscribe, doSubscribe");
        if (callback != null) {
            Log.i(HttpConstants.LOG_TAG, "HttpClient: doSubscribe, onStart");
            callback.onStart();
        }

        if (onUiCallBack) {
            observable = observable.observeOn(AndroidSchedulers.mainThread());
        }

        observable.retryWhen(new RetryFunction(retryTimes, retryDelayMillis))
                .subscribeOn(Schedulers.io())
                .map(new Function<Response<String>, Pair<Integer, T>>() {
                    @Override
                    public Pair<Integer, T> apply(Response<String> response) throws Exception {

                        Pair<Integer, T> pair = null;
                        if (response.isSuccessful()) {
                            String data = response.body();
                            if (data != null) {
                                Type type = getParameterizedTypeClass(callback);
                                if (type != null && "class java.lang.String".equals(type.toString())) { //这里处理泛型为String的情况.比如歌词类请求.后面可扩展专门请求String的方法 fr:wsh
                                    pair = new Pair<>(null, (T) data);
                                    return pair;
                                }
                                T t = null;
                                try {
                                    t = mGson.fromJson(data, type);
                                } catch (Exception e) {
                                    t = null;
                                }

                                if (t != null) {
                                    pair = new Pair<>(null, t);
                                } else {
//                                    msg = "json parse fail";
//                                    pair = new Pair<>(msg, null);
                                    pair = new Pair<>(ExceptionHandle.ERROR.PARSE_ERROR, null);  // "JSON字符串解析失败"
                                }
                            } else {
//                                msg = "response body is null";
                                pair = new Pair<>(ExceptionHandle.ERROR.NETWORD_ERROR, null);  // "服务器错误，返回数据为空"
                            }
                        } else {
                            ResponseBody errorBody = response.errorBody();
                            if (errorBody == null) {
                                pair = new Pair<>(ExceptionHandle.ERROR.NETWORD_ERROR, null);
                            } else {
                                String msg = errorBody.string();
                                T t = null;
                                try {
                                    Type type = getParameterizedTypeClass(callback);
                                    if (response.code() < 400 && type != null && "class java.lang.String".equals(type.toString())) { //这里处理泛型为String的情况.比如歌词类请求.后面可扩展专门请求String的方法 fr:wsh
                                        pair = new Pair<>(null, (T) errorBody);
                                        return pair;
                                    }
                                    msg = msg.replace("\"\"", "null");
                                    t = mGson.fromJson(msg, type);
                                } catch (Exception e) {
                                    t = null;
                                }

                                if (t != null) {
                                    pair = new Pair<>(null, t);
                                } else {
                                    pair = new Pair<>(ExceptionHandle.ERROR.PARSE_ERROR, null);  // "JSON字符串解析失败"
                                }
                            }
                        }
                        return pair;
                    }
                }).subscribe(new Observer<Pair<Integer, T>>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                cacheDisposableIfNeed(disposable, tagHash, httpKey);
            }

            @Override
            public void onNext(Pair<Integer, T> object) {
                if (callback == null) {
                    return;
                }

                if (object != null) {
                    if (object.second != null) {
                        callback.onSuccess(object.second);
                    } else {
                        if (callback != null) {
                            callback.onError(object.first, ExceptionHandle.getErrorMessage(object.first));
                        }
                    }
                } else {
                    callback.onError(ExceptionHandle.ERROR.UNKNOWN, ExceptionHandle.STR_UNKNOWN_ERROR);  // 这里待确认temp_hef
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Log.i(HttpConstants.LOG_TAG, "onError, message: " + throwable.getMessage());
                dispose(tagHash, httpKey);

                ExceptionHandle.ResponeThrowable responseThrowable = ExceptionHandle.handleException(throwable);
                if (callback != null) {
                    callback.onError(responseThrowable.code, responseThrowable.message);
                }
            }

            @Override
            public void onComplete() {
                Log.i(HttpConstants.LOG_TAG, "onComplete");
                dispose(tagHash, httpKey);
                if (callback != null) {
                    callback.onComplete();
                }
            }
        });

        return httpKey;
    }

    /**
     * 缓存Disposable
     *
     * @param disposable
     * @param tagHash
     * @param httpKey
     */
    private void cacheDisposableIfNeed(Disposable disposable, int tagHash, int httpKey) {
        if (disposable == null) {
            Log.e(HttpConstants.LOG_TAG, "HttpClient: cacheDisposableIfNeed, disposable == null");
            return;
        }
        Log.i(HttpConstants.LOG_TAG, "HttpClient: cacheDisposableIfNeed");
        Pair<Integer, Disposable> pair = Pair.create(httpKey, disposable);
        List<Pair<Integer, Disposable>> list = mDisposableCache.get(tagHash);
        if (list == null) {
            list = new ArrayList<>();
        }
        synchronized (list) {
            list.add(pair);
        }
        mDisposableCache.put(tagHash, list);
    }

    public Type getParameterizedTypeClass(Object obj) {
        ParameterizedType pt = (ParameterizedType) obj.getClass().getGenericSuperclass();
        Type[] atr = pt.getActualTypeArguments();
        try {
            if (atr != null && atr.length > 0) {
                return atr[0];
            }
        } catch (Exception e) {
            Log.e(HttpConstants.LOG_TAG, "getParameterizedTypeClass error");
        }
        return null;
    }

    /**
     * @param tag 请求时传入的tag
     */
    public void cancel(Object tag) {
        if (tag == null) return;
        Log.i(HttpConstants.LOG_TAG, "HttpClient: cancel  tag = " + tag);
        List<Pair<Integer, Disposable>> disposableList;
        disposableList = mDisposableCache.get(tag.hashCode());
        if (disposableList != null) {
            for (Pair<Integer, Disposable> pair : disposableList) {
                pair.second.dispose();
            }
            mDisposableCache.remove(tag.hashCode());
        }
    }

    /**
     * @param tagHash
     * @param httpKey
     * @return 返回是否成功删除
     */
    public boolean dispose(int tagHash, @NonNull int httpKey) {
        Log.i(HttpConstants.LOG_TAG, "HttpClient: cancel  dispose ");
        List<Pair<Integer, Disposable>> list = mDisposableCache.get(tagHash);
        Pair<Integer, Disposable> removePair = null;
        if (list != null) {
            synchronized (list) {
                for (Pair<Integer, Disposable> pair : list) {
                    if (pair == null) {
                        continue;
                    }
                    if (httpKey == pair.first) {
                        pair.second.dispose();
                        removePair = pair;
                        break;
                    }
                }

                if (removePair != null) {
                    list.remove(removePair);
                    Log.i(HttpConstants.LOG_TAG, "HttpClient: cancel  list.remove(removePair); ");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
        if (source.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
            Log.i(HttpConstants.LOG_TAG, "HttpClient: onStateChanged LifecycleOwner = " + source.toString());
            source.getLifecycle().removeObserver(this);
            cancel(source);
        }
    }
}
