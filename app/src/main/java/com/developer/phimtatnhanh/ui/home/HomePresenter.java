package com.developer.phimtatnhanh.ui.home;


import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.fragment.app.DialogFragment;

import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.base.BaseApplication;
import com.developer.phimtatnhanh.base.BaseMvpPresenter;
import com.developer.phimtatnhanh.data.ListUtils;
import com.developer.phimtatnhanh.data.Pref;
import com.developer.phimtatnhanh.data.PrefUtil;
import com.developer.phimtatnhanh.dialog.OptionDialog;
import com.developer.phimtatnhanh.ui.garelly.GarellyActivity;
import com.developer.phimtatnhanh.ui.garelly.GarellyView;
import com.developer.phimtatnhanh.ui.menulayout.MenuActivity;
import com.developer.phimtatnhanh.ui.menulayout.TypeEditMenu;
import com.developer.phimtatnhanh.ui.settingcapturevideo.SettingCaptureVideoActivity;
import com.developer.phimtatnhanh.ui.touch.TouchActivity;
import com.developer.phimtatnhanh.ui.touch.TypeEditTouch;

import javax.inject.Inject;

public class HomePresenter implements BaseMvpPresenter<HomeView>, OptionDialog.SingleChoiceListener {

    private HomeView homeView;

    private DialogFragment optionDialogOne;

    private DialogFragment optionDialogTwo;

    private DialogFragment optionDialogLong;

    public HomeView getHomeView() {
        return homeView;
    }

    @Inject
    PrefUtil prefUtil;

    @Inject
    ListUtils listUtils;

    @Inject
    public HomePresenter(Context context) {
        ((BaseApplication) context.getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Override
    public void attachView(HomeView view) {
        this.homeView = view;
        this.optionDialogOne = new OptionDialog(this, getHomeView().getContext(), this.listUtils.getString(R.string.click_one), Pref.KEY_CLICK_SINGLE, this.prefUtil, this.listUtils);
        this.optionDialogTwo = new OptionDialog(this, getHomeView().getContext(), this.listUtils.getString(R.string.click_two), Pref.KEY_CLICK_DOUBLE, this.prefUtil, this.listUtils);
        this.optionDialogLong = new OptionDialog(this, getHomeView().getContext(), this.listUtils.getString(R.string.click_long), Pref.KEY_CLICK_LONG, this.prefUtil, this.listUtils);

    }

    @Override
    public void detachView() {
        this.homeView = null;
    }

    public void clickView(View view) {
        int id = view.getId();
        this.switchClick(id);
    }

    private void switchClick(int id) {
        Intent intent = new Intent(this.getHomeView().getContext(), GarellyActivity.class);
        switch (id) {
            case R.id.cs_click_one:
                if (this.optionDialogOne.isAdded()) {
                    return;
                }
                this.optionDialogOne.show(getHomeView().fragmentManager(), Pref.KEY_CLICK_SINGLE);
                break;
            case R.id.cs_click_two:
                if (this.optionDialogTwo.isAdded()) {
                    return;
                }
                this.optionDialogTwo.show(getHomeView().fragmentManager(), Pref.KEY_CLICK_DOUBLE);
                break;
            case R.id.cs_click_long:
                if (this.optionDialogLong.isAdded()) {
                    return;
                }
                this.optionDialogLong.show(getHomeView().fragmentManager(), Pref.KEY_CLICK_LONG);
                break;
            case R.id.cs_menu_layout:
                getHomeView().startActivity(MenuActivity.class, TypeEditMenu.EDIT_MENU_FUN, null);
                break;
            case R.id.cs_menu_location:
                getHomeView().onLocationTouch();
                break;
            case R.id.cs_menu_bg_color:
                getHomeView().onBgColorMenu();
                break;
            case R.id.cs_menu_bg_photo:
                getHomeView().onBgPhotoMenu();
                break;
            case R.id.cs_menu_color:
                getHomeView().startActivity(MenuActivity.class, TypeEditMenu.EDIT_BG_COLOR, null);
                break;
            case R.id.cs_menu_photo:
                getHomeView().startActivity(MenuActivity.class, TypeEditMenu.EDIT_BG_PHOTO, null);
                break;
            case R.id.cs_restore_menu_touch:
                getHomeView().onRestoreMenuTouch();
                break;

            case R.id.cs_float_style:
                getHomeView().startActivity(TouchActivity.class, null, TypeEditTouch.EDIT_ICON_TOUCH);
                break;
            case R.id.cs_float_size:
            case R.id.cs_float_color:
                getHomeView().startActivity(TouchActivity.class, null, TypeEditTouch.EDIT_BG_TOUCH);
                break;
            case R.id.cs_brightness:
                getHomeView().onBrightness();
                break;
            case R.id.cs_capture_preview:
                getHomeView().onCapturePreview();
                break;
            case R.id.cs_garelly:
                intent.putExtra(GarellyView.TYPE, GarellyView.IMG);
                getHomeView().startActivityCustom(intent);
                break;
            case R.id.cs_video:
                intent.putExtra(GarellyView.TYPE, GarellyView.VIDEO);
                getHomeView().startActivityCustom(intent);
                break;
            case R.id.cs_setting_video:
                getHomeView().startActivity(SettingCaptureVideoActivity.class, null, null);

        }
    }

    @Override
    public void onPositiveButtonClicked(String text, String key) {
        if (this.getHomeView() == null) {
            return;
        }
        this.getHomeView().onUpdateTextSetting(text, key);
    }

    @Override
    public void onNegativeButtonClicked() {
        //->>Not...
    }
}
