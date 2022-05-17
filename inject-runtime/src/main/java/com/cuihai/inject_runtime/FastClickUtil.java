package com.cuihai.inject_runtime;

public class FastClickUtil {

    private static final int FAST_CLICK_TIME_DISTANCE = 300;
    private static long sLastClickTime = 0;

    public static boolean isGoClick() {
        long time = System.currentTimeMillis();
        long timeDistance = time - sLastClickTime;
        if (timeDistance > FAST_CLICK_TIME_DISTANCE) {
            sLastClickTime = time;
            return true;
        }
        return false;
    }
}
