package com.developer.phimtatnhanh.ui;


import android.content.Intent;

import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.base.BaseActivity;
import com.developer.phimtatnhanh.ui.home.HomeActivity;

import java.util.Timer;
import java.util.TimerTask;

public class Screen extends BaseActivity {

    @Override
    protected int initLayout() {
        return R.layout.activity_screen;
    }

    @Override
    protected boolean fullScreen() {
        return true;
    }

    @Override
    protected void initViews() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(Screen.this, HomeActivity.class));
                finish();
            }
        }, 2000);
    }
}