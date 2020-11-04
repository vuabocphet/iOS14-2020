package com.developer.phimtatnhanh.ui.ram;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class HandlerScanApp {

    private Animator.AnimatorListener animatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationRepeat(Animator animation) {
            animationRepeat();
        }

        @Override
        public void onAnimationStart(Animator animation) {
            animationRepeat();
        }
    };
    private int count = 0;
    private List<String> list = new ArrayList<>();
    private ObserveHandlerScanApp observeHandlerScanApp;

    public HandlerScanApp setObserveHandlerScanApp(ObserveHandlerScanApp observeHandlerScanApp) {
        this.observeHandlerScanApp = observeHandlerScanApp;
        return this;
    }

    public Animator.AnimatorListener getAnimatorListener() {
        return animatorListener;
    }

    public HandlerScanApp setList(List<String> list) {
        this.list = list;
        return this;
    }

    public float getTimeDelay() {
        return 0.1f;
    }

    private void animationRepeat() {
        if (this.list == null || this.list.isEmpty()){
            if (this.observeHandlerScanApp != null) {
                this.observeHandlerScanApp.onCompleteHandlerScanApp();
            }
            return;
        }
        if (this.count == list.size() - 1) {
            if (this.observeHandlerScanApp != null) {
                this.observeHandlerScanApp.onProgressHandlerScanApp(this.list.get(this.count), (this.count + 1) + "/" + this.list.size());
                this.observeHandlerScanApp.onCompleteHandlerScanApp();
            }
            return;
        }
        if (this.observeHandlerScanApp != null) {
            this.observeHandlerScanApp.onProgressHandlerScanApp(this.list.get(this.count), (this.count + 1) + "/" + this.list.size());
        }
        this.count++;
    }

    public interface ObserveHandlerScanApp {

        void onProgressHandlerScanApp(String packnameApp, String count);

        void onCompleteHandlerScanApp();
    }
}
