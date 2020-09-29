package com.developer.phimtatnhanh.util;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Insets;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.webkit.MimeTypeMap;

import androidx.core.content.FileProvider;

import com.developer.phimtatnhanh.BuildConfig;
import com.developer.phimtatnhanh.R;

import java.io.File;

public class DeviceUtils {

    /*Cấu hình để lấy size điện thoại*/
    private static DisplayMetrics get(Activity activity) {
        if (activity == null) {
            return new DisplayMetrics();
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        try {
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        } catch (Exception e) {
            e.printStackTrace();
            return new DisplayMetrics();
        }
        return displayMetrics;
    }

    private static DisplayMetrics get(WindowManager windowManager) {
        if (windowManager == null) {
            return new DisplayMetrics();
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        try {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        } catch (Exception e) {
            e.printStackTrace();
            return new DisplayMetrics();
        }
        return displayMetrics;
    }

    /*Lấy chiều dài của điện thoại*/
    public static int w(Activity a) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowMetrics metrics = a.getWindowManager().getCurrentWindowMetrics();
            Insets insets = metrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            return metrics.getBounds().width() - insets.left - insets.right;
        }
        return get(a).widthPixels;
    }

    public static int w(WindowManager windowManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowMetrics metrics = windowManager.getCurrentWindowMetrics();
            Insets insets = metrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            return metrics.getBounds().width() - insets.left - insets.right;
        }
        return get(windowManager).widthPixels;
    }

    /*Lấy chiều cao của điện thoại*/
    public static int h(Activity a) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowMetrics metrics = a.getWindowManager().getCurrentWindowMetrics();
            Insets insets = metrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            return metrics.getBounds().height() - insets.top - insets.bottom;
        }
        return get(a).heightPixels;
    }

    public static int h(WindowManager windowManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowMetrics metrics = windowManager.getCurrentWindowMetrics();
            Insets insets = metrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            return metrics.getBounds().height() - insets.top - insets.bottom;
        }
        return get(windowManager).heightPixels;
    }

    public static int dpi(WindowManager windowManager) {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.densityDpi;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /*hiển thị toàn màn hình*/
    public static void fullscreen(Window w) {
        if (w == null) {
            return;
        }
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void sendMessenger(Context context) {
        try {
            File newFile = new File(context.getCacheDir(), "image.png");
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String ext = newFile.getName().substring(newFile.getName().lastIndexOf(".") + 1);
            String type = mime.getMimeTypeFromExtension(ext);
            Intent sharingIntent = new Intent();
            sharingIntent.setAction(Intent.ACTION_SEND);
            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            sharingIntent.setType(type);
            if (getVersionSDK() > 23) {
                sharingIntent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", newFile));
            } else {
                sharingIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(newFile));
            }
            context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.chonapp)));
        } catch (Exception e) {

        }
    }

    private static int getVersionSDK() {
        return Build.VERSION.SDK_INT;
    }

}
