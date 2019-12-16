package com.life.base.mvp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author: cuihai
 * @description: 类描述
 * @date: 2019/12/16
 * @email: nicech6@163.com
 */
public abstract class BaseMvpFragment<P extends BasePresenter> extends Fragment {
    private P mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = LayoutInflater.from(getContext()).inflate(initLayout(), container, false);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
        bindEvent();
        mPresenter = initPresenter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mPresenter) {
            mPresenter.onDetach();
        }
    }

    protected abstract void initView();

    protected abstract void bindEvent();

    protected abstract void initData();

    protected abstract int initLayout();

    protected abstract P initPresenter();
}
