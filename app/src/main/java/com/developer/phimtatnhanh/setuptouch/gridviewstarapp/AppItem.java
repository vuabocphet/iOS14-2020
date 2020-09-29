package com.developer.phimtatnhanh.setuptouch.gridviewstarapp;

import android.content.pm.ApplicationInfo;

public class AppItem {

    public String name = "";
    public String packname = "";
    public ApplicationInfo applicationInfo = null;
    public TypeStar typeStar;

    public AppItem() {
    }

    public static AppItem init() {
        return new AppItem();
    }

    public AppItem setName(String name) {
        this.name = name;
        return this;
    }

    public AppItem setPackname(String packname) {
        this.packname = packname;
        return this;
    }

    public AppItem setApplicationInfo(ApplicationInfo applicationInfo) {
        this.applicationInfo = applicationInfo;
        return this;
    }

    public AppItem setTypeStar(TypeStar typeStar) {
        this.typeStar = typeStar;
        return this;
    }
}

