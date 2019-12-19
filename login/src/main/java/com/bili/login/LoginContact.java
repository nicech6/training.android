package com.bili.login;

import com.bili.base.entity.LoginEntity;

import com.bili.base.mvp.BaseModel;
import com.bili.base.mvp.BaseView;
import com.bili.http.MyCallBack;

/**
 * @author: cuihai
 * @description: 类描述
 * @date: 2019/11/26
 * @email: nicech6@163.com
 */
public interface LoginContact {

    interface View extends BaseView {
        void onLogin(LoginEntity entity);
    }

    interface Presenter {
        void getLogin(String user, String pwd);
    }

    interface Model extends BaseModel {
        void sendLogin(String user, String pwd, MyCallBack myCallBack);
    }
}
