package com.developer.phimtatnhanh.setuptouch.utilities;


import android.content.ContentResolver;
import android.content.Context;

import com.developer.phimtatnhanh.data.Pref;
import com.developer.phimtatnhanh.data.PrefUtil;
import com.developer.phimtatnhanh.setuptouch.config.ConfigBrightness;

public class BrightnessUtil implements ConfigBrightness {

    public static void setBrightness(Context context, int brightness) {
        if (brightness < BRIGHTNESS_MIN)
            brightness = BRIGHTNESS_MIN;
        else {
            boolean bool = PrefUtil.get().getBool(Pref.BRIGHTNESS, false);
            if (brightness > (bool ? BRIGHTNESS_MAX_XIAOMI : BRIGHTNESS_MAX))
                brightness = bool ? BRIGHTNESS_MAX_XIAOMI : BRIGHTNESS_MAX;
        }
        ContentResolver cResolver = context.getContentResolver();
        android.provider.Settings.System.putInt(cResolver, android.provider.Settings.System.SCREEN_BRIGHTNESS, brightness);
    }

    public static int getBrightness(Context context) {
        ContentResolver cResolver = context.getContentResolver();
        return android.provider.Settings.System.getInt(cResolver, android.provider.Settings.System.SCREEN_BRIGHTNESS, BRIGHTNESS_DEFAULT);
    }
}
