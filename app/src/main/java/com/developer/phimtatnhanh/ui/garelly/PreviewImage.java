package com.developer.phimtatnhanh.ui.garelly;


import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.base.BaseActivity;
import com.developer.phimtatnhanh.delayclickview.PostDelayClick;
import com.developer.phimtatnhanh.di.component.ActivityComponent;
import com.developer.phimtatnhanh.setuptouch.config.ConfigAll;
import com.developer.phimtatnhanh.setuptouch.utilities.SaveBitmapUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class PreviewImage extends BaseActivity implements ConfigAll {

    public static void open(Activity context, String path, int rqCode, String type) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, PreviewImage.class);
        intent.putExtra(PATH, path);
        intent.putExtra(GarellyView.TYPE, type);
        context.startActivityForResult(intent, rqCode);
    }

    private String path;
    private String type;

    @BindView(R.id.tv_name_image)
    AppCompatTextView tvNameImage;

    @BindView(R.id.iv_preview)
    AppCompatImageView ivPreview;

    @OnClick(R.id.iv_preview)
    public void playVideo(View view) {
        if (TextUtils.equals(this.type, GarellyView.IMG)) {
            return;
        }
        PostDelayClick.get().postDelayViewClick(view);
        SaveBitmapUtil.openCaptureScreen(this, new File(this.path),GarellyView.VIDEO);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_preview_image;
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {

    }

    @Override
    protected boolean fullScreen() {
        return true;
    }

    public void onBack(View view) {
        PostDelayClick.get().postDelayViewClick(view);
        finish();
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        this.path = intent.getStringExtra(PATH);
        this.type = intent.getStringExtra(GarellyView.TYPE);
        if (TextUtils.equals(this.type, GarellyView.VIDEO)) {
            this.tvNameImage.setText(getString(R.string.user_video));
            SaveBitmapUtil.openCaptureScreen(this, new File(this.path),GarellyView.VIDEO);
        }
        Glide.with(this).load(this.path).into(this.ivPreview);
    }

    public void onShare(View view) {
        PostDelayClick.get().postDelayViewClick(view);
        SaveBitmapUtil.sendMessenger(view.getContext(), new File(this.path));
    }

    public void onDelete(View view) {
        PostDelayClick.get().postDelayViewClick(view);
        File file = new File(this.path);
        file.delete();
        setResult(RESULT_OK, getIntent());
        finish();
    }
}