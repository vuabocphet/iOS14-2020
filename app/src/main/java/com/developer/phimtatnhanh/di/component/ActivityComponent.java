package com.developer.phimtatnhanh.di.component;


import com.developer.phimtatnhanh.di.ActivityScope;
import com.developer.phimtatnhanh.ui.cleanjunk.CleanJunkActivity;
import com.developer.phimtatnhanh.ui.garelly.GarellyActivity;
import com.developer.phimtatnhanh.ui.home.HomeActivity;
import com.developer.phimtatnhanh.ui.junk.JunkActivity;
import com.developer.phimtatnhanh.ui.menulayout.MenuActivity;
import com.developer.phimtatnhanh.ui.settingcapturevideo.SettingCaptureVideoActivity;
import com.developer.phimtatnhanh.ui.touch.TouchActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class)
public interface ActivityComponent extends ApplicationComponent {

    void inject(HomeActivity mainActivity);

    void inject(MenuActivity menuLayout);

    void inject(TouchActivity touchActivity);

    void inject(GarellyActivity garellyActivity);

    void inject(SettingCaptureVideoActivity settingCaptureVideoActivity);

    void inject(JunkActivity junkActivity);

    void inject(CleanJunkActivity cleanJunkActivity);

}