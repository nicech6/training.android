package com.cuihai.base.mvp;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.cuihai.base.BaseActivity;
import com.cuihai.base.R;
import com.gyf.immersionbar.ImmersionBar;

public abstract class BaseMvpActivity<P extends BasePresenter> extends BaseActivity {
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
                    .statusBarColor(R.color.theme_color_primary)
                    .navigationBarColor(R.color.config_color_black)
                    .statusBarDarkFont(true)
                    .fitsSystemWindows(true)
                    .init();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mPresenter) {
            mPresenter.onDetach();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected abstract void initView();

    protected abstract P initPresenter();

}
