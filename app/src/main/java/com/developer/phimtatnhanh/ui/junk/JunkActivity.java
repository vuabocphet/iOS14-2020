package com.developer.phimtatnhanh.ui.junk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.developer.phimtatnhanh.ads.NativeAdLoader;
import com.developer.phimtatnhanh.ads.NativeAdView;
import com.developer.phimtatnhanh.base.BaseActivity;
import com.developer.phimtatnhanh.di.component.ActivityComponent;
import com.developer.phimtatnhanh.ui.junk.viewjunk.ViewListJunk;
import com.github.florent37.viewanimator.ViewAnimator;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

import static com.developer.phimtatnhanh.ads.UnitID.NT_AD;
import static com.developer.phimtatnhanh.ads.UnitID.NT_AD_KEY;
import static com.developer.phimtatnhanh.ads.UnitID.NT_AD_LIVE;

public class JunkActivity extends BaseActivity implements IScanCallback, CleanUtil.CleanCallBack {

    @BindView(R.id.native_view)
    NativeAdView nativeView;
    @BindView(R.id.lottie)
    LottieAnimationView lottie;
    @BindView(R.id.tv_preview)
    AppCompatTextView tvPreview;
    @BindView(R.id.cs_layout_all)
    ConstraintLayout csLayoutAll;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.tv_title_bottom)
    AppCompatTextView tvTitleBottom;
    @BindView(R.id.clear_all)
    RelativeLayout clearAll;
    @BindView(R.id.junk_cache)
    ViewListJunk junkCache;
    @BindView(R.id.junk_apk)
    ViewListJunk junkApk;
    @BindView(R.id.junk_tmp)
    ViewListJunk junkTmp;
    @BindView(R.id.junk_log)
    ViewListJunk junkLog;
    @BindView(R.id.junk_other)
    ViewListJunk junkOther;

    private Disposable disposableClean;
    private HashMap<Object, Object> mJunkGroups;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, JunkActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private Handler handler;

    private JunkScanRx junkScanRx;

    private long sizeJunk = 0L;

    private long sizeJunkCache = 0L;

    private long sizeJunkApk = 0L;

    private long sizeJunkLog = 0L;

    private long sizeJunkTmp = 0L;

    private long sizeJunkOther = 0L;


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
        this.loadAd();
        this.mJunkGroups = new HashMap<>();
        this.handler = new Handler(Looper.getMainLooper());
        this.junkScanRx = new JunkScanRx(this, this);

        JunkGroup cacheGroup = new JunkGroup();
        cacheGroup.mName = getString(R.string.cache_clean);
        cacheGroup.mChildren = new ArrayList<>();
        this.junkCache.setTvName(cacheGroup.mName);
        mJunkGroups.put(JunkGroup.GROUP_CACHE, cacheGroup);

        JunkGroup apkGroup = new JunkGroup();
        apkGroup.mName = getString(R.string.apk_clean);
        apkGroup.mChildren = new ArrayList<>();
        this.junkApk.setTvName(apkGroup.mName);
        mJunkGroups.put(JunkGroup.GROUP_APK, apkGroup);

        JunkGroup tmpGroup = new JunkGroup();
        tmpGroup.mName = getString(R.string.tmp_clean);
        tmpGroup.mChildren = new ArrayList<>();
        this.junkTmp.setTvName(tmpGroup.mName);
        mJunkGroups.put(JunkGroup.GROUP_TMP, tmpGroup);

        JunkGroup logGroup = new JunkGroup();
        logGroup.mName = getString(R.string.log_clean);
        logGroup.mChildren = new ArrayList<>();
        this.junkLog.setTvName(logGroup.mName);
        mJunkGroups.put(JunkGroup.GROUP_LOG, logGroup);

        JunkGroup otherGroup = new JunkGroup();
        otherGroup.mName = getString(R.string.other_clean);
        otherGroup.mChildren = new ArrayList<>();
        this.junkOther.setTvName(otherGroup.mName);
        mJunkGroups.put(JunkGroup.GROUP_OTHER, otherGroup);

        this.handler.postDelayed(() -> this.junkScanRx.startScanJunkRx(), 1000);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected boolean fullScreen() {
        return true;
    }

    @Override
    public void onStartJunk() {
        if (this.lottie != null) {
            this.lottie.playAnimation();
        }
        this.handler.post(() -> this.tvPreview.setText(CleanUtil.formatShortFileSize(this, this.sizeJunk)));
    }

    @Override
    public void onStopJunk() {
        if (this.lottie != null) {
            this.lottie.setRepeatCount(0);
        }
    }

    @Override
    public void onProgress(JunkInfo info) {
        this.sizeJunk += info.mSize;
        if (this.handler == null) {
            return;
        }
        this.handler.post(() -> {
            if (this.tvPreview == null || this.tvTitleBottom == null) {
                return;
            }
            this.tvPreview.setText(CleanUtil.formatShortFileSize(this, this.sizeJunk));
            this.tvTitleBottom.setText(info.mPackageName);
        });
    }

    @Override
    public void onProgressCache(JunkInfo info) {
        this.sizeJunkCache += info.mSize;
        setProgressJunk(this.junkCache, this.sizeJunkCache);
    }

    @Override
    public void onProgressApk(JunkInfo info) {
        this.sizeJunkApk += info.mSize;
        setProgressJunk(this.junkApk, this.sizeJunkApk);
    }

    @Override
    public void onProgressTmp(JunkInfo info) {
        this.sizeJunkTmp += info.mSize;
        setProgressJunk(this.junkTmp, this.sizeJunkTmp);
    }

    @Override
    public void onProgressLog(JunkInfo info) {
        this.sizeJunkLog += info.mSize;
        setProgressJunk(this.junkLog, this.sizeJunkLog);
    }

    @Override
    public void onProgressOther(JunkInfo info) {
        this.sizeJunkOther += info.mSize;
        setProgressJunk(this.junkOther, this.sizeJunkOther);
    }

    private void setProgressJunk(ViewListJunk junkView, long sizeJunk) {
        if (this.handler == null) {
            return;
        }
        this.handler.post(() -> {
            if (junkView == null) {
                return;
            }
            junkView.setTvSize(CleanUtil.formatShortFileSize(this, sizeJunk));
        });
    }

    @Override
    public void onFinish(ArrayList<JunkInfo> junkInfos, ArrayList<JunkInfo> sysCaches) {
        Log.e("TinhNv", "onFinish: ");
        if (this.handler != null) {
            this.handler.post(() -> {
                if (this.tvTitle == null || this.tvTitleBottom == null) {
                    return;
                }
                this.tvTitle.setText(getString(R.string.clean_junk_finish));
                this.clearAll.setVisibility(View.VISIBLE);
                ViewAnimator.animate(this.tvTitleBottom).scale(1f, 0f).duration(1000).start().onStop(() -> this.tvTitleBottom.setVisibility(View.GONE));
                ViewAnimator.animate(this.clearAll).bounceIn().slideBottomIn().onStop(() -> ViewAnimator.animate(this.clearAll).pulse().duration(1000).repeatCount(ViewAnimator.INFINITE).repeatMode(ViewAnimator.REVERSE).start()).duration(1000).start();
            });
        }
        this.clearAll.setOnClickListener(view -> this.disposableClean = CleanUtil.freeJunkInfos(this, junkInfos, sysCaches, this));
    }

    @Override
    public void onErrorJunk(Throwable e) {
        Toast.makeText(this, getString(R.string.clean_junk_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.junkScanRx != null) {
            this.junkScanRx.stopScanJunkRx();
        }
        if (this.handler != null) {
            this.handler.removeCallbacksAndMessages(null);
        }
        if (this.disposableClean != null) {
            this.disposableClean.dispose();
        }
    }

    private void loadAd() {
        NativeAdLoader.newInstance().setAdPosition(NT_AD)
                .setAdUnit(NT_AD_KEY)
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
    public void onComplete() {

    }

    @Override
    public void onProgressClean(JunkInfo info) {
        this.sizeJunk -= info.mSize;
        if (this.handler == null) {
            return;
        }
        this.handler.post(() -> {
            if (this.tvPreview == null || this.tvTitleBottom == null) {
                return;
            }
            this.tvPreview.setText(CleanUtil.formatShortFileSize(this, this.sizeJunk));
            this.tvTitleBottom.setText(info.mPackageName);
        });
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}