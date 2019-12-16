package com.life.login;

import com.life.base.entity.LoginEntity;
import com.life.base.http.MyCallBack;
import com.life.base.mvp.BaseModel;
import com.life.base.mvp.BaseView;

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
