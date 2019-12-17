package com.life.http;


public interface MyCallBack<T> {
    void onNext(T t);

    void onComplete();

    void onError(Throwable e);
}
