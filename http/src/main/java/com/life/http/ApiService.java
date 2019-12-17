package com.life.http;


import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;


public interface ApiService {

    @FormUrlEncoded
    @GET("/authorize")
    Observable<Object> login(@Field("client_id") String client_id,
                                         @Field("redirect_uri") String redirect_uri,
                                         @Field("response_type") String response_type,
                                         @Field("scope") String scope);

}