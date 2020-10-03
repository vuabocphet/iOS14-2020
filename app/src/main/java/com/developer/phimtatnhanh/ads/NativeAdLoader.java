package com.developer.phimtatnhanh.ads;


import android.content.Context;

import com.developer.phimtatnhanh.ads.config.ConfigLoader;
import com.developer.phimtatnhanh.ads.config.ConfigStrategy;
import com.developer.phimtatnhanh.ads.util.AdUtil;
import com.developer.phimtatnhanh.util.DeviceUtils;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

public class NativeAdLoader {

    public abstract static class NativeAdLoaderListener {
        public void onAdLoaded(UnifiedNativeAd unifiedNativeAd) {
        }

        public void onAdClicked() {
        }

        public void onAdLoadFailed(String message) {
        }
    }

    private NativeAdLoaderListener onAdLoaderListener;
    private String adPosition;
    private String adUnit;
    private String liveKey;

    public NativeAdLoader setAdPosition(String adPosition) {
        this.adPosition = adPosition;
        return this;
    }

    public NativeAdLoader setOnAdLoaderListener(NativeAdLoaderListener onAdLoaderListener) {
        this.onAdLoaderListener = onAdLoaderListener;
        return this;
    }

    public NativeAdLoader setLiveKey(String liveKey) {
        this.liveKey = liveKey;
        return this;
    }

    public NativeAdLoader setAdUnit(String adUnit) {
        this.adUnit = adUnit;
        return this;
    }

    public static NativeAdLoader newInstance() {
        return new NativeAdLoader();
    }

    public AdLoader configAd(Context context) {
        ConfigStrategy config = new ConfigLoader(context).getConfig();
        if (!DeviceUtils.isConnected(context)) {
            onAdLoadFailed("Not connect to internet!");
            return null;
        }
        if (!config.isLiveAdMob(liveKey) || !config.isLivePlacement(liveKey)) {
            onAdLoadFailed("Ad is turn off!");
            return null;
        }

        com.google.android.gms.ads.AdLoader.Builder builder = new com.google.android.gms.ads.AdLoader.Builder(context, adUnit)
                .forUnifiedNativeAd(unifiedNativeAd -> {
                    if (onAdLoaderListener != null) {
                        onAdLoaderListener.onAdLoaded(unifiedNativeAd);
                    }
                });
        return builder.withAdListener(new AdListenerImp(context, adPosition) {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                onAdLoadFailed("Error with code = " + i);
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                if (onAdLoaderListener != null) {
                    onAdLoaderListener.onAdClicked();
                }
            }
        }).build();
    }

    public void loadAd(Context context) {
        AdLoader adLoader = configAd(context);
        if (adLoader == null) {
            onAdLoadFailed("AdLoader null");
            return;
        }
        adLoader.loadAd(AdUtil.getAdRequestBuilderWithTestDevice(context).build());
    }

    private void onAdLoadFailed(String message) {
        if (this.onAdLoaderListener != null) {
            this.onAdLoaderListener.onAdLoadFailed(message);
        }
    }
}
