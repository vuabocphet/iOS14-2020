package com.developer.phimtatnhanh.setuptouch.gridviewstarapp;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class AppUtil {

    public static AppUtil init() {
        return new AppUtil();
    }

    public AppItem getApplication(PackageManager packageManager, String packname) {
        ApplicationInfo applicationInfo = null;
        AppItem item = AppItem.init();
        try {

            applicationInfo = packageManager.getApplicationInfo(packname, 0);

        } catch (Exception ignored) {

        }
        if (applicationInfo != null) {
            CharSequence label = packageManager.getApplicationLabel(applicationInfo);
            return item.setApplicationInfo(applicationInfo).setName(label.toString()).setPackname(packname).setTypeStar(TypeStar.APP);
        }
        return item.setTypeStar(TypeStar.NONE);
    }

}
