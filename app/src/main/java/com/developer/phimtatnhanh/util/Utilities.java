package com.developer.phimtatnhanh.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.developer.phimtatnhanh.BuildConfig;
import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.data.PrefUtil;


public class Utilities implements Const {


    public static void openCHPlay(Context context) {
        if (PrefUtil.get().getBool(RATE, false)) {
            return;
        }
        PrefUtil.get().postBool(RATE, true);
        final String appPackageName = BuildConfig.DEBUG ? "com.creative.lovedays&hl=vi" : context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(LINKCHPLAY + appPackageName)));
        }
    }

    public static void doShareApp(Context context) {
        final String appPackageName = BuildConfig.DEBUG ? "com.creative.lovedays&hl=vi" : context.getPackageName();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        Intent chooserIntent = Intent.createChooser(shareIntent, context.getString(R.string.chiasebanbe));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.content_fb) + " " + LINKCHPLAY + appPackageName);
            Bundle facebookBundle = new Bundle();
            facebookBundle.putString(Intent.EXTRA_TEXT, LINKCHPLAY + appPackageName);
            Bundle replacement = new Bundle();
            replacement.putBundle("com.facebook.katana", facebookBundle);
            chooserIntent.putExtra(Intent.EXTRA_REPLACEMENT_EXTRAS, replacement);
        } else {
            shareIntent.putExtra(Intent.EXTRA_TEXT, LINKCHPLAY + appPackageName);
        }
        chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(chooserIntent);
    }


    public static void openUrl(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vuabocphet98.wixsite.com/dieukhoannguoidung"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
