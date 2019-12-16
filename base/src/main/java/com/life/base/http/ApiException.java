package com.life.base.http;

import android.text.TextUtils;

import com.life.base.util.ToastUtils;


public class ApiException extends RuntimeException {
    private int mErrorCode;
    private String mMessage;

    public ApiException(int resultCode, String msg) {
        mErrorCode = resultCode;
        mMessage = msg;
        //统一的toast提醒
        if (!TextUtils.isEmpty(mMessage)){
            ToastUtils.showLong(msg);
        }else {
            ToastUtils.showLong(mErrorCode+"");
        }
    }

    @Override
    public String getMessage() {
        return mMessage;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

}
