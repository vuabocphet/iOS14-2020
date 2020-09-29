package com.developer.phimtatnhanh.setuptouch.dialog;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.developer.phimtatnhanh.R;

import butterknife.ButterKnife;

public abstract class BaseDialog {

    protected Dialog dialog;

    protected abstract Context context();


    protected boolean fullscreen() {
        return false;
    }

    protected boolean cancelable() {
        return true;
    }

    protected int gravity() {
        return Gravity.BOTTOM;
    }

    protected abstract int onLayout();

    public void createDialog() {
        if (dialog == null) {
            dialog = create();
        }
        this.init();
    }

    @SuppressLint("InflateParams")
    private Dialog create() {
        if (context() == null) {
            return null;
        }
        Dialog dialog = new Dialog(context(), R.style.BottomDialog);
        View viewTouchDialog = LayoutInflater.from(context()).inflate(onLayout(), null);
        dialog.setContentView(viewTouchDialog);
        ButterKnife.bind(this, viewTouchDialog);
        Window window = dialog.getWindow();
        int parent = fullscreen() ? WindowManager.LayoutParams.MATCH_PARENT : WindowManager.LayoutParams.WRAP_CONTENT;
        if (window != null) {
            window.setGravity(gravity());
            window.setLayout(parent, parent);
            this.onAttachedToWindow(window);
        }
        dialog.setCancelable(cancelable());
        return dialog;
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

    public void init() {
    }

    public void show() {
        if (this.dialog == null || this.dialog.isShowing()) {
            return;
        }
        this.dialog.show();
    }

    public void cancel() {
        if (this.dialog == null || !this.dialog.isShowing()) {
            return;
        }
        this.dialog.cancel();
    }
}
