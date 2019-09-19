package com.smona.base.http;

public abstract class HttpCallBack<T> {
    //onStart 运行在当前线程
    public void onStart(){}
    //onSuccess被指定是否运行在主线程
    public abstract void onSuccess(T t);
    //onError被指定是否运行在主线程
    public abstract void onError(int stateCode, String errorInfo);

    public void onComplete() {}
}
