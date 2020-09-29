package com.developer.phimtatnhanh.setuptouch.utilities.capturescreen.video;

public interface LifeCaptureVideo {

    void onError(TypeError typeError);

    void onResult(String path);

    void onStart();
}
