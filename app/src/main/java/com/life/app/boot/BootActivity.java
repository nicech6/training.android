package com.life.app.boot;

import android.os.Handler;

import com.alibaba.android.arouter.launcher.ARouter;
import com.life.base.constant.Path;
import com.life.base.mvp.BaseMvpActivity;
import com.life.base.mvp.BasePresenter;
import com.life.app.R;

/**
 * @author: cuihai
 * @description: 类描述
 * @date: 2019/12/16
 * @email: nicech6@163.com
 */
public class BootActivity extends BaseMvpActivity {
    @Override
    protected void initView() {
        new Handler().postDelayed(() -> {
            ARouter.getInstance().build(Path.APP_MAIN).navigation();
            finish();
        }, 2000);
    }

    @Override
    protected void bindEvent() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int initLayout() {
        return R.layout.activity_boot;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected boolean immersion() {
        return true;
    }
}
