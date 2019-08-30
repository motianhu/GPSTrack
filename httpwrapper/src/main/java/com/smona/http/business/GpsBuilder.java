package com.smona.http.business;

import com.smona.http.wrapper.BaseRequest;
import com.smona.http.wrapper.BaseResponse;
import com.smona.http.wrapper.RequestBuilder;

public class GpsBuilder<R> extends RequestBuilder<R> {
    
    public GpsBuilder(int type, String path) {
        super(type, path);
    }

    @Override
    public BaseRequest<BaseResponse<R>> getGetRequest(String path) {
        return new GetInfoRequest<>(path);
    }

    @Override
    public BaseRequest<BaseResponse<R>> getPostRequest(String path) {
        return new PostInfoRequest<>(path);
    }
}
