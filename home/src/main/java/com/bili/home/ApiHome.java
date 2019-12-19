package com.bili.home;


import com.bili.base.entity.home.HomeLiveEntity;
import com.bili.http.CommonBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author: cuihai
 * @description: 类描述
 * @date: 2019/12/19
 * @email: nicech6@163.com
 */
public interface ApiHome {

    @GET("/room/v1/AppIndex/getAllList?_device=android&_hwid=GipPLBx9THkfKEpyDnJKek0vFiAQdTVEIRAjTixQaFhvDTQCMldgBD0O&appkey=1d8b6e7d45233436&build=519000&device=android&mobi_app=android&platform=android&scale=xhdpi&src=bili&trace_id=20180228170800004&ts=1519808884&version=5.19.0.519000&sign=36a418975dbbd41a34b8d91bc266b006")
    Observable<CommonBean<HomeLiveEntity>> getAllList();
}
