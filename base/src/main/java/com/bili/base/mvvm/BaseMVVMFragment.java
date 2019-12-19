package com.bili.base.mvvm;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProviders;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author: cuihai
 * @description: 类描述
 * @date: 2019/12/19
 * @email: nicech6@163.com
 */
public abstract class BaseMVVMFragment<VM extends AndroidViewModel, V extends ViewDataBinding> extends Fragment {
    public Context mContext;
    // ViewModel
    protected VM mViewModel;
    // 布局view
    protected V mBindingView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBindingView = DataBindingUtil.inflate(inflater, initContentView(inflater, container, savedInstanceState), container, false);
        initViewModel();
        return mBindingView.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        bindEvent();
    }

    public abstract int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    protected abstract void initData();

    protected abstract void bindEvent();

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
