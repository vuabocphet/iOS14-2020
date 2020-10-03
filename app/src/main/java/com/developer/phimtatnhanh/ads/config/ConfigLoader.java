package com.developer.phimtatnhanh.ads.config;

import android.content.Context;

public class ConfigLoader {
    private final ConfigStrategy configStrategy;

    public ConfigLoader(Context context) {
        this.configStrategy = new AppRemoteConfig(context);
    }

    public ConfigStrategy getConfig() {
        return configStrategy;
    }
}
