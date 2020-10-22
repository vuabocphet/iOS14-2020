package com.developer.phimtatnhanh.ui.garelly;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;

import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.base.BaseActivity;
import com.developer.phimtatnhanh.delayclickview.PostDelayClick;
import com.developer.phimtatnhanh.di.component.ActivityComponent;
import com.developer.phimtatnhanh.ui.garelly.gridview.CustomGarellyAdapter;
import com.developer.phimtatnhanh.ui.garelly.gridview.FileItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

import static com.developer.phimtatnhanh.setuptouch.config.ConfigAll.PATH;

public class GarellyActivity extends BaseActivity implements GarellyView {

    @BindView(R.id.grid_view)
    GridView gridView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;

    private GarellyPresenter garellyPresenter;
    private CustomGarellyAdapter customGarellyAdapter;
    private String type;

    @Override
    protected void init() {
        EventBus.getDefault().register(this);
        this.garellyPresenter = new GarellyPresenter();
        this.garellyPresenter.attachView(this);
        this.setUpView();
    }

    private void setUpView() {
        if (this.garellyPresenter == null) {
            finish();
            return;
        }
        Intent intent = this.getIntent();
        if (intent == null) {
            finish();
            return;
        }
        this.type = intent.getStringExtra(GarellyView.TYPE);
        if (TextUtils.isEmpty(type)) {
            finish();
            return;
        }
        if (TextUtils.equals(type, GarellyView.IMG)) {
            this.garellyPresenter.createRxGetFileGarelly();
        } else {
            this.tvTitle.setText(getString(R.string.user_video));
            this.garellyPresenter.createRxGetFileVideo();
        }
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_garelly;
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public FragmentManager fragmentManager() {
        return getSupportFragmentManager();
    }

    @Override
    protected boolean fullScreen() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (this.garellyPresenter != null) {
            this.garellyPresenter.onStopRxGetFileGarelly();
        }
    }

    @Override
    public void onResult(List<FileItem> fileItems) {
        this.customGarellyAdapter = CustomGarellyAdapter.init(this, fileItems, this.gridView);
        this.customGarellyAdapter.setClickItem(fileItem -> {
            if (TextUtils.equals(this.type, GarellyView.IMG)) {
                PreviewImage.open(this, fileItem.path, 1234, GarellyView.IMG);
                return;
            }
            PreviewImage.open(this, fileItem.path, 1234, GarellyView.VIDEO);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234 && resultCode == RESULT_OK && data != null) {
            String stringExtra = data.getStringExtra(PATH);
            if (this.customGarellyAdapter != null) {
                this.customGarellyAdapter.deleteItem(stringExtra);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusPathScreenShot event) {
        if (this.customGarellyAdapter != null) {
            this.customGarellyAdapter.setFileItem(event.path);
        }
    }

    public void onBack(View view) {
        PostDelayClick.get().postDelayViewClick(view);
        finish();
    }
}