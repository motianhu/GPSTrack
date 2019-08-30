package com.smona.http.wrapper;

import com.smona.base.http.HttpMethod;

public abstract class BasePostRequest<T> extends BaseRequest<T> {

    protected BasePostRequest(String path) {
        super(path);
    }

    @Override
    protected String getMethod() {
        return HttpMethod.POST;
    }
}
