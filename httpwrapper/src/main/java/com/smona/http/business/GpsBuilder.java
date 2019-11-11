package com.smona.http.business;

import com.smona.http.wrapper.BaseRequest;
import com.smona.http.wrapper.GoogleRequest;
import com.smona.http.wrapper.RequestBuilder;

public class GpsBuilder<R> extends RequestBuilder<R> {

    public GpsBuilder(int type, String path) {
        super(type, path);
    }

    @Override
    public BaseRequest<R> getGetRequest(String path) {
        return new GetInfoRequest<>(path);
    }

    @Override
    public BaseRequest<R> getPostRequest(String path) {
        return new PostInfoRequest<>(path);
    }

    @Override
    public BaseRequest<R> getPutRequest(String path) {
        return new PutInfoRequest<>(path);
    }

    @Override
    public BaseRequest<R> getDeleteRequest(String path) {
        return new DeleteInfoRequest<>(path);
    }

    @Override
    public BaseRequest<R> getCustomRequest(String path) {
        return new GoogleRequest<>(path);
    }
}
