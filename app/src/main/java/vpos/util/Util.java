package vpos.util;

import android.util.Log;

public class Util {
    public static void sleepMs(int i) {
        try {
            Thread.sleep((long) i);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("SleepMs", "SleepMs fail : " + e.getMessage().toString());
        }
    }
}
