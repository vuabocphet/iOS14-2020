package com.developer.phimtatnhanh.ads;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Size;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class AdLogger {
    private static final String TYPE = "type";
    private static final String CHANNEL = "channel";
    private static final String NAME = "name";
    private static final String EVENT = "event";
    private static final String ID = "id";
    private static final String MESSAGE = "message";
    private static volatile AdLogger adLogger;
    private FirebaseAnalytics firebaseAnalytics;
    private boolean debugLog = true;

    private AdLogger(Context context) {
        this.firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public static AdLogger getInstance(Context context) {
        if (adLogger == null) {
            adLogger = new AdLogger(context);
        }

        return adLogger;
    }

    public void log(@NonNull @Size(min = 1L, max = 32L) String eventName) {
        this.log(eventName, "");
    }

    public void log(Exception exception) {
        if (debugLog) {
            exception.printStackTrace();
        }
    }

    public void setDebugLog(boolean debugLog) {
        this.debugLog = true;
    }

    public void log(@NonNull @Size(min = 1L, max = 32L) String eventName, @NonNull String message) {
        Bundle bundle = new Bundle();
        String msg = eventName;
        if (!TextUtils.isEmpty(message)) {
            msg = eventName + " | " + message;
            bundle.putString(MESSAGE, message);
        }

        Log.i("AdLogger", msg);
        this.log(eventName, bundle);
    }

    public void log(@NonNull String type, @NonNull String channel, @NonNull String name, @NonNull String event, @NonNull String id) {
        this.log(type, channel, name, event, id, "");
    }

    public void log(@NonNull String type, @NonNull String channel, @NonNull String name, @NonNull String event, @NonNull String id, @NonNull String message) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(TYPE, type);
            bundle.putString(CHANNEL, channel);
            bundle.putString(NAME, name);
            bundle.putString(EVENT, event);
            bundle.putString(ID, id);
            bundle.putString(MESSAGE, message);
            this.log("ads", bundle);
        } catch (Exception var8) {
            var8.printStackTrace();
        }

    }

    public void log(@NonNull @Size(min = 1L, max = 32L) String eventName, @NonNull Bundle bundle) {
        try {
            long time = System.currentTimeMillis();
            bundle.putLong("time_long", time);
            bundle.putString("time_string", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())).format(new Date(time)));
            this.firebaseAnalytics.logEvent(eventName, bundle);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }
}