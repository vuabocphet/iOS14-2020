package com.developer.phimtatnhanh.ui;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.constraintlayout.widget.Group;

import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.ads.InterAds;
import com.developer.phimtatnhanh.base.BaseActivity;
import com.developer.phimtatnhanh.ui.home.HomeActivity;
import com.github.florent37.viewanimator.ViewAnimator;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class Screen extends BaseActivity {

    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.textView3)
    TextView textView3;
    private boolean isRunning = false;
    private Disposable disposable;

    private int i = 0;

    @Override
    protected int initLayout() {
        return R.layout.activity_screen;
    }

    @Override
    protected boolean fullScreen() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.isRunning = true;
        if (this.i >= 100) {
            InterAds.get().showScreen();
        }
    }

    private void skip() {
        startActivity(new Intent(Screen.this, HomeActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.isRunning = false;
    }

    @Override
    protected void initViews() {
        InterAds.get().reload();
        InterAds.get().setAdCallBack(this::skip);
        ViewAnimator.animate(this.textView2,this.textView3).slideTopIn().duration(500).onStop(this::start).start();
    }

    private void start() {
        this.disposable = Observable.interval(100, TimeUnit.MILLISECONDS).subscribe(aLong -> {
            this.i++;
            Log.d("TinhNv", "initViews: " + i);
            if (this.isRunning) {
                if (this.i <= 60) {
                    runOnUiThread(() -> {
                        boolean b = InterAds.get().showScreen();
                        if (b) {
                            this.stop();
                        }
                    });
                    return;
                }
                this.stop();
                this.skip();
                return;
            }
            if (this.i == 100) {
                this.stop();
            }
        }, throwable -> Log.d("TinhNv", "initViews: " + throwable.toString()));
    }

    private void stop() {
        if (this.disposable != null) {
            this.disposable.dispose();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}