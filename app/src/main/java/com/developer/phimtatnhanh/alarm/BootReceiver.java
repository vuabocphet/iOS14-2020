package com.developer.phimtatnhanh.alarm;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        String actionBootCompleted = Intent.ACTION_BOOT_COMPLETED;
        if (actionBootCompleted.equals(intent.getAction())) {
            AlarmHelper.get(context).startAlarm();
        }
    }
}
