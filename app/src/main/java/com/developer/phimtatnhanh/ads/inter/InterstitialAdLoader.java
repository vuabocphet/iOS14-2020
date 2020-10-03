package com.developer.phimtatnhanh.ads.inter;

import android.content.Context;

import com.developer.phimtatnhanh.ads.AdListenerImp;
import com.developer.phimtatnhanh.ads.config.ConfigLoader;
import com.developer.phimtatnhanh.ads.config.ConfigStrategy;
import com.developer.phimtatnhanh.ads.inter.cachetime.AdRequestCapping;
import com.developer.phimtatnhanh.ads.inter.cachetime.Capping;
import com.developer.phimtatnhanh.ads.util.AdUtil;
import com.developer.phimtatnhanh.util.DeviceUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;


import java.util.Calendar;


public class InterstitialAdLoader {

    private static InterstitialAdLoader instance;

    private final InterstitialAd interstitialAd;
    private final Context context;
    private String adPosition;
    private boolean isAdShowed;
    private boolean isUserClickAd;
    private String liveKey;
    private AdListener adListener;

    public interface SkipAdListener {
        void onSkip();
    }

    private SkipAdListener skipAdListener;

    public InterstitialAdLoader setAdListener(AdListener adListener) {
        this.adListener = adListener;
        return this;
    }

    public InterstitialAdLoader setSkipAdListener(SkipAdListener skipAdListener) {
        this.skipAdListener = skipAdListener;
        return this;
    }

    public InterstitialAdLoader setLiveKey(String liveKey) {
        this.liveKey = liveKey;
        return this;
    }

    public static InterstitialAdLoader init(Context context, String adUnit) {
        if (instance == null) {
            instance = new InterstitialAdLoader(context, adUnit);
        }
        return instance;
    }

    public static InterstitialAdLoader get() {
        return instance;
    }

    public void reload() {
        isAdShowed = false;
        isUserClickAd = false;
    }

    private InterstitialAdLoader(Context context, String adUnitId) {
        this.context = context;
        this.interstitialAd = new InterstitialAd(context);
        this.interstitialAd.setAdUnitId(adUnitId);
    }

    public InterstitialAdLoader setAdPosition(String adPosition) {
        this.adPosition = adPosition;
        return this;
    }


    public void show() {
        this.isAdShowed = true;
        this.interstitialAd.show();
    }

    public boolean isAdLoaded() {
        return this.interstitialAd != null && this.interstitialAd.isLoaded();
    }

    public InterstitialAdLoader load() {
        ConfigStrategy config = new ConfigLoader(context).getConfig();
        if (!config.isLivePlacement(liveKey) || !DeviceUtils.isConnected(context)) {
            return this;
        }

        AdRequestCapping periodicJob = AdRequestCapping.create(new Capping(context, adPosition,
                config.getAdCacheTime(), Calendar.MINUTE) {
            @Override
            protected void task() {
                if (interstitialAd == null) {
                    return;
                }
                loadAd(adListener);
            }

            @Override
            protected void skip() {
                if (skipAdListener != null) {
                    skipAdListener.onSkip();
                }
            }
        });
        periodicJob.schedule();
        return this;
    }

    private void loadAd(AdListener adListener) {
        interstitialAd.setAdListener(new AdListenerImp(context, adPosition) {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (adListener != null) {
                    adListener.onAdLoaded();
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                if (adListener != null) {
                    adListener.onAdFailedToLoad(i);
                }
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                isUserClickAd = true;
                if (adListener != null) {
                    adListener.onAdClicked();
                }
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                if (adListener != null) {
                    adListener.onAdImpression();
                }
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if (adListener != null) {
                    adListener.onAdClosed();
                }
            }
        });
        this.interstitialAd.loadAd(AdUtil.getAdRequestBuilderWithTestDevice(context).build());
    }

    public boolean isUserClickAd() {
        return isUserClickAd;
    }

    public boolean isAdShowed() {
        return isAdShowed;
    }

    public void reset() {
        this.isAdShowed = false;
    }

}
