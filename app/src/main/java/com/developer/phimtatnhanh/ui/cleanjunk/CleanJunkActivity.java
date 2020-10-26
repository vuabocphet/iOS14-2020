package com.developer.phimtatnhanh.ui.cleanjunk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.airbnb.lottie.LottieAnimationView;
import com.daasuu.cat.CountAnimationTextView;
import com.developer.memory.junk.model.JunkInfo;
import com.developer.memory.junk.util.CleanUtil;
import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.base.BaseActivity;
import com.developer.phimtatnhanh.di.component.ActivityComponent;
import com.developer.phimtatnhanh.ui.junk.JunkActivity;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

import static com.developer.memory.junk.util.CleanUtil.formatShortFileSize;

public class CleanJunkActivity extends BaseActivity implements CleanUtil.CleanCallBack {

    @BindView(R.id.lottieAnimationView2)
    LottieAnimationView lottie;
    @BindView(R.id.lottieAnimationView3)
    LottieAnimationView lottie1;
    @BindView(R.id.count_animation_textView)
    AppCompatTextView countAnimationTextView;
    private Handler handler;

    private ArrayList<JunkInfo> junkInfos;
    private long sizeJunk = 0L;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, CleanJunkActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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
        this.junkInfos = new ArrayList<>();
        this.junkInfos.addAll(JunkActivity.getListJunkAll());
        this.sizeJunk = JunkActivity.getSizeJunk();
        this.handler = new Handler(Looper.getMainLooper());
        this.disposable = CleanUtil.freeJunkInfos(this, this.junkInfos == null ? new ArrayList<>() : this.junkInfos, this);
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
        this.handler.post(() -> {
            if (this.countAnimationTextView != null) {
                this.countAnimationTextView.setText(size);
            }
        });
    }

    @Override
    public void onError(Throwable throwable) {

    }

}