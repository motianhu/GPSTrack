package com.smona.http.wrapper;

import com.smona.base.http.HttpCallBack;

import java.util.Map;

public abstract class RequestBuilder<R> {

    public static final int REQUEST_GET = 1;
    public static final int REQUEST_POST = 2;
    public static final int REQUEST_PUT = 3;
    public static final int REQUEST_DELETE = 4;

    public static final int REQUEST_CUSTOM = 10;

    private BaseRequest<R> request;

    public RequestBuilder(int type, String path) {
        if (type == REQUEST_GET) {
            this.request = getGetRequest(path);
        } else if (type == REQUEST_POST) {
            this.request = getPostRequest(path);
        } else if (type == REQUEST_PUT) {
            this.request = getPutRequest(path);
        } else if (type == REQUEST_DELETE) {
            this.request = getDeleteRequest(path);
        } else if (type == REQUEST_CUSTOM) {
            this.request = getCustomRequest(path);
        }
    }

    public void addHeader(String key, String value) {
        request.addHeader(key, value);
    }

    public void requestData(Object params, HttpCallBack<R> listener) {
        request.addBodyObj(params).build(listener);
    }

    public void requestData(Map<String, String> params, HttpCallBack<R> listener) {
        request.addParamsMap(params).build(listener);
    }

    public void requestData(HttpCallBack<R> listener) {
        request.build(listener);
    }

    public abstract BaseRequest<R> getGetRequest(String path);

    public abstract BaseRequest<R> getPostRequest(String path);

    public abstract BaseRequest<R> getPutRequest(String path);

    public abstract BaseRequest<R> getDeleteRequest(String path);

    public abstract BaseRequest<R> getCustomRequest(String path);
}
