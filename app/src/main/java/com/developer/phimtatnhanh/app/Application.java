package com.developer.phimtatnhanh.app;


import android.os.Build;
import android.util.Log;

import com.developer.phimtatnhanh.BuildConfig;
import com.developer.phimtatnhanh.ads.AppLogEvent;
import com.developer.phimtatnhanh.ads.AppOpen;
import com.developer.phimtatnhanh.ads.InterAds;
import com.developer.phimtatnhanh.ads.UnitID;
import com.developer.phimtatnhanh.ads.util.AdPrefs;
import com.developer.phimtatnhanh.base.BaseApplication;
import com.developer.phimtatnhanh.data.PrefUtil;
import com.developer.phimtatnhanh.ui.Screen;
import com.developer.phimtatnhanh.ui.junk.JunkActivity;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.tencent.mmkv.MMKV;

import java.util.Collections;
import java.util.List;

public class Application extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        AppContext.create(getApplicationContext());
        MMKV.initialize(this);
        PrefUtil.init();
        AppLogEvent.initialize(getApplicationContext());
        AdPrefs.initialize(getApplicationContext());
        MobileAds.initialize(getApplicationContext());

        List<String> testDeviceIds = Collections.singletonList("69F6E77E43D5FD63F0E6FA2FAA558B48");

        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();

        MobileAds.setRequestConfiguration(configuration);

        InterAds.initialize(getApplicationContext());

        AppOpen.create(this)
                .setAdUnitId(UnitID.APP_OPEN)
                .setAutoShowAds(BuildConfig.DEBUG)
                .setListActivityNotShowAds(Screen.class, JunkActivity.class)
                .setOpenAdsCallBack(new AppOpen.OpenAdsCallBack() {
                    @Override
                    public void onAppOpenDismiss() {
                        Log.i("TinhNv", "onAppOpenDismiss: ");
                    }

                    @Override
                    public void onAppOpenFailed(String message) {
                        Log.i("TinhNv", "onAppOpenFailed: " + message);
                    }
                }).buildAds();
    }
}
