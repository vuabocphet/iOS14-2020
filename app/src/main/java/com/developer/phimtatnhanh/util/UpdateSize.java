package com.developer.phimtatnhanh.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


public class UpdateSize {

    public static void set(Activity a, View view, int size) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            return;
        }
        params.height = params.width = (DeviceUtils.w(a) / size) - pxFromDp(a, 4);
        view.setLayoutParams(params);
    }

    public static void setW(Activity a, View view, int size) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            return;
        }
        params.width = (DeviceUtils.w(a) / size) - pxFromDp(a, 4);
        view.setLayoutParams(params);
    }

    public static void setWHFull(int w, View view) {
        ViewGroup.LayoutParams params =view.getLayoutParams();
        if (params == null) {
            Log.i("setWHFull", "setWHFull: params null");
            return;
        }
        params.width =w-pxFromDp(view.getContext(),35);
        params.height =w-pxFromDp(view.getContext(),35);
        view.setLayoutParams(params);
    }


    public static void setH(Activity a, View view, int size) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            return;
        }
        params.height = (DeviceUtils.w(a) / size) + pxFromDp(a, 10);
        view.setLayoutParams(params);
    }

    public static void setHeightHoder1(Activity a, View view, double size) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            return;
        }
        params.height = (int) (DeviceUtils.w(a) * size);
        view.setLayoutParams(params);
    }

    public static int dpFromPx(final Context context, final float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    public static int pxFromDp(final Context context, final float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }


}
