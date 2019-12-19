package com.bili.home.live;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bili.base.constant.Path;
import com.bili.base.mvvm.BaseMVVMFragment;
import com.bili.home.R;
import com.bili.home.databinding.FragmentLiveBinding;

/**
 * @author: cuihai
 * @description: 类描述
 * @date: 2019/12/19
 * @email: nicech6@163.com
 */
@Route(path = Path.HOME_LIVE)
public class LiveFragment extends BaseMVVMFragment<LiveViewModel, FragmentLiveBinding> {
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_live;
    }

    @Override
    protected void initData() {
        mViewModel.getData().observe(this, new Observer<Object>() {
            @Override
            public void onChanged(Object listCommonBean) {

            }
        });
    }

    @Override
    protected void bindEvent() {

    }
}
