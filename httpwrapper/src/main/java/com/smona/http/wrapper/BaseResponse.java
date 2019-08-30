package com.smona.http.wrapper;

public class BaseResponse<R> {
    public String code;
    public String message;
    public R data;
}
