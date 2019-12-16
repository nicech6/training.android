package com.life.login;

import android.content.Context;

import com.life.base.entity.LoginEntity;
import com.life.base.http.MyCallBack;
import com.life.base.mvp.BasePresenter;

public class LoginPresenter extends BasePresenter<LoginContact.View, LoginModel> implements LoginContact.Presenter {


    public LoginPresenter(LoginContact.View view, Context context) {
        super(view, context);
    }

    @Override
    public void getLogin(String user, String pwd) {
        if (getView() == null) {
            return;
        }
        mModel.sendLogin(user, pwd, new MyCallBack() {
            @Override
            public void onNext(Object o) {
                LoginEntity entity = (LoginEntity) o;
                mView.onLogin(entity);
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }
}