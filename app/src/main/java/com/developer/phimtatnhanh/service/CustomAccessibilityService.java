package com.developer.phimtatnhanh.service;


import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

import com.developer.phimtatnhanh.setuptouch.utilities.ObservableTouchUtil;

public class CustomAccessibilityService extends AccessibilityService implements ObservableTouchUtil.KeyServiceListener {


    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        ObservableTouchUtil.get().setKeyServiceListener(this);
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void backKey() {
        performGlobalAction(GLOBAL_ACTION_BACK);
    }

    @Override
    public void homeKey() {
        performGlobalAction(GLOBAL_ACTION_HOME);
    }

    @Override
    public void recentKey() {
        performGlobalAction(GLOBAL_ACTION_RECENTS);
    }

    @Override
    public void settingKey() {
        performGlobalAction(GLOBAL_ACTION_QUICK_SETTINGS);
    }

    @Override
    public void powerDialog() {
        performGlobalAction(GLOBAL_ACTION_POWER_DIALOG);
    }

    @Override
    public void notificationKey() {
        performGlobalAction(GLOBAL_ACTION_NOTIFICATIONS);
    }

    @Override
    public void onServicTouchStop() {
        TouchService.start(this);
    }

    @Override
    public void onLockScreen() {
        performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN);
    }
}
