package com.smona.http.business;

import com.smona.http.wrapper.BasePostRequest;

public class PostInfoRequest<T> extends BasePostRequest<T> {

    PostInfoRequest(String path) {
        super(path);
    }
}
