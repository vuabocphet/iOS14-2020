package com.developer.phimtatnhanh.data;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import android.util.Log;

import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.setuptouch.config.ConfigAll;
import com.developer.phimtatnhanh.setuptouch.gridviewstarapp.AppItem;
import com.developer.phimtatnhanh.setuptouch.gridviewstarapp.AppUtil;
import com.developer.phimtatnhanh.setuptouch.gridviewstarapp.TypeStar;
import com.developer.phimtatnhanh.ui.touch.gridview.IconModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ListUtils implements ConfigAll {


    public static ListUtils get(Context context) {
        return new ListUtils(context);
    }

    private Context context;

    private Disposable disposable;

    @Inject
    public ListUtils(Context context) {
        this.context = context;
    }

    public List<String> listStringClickTouch() {
        if (this.context == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(
                getString(R.string.not_click_touch),
                getString(R.string.back_click_touch),
                getString(R.string.home_click_touch),
                getString(R.string.recent_click_touch),
                getString(R.string.menu_floating_click_touch),
                getString(R.string.notification_click_touch),
                getString(R.string.lock_click_touch)
        );
    }

    public List<String> listStringMenuTouch() {
        if (this.context == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(
                getString(R.string.not_click_touch),
                getString(R.string.home_menu_touch),
                getString(R.string.recent_menu_touch),
                getString(R.string.back_menu_touch),
                getString(R.string.air_menu_touch),
                getString(R.string.bluetool_menu_touch),
                getString(R.string.bright_menu_touch),
                getString(R.string.setting_menu_touch),
                getString(R.string.notification_menu_touch),
                getString(R.string.flash_menu_touch),
                getString(R.string.loc_screen_menu_touch),
                getString(R.string.rotate_menu_touch),
                getString(R.string.volue_up_menu_touch),
                getString(R.string.volue_out_menu_touch),
                getString(R.string.wifi_menu_touch),
                getString(R.string.network_menu_touch),
                getString(R.string.star_menu_touch),
                getString(R.string.time_bright_menu_touch),
                getString(R.string.device_menu_touch),
                getString(R.string.capture_video_menu_touch),
                getString(R.string.capture_menu_touch),
                getString(R.string.power_menu_touch),
                getString(R.string.clean_junk)
        );
    }

    public List<Integer> listIconMenuTouch() {
        if (this.context == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(
                R.drawable.all_ic_not,
                R.drawable.ic_all_ic_home,
                R.drawable.ic_all_ic_recent,
                R.drawable.ic_all_ic_back,
                R.drawable.ic_all_ic_planes,
                R.drawable.ic_all_ic_bluetool,
                R.drawable.ic_all_ic_bright,
                R.drawable.ic_all_ic_settings,
                R.drawable.ic_all_ic_notification,
                R.drawable.ic_all_ic_flashlight,
                R.drawable.ic_all_ic_lock,
                R.drawable.ic_all_ic_rotate,
                R.drawable.ic_all_ic_volue_up,
                R.drawable.ic_all_ic_volue_out,
                R.drawable.ic_all_ic_wifi,
                R.drawable.ic_all_ic_network_mobile,
                R.drawable.ic_all_ic_star,
                R.drawable.ic_all_ic_time_bright,
                R.drawable.ic_all_ic_recent,
                R.drawable.ic_all_ic_video,
                R.drawable.ic_all_ic_capture,
                R.drawable.all_ic_power,
                R.drawable.all_ic_magic_broom
        );
    }

    public List<Integer> listIconClickMenuTouch() {
        if (this.context == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(
                R.drawable.all_ic_not,
                R.drawable.all_click_home,
                R.drawable.all_click_recent,
                R.drawable.all_click_back,
                R.drawable.all_click_planes,
                R.drawable.all_click_bluetool,
                R.drawable.all_click_bright,
                R.drawable.all_click_setting,
                R.drawable.all_click_notification,
                R.drawable.all_click_flashlight,
                R.drawable.all_click_lock,
                R.drawable.all_click_rotate,
                R.drawable.all_click_volue_up,
                R.drawable.all_click_volue_out,
                R.drawable.all_click_wifi,
                R.drawable.all_click_network,
                R.drawable.all_click_star,
                R.drawable.all_click_time_bright,
                R.drawable.all_click_recent,
                R.drawable.all_click_capture_video,
                R.drawable.all_click_capture,
                R.drawable.all_click_power,
                R.drawable.all_click_boom
        );
    }

    public List<MenuModel> menuModelList() {
        List<String> titles = this.listStringMenuTouch();
        List<Integer> icons1 = this.listIconMenuTouch();
        List<Integer> icons2 = this.listIconClickMenuTouch();
        if (titles.size() != icons1.size() || titles.size() != icons2.size()) {
            return new ArrayList<>();
        }
        List<MenuModel> menuModels = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            Log.d("TinhNv", "menuModelList: " + i + "----" + titles.get(i));
            menuModels.add(MenuModel.init().setTitle(titles.get(i)).setIcon(new int[]{icons1.get(i), icons2.get(i)}));
        }
        return menuModels;
    }


    public List<String> getListPackNameStarApp() {
        List<String> stringList = new ArrayList<>();
        String string0 = PrefUtil.get().getString(Pref.A0);
        String string1 = PrefUtil.get().getString(Pref.A1);
        String string2 = PrefUtil.get().getString(Pref.A2);
        String string3 = PrefUtil.get().getString(Pref.A3);
        String string4 = PrefUtil.get().getString(Pref.A4, backStarAppMenuTouch);
        String string5 = PrefUtil.get().getString(Pref.A5);
        String string6 = PrefUtil.get().getString(Pref.A6);
        String string7 = PrefUtil.get().getString(Pref.A7);
        String string8 = PrefUtil.get().getString(Pref.A8);
        stringList.add(string0);
        stringList.add(string1);
        stringList.add(string2);
        stringList.add(string3);
        stringList.add(string4);
        stringList.add(string5);
        stringList.add(string6);
        stringList.add(string7);
        stringList.add(string8);
        return stringList;
    }

    public List<IconModel> getListIconTouch() {
        List<Integer> listIcon = new ArrayList<>();
        List<IconModel> iconModels = new ArrayList<>();
        listIcon.add(R.drawable.all_ic_logo);
        listIcon.add(R.raw.a_1);
        listIcon.add(R.raw.a_2);
        listIcon.add(R.raw.a_3);
        listIcon.add(R.raw.a_4);
        listIcon.add(R.raw.a_5);
        listIcon.add(R.raw.a_6);
        listIcon.add(R.raw.a_7);
        listIcon.add(R.raw.a_8);
        listIcon.add(R.raw.a_9);
        listIcon.add(R.raw.a_10);
        listIcon.add(R.raw.a_11);
        listIcon.add(R.raw.a_12);
        listIcon.add(R.raw.a_13);
        listIcon.add(R.raw.a_14);
        listIcon.add(R.raw.a_15);
        listIcon.add(R.raw.a_16);
        listIcon.add(R.raw.a_17);
        listIcon.add(R.raw.a_18);
        listIcon.add(R.raw.a_19);
        listIcon.add(R.raw.a_20);
        listIcon.add(R.raw.a_21);
        listIcon.add(R.raw.a_22);
        listIcon.add(R.raw.a_23);
        listIcon.add(R.raw.a_24);
        listIcon.add(R.raw.a_25);
        listIcon.add(R.raw.a_26);
        listIcon.add(R.raw.a_27);
        listIcon.add(R.raw.a_28);
        listIcon.add(R.raw.a_29);
        listIcon.add(R.raw.a_30);
        listIcon.add(R.raw.a_31);
        listIcon.add(R.raw.a_32);
        listIcon.add(R.raw.a_33);
        listIcon.add(R.raw.a_34);
        listIcon.add(R.raw.a_35);
        listIcon.add(R.raw.a_36);
        listIcon.add(R.raw.a_37);
        listIcon.add(R.raw.a_38);
        listIcon.add(R.raw.a_39);
        listIcon.add(R.raw.a_40);
        listIcon.add(R.raw.a_41);
        listIcon.add(R.raw.a_42);
        listIcon.add(R.raw.a_43);
        listIcon.add(R.raw.a_44);
        listIcon.add(R.raw.a_45);
        listIcon.add(R.raw.a_46);
        listIcon.add(R.raw.a_47);
        listIcon.add(R.raw.a_48);
        listIcon.add(R.raw.a_49);
        listIcon.add(R.raw.a_50);

        int anInt = PrefUtil.get().getInt(Pref.ICON_TOUCH, R.drawable.all_ic_logo);
        for (Integer integer : listIcon) {
            if (integer == anInt) {
                iconModels.add(IconModel.create().setIconId(integer).setStatus(true));
                continue;
            }
            iconModels.add(IconModel.create().setIconId(integer).setStatus(false));
        }
        return iconModels;
    }


    private List<AppItem> appItemListStar() {
        AppUtil init = AppUtil.init();
        if (this.context == null) {
            return new ArrayList<>();
        }
        List<AppItem> appItems = new ArrayList<>();
        for (String packname : this.getListPackNameStarApp()) {
            if (TextUtils.isEmpty(packname)) {
                appItems.add(AppItem.init().setTypeStar(TypeStar.NONE));
                continue;
            }
            if (packname.equals(backStarAppMenuTouch)) {
                appItems.add(AppItem.init().setTypeStar(TypeStar.BACK));
                continue;
            }
            appItems.add(init.getApplication(this.context.getPackageManager(), packname));
        }
        return appItems;
    }

    private List<AppItem> appItemListAll() {
        if (this.context == null) {
            return new ArrayList<>();
        }

        List<AppItem> appItems = new ArrayList<>();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = context.getPackageManager().queryIntentActivities(mainIntent, 0);
        if (pkgAppsList.isEmpty()) {
            return new ArrayList<>();
        }
        PackageManager packageManager = context.getPackageManager();
        for (ResolveInfo resolveInfo : pkgAppsList) {
            String packageName = resolveInfo.activityInfo.packageName;
            AppItem application = AppUtil.init().getApplication(packageManager, packageName);
            appItems.add(application);
        }
        return appItems;
    }

    public void starGetAppItemList(LifeStarApp lifeStarApp) {
        if (lifeStarApp == null) {
            return;
        }
        Single<List<AppItem>> single = this.rxGetAppStar();
        single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<AppItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(List<AppItem> appItems) {
                        lifeStarApp.onResult(appItems);
                        if (disposable != null) {
                            disposable.dispose();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TinhNv", "onError: " + e);
                    }
                });
    }

    public void getListAppAll(LifeStarApp lifeStarApp) {
        if (lifeStarApp == null) {
            return;
        }
        Single<List<AppItem>> single = this.rxGetAllApp();
        single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<AppItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(List<AppItem> appItems) {
                        lifeStarApp.onResult(appItems);
                        if (disposable != null) {
                            disposable.dispose();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TinhNv", "onError: " + e);
                    }
                });
    }

    public interface LifeStarApp {
        void onResult(List<AppItem> appItems);
    }

    private Single<List<AppItem>> rxGetAppStar() {
        return Single.create(emitter -> {
            try {
                List<AppItem> appItems = this.appItemListStar();
                emitter.onSuccess(appItems);
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        });
    }

    private Single<List<AppItem>> rxGetAllApp() {
        return Single.create(emitter -> {
            try {
                List<AppItem> appItems = this.appItemListAll();
                emitter.onSuccess(appItems);
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        });
    }

    public String getString(int idS) {
        if (this.context == null) {
            return "";
        }
        return this.context.getString(idS);
    }

    public List<String> listAudioSource() {
        return Arrays.asList(
                "Default",
                "Camcoder",
                "Mic"

        );
    }

    public List<String> listVideoEncoder() {
        return Arrays.asList(
                "Default",
                "H264",
                "H263",
                "HEVC",
                "MPEG_4_SP",
                "VP8"
        );
    }

    public List<String> listVideoResolution() {
        return Arrays.asList(
                "426 * 240",
                "640 * 360",
                "854 * 480",
                "1280 * 720",
                "1920 * 1080"
        );
    }

    public List<String> listVideoFrameRate() {
        return Arrays.asList(
                "60 Fps",
                "50 Fps",
                "48 Fps",
                "30 Fps",
                "25 Fps",
                "24 Fps"

        );
    }

    public List<String> listVideoBitRate() {
        return Arrays.asList(
                "Auto",
                "12 Mbps",
                "8 Mbps",
                "7.5 Mbps",
                "5 Mbps",
                "4 Mbps",
                "2.5 Mbps",
                "1.5 Mbps",
                "1 Mbps"
        );
    }

    public List<String> listVideoFormat() {
        return Arrays.asList(
                "Default",
                "MPEG_4",
                "THREE_GPP",
                "WEBM"
        );
    }
}
