package com.smona.http.business;

import com.smona.http.wrapper.BaseGetRequest;

public class GetInfoRequest<T> extends BaseGetRequest<T> {

    GetInfoRequest(String path) {
        super(path);
    }
}
