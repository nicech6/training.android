package com.cuihai.base.mvvm;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.cuihai.base.BaseActivity;
import com.cuihai.base.R;
import com.gyf.immersionbar.ImmersionBar;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author: cuihai
 * @description: 类描述
 * @date: 2019/12/18
 * @email: nicech6@163.com
 */
public abstract class BaseMVVMActivity<VM extends AndroidViewModel, V extends ViewDataBinding> extends BaseActivity {
    // ViewModel
    protected VM mViewModel;
    // 布局view
    protected V mBindingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        mBindingView = DataBindingUtil.setContentView(this, initLayout());
        initViewModel();
        if (immersion()) {
            ImmersionBar.with(this)
                    .statusBarColor(R.color.theme_color_primary)
                    .navigationBarColor(R.color.config_color_black)
                    .statusBarDarkFont(true)
                    .fitsSystemWindows(true)
                    .init();
        }
        bindEvent();
        initData();
    }

    public static <T> Class<T> getViewModel(Object obj) {
        Class<?> currentClass = obj.getClass();
        Class<T> tClass = getGenericClass(currentClass, AndroidViewModel.class);
        if (tClass == null || tClass == AndroidViewModel.class || tClass == NoViewModel.class) {
            return null;
        }
        return tClass;
    }

    private static <T> Class<T> getGenericClass(Class<?> klass, Class<?> filterClass) {
        Type type = klass.getGenericSuperclass();
        if (type == null || !(type instanceof ParameterizedType)) return null;
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] types = parameterizedType.getActualTypeArguments();
        for (Type t : types) {
            Class<T> tClass = (Class<T>) t;
            if (filterClass.isAssignableFrom(tClass)) {
                return tClass;
            }
        }
        return null;
    }

    private void initViewModel() {
        Class<VM> viewModelClass = getViewModel(this);
        if (viewModelClass != null) {
            this.mViewModel = ViewModelProviders.of(this).get(viewModelClass);
        }
    }

    static class NoViewModel extends AndroidViewModel {

        public NoViewModel(@NonNull Application application) {
            super(application);
        }
    }
}
