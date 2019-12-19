package com.bili.home.live;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bili.base.entity.home.HomeLiveEntity;
import com.bili.base.util.SizeUtils;
import com.bili.home.ApiHome;
import com.bili.http.Api;
import com.bili.http.ApiHelper;
import com.bili.http.CommonBean;
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

    public MutableLiveData<CommonBean<HomeLiveEntity>> getData() {
        final MutableLiveData<CommonBean<HomeLiveEntity>> data = new MutableLiveData<>();
        HttpUtil.getInstance().request(Api.getDefault(Api.LIVEHOST, ApiHome.class).getAllList(), new Subscriber<CommonBean<HomeLiveEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                data.setValue(null);
            }

            @Override
            public void onNext(CommonBean<HomeLiveEntity> o) {
                data.setValue(o);
            }
        });
        return data;
    }
}
