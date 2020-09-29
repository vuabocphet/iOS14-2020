package com.developer.phimtatnhanh.app;



import com.developer.phimtatnhanh.base.BaseApplication;
import com.developer.phimtatnhanh.data.PrefUtil;
import com.tencent.mmkv.MMKV;

public class Application extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        AppContext.create(getApplicationContext());
        MMKV.initialize(this);
        PrefUtil.init();
    }
}
