package com.developer.phimtatnhanh.di.component;


import com.developer.phimtatnhanh.base.BaseApplication;
import com.developer.phimtatnhanh.data.ListUtils;
import com.developer.phimtatnhanh.data.PrefUtil;
import com.developer.phimtatnhanh.di.module.ApplicationModule;
import com.developer.phimtatnhanh.ui.home.HomePresenter;
import com.developer.phimtatnhanh.ui.settingcapturevideo.SettingPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(HomePresenter homePresenter);

    void inject(SettingPresenter settingPresenter);

    void inject(BaseApplication baseApplication);

    PrefUtil prefsHelper();

    ListUtils getListUtils();

}