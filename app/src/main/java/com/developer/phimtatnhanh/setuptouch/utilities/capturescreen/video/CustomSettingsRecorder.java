package com.developer.phimtatnhanh.setuptouch.utilities.capturescreen.video;


import com.developer.phimtatnhanh.ui.settingcapturevideo.PreSetting;

public class CustomSettingsRecorder {

    public static String setAudioSource() {
        String audio_source = PreSetting.getAudioSource();
        if (audio_source != null) {
            switch (audio_source) {
                case "0":
                    return "DEFAULT";
                case "1":
                    return "CAMCODER";
                case "2":
                    return "MIC";
            }
        }
        return "DEFAULT";
    }

    public static String setVideoEncoder() {
        String video_encoder = PreSetting.getVideoEncoder();
        if (video_encoder != null) {
            switch (video_encoder) {
                case "0":
                    return "DEFAULT";
                case "1":
                    return "H264";
                case "2":
                    return "H263";
                case "3":
                    return "HEVC";
                case "4":
                    return "MPEG_4_SP";
                case "5":
                    return "VP8";
            }
        }
        return "DEFAULT";
    }

    public static int[] setScreenDimensions() {
        String video_resolution = PreSetting.getVideoResolution();
        if (video_resolution != null) {
            switch (video_resolution) {
                case "0":
                    return new int[]{426, 240};
                case "1":
                    return new int[]{640, 360};
                case "2":
                    return new int[]{854, 480};
                case "3":
                    return new int[]{1280, 720};
                case "4":
                    return new int[]{1920, 1080};
            }
        }
        return new int[]{1280, 720};
    }

    public static int setVideoFrameRate() {
        String video_frame_rate = PreSetting.getVideoFps();
        if (video_frame_rate != null) {
            switch (video_frame_rate) {
                case "0":
                    return 60;
                case "1":
                    return 50;
                case "2":
                    return 48;
                case "3":
                    return 30;
                case "4":
                    return 25;
                case "5":
                    return 24;
            }
        }
        return 60;
    }

    public static int setVideoBitRate() {
        String video_bit_rate = PreSetting.getVideoBitRate();
        if (video_bit_rate != null) {
            switch (video_bit_rate) {
                case "1":
                    return 12000000;
                case "2":
                    return 8000000;
                case "3":
                    return 7500000;
                case "4":
                    return 5000000;
                case "5":
                    return 4000000;
                case "6":
                    return 2500000;
                case "7":
                    return 1500000;
                case "8":
                    return 1000000;
            }
        }
        return 12000000;
    }

    public static String setOutputFormat() {
        String output_format = PreSetting.getOutputFormat();
        if (output_format != null) {
            switch (output_format) {
                case "0":
                    return "DEFAULT";
                case "1":
                    return "MPEG_4";
                case "2":
                    return "THREE_GP";
                case "3":
                    return "WEBM";
            }
        }
        return "DEFAULT";
    }
}
