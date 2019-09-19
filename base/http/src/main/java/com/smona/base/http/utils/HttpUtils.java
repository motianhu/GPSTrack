package com.smona.base.http.utils;

import android.text.TextUtils;

import com.smona.base.http.HttpConfig;
import java.util.Map;

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
}
