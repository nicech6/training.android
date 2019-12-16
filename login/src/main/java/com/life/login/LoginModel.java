package com.life.login;

import com.life.base.entity.LoginEntity;
import com.life.base.http.Api;
import com.life.base.http.CommonBean;
import com.life.base.http.HttpUtil;
import com.life.base.http.MyCallBack;

import rx.Subscriber;

public class LoginModel implements LoginContact.Model {
    @Override
    public void sendLogin(String user, String pwd, MyCallBack myCallBack) {
        HttpUtil.getInstance().request(Api.getDefault().login(user, pwd), new Subscriber<CommonBean<LoginEntity>>() {
            @Override
            public void onCompleted() {
                myCallBack.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                myCallBack.onError(e);
            }

            @Override
            public void onNext(CommonBean<LoginEntity> o) {
                myCallBack.onNext(o.data);
            }
        });
    }
}
