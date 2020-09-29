package com.developer.phimtatnhanh.setuptouch.dialog;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.data.Pref;
import com.developer.phimtatnhanh.data.PrefUtil;
import com.developer.phimtatnhanh.setuptouch.utilities.SaveBitmapUtil;
import com.developer.phimtatnhanh.util.DeviceUtils;
import com.github.florent37.viewanimator.ViewAnimator;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.WINDOW_SERVICE;

public class ReviewScreenDialog {


    @BindView(R.id.iv_review_screen)
    AppCompatImageView ivReviewScreen;

    @BindView(R.id.cs_layout)
    ConstraintLayout csLayout;

    @OnClick(R.id.iv_review_screen)
    public void open(View view) {
        view.setClickable(false);
        cancelReviewScreen();
        SaveBitmapUtil.sendMessenger(view.getContext(), this.file);
    }

    private Bitmap bitmap;
    private Context context;
    private Dialog dialog;
    private WindowManager windowManager;
    private File file;

    public void setFile(File file) {
        this.file = file;
    }

    public static ReviewScreenDialog init(Context context, Bitmap bitmap, String path) {
        return new ReviewScreenDialog(context, bitmap, path);
    }

    private ReviewScreenDialog(Context context, Bitmap bitmap, String path) {
        this.bitmap = bitmap;
        this.context = context;
        this.windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        this.file = new File(path);
        this.dialog = create();

    }

    @SuppressLint("InflateParams")
    private Dialog create() {
        if (context == null) {
            return null;
        }
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View viewTouchDialog = LayoutInflater.from(context).inflate(R.layout.review_screen_touch, null);
        dialog.setContentView(viewTouchDialog);
        ButterKnife.bind(this, viewTouchDialog);
        ViewGroup.MarginLayoutParams paramsOpenTouch = (ViewGroup.MarginLayoutParams) viewTouchDialog.getLayoutParams();
        paramsOpenTouch.width = ViewGroup.LayoutParams.MATCH_PARENT;
        paramsOpenTouch.height = ViewGroup.LayoutParams.MATCH_PARENT;
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
            this.onAttachedToWindow(window);
        }
        dialog.setCancelable(false);
        viewTouchDialog.setLayoutParams(paramsOpenTouch);
        this.bindView();
        return dialog;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void bindView() {
        if (bitmap == null) {
            this.dialog.cancel();
        }
        try {
            Glide.with(this.context).load(bitmap).addListener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    ConstraintSet set = new ConstraintSet();
                    set.clone(csLayout);
                    ChangeBounds transition = new ChangeBounds();
                    transition.setDuration(500);
                    transition.addListener(new Transition.TransitionListener() {
                        @Override
                        public void onTransitionStart(Transition transition) {

                        }

                        @Override
                        public void onTransitionEnd(Transition transition) {
                            ViewAnimator
                                    .animate(ivReviewScreen)
                                    .translationX(-12f, 12f)
                                    .repeatMode(ViewAnimator.REVERSE)
                                    .repeatCount(10)
                                    .duration(300)
                                    .onStop(() -> ViewAnimator
                                            .animate(ivReviewScreen).translationX(-1000).duration(200).onStop(ReviewScreenDialog.this::cancelReviewScreen).start())
                                    .start();
                        }

                        @Override
                        public void onTransitionCancel(Transition transition) {

                        }

                        @Override
                        public void onTransitionPause(Transition transition) {

                        }

                        @Override
                        public void onTransitionResume(Transition transition) {

                        }
                    });
                    TransitionManager.beginDelayedTransition(csLayout, transition);
                    int anInt = PrefUtil.get().getInt(Pref.SIZE_REVIEW_CAPTURE_SCREEN, 4);
                    set.constrainWidth(R.id.iv_review_screen, DeviceUtils.w(windowManager) / anInt);
                    set.constrainHeight(R.id.iv_review_screen, DeviceUtils.h(windowManager) / anInt);
                    set.applyTo(csLayout);
                    return false;
                }
            }).into(this.ivReviewScreen);
        } catch (Exception e) {
            e.printStackTrace();
            cancelReviewScreen();
        }
    }

    public void showReviewScreen() {
        if (this.dialog == null || this.dialog.isShowing()) {
            return;
        }
        this.dialog.show();
    }

    public void cancelReviewScreen() {
        if (this.dialog == null || !this.dialog.isShowing()) {
            return;
        }
        if (this.bitmap != null) {
            this.bitmap.recycle();
        }
        this.dialog.cancel();
    }

    private void onAttachedToWindow(Window window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        }
        window.setType(LAYOUT_FLAG);
    }

}

