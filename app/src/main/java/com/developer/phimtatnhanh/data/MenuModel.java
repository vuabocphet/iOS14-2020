package com.developer.phimtatnhanh.data;

public class MenuModel {

    public String title;

    public int[] icon;

    public static MenuModel init() {
        return new MenuModel();
    }

    public MenuModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public MenuModel setIcon(int[] icon) {
        this.icon = icon;
        return this;
    }
}
