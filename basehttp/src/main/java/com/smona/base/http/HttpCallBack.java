package com.smona.base.http;

public abstract class HttpCallBack<T> {

    //onStart 运行在当前线程
    public void onStart(){}
    //onSuccess被指定是否运行在主线程
    public abstract void onSuccess(T t);
    //onError被指定是否运行在主线程
    public abstract void onError(int stateCode, String errorInfo);
    //onProgress运行在主线程
    public void onProgress(int progress){}

    /**
     * 有一些特别的需求需要Rxjava执行完后返回的onComplete
     * onComplete运行在主线程
     */
    public void onComplete() {};
}
