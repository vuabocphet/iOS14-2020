package com.developer.phimtatnhanh.setuptouch.utilities;


import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.widget.Toast;

import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.setuptouch.config.ConfigScreenOrientation;

public class ScreenOrientationUtil implements ConfigScreenOrientation {

    private static boolean getScreenOrientation(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        int anInt = Settings.System.getInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0);
        return anInt == ON;
    }

    public static void setScreenOrientation(Context context) {
        boolean screenOrientation = getScreenOrientation(context);
        ContentResolver contentResolver = context.getContentResolver();
        if (screenOrientation) {
            Settings.System.putInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION, OFF);
            Toast.makeText(context, context.getString(R.string.screen_orientation_off), Toast.LENGTH_SHORT).show();
        } else {
            Settings.System.putInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION, ON);
            Toast.makeText(context, context.getString(R.string.screen_orientation_on), Toast.LENGTH_SHORT).show();
        }
    }

}
