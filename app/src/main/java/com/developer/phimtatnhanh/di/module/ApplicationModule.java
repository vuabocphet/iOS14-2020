package com.developer.phimtatnhanh.di.module;


import com.developer.phimtatnhanh.base.BaseApplication;
import com.developer.phimtatnhanh.data.ListUtils;
import com.developer.phimtatnhanh.data.PrefUtil;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final BaseApplication mBaseApplication;

    @Inject
    public ApplicationModule(BaseApplication baseApplication) {
        this.mBaseApplication = baseApplication;
    }

    @Provides
    @Singleton
    public BaseApplication provideApplication() {
        return mBaseApplication;
    }

    @Provides
    @Singleton
    public PrefUtil prefUtil() {
        return new PrefUtil();
    }

    @Provides
    @Singleton
    public ListUtils listUtils() {
        return new ListUtils(mBaseApplication);
    }

}