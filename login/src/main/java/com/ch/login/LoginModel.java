package com.ch.login;

import com.ch.base.entity.LoginEntity;
import com.ch.base.http.Api;
import com.ch.base.http.CommonBean;
import com.ch.base.http.HttpUtil;
import com.ch.base.http.MyCallBack;

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
