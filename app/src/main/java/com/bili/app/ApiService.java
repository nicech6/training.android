package com.bili.app;


import com.bili.base.entity.SplashEntity;
import com.bili.http.CommonBean;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


public interface ApiService {

    @GET("/x/v2/splash")
    Observable<CommonBean<List<SplashEntity>>> getSplash(@Query("mobi_app") String mobi_app,
                                                         @Query("build") String build,
                                                         @Query("channel") String channel,
                                                         @Query("width") int width,
                                                         @Query("height") int height,
                                                         @Query("ver") String ver
    );
}