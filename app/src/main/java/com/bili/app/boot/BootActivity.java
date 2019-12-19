package com.bili.app.boot;

import android.os.Handler;

import androidx.lifecycle.Observer;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bili.app.R;
import com.bili.app.databinding.ActivityBootBinding;
import com.bili.base.constant.Path;
import com.bili.base.entity.SplashEntity;
import com.bili.base.mvvm.BaseMVVMActivity;
import com.bili.http.CommonBean;

import java.util.List;

/**
 * @author: cuihai
 * @description: 类描述
 * @date: 2019/12/16
 * @email: nicech6@163.com
 */
public class BootActivity extends BaseMVVMActivity<BootViewModel, ActivityBootBinding> {

    @Override
    protected void bindEvent() {

    }

    @Override
    protected void initData() {
        mViewModel.getSplash().observe(this, new Observer<CommonBean<List<SplashEntity>>>() {
            @Override
            public void onChanged(CommonBean<List<SplashEntity>> bean) {
                Glide.with(BootActivity.this)
                        .load(bean.data.get(0).getThumb())
                        .transition(DrawableTransitionOptions.withCrossFade(1500))
                        .into(mBindingView.iv);
                new Handler().postDelayed(() -> {
                    ARouter.getInstance().build(Path.APP_MAIN).navigation();
                    finish();
                }, 2000);
            }
        });
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_boot;
    }

    @Override
    protected boolean immersion() {
        return true;
    }
}
