package com.developer.phimtatnhanh.ui.ram;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.developer.memory.junk.RamMaster;
import com.developer.phimtatnhanh.BuildConfig;
import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.ads.AppLogEvent;
import com.developer.phimtatnhanh.ads.InterAds;
import com.developer.phimtatnhanh.ads.NativeAdLoader;
import com.developer.phimtatnhanh.ads.NativeAdView;
import com.developer.phimtatnhanh.base.BaseActivity;
import com.developer.phimtatnhanh.data.PrefUtil;
import com.developer.phimtatnhanh.delayclickview.PostDelayClick;
import com.developer.phimtatnhanh.di.component.ActivityComponent;
import com.developer.phimtatnhanh.setuptouch.gridviewstarapp.AppUtil;
import com.developer.phimtatnhanh.ui.cleanjunk.CleanJunkActivity;
import com.developer.phimtatnhanh.ui.junk.JunkActivity;
import com.developer.phimtatnhanh.util.loadiconapp.GlideApp;
import com.developer.phimtatnhanh.view.AppTextViewFont;
import com.github.florent37.viewanimator.ViewAnimator;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tencent.mmkv.MMKV;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.developer.phimtatnhanh.ads.UnitID.NT_AD;
import static com.developer.phimtatnhanh.ads.UnitID.NT_AD_KEY_4;
import static com.developer.phimtatnhanh.ads.UnitID.NT_AD_LIVE;

public class RamActivity extends BaseActivity implements HandlerScanApp.ObserveHandlerScanApp {

    @BindView(R.id.iv_app)
    RoundedImageView ivApp;

    @BindView(R.id.lottieAnimationView4)
    LottieAnimationView animationView;
    @BindView(R.id.tv_position)
    AppTextViewFont tvPosition;
    @BindView(R.id.tv_title)
    AppTextViewFont tvTitle;
    @BindView(R.id.tv_name_app)
    AppTextViewFont tvNameApp;
    @BindView(R.id.nativeAdView)
    NativeAdView nativeAdView;
    @BindView(R.id.lottieAnimationHappy)
    LottieAnimationView lottieAnimationHappy;
    @BindView(R.id.lottieAnimationRipper)
    LottieAnimationView lottieAnimationRipper;
    @BindView(R.id.clear_complete)
    RelativeLayout clearComplete;

    private HandlerScanApp handlerScanApp;
    private PackageManager packageManager;

    public static void open(Context context, boolean newClear) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, RamActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("NEWCLEAR", newClear);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_ram;
    }

    @Override
    protected void injectDagger(@Nullable ActivityComponent activityComponent) {
        if (activityComponent != null) {
            activityComponent.inject(this);
        }
    }

    @Override
    protected boolean fullScreen() {
        return true;
    }

    @Override
    protected void init() {
        MMKV.initialize(this);
        PrefUtil.initialize();
        AppLogEvent.initialize(this);
        InterAds.initialize(this);
        InterAds.get().reload();
        AppLogEvent.getInstance().log("RamActivity_show");
        this.handlerScanApp = new HandlerScanApp();
        this.handlerScanApp.setObserveHandlerScanApp(this);
        this.packageManager = getApplicationContext().getPackageManager();
    }

    @Override
    protected void initViews() {
        this.loadAd();
        boolean newclear = getIntent().getBooleanExtra("NEWCLEAR", false);
        this.clearComplete.setOnClickListener(view -> {
            PostDelayClick.get().postDelayViewClick(view);
            this.finish();
        });
        this.tvPosition.setFont(R.font.russoone_regular);
        this.tvTitle.setFont(R.font.thin_ios);
        this.tvNameApp.setFont(R.font.thin_ios);

        Log.e("TinhNv", "initViews: "+newclear );

        List<String> list = RamMaster.printForegroundTask(this);
        Log.e("TinhNv", "initViews: "+list.size());

        if (newclear) {
            clearComplete.setVisibility(View.VISIBLE);
            this.complete();
            return;
        }

        this.handlerScanApp.setList(RamMaster.printForegroundTask(this));
        this.animationView.setSpeed(BuildConfig.DEBUG ? 2f : 1f);
        this.animationView.addAnimatorListener(this.handlerScanApp.getAnimatorListener());
        this.animationView.playAnimation();
        this.lottieAnimationHappy.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                clearComplete.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onProgressHandlerScanApp(String packnameApp, String count) {
        this.tvPosition.setText(count);
        try {
            String appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(packnameApp, PackageManager.GET_META_DATA));
            this.tvNameApp.setText(appName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        GlideApp.with(RamActivity.this)
                .load(AppUtil.init().getApplication(RamActivity.this.getPackageManager(), packnameApp).applicationInfo)
                .into(ivApp);
        try {
            RamMaster.killPackageProcesses(this, packnameApp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompleteHandlerScanApp() {
        AppLogEvent.getInstance().log("RamActivity_complete");
        PrefUtil.get().postLong(CACHE_RAM, System.currentTimeMillis());
        this.complete();
    }

    private void complete() {
        this.animationView.cancelAnimation();
        this.lottieAnimationRipper.cancelAnimation();
        ViewAnimator.animate(this.animationView, this.lottieAnimationRipper).scale(1f, 0f).duration(500)
                .onStop(() -> {
                    this.lottieAnimationHappy.setVisibility(View.VISIBLE);
                    this.animationView.setVisibility(View.INVISIBLE);
                    this.ivApp.setVisibility(View.INVISIBLE);
                    this.tvTitle.setText(getString(R.string.junk_happy));
                    this.tvPosition.setVisibility(View.GONE);
                    this.tvNameApp.setVisibility(View.INVISIBLE);
                    this.lottieAnimationRipper.setVisibility(View.GONE);
                    ViewAnimator.animate(this.lottieAnimationHappy).scale(0f, 1f).duration(500)
                            .onStop(() -> lottieAnimationHappy.playAnimation())
                            .start();
                }).start();
    }

    private void loadAd() {
        NativeAdLoader.newInstance().setAdPosition(NT_AD)
                .setAdUnit(NT_AD_KEY_4)
                .setLiveKey(NT_AD_LIVE)
                .setOnAdLoaderListener(new NativeAdLoader.NativeAdLoaderListener() {
                    @Override
                    public void onAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        super.onAdLoaded(unifiedNativeAd);
                        if (nativeAdView != null) {
                            nativeAdView.show(unifiedNativeAd);
                        }
                    }

                    @Override
                    public void onAdLoadFailed(String message) {
                        super.onAdLoadFailed(message);
                       /* if (nativeView != null) {
                            nativeView.setVisibility(View.GONE);
                        }*/
                    }
                }).loadAd(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}