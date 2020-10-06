package com.developer.phimtatnhanh.ads;

import android.annotation.SuppressLint;
import android.content.Context;

import com.developer.phimtatnhanh.ads.util.AdUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;


public class InterAds implements UnitID {

    @SuppressLint("StaticFieldLeak")
    private static InterAds instance;

    private InterstitialAd mInterstitialAd;

    private AdReceiver adReceiver;

    private Context context;

    public static void initialize(Context context) {
        if (instance == null) {
            instance = new InterAds(context);
        }
    }

    public static InterAds get() {
        return instance;
    }

    public InterAds(Context context) {
        this.context = context;
        this.mInterstitialAd = new InterstitialAd(context);
        this.mInterstitialAd.setAdUnitId(IT_AD_KEY);
        AdListener adListener = new AdListener() {

            @Override
            public void onAdLoaded() {
                AppLogEvent.getInstance().log("ADS_LOAD_IT");
            }

            @Override
            public void onAdOpened() {
                AppLogEvent.getInstance().log("ADS_OPEN_IT");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                AppLogEvent.getInstance().log("ADS_LOAD_FAILED_IT");
            }

            @Override
            public void onAdClosed() {
                if (adReceiver != null) {
                    adReceiver.onAdReceiver();
                }
                AppLogEvent.getInstance().log("ADS_CLOSE_IT");
            }

            @Override
            public void onAdClicked() {
                AppLogEvent.getInstance().log("ADS_CLICK_IT");
            }
        };
        this.mInterstitialAd.setAdListener(adListener);
    }

    public void loadAds() {
        if (this.mInterstitialAd == null) {
            return;
        }
        this.mInterstitialAd.loadAd(AdUtil.getAdRequestBuilderWithTestDevice(this.context).build());
    }

    public void reload() {
        if (this.mInterstitialAd == null || this.mInterstitialAd.isLoaded()) {
            return;
        }
        this.loadAds();
    }

    public void show(AdReceiver adReceiver) {
        this.setAdCallBack(adReceiver);
        if (this.adReceiver == null) {
            return;
        }
        if (this.mInterstitialAd == null || !this.mInterstitialAd.isLoaded()) {
            this.adReceiver.onAdReceiver();
            return;
        }
        this.mInterstitialAd.show();
    }

    public void setAdCallBack(AdReceiver adReceiver) {
        this.adReceiver = adReceiver;
    }

    public boolean showScreen() {
        if (this.adReceiver == null) {
            return false;
        }
        if (this.mInterstitialAd == null || !this.mInterstitialAd.isLoaded()) {
            return false;
        }
        this.mInterstitialAd.show();
        return true;
    }

    public interface AdReceiver {
        void onAdReceiver();
    }
}
