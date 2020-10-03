package com.developer.phimtatnhanh.ads;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Size;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppLogEvent {

    private static final String TAG = "AppLogEvent";

    private static AppLogEvent instance;
    private FirebaseAnalytics mFirebaseAnalytics;

    private AppLogEvent(Context context) {
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }


    public void log(@NonNull @Size(min = 1L, max = 32L) String eventName, Bundle eventBundle) {
        eventName = eventName.toUpperCase(Locale.US);

        Log.d(TAG, "log: " + eventName);
        if (eventBundle != null) {
            this.mFirebaseAnalytics.logEvent(eventName, eventBundle);
            return;
        }
        this.mFirebaseAnalytics.logEvent(eventName, null);
    }

    public void log(@NonNull @Size(min = 1L, max = 32L) String eventName) {
        eventName = eventName.toUpperCase(Locale.US);

        Log.d(TAG, "log: " + eventName);
        Bundle bundle = new Bundle();
        long time = System.currentTimeMillis();
        bundle.putLong("time_long", time);
        bundle.putString("time_string", new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault()).format(new Date(time)));
        this.mFirebaseAnalytics.logEvent(eventName, null);
    }

    public void log(@NonNull @Size(min = 1L, max = 32L) String eventName, @NonNull String message) {
        eventName = eventName.toUpperCase(Locale.US);

        Log.d(TAG, "log: " + eventName + " | message=" + message);

        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        this.mFirebaseAnalytics.logEvent(eventName, null);
    }


    public static AppLogEvent getInstance() {
        if (instance == null) {
            throw new NullPointerException("Please initTranslator AppLogEvent on App Application!");
        }
        return instance;
    }

    public static AppLogEvent initialize(Context context) {
        if (instance == null) {
            instance = new AppLogEvent(context);
        }
        return instance;
    }
}
