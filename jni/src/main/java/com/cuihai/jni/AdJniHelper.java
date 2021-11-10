package com.cuihai.jni;

import androidx.annotation.Nullable;

public class AdJniHelper {
    static {
        System.loadLibrary("nativemark");
    }

    private AdJniHelper() {
    }

    /**
     * @return 系统启动标识
     */
    @Nullable
    public native static String bootMark();

    /**
     * @return 系统更新标识
     */
    @Nullable
    public native static String updateMark();
}
