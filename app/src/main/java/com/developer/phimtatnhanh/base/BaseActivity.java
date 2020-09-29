package com.developer.phimtatnhanh.base;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.developer.phimtatnhanh.setuptouch.utilities.permission.ConfigPer;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity implements ConfigPer {

    private Unbinder unbinder;

    protected Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        if (fullScreen()) {
            this.setupFullScreen();
        }
        this.unbinder = ButterKnife.bind(this);
        this.init();
        this.initViews();
    }

    private void setupFullScreen() {
        Window window = getWindow();
        if (window == null) {
            return;
        }
        if (Build.VERSION.SDK_INT < 30) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            return;
        }
        WindowInsetsController controller = window.getInsetsController();
        if (controller != null) {
            controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        }

    }

    protected abstract int initLayout();

    protected boolean fullScreen() {
        return false;
    }

    protected void init() {

    }

    protected void initViews() {

    }

    @Override
    protected void onDestroy() {
        if (this.unbinder != null) {
            this.unbinder.unbind();
        }
        super.onDestroy();
    }

    protected void startActivity(Class<BaseActivity> aClass, boolean finish, Bundle... bundle) {
        Intent intent = new Intent(this, aClass);
        if (bundle != null && bundle.length > 0) {
            intent.putExtra(DATA_BUNDEL, bundle[0]);
        }
        this.startActivity(intent);
        if (finish) {
            this.finish();
        }
    }

}
