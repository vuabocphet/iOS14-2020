package com.developer.phimtatnhanh.ads.inter.cachetime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class Capping extends Job {

    private int countTime = 0, calendarUnit = Calendar.MINUTE;

    public Capping(Context context, String jobId, Integer time, Integer calendarUnit) {
        super(context, jobId);
        if (time != null) {
            this.countTime = time;
        }
        if (calendarUnit != null) {
            this.calendarUnit = calendarUnit;
        }
    }

    @Override
    public void execute() {
        if (this.mustExecute()) {
            Log.i(this.tag(), "[" + this.preferenceJobKey + "] Execute Job...");
            this.scheduleNext();
            this.task();
            return;
        }
        this.skip();
        Log.e(this.tag(), "[" + this.preferenceJobKey + "] Skip Job Execute...");

    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public boolean mustExecute() {
        long currentTime = System.currentTimeMillis();
        long nextTime = this.preferences.getLong(this.preferenceJobKey, 0);
        Log.i(this.tag(), "Next time: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(nextTime)));
        return currentTime > nextTime;
    }

    @Override
    public void scheduleNext() {
        this.editor.putLong(this.preferenceJobKey, this.calculateNextTime()).apply();
    }

    @Override
    protected String prefix() {
        return "capping";
    }

    @Override
    protected String tag() {
        return "AdRequestCapping";
    }

    private long calculateNextTime() {
        long currentTimeMillis = System.currentTimeMillis();
        switch (this.calendarUnit) {
            case Calendar.SECOND:
                return addSeconds(currentTimeMillis, this.countTime);
            case Calendar.MINUTE:
                return addMinutes(currentTimeMillis, this.countTime);
            case Calendar.HOUR:
                return addHours(currentTimeMillis, this.countTime);
            case Calendar.DAY_OF_MONTH:
                return addDays(currentTimeMillis, this.countTime);
            default:
                throw new RuntimeException("Invalid Schedule Timer, only: Calendar.SECOND/MINUTE/HOUR/DAY_OF_MONTH");
        }
    }

    private long addDays(long time, final int amount) {

        return time + (amount * 24 * 60 * 60 * 1000);
    }

    private long addHours(long time, final int amount) {
        return time + (amount * 60 * 60 * 1000);
    }

    private long addMinutes(long time, final int amount) {
        return time + (amount * 60 * 1000);
    }

    private long addSeconds(long time, final int amount) {

        return time + (amount * 1000);
    }


}
