package com.developer.phimtatnhanh.ads.util;


import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.developer.phimtatnhanh.BuildConfig;
import com.google.android.gms.ads.AdRequest;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdUtil {

    public static List<String> getTestDevices(Context context) {
        ArrayList<String> deviceIds = new ArrayList<>();
        if (context == null) {
            return deviceIds;
        }
        deviceIds.add(getTestDeviceId(context));
        return deviceIds;
    }

    public static AdRequest.Builder getAdRequestBuilderWithTestDevice(Context context) {
        AdRequest.Builder builder = new AdRequest.Builder();
        String testDeviceId = getTestDeviceId(context);
        Log.d("TinhNv", "getAdRequestBuilderWithTestDevice: "+testDeviceId);
        if (!TextUtils.isEmpty(testDeviceId)) {
            builder.addTestDevice(testDeviceId);
        }
        return builder;
    }

    @SuppressLint("HardwareIds")
    public static String getTestDeviceId(Context context) {
        if (!BuildConfig.DEBUG) {
            return "";
        }
        try {
             String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            return EncryptionUtils.encodeMd5(androidId).toUpperCase(Locale.US);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}