package com.smona.http.wrapper;

import com.smona.base.http.HttpCallBack;

public class HttpCallbackProxy<K> extends HttpCallBack<K> {

    private OnResultListener<K> realListener;
    private IDataIntercept<K> dataIntercept = null;

    public HttpCallbackProxy(OnResultListener<K> real) {
        this(real, null);
    }

    public HttpCallbackProxy(OnResultListener<K> real, IDataIntercept<K> dataIntercept) {
        this.realListener = real;
        this.dataIntercept = dataIntercept;
    }

    public boolean onIntercept() {
        return false;
    }

    @Override
    public void onSuccess(K data) {
        BaseResponse<?> response;
        if (data instanceof BaseResponse<?>) {
            response = (BaseResponse<?>) data;
            if (BusinessHttpCode.isSuccessful(response.code) || onIntercept()) {
                if (dataIntercept != null) {
                    dataIntercept.interceptData(data);
                }
                if (realListener != null) {
                    realListener.onSuccess(data);
                }
            } else {
                FilterChains.getInstance().exeFilter(response.code);
                if (realListener != null) {
                    realListener.onError(response.code, response.message);
                }
            }
        }

    }

    @Override
    public void onError(int stateCode, String errorInfo) {
        if (realListener != null) {
            realListener.onError(String.valueOf(stateCode), errorInfo);
        }
    }
}
