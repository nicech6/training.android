package com.cuihai.inject_runtime;

import android.util.Log;
import android.widget.Toast;

public class FastClickUtil {
    private static final int FAST_CLICK_TIME_DISTANCE = 300;
    private static long sLastClickTime = 0;

    public static boolean isFastDoubleClick() {
        Log.i("TAG","AAAAAAAAAAAAAAAAAAAAAAAAAA");
        System.out.print("BBBBBBBBBBBBBBBBBB");
        long time = System.currentTimeMillis();
        long timeDistance = time - sLastClickTime;
        if (0 < timeDistance && timeDistance < FAST_CLICK_TIME_DISTANCE) {
            return true;
        }
        sLastClickTime = time;
        return false;
    }
}
