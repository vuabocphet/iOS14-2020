package com.developer.phimtatnhanh.ui.settingcapturevideo;


import android.content.Context;
import android.view.View;

import androidx.fragment.app.DialogFragment;

import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.base.BaseApplication;
import com.developer.phimtatnhanh.base.BaseMvpPresenter;
import com.developer.phimtatnhanh.data.ListUtils;
import com.developer.phimtatnhanh.data.Pref;
import com.developer.phimtatnhanh.dialog.OptionDialogSetting;

import javax.inject.Inject;

public class SettingPresenter implements BaseMvpPresenter<SettingView>, Pref, OptionDialogSetting.SingleChoiceListener {

    private SettingView settingView;


    private DialogFragment optionAudioSource;

    private DialogFragment optionVideoEncoder;

    private DialogFragment optionResolution;

    private DialogFragment optionFrameRate;

    private DialogFragment optionBitRate;

    private DialogFragment optionOuputFormat;

    @Inject
    ListUtils listUtils;


    @Inject
    public SettingPresenter(Context context) {
        ((BaseApplication) context.getApplicationContext()).getApplicationComponent().inject(this);
    }

    public SettingView getSettingView() {
        return settingView;
    }

    public void clickView(View view) {
        int id = view.getId();
        this.switchClick(id);
    }

    private void switchClick(int id) {
        switch (id) {
            case R.id.cs_audio_record:
                getSettingView().onUpdateSw();
                break;
            case R.id.cs_audio_source:
                if (this.optionAudioSource.isAdded()) {
                    return;
                }
                this.optionAudioSource.show(getSettingView().fragmentManager(), "AudioSource");
                break;
            case R.id.cs_bit_rate:
                if (this.optionBitRate.isAdded()) {
                    return;
                }
                this.optionBitRate.show(getSettingView().fragmentManager(), "BitRate");
                break;
            case R.id.cs_frame_rate:
                if (this.optionFrameRate.isAdded()) {
                    return;
                }
                this.optionFrameRate.show(getSettingView().fragmentManager(), "FrameRate");
                break;
            case R.id.cs_resolution:
                if (this.optionResolution.isAdded()) {
                    return;
                }
                this.optionResolution.show(getSettingView().fragmentManager(), "Resolution");
                break;
            case R.id.cs_video_ebcoder:
                if (this.optionVideoEncoder.isAdded()) {
                    return;
                }
                this.optionVideoEncoder.show(getSettingView().fragmentManager(), "VideoEncoder");
                break;
            case R.id.cs_video_format:
                if (this.optionOuputFormat.isAdded()) {
                    return;
                }
                this.optionOuputFormat.show(getSettingView().fragmentManager(), "OuputFormat");
        }
    }

    @Override
    public void attachView(SettingView view) {
        this.settingView = view;
        this.optionAudioSource = new OptionDialogSetting(this, getSettingView().getContext(), this.listUtils.getString(R.string.audio_source), KEY_AUDIO_SOURCE, this.listUtils.listAudioSource());
        this.optionVideoEncoder = new OptionDialogSetting(this, getSettingView().getContext(), this.listUtils.getString(R.string.video_encoder), KEY_VIDEO_ENCODER, this.listUtils.listVideoEncoder());
        this.optionResolution = new OptionDialogSetting(this, getSettingView().getContext(), this.listUtils.getString(R.string.resolution), KEY_VIDEO_RESOLUTION, this.listUtils.listVideoResolution());
        this.optionFrameRate = new OptionDialogSetting(this, getSettingView().getContext(), this.listUtils.getString(R.string.frame_rate), KEY_VIDEO_FPS, this.listUtils.listVideoFrameRate());
        this.optionBitRate = new OptionDialogSetting(this, getSettingView().getContext(), this.listUtils.getString(R.string.bit_rate), KEY_VIDEO_BITRATE, this.listUtils.listVideoBitRate());
        this.optionOuputFormat = new OptionDialogSetting(this, getSettingView().getContext(), this.listUtils.getString(R.string.output_format), KEY_OUTPUT_FORMAT, this.listUtils.listVideoFormat());
    }

    @Override
    public void detachView() {
        this.settingView = null;
    }

    @Override
    public void onPositiveButtonClicked(String text, String key) {
        if (this.getSettingView() == null) {
            return;
        }
        getSettingView().updateTextViewFromKey(key);
    }
}
