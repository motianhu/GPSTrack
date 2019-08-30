package com.smona.http.wrapper;

public interface IDataIntercept<T> {
    void interceptData(T data);
}