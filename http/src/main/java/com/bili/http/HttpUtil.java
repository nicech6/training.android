package com.bili.http;


import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

public class HttpUtil {
    /**
     * 构造方法私有
     */
    private HttpUtil() {
    }

    /**
     * 在访问HttpUtil时创建单例
     */
    private static class SingletonHolder {
        private static final HttpUtil INSTANCE = new HttpUtil();
    }

    /**
     * 获取单例
     */
    public static HttpUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    //添加线程管理并订阅
    public <T> Subscription request(Observable ob, final Subscriber<T> onNext) {
        //数据预处理
        Observable.Transformer<Object, Object> result = RxHelper.handleResult();
        return ob.compose(result).subscribe(onNext);
    }
}
