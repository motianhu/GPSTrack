package com.smona.http.wrapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smona.base.http.HttpCallBack;

public class HttpCallbackProxy<K> extends HttpCallBack<K> {
    private static final Gson sGson = new GsonBuilder().disableHtmlEscaping().create();

    private OnResultListener<K> realListener;

    public HttpCallbackProxy(OnResultListener<K> real) {
        this.realListener = real;
    }

    @Override
    public void onSuccess(K data) {
        if (realListener != null) {
            realListener.onSuccess(data);
        }
    }

    @Override
    public void onError(int stateCode, String errorInfo) {
        if (realListener != null) {
            try {
                ErrorInfo error = sGson.fromJson(errorInfo, ErrorInfo.class);
                realListener.onError(stateCode, error);
            } catch (Exception e) {
                ErrorInfo ei= new ErrorInfo();
                ei.setStatus(stateCode);
                ei.setMessage(errorInfo);
                realListener.onError(stateCode, ei);
            }
        }
        FilterChains.getInstance().exeFilter(stateCode);
    }
}
