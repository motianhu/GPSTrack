package com.smona.http.wrapper;

import com.smona.base.http.HttpMethod;

public abstract class BasePutRequest<T> extends BaseRequest<T> {

    protected BasePutRequest(String path) {
        super(path);
    }

    @Override
    protected String getMethod() {
        return HttpMethod.PUT;
    }
}
