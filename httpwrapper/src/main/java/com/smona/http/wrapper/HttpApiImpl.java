package com.smona.http.wrapper;

public class HttpApiImpl {

    public HttpApiImpl() {
        //token过期错误码统一拦截
        FilterChains.getInstance().addAspectRouter("00499", this::gotoLogin);
    }

    private void gotoLogin() {

    }
}
