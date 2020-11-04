package com.developer.phimtatnhanh.ui.junk;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.developer.memory.junk.JunkScanRx;
import com.developer.memory.junk.callback.IScanCallback;
import com.developer.memory.junk.model.JunkGroup;
import com.developer.memory.junk.model.JunkInfo;
import com.developer.memory.junk.util.CleanUtil;
import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.ads.AppLogEvent;
import com.developer.phimtatnhanh.ads.InterAds;
import com.developer.phimtatnhanh.ads.NativeAdLoader;
import com.developer.phimtatnhanh.ads.NativeAdView;
import com.developer.phimtatnhanh.base.BaseActivity;
import com.developer.phimtatnhanh.data.PrefUtil;
import com.developer.phimtatnhanh.delayclickview.PostDelayClick;
import com.developer.phimtatnhanh.di.component.ActivityComponent;
import com.developer.phimtatnhanh.ui.cleanjunk.CleanJunkActivity;
import com.github.florent37.viewanimator.ViewAnimator;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.developer.phimtatnhanh.ads.UnitID.NT_AD;
import static com.developer.phimtatnhanh.ads.UnitID.NT_AD_KEY_2;
import static com.developer.phimtatnhanh.ads.UnitID.NT_AD_LIVE;

public class JunkActivity extends BaseActivity implements IScanCallback {

    @BindView(R.id.native_view)
    NativeAdView nativeView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.lottieAnimationView)
    LottieAnimationView lottieAnimationView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_scan)
    AppCompatTextView tvScan;
    @BindView(R.id.tv_scan_size)
    AppCompatTextView tvScanSize;
    @BindView(R.id.clear_all)
    RelativeLayout clearAll;
    @BindView(R.id.cs_layout_all)
    ConstraintLayout csLayoutAll;
    @BindView(R.id.tv_content)
    AppCompatTextView tvContent;

    private boolean checkNullView() {
        return isView(this.clearAll,
                this.csLayoutAll,
                this.lottieAnimationView,
                this.nativeView,
                this.progressBar,
                this.tvContent,
                this.tvScan,
                this.tvScanSize,
                this.tvTitle);
    }

    private boolean isView(View... views) {
        if (views == null || views.length == 0) {
            return true;
        }
        for (View view : views) {
            if (view == null) {
                return true;
            }
        }
        return false;
    }

    @OnClick(R.id.close)
    public void close(View view) {
        this.isStartActivity = false;
        PostDelayClick.get().postDelayViewClick(view);
        AppLogEvent.getInstance().log("JunkActivity_finish");
        this.finish();
    }

    @OnClick(R.id.clear_all)
    public void clean(View view) {
        PostDelayClick.get().postDelayViewClick(view);
        AppLogEvent.getInstance().log("JunkActivity_clean");
        this.isStartActivity = true;
        InterAds.get().show(() -> {
            CleanJunkActivity.open(this, false);
            this.finish();
        });
    }

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, JunkActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private Handler handler;

    private JunkScanRx junkScanRx;

    private static long sizeJunk = 0L;

    private static ArrayList<JunkInfo> listJunkAll;

    private boolean onBack = true;

    private boolean isStartActivity = false;


    public static long getSizeJunk() {
        return sizeJunk;
    }

    public static ArrayList<JunkInfo> getListJunkAll() {
        return listJunkAll;
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_junk;
    }

    @Override
    protected void injectDagger(@Nullable ActivityComponent activityComponent) {
        if (activityComponent != null) {
            activityComponent.inject(this);
        }
    }

    @Override
    protected void init() {
        MMKV.initialize(this);
        PrefUtil.initialize();
        AppLogEvent.initialize(this);
        InterAds.initialize(this);
        InterAds.get().reload();
        AppLogEvent.getInstance().log("JunkActivity_show");
        this.loadAd();
        if (listJunkAll != null) {
            listJunkAll.clear();
            listJunkAll = null;
        }
        listJunkAll = new ArrayList<>();
        this.handler = new Handler(Looper.getMainLooper());
        this.junkScanRx = new JunkScanRx(this, this);
        this.handler.postDelayed(() -> {
            if (this.junkScanRx == null) {
                return;
            }
            this.junkScanRx.startScanJunkRx();
        }, 1000);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected boolean fullScreen() {
        return true;
    }

    @Override
    public void onCreateJunk(int typeScanJunk) {
        this.onBack = false;
        if (this.handler == null) {
            return;
        }
        this.handler.post(() -> {
            if (checkNullView()) return;
            progressBar.setVisibility(View.VISIBLE);
            tvTitle.setText(getString(R.string.dangkhoitao));
        });
    }

    @Override
    public void onStartJunk(int typeScanJunk) {
        this.onBack = false;
        if (this.handler == null) {
            return;
        }
        if (typeScanJunk == JunkGroup.GROUP_CACHE) {
            this.handler.post(() -> {
                if (checkNullView()) return;
                this.progressBar.setVisibility(View.GONE);
                this.lottieAnimationView.setVisibility(View.VISIBLE);
                this.tvScan.setVisibility(View.VISIBLE);
                this.tvScanSize.setVisibility(View.VISIBLE);
                this.lottieAnimationView.playAnimation();
            });
        }
    }

    @Override
    public void onCompleteJunk(int typeScanJunk) {

    }

    @Override
    public void onStopJunk() {

    }

    @Override
    public void onProgressCache(JunkInfo info) {
        sizeJunk += info.mSize;
        this.setProgressJunk(sizeJunk, info);
    }

    @Override
    public void onProgressApk(JunkInfo info) {
        sizeJunk += info.mSize;
        this.setProgressJunk(sizeJunk, info);
    }

    @Override
    public void onProgressTmp(JunkInfo info) {
        sizeJunk += info.mSize;
        this.setProgressJunk(sizeJunk, info);
    }

    @Override
    public void onProgressLog(JunkInfo info) {
        sizeJunk += info.mSize;
        this.setProgressJunk(sizeJunk, info);
    }

    @Override
    public void onProgressOther(JunkInfo info) {
        sizeJunk += info.mSize;
        setProgressJunk(sizeJunk, info);
    }

    private void setProgressJunk(long sizeJunk, JunkInfo info) {
        if (this.handler == null) {
            return;
        }
        this.handler.post(() -> {
            if (checkNullView()) return;
            this.tvScanSize.setText(CleanUtil.formatShortFileSize(this, sizeJunk));
            this.tvTitle.setText(TextUtils.isEmpty(info.name) ? "" : info.name);
        });
    }

    @Override
    public void onFinish() {
        this.onBack = true;
        if (this.handler != null) {
            this.handler.post(() -> {
                if (checkNullView()) return;
                listJunkAll.addAll(this.junkScanRx.getmApkInfo().mChildren);
                listJunkAll.addAll(this.junkScanRx.getmCacheInfo().mChildren);
                listJunkAll.addAll(this.junkScanRx.getmOtherInfo().mChildren);
                listJunkAll.addAll(this.junkScanRx.getmTmpInfo().mChildren);
                listJunkAll.addAll(this.junkScanRx.getmLogInfo().mChildren);
                this.tvContent.setText(getString(R.string.daquetxong));
                ViewAnimator.animate(this.lottieAnimationView).scale(1f, 0f).duration(500).onStart(() -> {
                }).onStop(() -> {
                    if (checkNullView()) return;
                    this.lottieAnimationView.pauseAnimation();
                    this.lottieAnimationView.setAnimation("s9.json");
                    ViewAnimator.animate(this.lottieAnimationView).scale(0f, 1f).duration(500).onStop(() -> {
                        if (checkNullView()) return;
                        this.lottieAnimationView.playAnimation();
                    }).start();
                }).start();
                TransitionManager.beginDelayedTransition(this.csLayoutAll);
                this.clearAll.setVisibility(View.VISIBLE);
                this.tvTitle.setVisibility(View.GONE);
                this.tvContent.setTextColor(getResources().getColor(R.color.red));
                ViewAnimator.animate(this.clearAll).slideBottomIn().duration(500).start();
            });
        }

    }

    @Override
    public void onErrorJunk(Throwable e) {
        Log.i("TinhNv", "onErrorJunk: " + e.toString());
    }

    @Override
    protected void onDestroy() {

        if (!this.isStartActivity) {
            cleanStatic();
        }

        if (this.junkScanRx != null) {
            this.junkScanRx.stopScanJunkRx();
        }
        if (this.handler != null) {
            this.handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }

    public static void cleanStatic() {
        listJunkAll = null;
        sizeJunk = 0L;
    }

    private void loadAd() {
        NativeAdLoader.newInstance().setAdPosition(NT_AD)
                .setAdUnit(NT_AD_KEY_2)
                .setLiveKey(NT_AD_LIVE)
                .setOnAdLoaderListener(new NativeAdLoader.NativeAdLoaderListener() {
                    @Override
                    public void onAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        super.onAdLoaded(unifiedNativeAd);
                        if (nativeView != null) {
                            nativeView.show(unifiedNativeAd);
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
    public void onBackPressed() {
        if (this.onBack) {
            super.onBackPressed();
        }
    }
}