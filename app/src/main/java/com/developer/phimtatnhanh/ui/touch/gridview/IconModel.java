package com.developer.phimtatnhanh.ui.touch.gridview;

public class IconModel {

    public int iconId;
    public boolean status;

    public static IconModel create() {
        return new IconModel();
    }

    public IconModel setIconId(int iconId) {
        this.iconId = iconId;
        return this;
    }

    public IconModel setStatus(boolean status) {
        this.status = status;
        return this;
    }
}
