package com.smona.base.http;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class RetryFunction implements Function<Observable<Throwable>, ObservableSource<?>> {

    private final int mMaxRetries;
    private final int mRetryDelayMillis;
    private int mRetryCount;

    public RetryFunction(int maxRetries, int retryDelayMillis) {
        this.mMaxRetries = maxRetries;
        this.mRetryDelayMillis = retryDelayMillis;
    }

    @Override
    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) {
        return throwableObservable.concatMap((Function<Throwable, ObservableSource<?>>) throwable -> {
            if (++mRetryCount <= mMaxRetries) {
                return Observable.timer(mRetryDelayMillis, TimeUnit.MILLISECONDS);
            }
            return Observable.error(throwable);
        });
    }
}

