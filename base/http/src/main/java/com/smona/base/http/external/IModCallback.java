package com.smona.base.http.external;

public interface IModCallback<Resp> {
    public void onResult(int errorCode, String errorMsg, Resp response);
}