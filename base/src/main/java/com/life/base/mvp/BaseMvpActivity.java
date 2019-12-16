package com.life.base.mvp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.life.base.R;
import com.gyf.immersionbar.ImmersionBar;

public abstract class BaseMvpActivity<P extends BasePresenter> extends AppCompatActivity {
    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        initView();
        bindEvent();
        initData();
        mPresenter = initPresenter();
        if (immersion()) {
            ImmersionBar.with(this)
                    .statusBarColor(R.color.config_color_white)
                    .navigationBarColor(R.color.config_color_black)
                    .statusBarDarkFont(true)
                    .fitsSystemWindows(true)
                    .init();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected abstract void initView();

    protected abstract void bindEvent();

    protected abstract void initData();

    protected abstract int initLayout();

    protected abstract P initPresenter();

    protected abstract boolean immersion();

}
