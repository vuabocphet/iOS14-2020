package com.developer.phimtatnhanh.data;


import com.developer.phimtatnhanh.setuptouch.config.ConfigAll;
import com.tencent.mmkv.MMKV;

import javax.inject.Inject;

public class PrefUtil implements ConfigAll {

    private MMKV mmkv;

    private static PrefUtil prefUtil;

    public static PrefUtil get() {
        if (prefUtil == null) {
            prefUtil = new PrefUtil();
        }
        return prefUtil;
    }

    public static void initialize() {
        if (prefUtil == null) {
            prefUtil = new PrefUtil();
        }
    }

    @Inject
    public PrefUtil() {
        this.mmkv = MMKV.defaultMMKV();
    }

    public void postInt(String key, int i) {
        this.mmkv.encode(key, i);
    }

    public void postFloat(String key, float i) {
        this.mmkv.encode(key, i);
    }

    public void postLong(String key, long i) {
        this.mmkv.encode(key, i);
    }

    public void postString(String key, String ms) {
        this.mmkv.encode(key, ms);
    }

    public String getString(String key, String... defaultValue) {
        return this.mmkv.decodeString(key, defaultValue == null || defaultValue.length == 0 ? packnameFake : defaultValue[0]);
    }

    public int getInt(String key, int... defaultValue) {
        return this.mmkv.decodeInt(key, defaultValue == null || defaultValue.length == 0 ? 4 : defaultValue[0]);
    }

    public float getFloat(String key, float... defaultValue) {
        return this.mmkv.decodeFloat(key, defaultValue == null || defaultValue.length == 0 ? 0.0f : defaultValue[0]);
    }

    public float getLong(String key, long... defaultValue) {
        return this.mmkv.decodeLong(key, defaultValue == null || defaultValue.length == 0 ? 0L : defaultValue[0]);
    }

    public void postBool(String key, boolean is) {
        this.mmkv.encode(key, is);
    }

    public boolean getBool(String key, boolean... is) {
        return this.mmkv.decodeBool(key, is != null && is.length != 0 && is[0]);
    }

    public void clearKey(String key) {
        this.mmkv.clear().remove(key).apply();
    }

}
