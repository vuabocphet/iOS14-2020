package com.developer.phimtatnhanh.ui.settingcapturevideo;


import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.base.MvpActivity;
import com.developer.phimtatnhanh.data.ListUtils;
import com.developer.phimtatnhanh.data.Pref;
import com.developer.phimtatnhanh.data.PrefUtil;
import com.developer.phimtatnhanh.delayclickview.PostDelayClick;
import com.suke.widget.SwitchButton;

import javax.inject.Inject;

import butterknife.BindView;

public class SettingCaptureVideoActivity extends MvpActivity implements View.OnClickListener, SettingView, Pref {

    @BindView(R.id.tv_audio_source)
    AppCompatTextView tvAudioSource;

    @BindView(R.id.switch_audio_record)
    SwitchButton switchAudioRecord;

    @BindView(R.id.cs_audio_record)
    ConstraintLayout csAudioRecord;

    @BindView(R.id.tv_video_encoder)
    AppCompatTextView tvVideoEncoder;

    @BindView(R.id.cs_video_ebcoder)
    ConstraintLayout csVideoEbcoder;

    @BindView(R.id.tv_resolution)
    AppCompatTextView tvResolution;

    @BindView(R.id.cs_resolution)
    ConstraintLayout csResolution;

    @BindView(R.id.tv_frame_rate)
    AppCompatTextView tvFrameRate;

    @BindView(R.id.cs_frame_rate)
    ConstraintLayout csFrameRate;

    @BindView(R.id.tv_bit_rate)
    AppCompatTextView tvBitRate;

    @BindView(R.id.cs_bit_rate)
    ConstraintLayout csBitRate;

    @BindView(R.id.tv_video_format)
    AppCompatTextView tvVideoFormat;

    @BindView(R.id.cs_video_format)
    ConstraintLayout csVideoFormat;

    @BindView(R.id.cs_audio_source)
    ConstraintLayout csAudioSource;

    @Inject
    PrefUtil prefUtil;

    @Inject
    ListUtils listUtils;

    private SettingPresenter settingPresenter;

    public SettingPresenter getSettingPresenter() {
        return settingPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        this.settingPresenter = new SettingPresenter(this);
        this.settingPresenter.attachView(this);
        this.initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.switchAudioRecord.setChecked(PreSetting.getAudioRecord());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.settingPresenter.detachView();
    }

    @Override
    protected boolean fullScreen() {
        return true;
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_setting_capture_video;
    }

    private void initView() {
        this.clickView(
                this.csAudioRecord,
                this.csAudioSource,
                this.csBitRate,
                this.csFrameRate,
                this.csResolution,
                this.csVideoEbcoder,
                this.csVideoFormat
        );
        this.updateTextView(KEY_VIDEO_RESOLUTION);
        this.updateTextView(KEY_AUDIO_SOURCE);
        this.updateTextView(KEY_VIDEO_ENCODER);
        this.updateTextView(KEY_VIDEO_FPS);
        this.updateTextView(KEY_VIDEO_BITRATE);
        this.updateTextView(KEY_OUTPUT_FORMAT);
        this.switchAudioRecord.setOnCheckedChangeListener((view, isChecked) -> this.prefUtil.postBool(KEY_RECORD_AUDIO, isChecked));
    }

    private void clickView(View... views) {
        if (views == null) {
            return;
        }
        for (View view : views) {
            if (view == null) {
                continue;
            }
            view.setOnClickListener(this);
        }
    }

    public void onBack(View view) {
        PostDelayClick.get().postDelayViewClick(view);
        finish();
    }

    @Override
    public void onClick(View view) {
        PostDelayClick.get().postDelayViewClick(view);
        this.getSettingPresenter().clickView(view);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public FragmentManager fragmentManager() {
        return getSupportFragmentManager();
    }

    private void updateTextView(String key) {
        int fromKey = Integer.parseInt(PreSetting.getFromKey(key));
        switch (key) {
            case KEY_VIDEO_RESOLUTION:
                this.tvResolution.setText(this.listUtils.listVideoResolution().get(fromKey));
                break;
            case KEY_AUDIO_SOURCE:
                this.tvAudioSource.setText(this.listUtils.listAudioSource().get(fromKey));
                break;
            case KEY_VIDEO_ENCODER:
                this.tvVideoEncoder.setText(this.listUtils.listVideoEncoder().get(fromKey));
                break;
            case KEY_VIDEO_FPS:
                this.tvFrameRate.setText(this.listUtils.listVideoFrameRate().get(fromKey));
                break;
            case KEY_VIDEO_BITRATE:
                this.tvBitRate.setText(this.listUtils.listVideoBitRate().get(fromKey));
                break;
            case KEY_OUTPUT_FORMAT:
                this.tvVideoFormat.setText(this.listUtils.listVideoFormat().get(fromKey));
                break;
        }

    }

    @Override
    public void updateTextViewFromKey(String key) {
        this.updateTextView(key);
    }

    @Override
    public void onUpdateSw() {
        this.switchAudioRecord.setChecked(!this.switchAudioRecord.isChecked());
    }
}