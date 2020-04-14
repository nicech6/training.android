package com.cuihai.base.mvp;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class BasePresenter<V, M> {
    public Context mContext;
    public V mView;
    private WeakReference<V> mViewRef;
    public M mModel;

    public BasePresenter(V view, Context context) {
        mView = view;
        mContext = context;
        mModel = getM(this, 1);
        mViewRef = new WeakReference<V>(view);
    }

    public V getView() {
        if (isAttach()) {
            return mViewRef.get();
        } else {
            return null;
        }
    }

    public boolean isAttach() {
        return null != mViewRef && null != mViewRef.get();
    }

    public void onDetach() {
        if (null != mViewRef) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    public <M> M getM(Object o, int i) {
        try {
            Type[] type = ((ParameterizedType) (o.getClass().getGenericSuperclass())).getActualTypeArguments();
            if (null != type && type.length <= 0) {
                return null;
            }
            return ((Class<M>) ((ParameterizedType) (o.getClass().getGenericSuperclass())).getActualTypeArguments()[i]).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
