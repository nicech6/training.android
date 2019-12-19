package com.bili.app.boot;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bili.base.entity.SplashEntity;
import com.bili.http.Api;
import com.bili.app.ApiService;
import com.bili.http.CommonBean;
import com.bili.http.HttpUtil;

import java.util.List;

import rx.Subscriber;

/**
 * @author: cuihai
 * @description: 类描述
 * @date: 2019/12/18
 * @email: nicech6@163.com
 */
public class BootViewModel extends AndroidViewModel {
    public BootViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<CommonBean<List<SplashEntity>>> getSplash() {
        final MutableLiveData<CommonBean<List<SplashEntity>>> data = new MutableLiveData<>();
        HttpUtil.getInstance().request(Api.getDefault(ApiService.class).getSplash("android", "502000", "xiaomi", 1080, 1920, "4082600596548893087"), new Subscriber<CommonBean<List<SplashEntity>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                data.setValue(null);
            }

            @Override
            public void onNext(CommonBean<List<SplashEntity>> o) {
                data.setValue(o);
            }
        });
        return data;
    }
}
