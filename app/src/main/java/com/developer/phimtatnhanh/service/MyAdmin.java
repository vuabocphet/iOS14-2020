package com.developer.phimtatnhanh.service;


import android.annotation.SuppressLint;
import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.developer.phimtatnhanh.data.Pref;
import com.developer.phimtatnhanh.data.PrefUtil;

public class MyAdmin extends DeviceAdminReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);

    }

    @Override
    public void onEnabled(@NonNull Context context, @NonNull Intent intent) {
        PrefUtil.get().postBool(Pref.ADMIN, true);
    }

    @Override
    public void onDisabled(@NonNull Context context, @NonNull Intent intent) {
        PrefUtil.get().postBool(Pref.ADMIN, false);
    }

}
