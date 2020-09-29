package com.developer.phimtatnhanh.setuptouch.utilities.capturescreen.video;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.WindowManager;

import com.developer.phimtatnhanh.app.AppContext;
import com.developer.phimtatnhanh.ui.settingcapturevideo.PreSetting;
import com.developer.phimtatnhanh.util.DeviceUtils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ScreenCaptureVideo {

    private static final String TAG = "ScreenCaptureVideo";

    public static ScreenCaptureVideo create() {
        return new ScreenCaptureVideo();
    }

    private WindowManager windowManager;

    private int mScreenWidth = 0;

    private int mScreenHeight = 0;

    private int mScreenDensity = 0;

    private boolean isAudioEnabled = true;

    private MediaProjection mMediaProjection;

    private MediaRecorder mMediaRecorder;

    private VirtualDisplay mVirtualDisplay;

    private int audioSourceAsInt = 0;

    private int videoEncoderAsInt = 0;

    private int videoFrameRate = 30;

    private int videoBitrate = 40000000;

    private int outputFormatAsInt = 0;

    private LifeCaptureVideo lifeCaptureVideo;
    private String filePath = "";
    private Uri uriAndroidQ = null;
    private int mResultCode = -1;
    private Intent mResultData = null;

    public ScreenCaptureVideo setLifeCaptureVideo(LifeCaptureVideo lifeCaptureVideo) {
        this.lifeCaptureVideo = lifeCaptureVideo;
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ScreenCaptureVideo setmResultCode(int mResultCode) {
        this.mResultCode = mResultCode;
        return this;
    }

    public void setmResultData(Intent mResultData) {
        this.mResultData = mResultData;
    }

    public String getFilePath() {
        return filePath;
    }

    public WindowManager getWindowManager() {
        return windowManager;
    }

    public ScreenCaptureVideo setUriAndroidQ(Uri uriAndroidQ) {
        this.uriAndroidQ = uriAndroidQ;
        return this;
    }

    public Uri getUriAndroidQ() {
        return uriAndroidQ;
    }

    /*
     * setup mScreenHeight+mScreenWidth
     * */
    public void setup() {
        try {
            this.createWindownManager();
            this.setScreenWidth();
            this.setScreenHeight();
            this.setScreenDensity();

            this.setAudioSource();
            this.setvideoEncoder();
            this.setOutputformat();

            this.isAudioEnabled = PreSetting.getAudioRecord();

            this.videoFrameRate = CustomSettingsRecorder.setVideoFrameRate();
            this.videoBitrate = CustomSettingsRecorder.setVideoBitRate();

            this.createRecorder();
            this.createVirtualDisplay();
        } catch (Exception e) {
            e.printStackTrace();
            observerError(TypeError.Exception);
        }
    }

    /*
     * createMediaProjection
     * */
    private void createMediaProjection() {
        try {
            MediaProjectionManager systemService = (MediaProjectionManager) AppContext.get().getContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            if (systemService == null) {
                observerError(TypeError.NullPointerException);
                return;
            }
            this.mMediaProjection = systemService.getMediaProjection(this.mResultCode, this.mResultData);
        } catch (Exception e) {
            e.printStackTrace();
            observerError(TypeError.Exception);
        }
    }

    /*
     * createVirtualDisplay
     * */
    private void createWindownManager() {
        this.windowManager = (WindowManager) AppContext.get().getContext().getSystemService(Context.WINDOW_SERVICE);
    }

    /*
     * createVirtualDisplay
     * */
    private void createVirtualDisplay() {
        if (this.mMediaProjection == null) {
            this.createMediaProjection();
        }
        try {
            this.mVirtualDisplay = this.mMediaProjection.createVirtualDisplay(TAG, this.mScreenWidth, this.mScreenHeight, this.mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mMediaRecorder.getSurface(), null, null);
        } catch (Exception e) {
            e.printStackTrace();
            observerError(TypeError.Exception);
        }
    }

    /*
     * createRecorder
     * */
    private void createRecorder() {

        try {
            this.mMediaRecorder = new MediaRecorder();

            if (this.isAudioEnabled) {
                this.mMediaRecorder.setAudioSource(audioSourceAsInt);
            }
            this.mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            this.mMediaRecorder.setOutputFormat(outputFormatAsInt);

            if (this.isAudioEnabled) {
                this.mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                int audioBitrate = 128000;
                this.mMediaRecorder.setAudioEncodingBitRate(audioBitrate);
                int audioSamplingRate = 44100;
                this.mMediaRecorder.setAudioSamplingRate(audioSamplingRate);
            }

            this.mMediaRecorder.setVideoEncoder(this.videoEncoderAsInt);

            if (this.getUriAndroidQ() == null) {
                this.mMediaRecorder.setOutputFile(this.getFilePath());
            } else {
                ContentResolver contentResolver = AppContext.get().getContext().getContentResolver();
                ParcelFileDescriptor rw = contentResolver.openFileDescriptor(this.getUriAndroidQ(), "rw");
                if (rw == null) {
                    this.mMediaRecorder.setOutputFile(this.getFilePath());
                } else {
                    FileDescriptor fileDescriptor = rw.getFileDescriptor();
                    this.mMediaRecorder.setOutputFile(fileDescriptor);
                }
            }

            this.mMediaRecorder.setVideoSize(this.mScreenWidth, this.mScreenHeight);

            this.mMediaRecorder.setVideoEncodingBitRate(videoBitrate);
            this.mMediaRecorder.setVideoFrameRate(videoFrameRate);
        } catch (Exception e) {
            e.printStackTrace();
            observerError(TypeError.Exception);
        }
        try {
            this.mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "createRecorder: " + e.toString());
            observerError(TypeError.IOException);
        }
    }

    /*
     * Check Mic
     * */
    private boolean getMicrophoneAvailable(Context context) {
        MediaRecorder recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setOutputFile(new File(context.getCacheDir(), "MediaUtil#micAvailTestFile").getAbsolutePath());
        boolean available = true;
        try {
            recorder.prepare();
            recorder.start();

        } catch (Exception exception) {
            available = false;
        }
        recorder.release();
        return available;
    }

    /*
     * setvideoEncoder
     * */
    private void setvideoEncoder() {
        switch (CustomSettingsRecorder.setVideoEncoder()) {
            case "DEFAULT":
                videoEncoderAsInt = 0;
                break;
            case "H263":
                videoEncoderAsInt = 1;
                break;
            case "H264":
                videoEncoderAsInt = 2;
                break;
            case "MPEG_4_SP":
                videoEncoderAsInt = 3;
                break;
            case "VP8":
                videoEncoderAsInt = 4;
                break;
            case "HEVC":
                videoEncoderAsInt = 5;
                break;
        }
    }

    /*
     * setAudioSource
     * */
    private void setAudioSource() {
        switch (CustomSettingsRecorder.setAudioSource()) {
            case "DEFAULT":
                audioSourceAsInt = 0;
                break;
            case "MIC":
                audioSourceAsInt = 1;
                break;
            case "VOICE_UPLINK":
                audioSourceAsInt = 2;
                break;
            case "VOICE_DOWNLINK":
                audioSourceAsInt = 3;
                break;
            case "VOICE_CALL":
                audioSourceAsInt = 4;
                break;
            case "CAMCODER":
                audioSourceAsInt = 5;
                break;
            case "VOICE_RECOGNITION":
                audioSourceAsInt = 6;
                break;
            case "VOICE_COMMUNICATION":
                audioSourceAsInt = 7;
                break;
            case "REMOTE_SUBMIX":
                audioSourceAsInt = 8;
                break;
            case "UNPROCESSED":
                audioSourceAsInt = 9;
                break;
            case "VOICE_PERFORMANCE":
                audioSourceAsInt = 10;
                break;
        }
    }

    /*
     * setOutputformat
     * */
    private void setOutputformat() {
        switch (CustomSettingsRecorder.setOutputFormat()) {
            case "DEFAULT":
                outputFormatAsInt = 0;
                break;
            case "THREE_GPP":
                outputFormatAsInt = 1;
                break;
            case "MPEG_4":
                outputFormatAsInt = 2;
                break;
            case "AMR_NB":
                outputFormatAsInt = 3;
                break;
            case "AMR_WB":
                outputFormatAsInt = 4;
                break;
            case "AAC_ADTS":
                outputFormatAsInt = 6;
                break;
            case "MPEG_2_TS":
                outputFormatAsInt = 8;
                break;
            case "WEBM":
                outputFormatAsInt = 9;
                break;
            case "OGG":
                outputFormatAsInt = 11;
                break;
            default:
                outputFormatAsInt = 2;
        }
    }

    /*
     * setScreenWidth
     * */
    private void setScreenWidth() {
        try {
            this.mScreenWidth = this.mScreenHeight = CustomSettingsRecorder.setScreenDimensions()[1];
        } catch (Exception e) {
            e.printStackTrace();
            observerError(TypeError.Exception);
        }
    }

    /*
     * setScreenHeight
     * */
    private void setScreenHeight() {
        try {
            this.mScreenHeight = CustomSettingsRecorder.setScreenDimensions()[0];
        } catch (Exception e) {
            e.printStackTrace();
            observerError(TypeError.Exception);
        }
    }

    /*
     *setScreenDensity
     * */
    private void setScreenDensity() {
        try {
            this.mScreenDensity = DeviceUtils.dpi(this.getWindowManager());
        } catch (Exception e) {
            e.printStackTrace();
            observerError(TypeError.Exception);
        }
    }

    private void observerError(TypeError exception) {
        if (this.lifeCaptureVideo != null) {
            this.lifeCaptureVideo.onError(exception);
        }
    }


    /*
     * Stop Capture Video
     * */
    @SuppressLint("CheckResult")
    public void stopAll() {
/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            return;
        }
        if (!this.resetAll()) {
            this.callOnComplete();
        }*/
        Single.create((SingleOnSubscribe<Void>) emitter -> {
            try {
                if (!this.resetAll()) {
                    this.callOnComplete();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (!this.resetAll()) {
                    this.callOnComplete();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    /*
     *
     * */
    public void start() {
        try {
            if (this.lifeCaptureVideo != null) {
                this.lifeCaptureVideo.onStart();
            }
            this.mMediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
            observerError(TypeError.Exception);
        }
    }

    private void callOnComplete() {
        if (this.lifeCaptureVideo != null) {
            this.lifeCaptureVideo.onResult(this.getFilePath());
        }
    }

    private boolean resetAll() {
        boolean is = false;
        try {
            try {
                if (this.mVirtualDisplay != null) {
                    this.mVirtualDisplay.release();
                    this.mVirtualDisplay = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                is = true;
            }
            try {
                if (this.mMediaRecorder != null) {
                    this.mMediaRecorder.setOnErrorListener(null);
                    this.mMediaRecorder.stop();
                    this.mMediaRecorder.release();
                    this.mMediaRecorder = null;
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                is = true;
            }
            try {
                if (this.mMediaProjection != null) {
                    this.mMediaProjection.stop();
                    this.mMediaProjection = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                is = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            is = true;
        }
        return is;
    }
}
