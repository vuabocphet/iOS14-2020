package com.developer.phimtatnhanh.setuptouch.utilities;

import android.content.Context;
import android.util.Log;

public class SettingsUtil {


    public static int getTimeScreenOut(Context context) {
        try {
            return android.provider.Settings.System.getInt(context.getContentResolver(),
                    android.provider.Settings.System.SCREEN_OFF_TIMEOUT);
        } catch (android.provider.Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return 60000;
    }

    public static boolean setTimeScreenOut(Context context, int value) {
        return android.provider.Settings.System.putInt(context.getContentResolver(),
                android.provider.Settings.System.SCREEN_OFF_TIMEOUT, value);
    }

    public static void setTimeout(Context context, int screenOffTimeout) {
        boolean b = setTimeScreenOut(context, screenOffTimeout);
        Log.i("TinhNv", "setTimeout: " + b);
    }

}
