package com.ch.base.http;


import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class RxHelper {
    /**
     * 对返回的通用模型做处理
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<CommonBean<T>, T> handleResult() {
        return new Observable.Transformer<CommonBean<T>, T>() {
            @Override
            public Observable<T> call(Observable<CommonBean<T>> commenBeanObservable) {
                return commenBeanObservable.flatMap(new Func1<CommonBean<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(CommonBean<T> tCommenBean) {
                        if (tCommenBean.errorCode == 0) {
                            return createData(tCommenBean.data);
                        } else {
                            return Observable.error(new ApiException(tCommenBean.errorCode, tCommenBean.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 创建成功的数据
     *
     * @param data
     * @param <T>
     * @return
     */
    private static <T> Observable<T> createData(final T data) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}
