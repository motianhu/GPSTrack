package com.smona.http.wrapper;

public class GoogleRequest<T> extends BaseGetRequest<T> {

    public GoogleRequest(String path) {
        super(path);
    }

    @Override
    protected String getBaseUrl() {
        return "https://maps.googleapis.com";
    }
}
