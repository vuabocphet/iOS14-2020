package com.developer.phimtatnhanh.ads.config;

import android.content.Context;

import com.developer.phimtatnhanh.BuildConfig;
import com.developer.phimtatnhanh.ads.util.AdPrefs;

public class AppRemoteConfig implements ConfigStrategy {

    private static final String GAD = "gad";
    private static volatile AppRemoteConfig instance;

    private AppRemoteConfig() {
    }

    public static AppRemoteConfig init() {
        if (instance == null) {
            synchronized (AppRemoteConfig.class) {
                if (instance == null) {
                    instance = new AppRemoteConfig();
                }
            }
        }
        return instance;
    }


    public AppRemoteConfig(Context context) {
    }

    @Override
    public boolean isLiveAdMob(String key, boolean defaultValue) {
        return AdPrefs.getBoolean(key, defaultValue);
    }

    @Override
    public boolean isLiveAdMob(String key) {
        return isLiveAdMob(GAD + "_" + key, true);
    }

    @Override
    public boolean isLivePlacement(String key) {
        return isLivePlacement(key, true);
    }

    @Override
    public boolean isLivePlacement(String key, boolean defaultValue) {
        return AdPrefs.getBoolean(key, defaultValue);
    }

    @Override
    public int getAdCacheTime() {
        return AdPrefs.getInt(ConfigConstant.AD_CACHE_TIME, BuildConfig.DEBUG ? 0 : 5);
    }
}
