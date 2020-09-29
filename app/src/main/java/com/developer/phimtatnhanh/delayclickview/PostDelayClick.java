package com.developer.phimtatnhanh.delayclickview;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

public class PostDelayClick {

    private Handler handler;

    private static final int TIMER_DELAY = 200;

    public PostDelayClick() {
        if (this.handler == null) {
            this.handler = new Handler(Looper.getMainLooper());
        }
    }

    public static PostDelayClick get() {
        return new PostDelayClick();
    }

    public void postDelayViewClick(View view) {
        if (view == null || !view.isClickable()) {
            return;
        }
        view.setClickable(false);
        Runnable runnable = () -> {
            try {
                view.setClickable(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        this.handler.postDelayed(runnable, TIMER_DELAY);
    }
}
