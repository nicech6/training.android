package com.training.app;


import com.cuihai.base.entity.SplashEntity;
import com.cuihai.http.CommonBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


public interface ApiApp {

    @GET("/x/v2/splash")
    Observable<CommonBean<List<SplashEntity>>> getSplash(@Query("mobi_app") String mobi_app,
                                                         @Query("build") String build,
                                                         @Query("channel") String channel,
                                                         @Query("width") int width,
                                                         @Query("height") int height,
                                                         @Query("ver") String ver
    );
}