package com.smona.http.wrapper;

public abstract class BaseRequest<T> extends BaseBuilder<T> {
    private String path;

    protected BaseRequest(String path) {
        this.path = path;
    }

    @Override
    protected String getPath() {
        return path;
    }
}
