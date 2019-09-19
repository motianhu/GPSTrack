package com.smona.http.wrapper;

public interface OnResultListener<T> {
    void onSuccess(T t);
    void onError(String stateCode, ErrorInfo errorInfo);
}
