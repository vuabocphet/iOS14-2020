package com.developer.phimtatnhanh.ads;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;


public class AdListenerImp extends AdListener {

    private static final String SUCCESS = "success";
    private static final String IMPRESS = "impress";
    private static final String CLICK = "click";
    private static final String DISMISS = "dismiss";
    private static final String ERROR = "error";

    private final String adPlacement;
    private final Context context;

    public AdListenerImp(Context context, @NonNull String adPlacement) {
        super();
        this.adPlacement = adPlacement;
        this.context = context;
    }

    @Override
    public void onAdClosed() {
        super.onAdClosed();
        if (adPlacement.startsWith(Consts.IT)) {
            logEvent(adPlacement, DISMISS);
        }
    }

    private void logEvent(String adPlacement, String event) {
        AdLogger.getInstance(context).log("gad_" + adPlacement + "_" + event);
    }

    @Override
    public void onAdFailedToLoad(int i) {
        super.onAdFailedToLoad(i);
        logEvent(adPlacement, ERROR);
    }

    @Override
    public void onAdLeftApplication() {
        super.onAdLeftApplication();
    }

    @Override
    public void onAdOpened() {
        super.onAdOpened();
        if (adPlacement.startsWith(Consts.IT)) {
            logEvent(adPlacement, IMPRESS);
        }
    }

    @Override
    public void onAdLoaded() {
        super.onAdLoaded();
        logEvent(adPlacement, SUCCESS);
    }

    @Override
    public void onAdClicked() {
        super.onAdClicked();
        logEvent(adPlacement, CLICK);
    }

    @Override
    public void onAdImpression() {
        super.onAdImpression();
        logEvent(adPlacement, IMPRESS);
    }
}
