package com.ch.base.http;


import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;


public interface ApiService {

    @FormUrlEncoded
    @POST("/user/login")
    Observable<CommonBean<Object>> login(@Field("phone") String userName, @Field("password") String pwd);

}