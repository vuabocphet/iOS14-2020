package com.developer.phimtatnhanh.ui.settingcapturevideo;


import com.developer.phimtatnhanh.data.Pref;
import com.developer.phimtatnhanh.data.PrefUtil;

public class PreSetting implements Pref {

    public static String getFromKey(String key) {
        switch (key) {
            case KEY_VIDEO_RESOLUTION:
                return getVideoResolution();
            case KEY_AUDIO_SOURCE:
                return getAudioSource();
            case KEY_VIDEO_ENCODER:
                return getVideoEncoder();
            case KEY_VIDEO_FPS:
                return getVideoFps();
            case KEY_VIDEO_BITRATE:
                return getVideoBitRate();
            case KEY_OUTPUT_FORMAT:
                return getOutputFormat();
        }
        return "0";
    }

    public static String getVideoResolution() {
        return PrefUtil.get().getString(KEY_VIDEO_RESOLUTION, "4");
    }

    public static boolean getAudioRecord() {
        return PrefUtil.get().getBool(KEY_RECORD_AUDIO, true);
    }

    public static String getAudioSource() {
        return PrefUtil.get().getString(KEY_AUDIO_SOURCE, "2");
    }

    public static String getVideoEncoder() {
        return PrefUtil.get().getString(KEY_VIDEO_ENCODER, "0");
    }

    public static String getVideoFps() {
        return PrefUtil.get().getString(KEY_VIDEO_FPS, "3");
    }

    public static String getVideoBitRate() {
        return PrefUtil.get().getString(KEY_VIDEO_BITRATE, "5");
    }

    public static String getOutputFormat() {
        return PrefUtil.get().getString(KEY_OUTPUT_FORMAT, "1");
    }

}
