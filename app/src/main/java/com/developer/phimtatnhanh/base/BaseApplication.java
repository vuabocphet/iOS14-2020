package com.developer.phimtatnhanh.base;


import android.app.Application;
import android.content.Context;

import com.developer.phimtatnhanh.di.component.ApplicationComponent;
import com.developer.phimtatnhanh.di.component.DaggerApplicationComponent;
import com.developer.phimtatnhanh.di.module.ApplicationModule;

public class BaseApplication extends Application {

    private ApplicationComponent mApplicationComponent;

    public static BaseApplication get(Context context) {
        return (BaseApplication) context.getApplicationContext();
    }

    public ApplicationComponent getApplicationComponent() {
        return this.mApplicationComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.mApplicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();

        this.mApplicationComponent.inject(this);
    }

}
