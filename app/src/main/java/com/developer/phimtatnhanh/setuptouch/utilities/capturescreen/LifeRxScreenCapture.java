package com.developer.phimtatnhanh.setuptouch.utilities.capturescreen;

import android.graphics.Bitmap;

public interface LifeRxScreenCapture {

    void onResultCaptureScreen(Bitmap bitmap);

    void onFailedCaptureScreen();

    void onStartCaptureScreen();

    void onStopCaptureScreen();

    void onResultCaptureScreenVideo(String path);

    void onStartCaptureScreenVideo();

    void onErrorCaptureScreenVideo();

}
