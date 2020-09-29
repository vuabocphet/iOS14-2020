package com.developer.phimtatnhanh.ui.home;


import android.content.Intent;

import com.developer.phimtatnhanh.base.BaseView;
import com.developer.phimtatnhanh.ui.menulayout.TypeEditMenu;
import com.developer.phimtatnhanh.ui.touch.TypeEditTouch;

public interface HomeView extends BaseView {

    void onUpdateTextSetting(String text, String key);

    void startActivity(Class aClass, TypeEditMenu typeEditMenu, TypeEditTouch typeEditTouch);

    void startActivityCustom(Intent intent);

    void onRestoreMenuTouch();

    void onLocationTouch();

    void onBgColorMenu();

    void onBgPhotoMenu();

    void onBrightness();

    void onCapturePreview();

}
