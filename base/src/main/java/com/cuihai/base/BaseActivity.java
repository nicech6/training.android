package com.cuihai.base;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author: cuihai
 * @description: 类描述
 * @date: 2019/12/18
 * @email: nicech6@163.com
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected abstract void initData();

    protected abstract void bindEvent();

    protected abstract int initLayout();

    protected abstract boolean immersion();
}
