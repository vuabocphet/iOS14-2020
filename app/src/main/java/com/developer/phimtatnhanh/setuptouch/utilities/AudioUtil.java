package com.developer.phimtatnhanh.setuptouch.utilities;

import android.media.AudioManager;

public class AudioUtil {

    public static int getMaxAudio(AudioManager am) {
        int streamMaxVolume = 0;
        if (am != null) {
            streamMaxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        }
        return streamMaxVolume;
    }

    public static int getCurrenAudio(AudioManager am) {
        int streamVolume = 0;
        if (am != null) {
            streamVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        }
        return streamVolume;
    }

    public static void setAudio(AudioManager am,int value) {
        if (am != null) {
            am.setStreamVolume(AudioManager.STREAM_MUSIC, value, 0);
        }
    }
}
