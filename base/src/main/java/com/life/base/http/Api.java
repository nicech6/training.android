package com.life.base.http;

import com.life.base.util.SPUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class Api {
    private static ApiService SERVICE;
    //超时时间
    private static final int DEFAULT_TIMEOUT = 10000;
    private static String COOKIE = "";

    /**
     * 获取用户token
     *
     * @return
     */
    public static String getCookie() {
        //先从sp获取
        if (COOKIE.isEmpty()) {
            COOKIE = SPUtils.getInstance().getString("cookie", "");
        }
        return COOKIE;
    }

    public static ApiService getDefault() {
        if (SERVICE == null) {
            // log拦截器  打印所有的log
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request build = chain.request()
                                    .newBuilder()
                                    .addHeader("cookie", getCookie())
                                    .build();
                            return chain.proceed(build);
                        }
                    }).build();

            String url = "https://www.wanandroid.com";

            SERVICE = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(url)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(ApiService.class);
        }

        return SERVICE;
    }

}
