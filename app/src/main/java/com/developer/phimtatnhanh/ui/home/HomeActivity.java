package com.developer.phimtatnhanh.ui.home;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.ads.AppLogEvent;
import com.developer.phimtatnhanh.ads.InterAds;
import com.developer.phimtatnhanh.ads.NativeAdLoader;
import com.developer.phimtatnhanh.ads.NativeAdView;
import com.developer.phimtatnhanh.ads.UnitID;
import com.developer.phimtatnhanh.base.BaseActivity;
import com.developer.phimtatnhanh.data.ListUtils;
import com.developer.phimtatnhanh.data.Pref;
import com.developer.phimtatnhanh.data.PrefUtil;
import com.developer.phimtatnhanh.delayclickview.PostDelayClick;
import com.developer.phimtatnhanh.di.component.ActivityComponent;
import com.developer.phimtatnhanh.notui.PerActivityTransparent;
import com.developer.phimtatnhanh.service.MyAdmin;
import com.developer.phimtatnhanh.service.TouchService;
import com.developer.phimtatnhanh.setuptouch.utilities.permission.ConfigPer;
import com.developer.phimtatnhanh.setuptouch.utilities.permission.PermissionUtils;
import com.developer.phimtatnhanh.ui.menulayout.TypeEditMenu;
import com.developer.phimtatnhanh.ui.touch.TypeEditTouch;
import com.developer.phimtatnhanh.util.DeviceUtils;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.suke.widget.SwitchButton;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements HomeView, View.OnClickListener, ConfigPer, UnitID {

    //region Butter Knife
    @BindView(R.id.switch_on_off)
    SwitchButton switchOnOff;

    @BindView(R.id.tv_click_one)
    AppCompatTextView tvClickOne;

    @BindView(R.id.cs_click_one)
    ConstraintLayout csClickOne;

    @BindView(R.id.tv_click_two)
    AppCompatTextView tvClickTwo;

    @BindView(R.id.cs_click_two)
    ConstraintLayout csClickTwo;

    @BindView(R.id.tv_click_long)
    AppCompatTextView tvClickLong;

    @BindView(R.id.cs_click_long)
    ConstraintLayout csClickLong;

    @BindView(R.id.tv_float_style)
    AppCompatTextView tvFloatStyle;

    @BindView(R.id.cs_float_style)
    ConstraintLayout csFloatStyle;

    @BindView(R.id.cs_float_size)
    ConstraintLayout csFloatSize;

    @BindView(R.id.cs_float_color)
    ConstraintLayout csFloatColor;

    @BindView(R.id.cs_menu_layout)
    ConstraintLayout csMenuLayout;

    @BindView(R.id.switch_menu_locaiton)
    SwitchButton switchMenuLocaiton;

    @BindView(R.id.cs_menu_location)
    ConstraintLayout csMenuLocation;

    @BindView(R.id.cs_menu_color)
    ConstraintLayout csMenuColor;

    @BindView(R.id.cs_rate)
    ConstraintLayout csRate;

    @BindView(R.id.cs_share)
    ConstraintLayout csShare;

    @BindView(R.id.cs_policy)
    ConstraintLayout csPolicy;

    @BindView(R.id.switch_menu_bg_color)
    SwitchButton switchMenuBgColor;

    @BindView(R.id.cs_menu_bg_color)
    ConstraintLayout csMenuBgColor;

    @BindView(R.id.switch_menu_bg_photo)
    SwitchButton switchMenuBgPhoto;

    @BindView(R.id.cs_menu_bg_photo)
    ConstraintLayout csMenuBgPhoto;

    @BindView(R.id.cs_menu_photo)
    ConstraintLayout csMenuPhoto;

    @BindView(R.id.cs_restore_menu_touch)
    ConstraintLayout csRestoreMenuTouch;

    @BindView(R.id.nv_view)
    NativeAdView nvView;

    //endregion

    @Inject
    PrefUtil prefUtil;

    @Inject
    ListUtils listUtils;
    @BindView(R.id.switch_brightness_fix)
    SwitchButton switchBrightnessFix;
    @BindView(R.id.cs_brightness)
    ConstraintLayout csBrightness;
    @BindView(R.id.switch_capture_preview)
    SwitchButton switchCapturePreview;
    @BindView(R.id.cs_capture_preview)
    ConstraintLayout csCapturePreview;
    @BindView(R.id.cs_garelly)
    ConstraintLayout csGarelly;
    @BindView(R.id.cs_video)
    ConstraintLayout csVideo;
    @BindView(R.id.cs_setting_video)
    ConstraintLayout csSettingVideo;
    @BindView(R.id.cr_all_per)
    CardView crAllPer;

    @OnClick(R.id.cr_open_per)
    public void onClickPer(View view) {
        PostDelayClick.get().postDelayViewClick(view);
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter",
                "com.miui.permcenter.permissions.PermissionsEditorActivity");
        intent.putExtra("extra_pkgname", getPackageName());
        try {
            this.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isRestore = false;

    private HomePresenter homePresenter;

    public HomePresenter getHomePresenter() {
        return homePresenter;
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected boolean fullScreen() {
        return true;
    }

    @Override
    protected void init() {
        AppLogEvent.getInstance().log("HomeActivity_Show");
        this.homePresenter = new HomePresenter(this);
        this.homePresenter.attachView(this);
        this.initView();
        this.prefUtil.postInt(Pref.W, DeviceUtils.w(this));
        InterAds.get().reload();
        this.loadAd();
    }

    @Override
    protected void onDestroy() {
        this.homePresenter.detachView();
        super.onDestroy();
    }

    private void initView() {
        String manufacturer = Build.MANUFACTURER.toUpperCase();
        String xiaomi = "XIAOMI";
        if (!manufacturer.equals(xiaomi)) {
            this.crAllPer.setVisibility(View.GONE);
        } else {
            this.crAllPer.setVisibility(View.VISIBLE);
        }
        this.handler = new Handler(Looper.getMainLooper());
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        ComponentName compName = new ComponentName(this, MyAdmin.class);
        devicePolicyManager.removeActiveAdmin(compName);
        this.clickView(
                this.csClickLong,
                this.csClickOne,
                this.csClickTwo,
                this.csFloatColor,
                this.csFloatSize,
                this.csFloatStyle,
                this.csMenuColor,
                this.csMenuLocation,
                this.csMenuLayout,
                this.csPolicy,
                this.csRate,
                this.csShare,
                this.csMenuColor,
                this.csMenuBgColor,
                this.csMenuPhoto,
                this.csMenuBgPhoto,
                this.csRestoreMenuTouch,
                this.csBrightness,
                this.csCapturePreview,
                this.csGarelly,
                this.csVideo,
                this.csSettingVideo
        );

        if (this.prefUtil != null && this.listUtils != null) {
            setTextSettingCreate(Pref.KEY_CLICK_SINGLE);
            setTextSettingCreate(Pref.KEY_CLICK_DOUBLE);
            setTextSettingCreate(Pref.KEY_CLICK_LONG);
        }

        this.switchOnOff.setOnCheckedChangeListener((view, isChecked) -> {
            if (!PermissionUtils.checkPerSystemAler(this)) {
                PerActivityTransparent.open(this, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE, 0);
                return;
            }
            if (isChecked) {
                TouchService.start(this);
                return;
            }
            TouchService.stop(this);
        });

        this.switchMenuLocaiton.setOnCheckedChangeListener((view, isChecked) -> this.prefUtil.postBool(Pref.TOUCH_LOCK, isChecked));
        this.switchMenuBgColor.setOnCheckedChangeListener((view, isChecked) -> this.setSwBgMenu(this.csMenuColor, this.csMenuPhoto, isChecked, Pref.BG_COLOR, this.switchMenuBgPhoto, Pref.BG_PHOTO));
        this.switchMenuBgPhoto.setOnCheckedChangeListener((view, isChecked) -> this.setSwBgMenu(this.csMenuPhoto, this.csMenuColor, isChecked, Pref.BG_PHOTO, this.switchMenuBgColor, Pref.BG_COLOR));
        this.switchBrightnessFix.setOnCheckedChangeListener((view, isChecked) -> this.prefUtil.postBool(Pref.BRIGHTNESS, isChecked));
        this.switchCapturePreview.setOnCheckedChangeListener((view, isChecked) -> this.prefUtil.postBool(Pref.CAPTURE_SCREEN_PREVIEW, isChecked));
    }

    private void setSwBgMenu(ConstraintLayout c2, ConstraintLayout c1, boolean isChecked, String bgPhoto, SwitchButton sw, String bgColor) {
        if (this.isRestore) {
            return;
        }
        if (isChecked) {
            this.isAlphaAndClickColorPhoto(c1, 0.5f, false, c2, 1f, true);
            this.prefUtil.postString(Pref.BG_MENU, bgPhoto);
            sw.setChecked(false);
        } else {
            this.isAlphaAndClickColorPhoto(c1, 1f, true, c2, 0.5f, false);
            sw.setChecked(true);
            this.prefUtil.postString(Pref.BG_MENU, bgColor);
        }
    }

    private void clickView(View... views) {
        if (views == null || views.length == 0) {
            return;
        }
        for (View view : views) {
            if (view == null) {
                continue;
            }
            view.setOnClickListener(this);
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public FragmentManager fragmentManager() {
        return getSupportFragmentManager();
    }

    @Override
    public void onClick(View view) {
        this.getHomePresenter().clickView(view);
    }

    @Override
    public void onUpdateTextSetting(String text, String key) {
        this.setTextSetting(text, key);
    }

    @Override
    public void onLocationTouch() {
        this.switchMenuLocaiton.setChecked(!this.switchMenuLocaiton.isChecked());
    }

    @Override
    public void onBgColorMenu() {
        this.switchMenuBgColor.setChecked(!this.switchMenuBgColor.isChecked());
    }

    @Override
    public void onBgPhotoMenu() {
        this.switchMenuBgPhoto.setChecked(!this.switchMenuBgPhoto.isChecked());
    }

    @Override
    public void onBrightness() {
        this.switchBrightnessFix.setChecked(!this.switchBrightnessFix.isChecked());
    }

    @Override
    public void onCapturePreview() {
        this.switchCapturePreview.setChecked(!this.switchCapturePreview.isChecked());
    }

    @Override
    public void startActivity(Class a, TypeEditMenu typeEditMenu, TypeEditTouch typeEditTouch) {

        if (typeEditMenu != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(TYPE, typeEditMenu);
            InterAds.get().show(() -> {
                InterAds.get().reload();
                this.startActivity(a, false, bundle);
            });
            return;
        }
        if (typeEditTouch != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(TYPE, typeEditTouch);
            InterAds.get().show(() -> {
                InterAds.get().reload();
                this.startActivity(a, false, bundle);
            });
            return;
        }
        InterAds.get().show(() -> {
            InterAds.get().reload();
            this.startActivity(a, false);
        });
    }

    @Override
    public void startActivityCustom(Intent intent) {
        InterAds.get().show(() -> {
            InterAds.get().reload();
            this.startActivity(intent);
        });
    }

    @Override
    public void onRestoreMenuTouch() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.warning));
        alertDialog.setMessage(getString(R.string.warning_user));
        alertDialog.setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
            this.prefUtil.clearKey(Pref.BG_MENU);
            this.prefUtil.clearKey(Pref.BODER_MENU_TOUCH);
            this.prefUtil.clearKey(Pref.ALPHA_MENU_TOUCH);
            this.prefUtil.clearKey(Pref.COLOR_MENU_TOUCH);
            this.prefUtil.clearKey(Pref.PHOTO_PATH_MENU_TOUCH);
            this.prefUtil.clearKey(Pref.PHOTO_BLUR_MENU_TOUCH);
            this.isRestore = true;
            this.updateViewBg();
            this.handler.postDelayed(() -> this.isRestore = false, 500);
        });
        alertDialog.setNegativeButton(getString(R.string.cancel), null);
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PermissionUtils.isRunningService(this, TouchService.class, this.switchOnOff);
        this.switchMenuLocaiton.setChecked(PrefUtil.get().getBool(Pref.TOUCH_LOCK, false));
        this.switchBrightnessFix.setChecked(PrefUtil.get().getBool(Pref.BRIGHTNESS, false));
        this.switchCapturePreview.setChecked(PrefUtil.get().getBool(Pref.CAPTURE_SCREEN_PREVIEW, true));
        this.updateViewBg();
    }

    private void updateViewBg() {
        this.switchMenuBgColor.setChecked(PrefUtil.get().getString(Pref.BG_MENU, Pref.BG_COLOR).equals(Pref.BG_COLOR));
        this.switchMenuBgPhoto.setChecked(PrefUtil.get().getString(Pref.BG_MENU, Pref.BG_COLOR).equals(Pref.BG_PHOTO));
        if (PrefUtil.get().getString(Pref.BG_MENU, Pref.BG_COLOR).equals(Pref.BG_COLOR)) {
            isAlphaAndClickColorPhoto(this.csMenuPhoto, 0.5f, false, this.csMenuColor, 1f, true);
        } else {
            isAlphaAndClickColorPhoto(this.csMenuColor, 0.5f, false, this.csMenuPhoto, 1f, true);
        }
    }

    private void isAlphaAndClickColorPhoto(ConstraintLayout csMenuColor, float v, boolean b, ConstraintLayout csMenuPhoto, float v2, boolean b2) {
        csMenuColor.setAlpha(v);
        csMenuColor.setClickable(b);
        csMenuPhoto.setAlpha(v2);
        csMenuPhoto.setClickable(b2);
    }

    private void setTextSettingCreate(String keyClick) {
        setTextSetting(this.listUtils.listStringClickTouch().get(this.prefUtil.getInt(keyClick)), keyClick);
    }

    private void setTextSetting(String text, String key) {
        int position = PrefUtil.get().getInt(Pref.KEY_CLICK_SINGLE);
        if (position == 4) {
            //Disable double click
            this.csClickTwo.setAlpha(0.5f);
            this.csClickTwo.setClickable(false);
        } else {
            this.csClickTwo.setAlpha(1f);
            this.csClickTwo.setClickable(true);
        }
        if (key.equals(Pref.KEY_CLICK_SINGLE)) {
            this.tvClickOne.setText(text);
            return;
        }
        if (key.equals(Pref.KEY_CLICK_DOUBLE)) {
            this.tvClickTwo.setText(text);
            return;
        }
        if (key.equals(Pref.KEY_CLICK_LONG)) {
            this.tvClickLong.setText(text);
        }
    }

    @OnClick(R.id.g2)
    public void onClick() {
    }

    private void loadAd() {
        NativeAdLoader.newInstance().setAdPosition(NT_AD)
                .setAdUnit(NT_AD_KEY_1)
                .setLiveKey(NT_AD_LIVE)
                .setOnAdLoaderListener(new NativeAdLoader.NativeAdLoaderListener() {
                    @Override
                    public void onAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        super.onAdLoaded(unifiedNativeAd);
                        if (nvView != null) {
                            nvView.show(unifiedNativeAd);
                        }
                    }

                    @Override
                    public void onAdLoadFailed(String message) {
                        super.onAdLoadFailed(message);
                        if (nvView != null) {
                            nvView.setVisibility(View.GONE);
                        }
                    }
                }).loadAd(this);
    }
}