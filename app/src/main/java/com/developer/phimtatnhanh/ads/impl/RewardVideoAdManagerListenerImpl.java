package com.developer.phimtatnhanh.ads.impl;

import android.content.Context;

import com.developer.phimtatnhanh.ads.AdLogger;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;


public class RewardVideoAdManagerListenerImpl implements RewardedVideoAdListener {

    private static final String PREFIX_LOG = "gad";
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";
    private static final String IMPRESSION = "imp";
    private static final String CLOSE = "close";
    private static final String REWARDED = "rwd";

    private final String adPosition;
    private final Context context;

    public RewardVideoAdManagerListenerImpl(Context context, String adPosition) {
        this.context = context;
        this.adPosition = adPosition;
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        AdLogger.getInstance(this.context).log(getLogEvent(SUCCESS));
    }

    private String getLogEvent(String event) {
        return String.format("%s_%s_%s", PREFIX_LOG, adPosition, event);
    }

    @Override
    public void onRewardedVideoAdOpened() {
        AdLogger.getInstance(this.context).log(getLogEvent(IMPRESSION));
    }

    @Override
    public void onRewardedVideoStarted() {
    }

    @Override
    public void onRewardedVideoAdClosed() {
        AdLogger.getInstance(this.context).log(getLogEvent(CLOSE));
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        AdLogger.getInstance(this.context).log(getLogEvent(REWARDED));
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        AdLogger.getInstance(this.context).log(getLogEvent(FAIL));
    }

    @Override
    public void onRewardedVideoCompleted() {
    }
}
