package com.likelife.app;

import com.ch.base.mvp.BaseMvpActivity;
import com.ch.base.mvp.BasePresenter;

/**
 * @author: cuihai
 * @description: 类描述
 * @date: 2019/12/14
 * @email: nicech6@163.com
 */
public class MainActivity extends BaseMvpActivity {
    @Override
    protected void initView() {

    }

    @Override
    protected void bindEvent() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected boolean needImmersion() {
        return false;
    }
}
