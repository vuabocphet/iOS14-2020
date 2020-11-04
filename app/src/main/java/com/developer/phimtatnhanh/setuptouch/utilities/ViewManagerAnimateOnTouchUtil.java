package com.developer.phimtatnhanh.setuptouch.utilities;

/*
 * Create by Nguyễn Tình
 * */


import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.developer.phimtatnhanh.app.AppContext;
import com.developer.phimtatnhanh.data.Pref;
import com.developer.phimtatnhanh.data.PrefUtil;
import com.developer.phimtatnhanh.setuptouch.config.ConfigAll;
import com.developer.phimtatnhanh.setuptouch.listen.EvenClick;
import com.developer.phimtatnhanh.setuptouch.utilities.bus.EvenBusCaptureVideoUpdateTouch;
import com.developer.phimtatnhanh.setuptouch.utilities.capturescreen.RxScreenCapture;
import com.developer.phimtatnhanh.util.DeviceUtils;
import com.github.florent37.viewanimator.ViewAnimator;

public class ViewManagerAnimateOnTouchUtil implements View.OnTouchListener, ConfigAll {

    private ViewManagerUtil viewManagerUtil;
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    public int widthScreen;
    private int paramsY;
    private Handler handler;
    private Runnable runnable;
    private OrientationBroadcastReceiver orientationBR;
    private GestureDetector gestureDetector;
    private TypeClick typeClick;
    private EvenClick evenClick;
    private Vibrator vibrator;

    public void setEvenClick(EvenClick evenClick) {
        this.evenClick = evenClick;
    }

    private enum TypeClick {
        LONGCLICK,
        DOUBLECLICK,
        SINGLECLICK
    }

    public static ViewManagerAnimateOnTouchUtil init(ViewManagerUtil viewManagerUtil) {
        return new ViewManagerAnimateOnTouchUtil(viewManagerUtil);
    }

    public ViewManagerAnimateOnTouchUtil(@Nullable ViewManagerUtil viewManagerUtil) {
        if (viewManagerUtil == null || viewManagerUtil.getView() == null || viewManagerUtil.getView().getContext() == null) {
            return;
        }
        AppContext.create(viewManagerUtil.getView().getContext());
        this.viewManagerUtil = viewManagerUtil;
        this.orientationBR = new OrientationBroadcastReceiver();
        this.vibrator = (Vibrator) AppContext.get().getContext().getSystemService(Context.VIBRATOR_SERVICE);
        IntentFilter orientationIF = new IntentFilter(Intent.ACTION_CONFIGURATION_CHANGED);
        AppContext.get().getContext().registerReceiver(orientationBR, orientationIF);
        this.gestureDetector = new GestureDetector(AppContext.get().getContext(), new GestureListener());
        this.handler = new Handler(Looper.getMainLooper());
        this.widthScreen = PrefUtil.get().getInt(Pref.W);
        this.viewManagerUtil.getView().setOnTouchListener(this);
        this.runnable = () -> {
            if (this.viewManagerUtil == null) {
                return;
            }
            View view = this.viewManagerUtil.getView();
            if (view == null) {
                return;
            }
            ViewAnimator.animate(view).alpha(1f, alphaViewTouch).duration(250).onStop(
                    () -> {
                        try {
                            view.setAlpha(alphaViewTouch);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            ).start();
        };
        this.alphaTouch();
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            typeClick = TypeClick.SINGLECLICK;
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            int anInt = PrefUtil.get().getInt(Pref.KEY_CLICK_SINGLE);
            if (anInt == 4) {
                return false;
            }
            typeClick = TypeClick.SINGLECLICK;
            if (evenClick != null) {
                evenClick.onSingleClick();
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            int anInt = PrefUtil.get().getInt(Pref.KEY_CLICK_SINGLE);
            if (anInt == 4) {
                return false;
            }
            typeClick = TypeClick.DOUBLECLICK;
            if (evenClick != null) {
                evenClick.onDoubleClick();
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            typeClick = TypeClick.LONGCLICK;
            if (vibrator != null && vibrator.hasVibrator()) {
                vibrator.vibrate(durationVibrator);
            }
            if (evenClick != null) {
                evenClick.onLongClick();
            }
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (this.viewManagerUtil != null && this.viewManagerUtil.lifeCaptureVideo == EvenBusCaptureVideoUpdateTouch.LifeCaptureVideo.STOP) {
            this.gestureDetector.onTouchEvent(event);
        }

        boolean isToolLock = PrefUtil.get().getBool(Pref.TOUCH_LOCK, false);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return actionDown(v, event);
            case MotionEvent.ACTION_UP:
                return actionUp(v, event, isToolLock);
            case MotionEvent.ACTION_MOVE:
                return actionMove(event, isToolLock);
        }
        return false;
    }

    //region onTouch
    private boolean actionMove(MotionEvent event, boolean isToolLock) {
        if (isToolLock) {
            return false;
        }
        int xDiffx = Math.round(event.getRawX() - initialTouchX);
        int yDiffx = Math.round(event.getRawY() - initialTouchY);
        this.viewManagerUtil.getLayoutParams().x = initialX + xDiffx;
        this.viewManagerUtil.getLayoutParams().y = initialY + yDiffx;
        this.paramsY = initialY + yDiffx;
        this.viewManagerUtil.getWindowManager().updateViewLayout(this.viewManagerUtil.getView(), this.viewManagerUtil.getLayoutParams());
        this.viewManagerUtil.getView().setAlpha(1f);
        this.stopHanderAlpha();
        return true;
    }

    private boolean actionDown(View v, MotionEvent event) {
        v.setScaleX(v.getScaleX() - 0.029f);
        v.setScaleY(v.getScaleY() - 0.029f);
        initialX = this.viewManagerUtil.getLayoutParams().x;
        initialY = this.viewManagerUtil.getLayoutParams().y;
        initialTouchX = event.getRawX();
        initialTouchY = event.getRawY();
        this.viewManagerUtil.getView().setAlpha(1f);
        stopHanderAlpha();
        return true;
    }

    private boolean actionUp(View v, MotionEvent event, boolean isToolLock) {
        v.setScaleX(v.getScaleX() + 0.029f);
        v.setScaleY(v.getScaleY() + 0.029f);
        float xDiff = event.getRawX() - initialTouchX;
        float yDiff = event.getRawY() - initialTouchY;
        float abs = Math.abs(xDiff);
        float abs1 = Math.abs(yDiff);
        if ((abs < 5) && (abs1 < 5) && this.typeClick != TypeClick.LONGCLICK) {
            if (viewManagerUtil != null && viewManagerUtil.lifeCaptureVideo != EvenBusCaptureVideoUpdateTouch.LifeCaptureVideo.STOP) {
                RxScreenCapture.get().stopCaptureVideo();
                return true;
            }
            int anInt = PrefUtil.get().getInt(Pref.KEY_CLICK_SINGLE);
            if (anInt == 4) {
                if (evenClick != null) {
                    evenClick.onSingleClick();
                }
            }
            return true;
        }
        if (isToolLock) {
            this.alphaTouch();
            return false;
        }
        this.updateFloatingTouchView();
        return true;
    }
    //endregion

    public void updateFloatingTouchView(boolean... isAlpha) {
        int width = this.viewManagerUtil.getView().getMeasuredWidth();
        int widthRest = this.widthScreen - width;
        final int halfWidthScreen = widthRest / 2;
        if (this.viewManagerUtil.getLayoutParams().x >= halfWidthScreen) {
            this.updateAnimateLocation(widthRest);
        } else {
            this.updateAnimateLocation(0);
        }
        if (isAlpha != null && isAlpha.length > 0) {
            return;
        }
        this.alphaTouch();
    }

    public void alphaTouch() {
        if (this.handler == null) {
            return;
        }
        this.handler.postDelayed(this.runnable, durationAlphaViewTouch);
    }

    public void cleanAll() {
        AppContext.get().getContext().unregisterReceiver(orientationBR);
        if (this.handler == null) {
            return;
        }
        this.stopHanderAlpha();
        this.handler.removeCallbacksAndMessages(null);
    }

    private void stopHanderAlpha() {
        if (this.handler == null) {
            return;
        }
        this.handler.removeCallbacks(this.runnable);
    }

    private void updateAnimateLocation(int i) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(this.viewManagerUtil.getLayoutParams().x, i);
        valueAnimator.addUpdateListener(it -> {
            this.viewManagerUtil.getLayoutParams().x = (int) it.getAnimatedValue();
            this.viewManagerUtil.getLayoutParams().y = paramsY;
            PrefUtil.get().postInt(Pref.X, this.viewManagerUtil.getLayoutParams().x);
            PrefUtil.get().postInt(Pref.Y, this.viewManagerUtil.getLayoutParams().y);
            PrefUtil.get().postInt(Pref.G, this.viewManagerUtil.getLayoutParams().gravity);
            this.viewManagerUtil.getWindowManager().updateViewLayout(this.viewManagerUtil.getView(), this.viewManagerUtil.getLayoutParams());
        });
        valueAnimator.setDuration(durationUpdateLocationViewTouch);
        valueAnimator.start();
    }

    public class OrientationBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int configOrientation = context.getResources().getConfiguration().orientation;
            if (configOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                PrefUtil.get().postInt(Pref.W, DeviceUtils.h(viewManagerUtil.getWindowManager()));
                PrefUtil.get().postInt(Pref.H, DeviceUtils.w(viewManagerUtil.getWindowManager()));
                widthScreen = PrefUtil.get().getInt(Pref.H);
                updateFloatingTouchView();
                return;
            }
            PrefUtil.get().postInt(Pref.W, DeviceUtils.w(viewManagerUtil.getWindowManager()));
            PrefUtil.get().postInt(Pref.H, DeviceUtils.h(viewManagerUtil.getWindowManager()));
            widthScreen = PrefUtil.get().getInt(Pref.W);
            updateFloatingTouchView();
        }
    }

}
