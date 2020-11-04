package com.developer.phimtatnhanh.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class AlarmHelper {

    private static final String TAG = "Tinh-nv";

    private Context context;

    public static AlarmHelper get(Context context) {
        return new AlarmHelper(context);
    }

    public AlarmHelper(Context context) {
        this.context = context;
    }

    public void startAlarm() {
        this.cancelAlarm();
        try {
            AlarmManager alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this.context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelAlarm() {
        try {
            AlarmManager alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this.context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 1, intent, 0);
            alarmManager.cancel(pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
