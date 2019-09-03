package com.smona.base.http.utils;

import android.text.TextUtils;

import com.smona.base.http.HttpConfig;
import com.smona.base.http.ssl.SslContextFactory;

import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.OkHttpClient;

public class HttpUtils {

    /**
     * 拼接存储Disposable的key值
     * @param path
     * @param params
     * @param mHttpCustomConfig
     * @return
     */
    public static int getHttpKey(String path, Map<String, String> mapHeader, Map<String, String> params, Object bodyObject, HttpConfig mHttpCustomConfig) {
        StringBuffer keyBuffer = new StringBuffer("");
        if (!TextUtils.isEmpty(path)) {
            keyBuffer.append(path);
        }

        if (mapHeader != null && mapHeader.size() > 0) {
            keyBuffer.append(mapHeader.toString());
        }
        if (params != null && params.size() > 0) {
            keyBuffer.append(params.toString());
        }
        if (bodyObject != null) {
            keyBuffer.append(bodyObject.toString());
        }

        if (mHttpCustomConfig != null) {
            keyBuffer.append(mHttpCustomConfig.toString());
        }

        return keyBuffer.toString().hashCode();
    }

    /**
     * 获取OkHttpClient
     * 设置允许HTTPS
     * 配置加载图片时候禁用掉所有的 SSL 证书检查
     * */
    public static OkHttpClient getOkHttpClient()
    {
        SSLSocketFactory sslSocketFactory = new SslContextFactory().createSSLSocketFactory();
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder = builder.sslSocketFactory(sslSocketFactory);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session)
            {
                return true;
            }
        });
        return builder.build();
    }

}
