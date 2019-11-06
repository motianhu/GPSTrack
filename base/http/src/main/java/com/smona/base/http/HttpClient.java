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

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
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
        return builder.build();
    }

    /**
     * Log interceptor
     *
     * @return
     */
    private Interceptor getLogInterceptor() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            private StringBuilder mMessage = new StringBuilder();
            @Override
            public void log(String message) {
                // 请求或者响应开始
                if (message.startsWith("--> POST") || message.startsWith("--> GET") || message.startsWith("--> PUT") || message.startsWith("--> DELETE")) {
                    mMessage.setLength(0);
                }
                // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
                if ((message.startsWith("{") && message.endsWith("}"))
                        || (message.startsWith("[") && message.endsWith("]"))) {
                    message = formatJson(decodeUnicode(message));
                }
                mMessage.append(message.concat("\n"));
                // 响应结束，打印整条日志
                if (message.startsWith("<-- END HTTP")) {
                    Log.i(HttpConstants.LOG_TAG, mMessage.toString());
                }

            }
        });
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logInterceptor;
    }

    private static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            //遇到{ [换行，且下一行缩进
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                //遇到} ]换行，当前行缩进
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                //遇到,换行
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }
        return sb.toString();
    }

    /**
     * 添加space
     *
     * @param sb
     * @param indent
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }

    private static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
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
     * post
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
     * get
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
     * put
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

    /**
     * Delete
     */
    public int delete(String path, int httpKey, int tagHash, int retryTimes,
                    int retryDelayMillis, boolean onUiCallBack, HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }
        Observable<Response<String>> observable = mCurrentServices.delete(path);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }


    public int deleteWithParamsMap(String path, int httpKey, Map<String, String> params,
                                 int tagHash, int retryTimes, int retryDelayMillis,
                                 boolean onUiCallBack, HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }
        Observable<Response<String>> observable = mCurrentServices.deleteWithParamsMap(path, params);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }


    public int delete(String path, int httpKey, Object bodyJson, int tagHash,
                    int retryTimes, int retryDelayMillis, boolean onUiCallBack,
                    HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }
        Observable<Response<String>> observable = mCurrentServices.delete(path, bodyJson);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }


    public int deleteWithHeaderMap(String path, int httpKey, Map mapHeader,
                                 int tagHash, int retryTimes, int retryDelayMillis,
                                 boolean onUiCallBack, HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }
        Observable<Response<String>> observable = mCurrentServices.deleteWithHeaderMap(path, mapHeader);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }


    public int deleteParamsAndObj(String path, int httpKey, Map<String, String> params,
                                Object bodyJson, int tagHash, int retryTimes, int retryDelayMillis,
                                boolean onUiCallBack, HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }
        Observable<Response<String>> observable = mCurrentServices.delete(path, params, bodyJson);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }


    public int delete(String path, int httpKey, Map<String, String> params,
                    Map<String, String> mapHeader, int tagHash, int retryTimes,
                    int retryDelayMillis, boolean onUiCallBack, HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }
        Observable<Response<String>> observable = mCurrentServices.delete(path, params, mapHeader);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }

    public int deleteMapHeaderAndObj(String path, int httpKey,
                                   Map<String, String> mapHeader, Object bodyJson, int tagHash,
                                   int retryTimes, int retryDelayMillis, boolean onUiCallBack,
                                   HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }

        Observable<Response<String>> observable = mCurrentServices.delete(path, bodyJson, mapHeader);
        return doSubscribe(httpKey, tagHash, observable, retryTimes, retryDelayMillis,
                onUiCallBack, callback);
    }

    public int delete(String path, int httpKey, Map<String, String> params,
                    Map<String, String> mapHeader, Object bodyJson, int tagHash,
                    int retryTimes, int retryDelayMillis, boolean onUiCallBack, HttpCallBack<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }

        Observable<Response<String>> observable = mCurrentServices.delete(path, params, bodyJson, mapHeader);
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
                .map(response -> {
                    Pair<Integer, Object> pair = null;
                    if (response.isSuccessful()) {
                        String data = response.body();
                        if (!TextUtils.isEmpty(data)) {
                            Type type = getParameterizedTypeClass(callback);
                            T t;
                            if (type != null && "class java.lang.String".equals(type.toString())) { //这里处理泛型为String的情况.比如歌词类请求.后面可扩展专门请求String的方法 fr:wsh
                                pair = new Pair<>(response.code(), data);
                                return pair;
                            }
                            try {
                                t = mGson.fromJson(data, type);
                            } catch (Exception e) {
                                t = null;
                            }

                            if (t != null) {
                                pair = new Pair<>(response.code(), t);
                            } else {
                                pair = new Pair<>(ExceptionHandle.ERROR.PARSE_ERROR, ExceptionHandle.STR_PARSE_ERROR);  // "JSON字符串解析失败"
                            }
                        } else {
                            try {
                                Type type = getParameterizedTypeClass(callback);
                                Class<?> clazz = getRawType(type);
                                pair = new Pair<>(response.code(), clazz.newInstance());// "服务器错误，返回数据为空"
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody == null) {
                            pair = new Pair<>(ExceptionHandle.ERROR.NETWORD_ERROR, ExceptionHandle.STR_NET_ERROR);
                        } else {
                            String msg = errorBody.string();
                            pair = new Pair<>(response.code(), msg);
                        }
                    }
                    return pair;
                }).subscribe(new Observer<Pair<Integer, Object>>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                cacheDisposableIfNeed(disposable, tagHash, httpKey);
            }

            @Override
            public void onNext(Pair<Integer, Object> object) {
                if (callback == null) {
                    return;
                }
                if (object != null) {
                    if (ExceptionHandle.ERROR.SUCCESS == object.first) {
                        callback.onSuccess((T) object.second);
                    } else {
                        callback.onError(object.first, (String) object.second);
                    }
                } else {
                    callback.onError(ExceptionHandle.ERROR.UNKNOWN, ExceptionHandle.STR_PARSE_ERROR);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e(HttpConstants.LOG_TAG, "onError, message: " + throwable.getMessage());
                dispose(tagHash, httpKey);

                ExceptionHandle.ResponeThrowable responseThrowable = ExceptionHandle.handleException(throwable);
                if (callback != null) {
                    callback.onError(responseThrowable.code, responseThrowable.message);
                }
            }

            @Override
            public void onComplete() {
                Log.e(HttpConstants.LOG_TAG, "onComplete");
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

    private Type getParameterizedTypeClass(Object obj) {
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

    private static Class<?> getRawType(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            return (Class) rawType;
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();
        } else if (type instanceof TypeVariable) {
            return Object.class;
        } else if (type instanceof WildcardType) {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <" + type + "> is of type " + className);
        }
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
