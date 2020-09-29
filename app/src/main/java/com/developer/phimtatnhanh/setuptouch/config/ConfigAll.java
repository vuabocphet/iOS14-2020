package com.developer.phimtatnhanh.setuptouch.config;


import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.setuptouch.utilities.permission.ConfigPer;

public interface ConfigAll extends ConfigPer, ConstKey {
    int durationShowViewTouch = 500;
    int durationHideViewTouch = 350;
    int durationUpdateLocationViewTouch = 250;
    float alphaViewTouch = 0.5f;
    int durationAlphaViewTouch = 3000;
    int durationDelayClick = 222;
    int durationVibrator = 50;
    int fontMenuTouch = R.font.thin_ios;
    int[] notCancelDialogTouch = new int[]{MENU_BACK, MENU_STAR};
    String backStarAppMenuTouch = "back";

    int durationShowHideStarApp = 250;

    int durationCaptureScreen = 255;

    String packnameFake = "com.teamdev.demngayyeu2020";

    String PATH = "path";


}
