package com.developer.phimtatnhanh.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.developer.phimtatnhanh.service.TouchService;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TinhNv", "onReceive: ");
        TouchService.start(context);
        AlarmHelper.get(context).startAlarm();
    }
}
