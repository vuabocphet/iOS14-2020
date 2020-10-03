package com.developer.phimtatnhanh.ads.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class AdPrefs {

    private static SharedPreferences pref;

    private static void initPrefs(Context context, String prefsName) {
        pref = context.getSharedPreferences(EncryptionUtils.encodeMd5(prefsName), Context.MODE_PRIVATE);
    }

    public static void putString(final String key, final String newValue) {
        pref.edit().putString(key, newValue).apply();
    }

    public static String getString(final String key, final String defValue) {
        return pref.getString(key, defValue);
    }

    public static void putInt(final String key, final int newValue) {
        pref.edit().putInt(key, newValue).apply();
    }

    public static int getInt(final String key, final int defValue) {
        return pref.getInt(key, defValue);
    }

    public static void putFloat(final String key, final float newValue) {
        pref.edit().putFloat(key, newValue).apply();
    }

    public static float getFloat(final String key, final float defValue) {
        return pref.getFloat(key, defValue);
    }

    public static void putDouble(final String key, final double value) {
        pref.edit().putLong(key, Double.doubleToRawLongBits(value)).apply();
    }

    public static double getDouble(final String key, final double defaultValue) {
        if (!pref.contains(key)) {
            return defaultValue;
        }
        return Double.longBitsToDouble(pref.getLong(key, 0));
    }

    public static void putLong(final String key, final long newValue) {
        pref.edit().putLong(key, newValue).apply();
    }

    public static long getLong(final String key, final long defValue) {
        return pref.getLong(key, defValue);
    }

    public static void putBoolean(final String key, final Boolean newValue) {
        pref.edit().putBoolean(key, newValue).apply();
    }

    public static boolean getBoolean(final String key, final Boolean defValue) {
        return pref.getBoolean(key, defValue);
    }

    public static void putStringSet(final String key, final Set<String> newValue) {
        pref.edit().putStringSet(key, newValue).apply();
    }

    public static Set<String> getStringSet(final String key, final Set<String> defValue) {
        return pref.getStringSet(key, defValue);
    }

    public static Map<String, ?> getAll() {
        return pref.getAll();
    }

    public static boolean containsPreference(final String key) {
        return pref.contains(key);
    }

    public static void removeKey(final String key) {
        pref.edit().remove(key).apply();
    }

    public static boolean hasKey(String key) {
        return pref.contains(key);
    }

    public static void initialize(Context context, String prefsName) {
        if (context == null) {
            throw new RuntimeException("Context not set, please set context on your Application class before using it.");
        }

        initPrefs(context, String.format(Locale.US, "%s_%s", context.getPackageName(), prefsName));
    }

    public static void initialize(Context context) {
        initPrefs(context, context.getPackageName());
    }

    public static void put(String key, Object value) {
        if (value == null) {
            return;
        }
        if (value instanceof Boolean) {
            putBoolean(key, (Boolean) value);
            return;
        }
        if (value instanceof String) {
            putString(key, (String) value);
            return;
        }
        if (value instanceof Integer) {
            putInt(key, (Integer) value);
            return;
        }
        if (value instanceof Long) {
            putLong(key, (Long) value);
            return;
        }
        if (value instanceof Float) {
            putFloat(key, (Float) value);
        }
    }
}
