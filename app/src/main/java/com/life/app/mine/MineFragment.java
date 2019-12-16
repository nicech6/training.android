package com.life.app.mine;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.life.app.R;
import com.life.base.constant.Path;
import com.life.base.mvp.BaseMvpFragment;
import com.life.base.mvp.BasePresenter;

@Route(path = Path.MINE)
public class MineFragment extends BaseMvpFragment {
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
        return R.layout.fragment_mine;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }
}
