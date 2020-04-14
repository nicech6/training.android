package com.cuihai.http;

public class ApiException extends RuntimeException {
    private int mErrorCode;
    private String mMessage;

    public ApiException(int resultCode, String msg) {
        mErrorCode = resultCode;
        mMessage = msg;

    }

    @Override
    public String getMessage() {
        return mMessage;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

}
