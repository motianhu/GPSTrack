package com.smona.http.wrapper;

import com.smona.base.http.HttpMethod;

public abstract class BaseDeleteRequest<T> extends BaseRequest<T> {

    protected BaseDeleteRequest(String path) {
        super(path);
    }

    @Override
    protected String getMethod() {
        return HttpMethod.DELETE;
    }
}
