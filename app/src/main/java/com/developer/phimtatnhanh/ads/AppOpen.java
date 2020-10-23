package com.developer.phimtatnhanh.ads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.developer.phimtatnhanh.ads.util.AdUtil;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.appopen.AppOpenAd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static androidx.lifecycle.Lifecycle.Event.ON_START;

public class AppOpen implements Application.ActivityLifecycleCallbacks, LifecycleObserver {

    @SuppressLint("StaticFieldLeak")
    private static AppOpen appOpen;

    private Application application;

    private AppOpenAd appOpenAds = null;

    private Activity activityCurren = null;

    private List<Class> listActivityNotShowAds;

    private OpenAdsCallBack openAdsCallBack;

    private boolean testDevice = false;

    private boolean autoShowAds = true;

    private boolean iap = false;

    /*ID TEST ADMOB DEFAULT*/
    private String adUnitId = "ca-app-pub-3940256099942544/1033173712";

    private boolean isShowingAd = false;

    private boolean loadAds = true;

    public static void initialize(Application application) {
        if (appOpen == null) {
            appOpen = create(application);
        }
    }

    public static AppOpen get(Application application) {
        if (appOpen == null) {
            appOpen = create(application);
        }
        return appOpen;
    }

    public static AppOpen create(Application application) {
        return new AppOpen(application);
    }

    private AppOpen(Application application) {
        this.application = application;
        this.listActivityNotShowAds = new ArrayList<>();
        this.application.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    public AppOpen setListActivityNotShowAds(Class... listActivityNotShowAds) {
        if (listActivityNotShowAds == null || listActivityNotShowAds.length == 0) {
            return this;
        }
        this.listActivityNotShowAds.addAll(Arrays.asList(listActivityNotShowAds));
        return this;
    }

    public AppOpen setTestDevice(boolean testDevice) {
        this.testDevice = testDevice;
        return this;
    }

    private void setTestDevice() {
        List<String> testDeviceIds = Collections.singletonList(AdUtil.getTestDeviceId(this.application));
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);
    }

    public AppOpen setAdUnitId(String adUnitId) {
        this.adUnitId = adUnitId;
        return this;
    }

    public AppOpen setOpenAdsCallBack(OpenAdsCallBack openAdsCallBack) {
        this.openAdsCallBack = openAdsCallBack;
        return this;
    }

    private AppOpenAd.AppOpenAdLoadCallback appOpenAdLoadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
        @Override
        public void onAppOpenAdLoaded(AppOpenAd appOpenAd) {
            appOpenAds = appOpenAd;
        }

        @Override
        public void onAppOpenAdFailedToLoad(LoadAdError loadAdError) {
            loadAds = false;
            if (openAdsCallBack != null) {
                openAdsCallBack.onAppOpenFailed(loadAdError.getMessage() + "\nCodeError:" + loadAdError.getCode());
            }
        }
    };

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        this.activityCurren = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        this.activityCurren = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        this.activityCurren = null;
    }

    @OnLifecycleEvent(ON_START)
    public void onStart() {
        if (this.isIap()) {
            return;
        }
        if (!this.isAbleShowAds()) {
            this.setAutoShowAds(true);
            return;
        }
        if (this.activityCurren == null || this.isActivityNotShowAds()) {
            return;
        }
        this.showAdsIfAvailable(true);
    }

    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    private void loadAds() {
        if (isAdAvailable() || isIap()) {
            return;
        }
        AdRequest request = getAdRequest();
        AppOpenAd.load(this.application, adUnitId, request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, this.appOpenAdLoadCallback);
    }

    public void buildAds() {
        if (this.testDevice) {
            this.setTestDevice();
        }
        this.loadAds();
    }

    private boolean isActivityNotShowAds() {
        String simpleName = this.activityCurren.getClass().getSimpleName();
        for (Class c : this.listActivityNotShowAds) {
            String simpleName1 = c.getSimpleName();
            if (TextUtils.equals(simpleName, simpleName1)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAdAvailable() {
        return this.appOpenAds != null;
    }

    public boolean isLoadAds() {
        return loadAds;
    }

    public boolean isAbleShowAds() {
        return autoShowAds;
    }

    public AppOpen setAutoShowAds(boolean autoShowAds) {
        this.autoShowAds = autoShowAds;
        return this;
    }

    public boolean isIap() {
        return iap;
    }

    public AppOpen setIap(boolean iap) {
        this.iap = iap;
        return this;
    }

    public boolean showAdsIfAvailable(boolean... isLoadAds) {
        if (!isShowingAd && isAdAvailable() && !isIap()) {
            FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    appOpenAds = null;
                    isShowingAd = false;
                    if (openAdsCallBack != null) {
                        openAdsCallBack.onAppOpenDismiss();
                    }
                    if (isLoadAds == null || isLoadAds.length <= 0 || isLoadAds[0]) {
                        loadAds();
                    }
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    isShowingAd = true;
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    if (openAdsCallBack != null) {
                        openAdsCallBack.onAppOpenFailed(adError.getMessage() + "\nCodeError:" + adError.getCode());
                    }
                }
            };
            this.appOpenAds.show(this.activityCurren, fullScreenContentCallback);
            return true;
        }
        return false;
    }

    public interface OpenAdsCallBack {
        void onAppOpenDismiss();

        void onAppOpenFailed(String message);
    }

}
