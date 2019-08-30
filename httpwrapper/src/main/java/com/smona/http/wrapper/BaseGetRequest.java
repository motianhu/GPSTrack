package com.smona.http.wrapper;

import com.smona.base.http.HttpMethod;

public abstract class BaseGetRequest<T> extends BaseRequest<T> {
    protected BaseGetRequest(String path) {
        super(path);
    }

    @Override
    protected String getMethod() {
        return HttpMethod.GET;
    }
}
