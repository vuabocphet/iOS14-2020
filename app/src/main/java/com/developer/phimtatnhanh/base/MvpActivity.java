package com.developer.phimtatnhanh.base;


import com.developer.phimtatnhanh.di.component.ActivityComponent;
import com.developer.phimtatnhanh.di.component.DaggerActivityComponent;

public abstract class MvpActivity extends BaseActivity {

    private ActivityComponent mComponent;

    @Override
    protected void init() {
       mComponent = DaggerActivityComponent.builder().applicationComponent(getApp().getApplicationComponent()).build();
    }

    protected ActivityComponent getComponent() {
        return mComponent;
    }

    protected BaseApplication getApp() {
        return (BaseApplication) getApplicationContext();
    }

}
