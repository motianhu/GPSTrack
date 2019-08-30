package com.smona.base.http.internal;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smona.base.http.ExceptionHandle;
import com.smona.base.http.HttpConstants;
import com.smona.base.http.RetryFunction;
import com.smona.base.http.converter.ConvertFactory;
import com.smona.base.http.external.HttpConfig;
import com.smona.base.http.external.IModCallback;
import com.smona.base.http.ssl.SslContextFactory;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class HttpClient {

    private Context mContext;
    private Retrofit mCurrentRetrofit;
    private HttpnServices mCurrentServices;
    private Gson mGson;

    private String mBaseUrl = "";
    private HttpConfig mHttpConfig;

    public HttpClient(Context context, String baseUrl, HttpConfig httpConfig) throws HttpClientCreateException{
        if(baseUrl == null || TextUtils.isEmpty(baseUrl)){
            throw new HttpClientCreateException();
        } else if(httpConfig == null){
            throw new HttpClientCreateException();
        }

        mContext = context;
        mBaseUrl = baseUrl;
        mHttpConfig = httpConfig;

        mGson = new GsonBuilder().disableHtmlEscaping().create();
        init(httpConfig);
    }

    private void init(HttpConfig httpConfig) {
        OkHttpClient client = getOkHttpClient();
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
            mCurrentServices = mCurrentRetrofit.create(HttpnServices.class);
        }
    }

    private OkHttpClient getOkHttpClient() {
        if (mContext == null) {
            Log.e(HttpConstants.LOG_TAG, "HttpClient: getOkHttpClient, mContext == null");
            return null;
        }

        //缓存路径
        String cacheFile = mContext.getCacheDir() + "/retrofit";
        Cache cache = new Cache(new File(cacheFile), HttpConstants.SIZE_OF_CACHE);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(mHttpConfig.getConnectTimeout(), TimeUnit.SECONDS)
                .readTimeout(mHttpConfig.getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(mHttpConfig.getWriteTimeout(), TimeUnit.SECONDS)
//                .addInterceptor(getHttpInterceptor(httpConfig))
                .addInterceptor(getLogInterceptor())
                .cache(cache);

        //测试用  跳过所有认证
        if (mBaseUrl.startsWith(HttpConstants.HTTPS)) {
            //SSLSocketFactory sslSocketFactory = new SslContextFactory().getSslSocket(mContext).getSocketFactory();
            //builder.sslSocketFactory(sslSocketFactory);
            builder.sslSocketFactory(new SslContextFactory().createSSLSocketFactory())
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });
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
                Log.i(HttpConstants.LOG_TAG, "HttpClient: request or response data =  " + message);
            }
        });
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logInterceptor;
    }

    public <T> int post(String path, boolean onUiCallBack, IModCallback<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }
        Observable<Response<String>> observable = mCurrentServices.post(path);
        return doSubscribe(observable, onUiCallBack, callback);
    }

    public <T> int post(String path, Object bodyJson,  boolean onUiCallBack, IModCallback<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }
        Observable<Response<String>> observable = mCurrentServices.post(path, bodyJson);
        return doSubscribe(observable, onUiCallBack, callback);
    }

    public <T> int postWithParams(String path, Map<String, String> params, boolean onUiCallBack,
                                  IModCallback<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }
        Observable<Response<String>> observable = mCurrentServices.postWithParams(path, params);
        return doSubscribe(observable, onUiCallBack, callback);
    }

    public <T> int postWithHeaders(String path, Map headers, boolean onUiCallBack, IModCallback<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }
        Observable<Response<String>> observable = mCurrentServices.postWithHeaders(path, headers);
        return doSubscribe(observable, onUiCallBack, callback);
    }


    public <T> int postParamsAndObj(String path, Map<String, String> params, Object bodyJson,
                                boolean onUiCallBack, IModCallback<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }
        Observable<Response<String>> observable = mCurrentServices.postWithParamsAndBody(path, params, bodyJson);
        return doSubscribe(observable, onUiCallBack, callback);
    }


    public <T> int post(String path, Map<String, String> params,
                    Map<String, String> mapHeader, boolean onUiCallBack, IModCallback<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }
        Observable<Response<String>> observable = mCurrentServices.post(path, params, mapHeader);
        return doSubscribe(observable, onUiCallBack, callback);
    }

    public <T> int postHeadersAndObj(String path, Map<String, String> headers, Object bodyJson,
                                   boolean onUiCallBack, IModCallback<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }

        Observable<Response<String>> observable = mCurrentServices.postWithHeadersAndBody(path, headers, bodyJson);
        return doSubscribe(observable, onUiCallBack, callback);
    }

    public <T> int post(String path, Map<String, String> params, Map<String, String> headers,
                    Object bodyJson, boolean onUiCallBack, IModCallback<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }

        Observable<Response<String>> observable = mCurrentServices.post(path, params, headers, bodyJson);
        return doSubscribe(observable, onUiCallBack, callback);
    }

    /**
     * @param path
     * @param callback
     */
    public <T> int get(String path, boolean onUiCallBack, IModCallback<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }

        Observable<Response<String>> observable = mCurrentServices.get(path);
        return doSubscribe(observable, onUiCallBack, callback);
    }

    public <T> int getWithParams(String path, Map<String, String> params, boolean onUiCallBack, IModCallback<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }

        Observable<Response<String>> observable = mCurrentServices.getWithParams(path, params);
        return doSubscribe(observable, onUiCallBack, callback);
    }

    public <T> int getWithHeaders(String path, Map<String, String> headers, boolean onUiCallBack, IModCallback<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }

        Observable<Response<String>> observable = mCurrentServices.getWithHeaders(path, headers);
        return doSubscribe(observable, onUiCallBack, callback);
    }

    public <T> int get(String path, Map<String, String> params, Map<String, String> headers,
                   boolean onUiCallBack, IModCallback<T> callback) {
        if (mCurrentServices == null) {
            return -1;
        }

        Observable<Response<String>> observable = mCurrentServices.get(path, params, headers);
        return doSubscribe(observable, onUiCallBack, callback);
    }

    private <T> int doSubscribe(Observable<Response<String>> observable, final boolean onUiCallBack,
                            final IModCallback<T> callback) {
        Log.i(HttpConstants.LOG_TAG, "HttpClient: doSubscribe, doSubscribe");

        if (onUiCallBack) {
            observable = observable.observeOn(AndroidSchedulers.mainThread());
        }

        if(mHttpConfig.getRetryCount() > 1){
            observable = observable.retryWhen(new RetryFunction(mHttpConfig.getRetryCount(), mHttpConfig.getRetryDelayMillis()));
        }

        observable.subscribeOn(Schedulers.io())
                .map(response -> {
                    Pair<Integer, T> pair = null;
                    if (response.isSuccessful()) {
                        String data = response.body();
                        if (data != null) {
                            Type type = getParameterizedTypeClass(callback);
                            // 如果泛型是String,则无需再做下面的Json字符串到javaBean的转换
                            if (type != null && "class java.lang.String".equals(type.toString())) {
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
                }).subscribe(new Observer<Pair<Integer, T>>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Pair<Integer, T> object) {
                if (callback == null) {
                    return;
                }

                if (object != null) {
                    if (object.second != null) {
                        callback.onResult(ExceptionHandle.ERROR.SUCCESS, null, object.second);
//                        callback.onSuccess(object.second);
                    } else {
                        if (callback != null) {
//                            callback.onError(object.first, ExceptionHandle.getErrorMessage(object.first));
                            callback.onResult(object.first, ExceptionHandle.getErrorMessage(object.first), null);
                        }
                    }
                } else {
//                    callback.onError(UNKNOWN, STR_UNKNOWN_ERROR);  // 这里待确认temp_hef
                    callback.onResult(ExceptionHandle.ERROR.UNKNOWN, ExceptionHandle.STR_UNKNOWN_ERROR, null);  // 这里待确认temp_hef
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Log.i(HttpConstants.LOG_TAG, "onError, message: " + throwable.getMessage());
//                dispose(tagHash, httpKey);

                ExceptionHandle.ResponeThrowable responseThrowable = ExceptionHandle.handleException(throwable);
                if (callback != null) {
//                    callback.onError(responseThrowable.code, responseThrowable.message);
                    callback.onResult(responseThrowable.code, responseThrowable.message, null);
                }
            }

            @Override
            public void onComplete() {
                Log.i(HttpConstants.LOG_TAG, "onComplete");
//                dispose(tagHash, httpKey);
//                if (callback != null) {
//                    callback.onComplete();
//                }
            }
        });

        return 1;
    }

//    /**
//     * 缓存Disposable
//     *
//     * @param disposable
//     * @param tagHash
//     * @param httpKey
//     */
//    private void cacheDisposableIfNeed(Disposable disposable, int tagHash, int httpKey) {
//        if (disposable == null) {
//            Log.e(HttpConstants.LOG_TAG, "HttpClient: cacheDisposableIfNeed, disposable == null");
//            return;
//        }
//        Log.i(HttpConstants.LOG_TAG, "HttpClient: cacheDisposableIfNeed");
//        Pair<Integer, Disposable> pair = Pair.create(httpKey, disposable);
//        List<Pair<Integer, Disposable>> list = mDisposableCache.get(tagHash);
//        if (list == null) {
//            list = new ArrayList<>();
//        }
//        synchronized (list) {
//            list.add(pair);
//        }
//        mDisposableCache.put(tagHash, list);
//    }

    /*public Class<T> getParameterizedTypeClass(Object obj) {
        ParameterizedType pt = (ParameterizedType) obj.getClass().getGenericSuperclass();
        Type[] atr = pt.getActualTypeArguments();
        if (atr != null && atr.length > 0) {
            return (Class<T>) atr[0];
        }
        return null;
    }*/


    public Type getParameterizedTypeClass(Object obj) {
        String targetClassName = IModCallback.class.getSimpleName();

        Type[] types = obj.getClass().getGenericInterfaces();
        Type targetType = null;
        for (Type type : types){
            if(type != null && type.toString().contains(targetClassName)){
                targetType = type;
                break;
            }
        }

        Log.d(HttpConstants.LOG_TAG, "getParameterizedTypeClass(), " + targetType.toString());
        ParameterizedType pt = (ParameterizedType)targetType;
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
//    public void cancel(Object tag) {
//        if (tag == null) return;
//        Log.i(HttpConstants.LOG_TAG, "HttpClient: cancel  tag = " + tag);
//        List<Pair<Integer, Disposable>> disposableList;
//        disposableList = mDisposableCache.get(tag.hashCode());
//        if (disposableList != null) {
//            for (Pair<Integer, Disposable> pair : disposableList) {
//                pair.second.dispose();
//            }
//            mDisposableCache.remove(tag.hashCode());
//        }
//    }

    /**
     * @param tagHash
     * @param httpKey
     * @return 返回是否成功删除
     */
//    public boolean dispose(int tagHash, @NonNull int httpKey) {
//        Log.i(HttpConstants.LOG_TAG, "HttpClient: cancel  dispose ");
//        List<Pair<Integer, Disposable>> list = mDisposableCache.get(tagHash);
//        Pair<Integer, Disposable> removePair = null;
//        if(list != null) {
//            synchronized (list) {
//                for (Pair<Integer, Disposable> pair : list) {
//                    if (pair == null) {
//                        continue;
//                    }
//                    if (httpKey == pair.first) {
//                        pair.second.dispose();
//                        removePair = pair;
//                        break;
//                    }
//                }
//
//                if (removePair != null) {
//                    list.remove(removePair);
//                    Log.i(HttpConstants.LOG_TAG, "HttpClient: cancel  list.remove(removePair); ");
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

//    @Override
//    public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
//        if (source.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
//            Log.i(HttpConstants.LOG_TAG, "HttpClient: onStateChanged LifecycleOwner = " + source.toString());
//            source.getLifecycle().removeObserver(this);
//            cancel(source);
//        }
//    }
}
