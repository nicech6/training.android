package com.bili.http;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.HashMap;
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
    private static String HOST = "http://app.bilibili.com";
    private static String LIVEHOST = "http://api.live.bilibili.com";
    //超时时间
    private static final int DEFAULT_TIMEOUT = 10000;
    private static String COOKIE = "";
    private static HashMap<String, Retrofit> mRetrofitHashMap = new HashMap<>();

    private static String getCookie() {
        //先从sp获取
        if (COOKIE.isEmpty()) {
            SharedPreferences sharedPreferences = Util.getApplicationByReflect().getSharedPreferences("config", Context.MODE_PRIVATE);
            SharedPreferences.Editor editorConfig = sharedPreferences.edit();
            COOKIE = sharedPreferences.getString("cookie", "");
        }
        return COOKIE;
    }

    public static <T> T getDefault(Class<T> clazz) {
        return getRetrofit(HOST).create(clazz);
    }

    private static Retrofit getRetrofit(String baseurl) {
        Retrofit retrofit;
        if (mRetrofitHashMap.containsKey(baseurl)) {
            retrofit = mRetrofitHashMap.get(baseurl);
        } else {
            retrofit = createRetrofit(baseurl);
        }
        return retrofit;
    }

    private static Retrofit createRetrofit(String baseUrl) {
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

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
