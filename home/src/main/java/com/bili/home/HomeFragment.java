package com.bili.home;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.bili.base.constant.Path;
import com.bili.base.mvp.BaseMvpFragment;
import com.bili.base.mvp.BasePresenter;

/**
 * @author: cuihai
 * @description: 类描述
 * @date: 2019/12/16
 * @email: nicech6@163.com
 */
@Route(path = Path.HOME)
public class HomeFragment extends BaseMvpFragment {

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
        return R.layout.fragment_home;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }
}
