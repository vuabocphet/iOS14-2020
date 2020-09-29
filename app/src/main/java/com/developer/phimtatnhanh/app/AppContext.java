package com.developer.phimtatnhanh.app;

import android.annotation.SuppressLint;
import android.content.Context;

public class AppContext {

    private Context context;

    @SuppressLint("StaticFieldLeak")
    private static AppContext appContext;

    public static void create(Context context) {
        if (AppContext.appContext == null || AppContext.appContext.getContext() == null) {
            AppContext.appContext = new AppContext(context);
        }
    }

    public AppContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return this.context;
    }

    public static AppContext get() {
        return AppContext.appContext;
    }
}
