package com.developer.phimtatnhanh.ui.cleanjunk;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.daasuu.cat.CountAnimationTextView;
import com.developer.memory.junk.model.JunkInfo;
import com.developer.memory.junk.util.CleanUtil;
import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.ads.AppLogEvent;
import com.developer.phimtatnhanh.ads.InterAds;
import com.developer.phimtatnhanh.base.BaseActivity;
import com.developer.phimtatnhanh.data.PrefUtil;
import com.developer.phimtatnhanh.delayclickview.PostDelayClick;
import com.developer.phimtatnhanh.di.component.ActivityComponent;
import com.developer.phimtatnhanh.ui.junk.JunkActivity;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.tencent.mmkv.MMKV;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

import static com.developer.memory.junk.util.CleanUtil.REQUEST_CODE_FOR_PERMISSION;
import static com.developer.memory.junk.util.CleanUtil.formatShortFileSize;

public class CleanJunkActivity extends BaseActivity implements CleanUtil.CleanCallBack {

    @BindView(R.id.lottieAnimationView2)
    LottieAnimationView lottie;
    @BindView(R.id.lottieAnimationView3)
    LottieAnimationView lottie1;
    @BindView(R.id.count_animation_textView)
    AppCompatTextView countAnimationTextView;
    @BindView(R.id.clear_complete)
    RelativeLayout clearComplete;
    @BindView(R.id.tv_happy)
    AppCompatTextView tvHappy;
    @BindView(R.id.iv_happy)
    AppCompatImageView ivHappy;
    private Handler handler;

    private ArrayList<JunkInfo> junkInfos;
    private long sizeJunk = 0L;

    public static void open(Context context, boolean newTask) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, CleanJunkActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        if (newTask) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    private Disposable disposable;

    @Override
    protected int initLayout() {
        return R.layout.activity_clean_junk;
    }


    @Override
    protected boolean fullScreen() {
        return true;
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
        this.clearComplete.setOnClickListener(view -> {
            PostDelayClick.get().postDelayViewClick(view);
            this.finish();
        });
        AppLogEvent.getInstance().log("CleanJunkActivity show");
        this.junkInfos = new ArrayList<>();
        this.junkInfos.addAll(JunkActivity.getListJunkAll() == null ? new ArrayList<>() : JunkActivity.getListJunkAll());
        this.sizeJunk = JunkActivity.getSizeJunk();
        this.handler = new Handler(Looper.getMainLooper());
        this.disposable = CleanUtil.freeJunkInfos(this, this.junkInfos == null ? new ArrayList<>() : this.junkInfos, this);
        this.lottie1.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (clearComplete == null || tvHappy == null || ivHappy == null) {
                    return;
                }
                Glide.with(CleanJunkActivity.this).asGif().load(R.drawable.happygif).into(ivHappy);
                clearComplete.setVisibility(View.VISIBLE);
                tvHappy.setVisibility(View.VISIBLE);
                ViewAnimator.animate(clearComplete)
                        .slideBottomIn()
                        .duration(500)
                        .start();
                ViewAnimator.animate(tvHappy)
                        .slideTopIn()
                        .duration(500)
                        .start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (this.disposable != null) {
            this.disposable.dispose();
            this.disposable = null;
        }
        if (this.handler != null) {
            this.handler.removeCallbacksAndMessages(null);
        }
        JunkActivity.cleanStatic();
        super.onDestroy();
    }

    @Override
    public void onComplete() {
        AppLogEvent.getInstance().log("CleanJunkActivity onComplete");
        if (this.junkInfos != null && !this.junkInfos.isEmpty()) {
            Log.i("TinhNv", "onComplete: post CACHE_JUNK");
            PrefUtil.get().postLong(JunkActivity.CACHE_JUNK, System.currentTimeMillis());
        } else {
            Log.i("TinhNv", "onComplete: post not CACHE_JUNK");
        }
        Log.i("TinhNv", "onComplete: ");
        if (this.handler != null) {
            this.handler.post(() -> {
                if (this.lottie == null) {
                    return;
                }
                ViewAnimator.animate(this.lottie)
                        .alpha(1f, 0f)
                        .duration(500)
                        .onStop(() -> {
                            if (this.lottie1 == null || this.lottie == null) {
                                return;
                            }
                            this.lottie.setVisibility(View.GONE);
                            this.lottie1.setVisibility(View.VISIBLE);
                            ViewAnimator.animate(this.lottie1)
                                    .scale(0f, 1f)
                                    .duration(500)
                                    .onStop(() -> {
                                        if (this.lottie1 == null) {
                                            return;
                                        }
                                        this.lottie1.playAnimation();
                                    })
                                    .start();
                        })
                        .start();
            });
        }
    }

    @Override
    public void onProgressClean(JunkInfo junkInfo) {
        this.sizeJunk = this.sizeJunk - junkInfo.mSize;
        String size = formatShortFileSize(this, this.sizeJunk);
        Log.i("TinhNv", "onProgressClean: " + junkInfo.name);
        this.handler.post(() -> {
            if (this.countAnimationTextView != null) {
                this.countAnimationTextView.setText(size);
            }
        });
    }

    @Override
    public void onError(Throwable throwable) {
        Log.i("TinhNv", "onError: " + throwable.toString());
    }
}