package com.smona.http.wrapper;

import com.smona.base.http.HttpCallBack;

import java.util.Map;

public abstract class RequestBuilder<R> {

    public static final int REQUEST_GET = 1;
    public static final int REQUEST_POST = 2;

    private BaseRequest<BaseResponse<R>> request;

    public RequestBuilder(int type, String path) {
        if (type == REQUEST_GET) {
            this.request = getGetRequest(path);
        } else {
            this.request = getPostRequest(path);
        }
    }

    public void requestData(Object params, HttpCallBack<BaseResponse<R>> listener) {
        request.addBodyObj(params).build(listener);
    }

    public void requestData(Map<String, String> params, HttpCallBack<BaseResponse<R>> listener) {
        request.addParamsMap(params).build(listener);
    }

    public void requestData(HttpCallBack<BaseResponse<R>> listener) {
        request.build(listener);
    }

    public abstract BaseRequest<BaseResponse<R>> getGetRequest(String path);

    public abstract BaseRequest<BaseResponse<R>> getPostRequest(String path);
}
