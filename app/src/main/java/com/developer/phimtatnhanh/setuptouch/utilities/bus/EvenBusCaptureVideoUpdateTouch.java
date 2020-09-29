package com.developer.phimtatnhanh.setuptouch.utilities.bus;

public class EvenBusCaptureVideoUpdateTouch {

    public LifeCaptureVideo lifeCaptureVideo;

    public enum LifeCaptureVideo {
        START,
        STOP,
        SETUP
    }

    public EvenBusCaptureVideoUpdateTouch setPlay(LifeCaptureVideo lifeCaptureVideo) {
        this.lifeCaptureVideo = lifeCaptureVideo;
        return this;
    }
}
