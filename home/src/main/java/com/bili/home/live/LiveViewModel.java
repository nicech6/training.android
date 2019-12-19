package com.bili.home.live;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bili.home.ApiHome;
import com.bili.http.Api;
import com.bili.http.ApiHelper;
import com.bili.http.HttpUtil;

import rx.Subscriber;

/**
 * @author: cuihai
 * @description: 类描述
 * @date: 2019/12/19
 * @email: nicech6@163.com
 */
public class LiveViewModel extends AndroidViewModel {
    public LiveViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Object> getData() {
        final MutableLiveData<Object> data = new MutableLiveData<>();
        HttpUtil.getInstance().request(Api.getDefault(Api.HOST, ApiHome.class).getIndex(ApiHelper.APP_KEY,
                ApiHelper.BUILD,
                0,
                1,
                ApiHelper.MOBI_APP,
                ApiHelper.NETWORK_WIFI,
                "cold",
                ApiHelper.PLATFORM,
                true,
                2,
                String.valueOf(System.currentTimeMillis())), new Subscriber<Object>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                data.setValue(null);
            }

            @Override
            public void onNext(Object o) {
                data.setValue(o);
            }
        });
        return data;
    }
}
