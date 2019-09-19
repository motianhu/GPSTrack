package com.smona.http.wrapper;

public interface OnResultListener<T> {
    void onSuccess(T t);
    void onError(int stateCode, ErrorInfo errorInfo);
}
