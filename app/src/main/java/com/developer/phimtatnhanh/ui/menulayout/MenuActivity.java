package com.developer.phimtatnhanh.ui.menulayout;


import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.app.AppContext;
import com.developer.phimtatnhanh.base.BaseActivity;
import com.developer.phimtatnhanh.data.ListUtils;
import com.developer.phimtatnhanh.data.MenuModel;
import com.developer.phimtatnhanh.data.Pref;
import com.developer.phimtatnhanh.data.PrefUtil;
import com.developer.phimtatnhanh.delayclickview.PostDelayClick;
import com.developer.phimtatnhanh.di.component.ActivityComponent;
import com.developer.phimtatnhanh.setuptouch.config.ConfigAll;
import com.developer.phimtatnhanh.setuptouch.config.ConstKey;
import com.developer.phimtatnhanh.setuptouch.utilities.permission.ConfigPer;
import com.developer.phimtatnhanh.setuptouch.utilities.permission.LifePermission;
import com.developer.phimtatnhanh.setuptouch.utilities.permission.PermissionUtils;
import com.developer.phimtatnhanh.ui.menulayout.dialogoptionfun.DialogOptionFun;
import com.developer.phimtatnhanh.util.PathUtil;
import com.developer.phimtatnhanh.util.PixelUtil;
import com.developer.phimtatnhanh.view.CompatView;
import com.divyanshu.colorseekbar.ColorSeekBar;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class MenuActivity extends BaseActivity implements MenuView, ConfigAll, ConstKey, LifePermission {

    @Inject
    ListUtils listUtils;

    @BindView(R.id.cs_color)
    ConstraintLayout csColor;

    @BindView(R.id.color_seek)
    ColorSeekBar colorSeek;

    @BindView(R.id.boder_seek)
    SeekBar boderSeek;

    @BindView(R.id.alpha_seek)
    AppCompatSeekBar alphaSeek;
    @BindView(R.id.btn_select_photo)
    Button btnSelectPhoto;

    @BindView(R.id.blur_seek)
    AppCompatSeekBar blurSeek;

    @BindView(R.id.blur_view)
    Group blurView;
    @BindView(R.id.bt_close)
    AppCompatImageView btClose;

    private MenuPresenter menuPresenter;
    private PermissionUtils permissionUtils;

    @BindView(R.id.tv)
    AppCompatTextView tv;

    @BindView(R.id.compatView)
    CompatView compatView;

    @BindView(R.id.iv_bg_menu)
    RoundedImageView ivBgMenu;

    @BindView(R.id.iv_bg)
    AppCompatImageView ivBg;

    @BindView(R.id.include)
    ConstraintLayout csHomeMenuTouch;

    @BindView(R.id.iv_top_to_center)
    public AppCompatImageView ivTopToCenter;
    @BindView(R.id.tv_top_to_center)
    public AppCompatTextView tvTopToCenter;

    @BindView(R.id.iv_bottom_to_center)
    public AppCompatImageView ivBottomToCenter;
    @BindView(R.id.tv_bottom_to_center)
    public AppCompatTextView tvBottomToCenter;

    @BindView(R.id.iv_top_to_left)
    public AppCompatImageView ivTopToLeft;
    @BindView(R.id.tv_top_to_left)
    public AppCompatTextView tvTopToLeft;

    @BindView(R.id.iv_top_to_right)
    public AppCompatImageView ivTopToRight;
    @BindView(R.id.tv_top_to_right)
    public AppCompatTextView tvTopToRight;

    @BindView(R.id.iv_bottom_to_right)
    public AppCompatImageView ivBottomToRight;
    @BindView(R.id.tv_bottom_to_right)
    public AppCompatTextView tvBottomToRight;

    @BindView(R.id.iv_bottom_to_left)
    public AppCompatImageView ivBottomToLeft;
    @BindView(R.id.tv_bottom_to_left)
    public AppCompatTextView tvBottomToLeft;

    @OnClick(R.id.btn_select_photo)
    public void selectPhoto(View view) {
        PostDelayClick.get().postDelayViewClick(view);
        boolean b = this.allReadWriteStogre();
        if (!b) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.permissionUtils.requestPermissions(ConfigPer.KEY_IMAGE_BG, ConfigPer.READWRITE);
            }
            return;
        }
        CropImage.album(this);
    }

    @OnClick({R.id.ll_bottom_to_centter,
            R.id.ll_bottom_to_left,
            R.id.ll_bottom_to_right,
            R.id.ll_top_to_centter,
            R.id.ll_top_to_left,
            R.id.ll_top_to_right})
    public void onClickV(View view) {
        PostDelayClick.get().postDelayViewClick(view);
        DialogOptionFun.create(this, this.listUtils, this.getKeyFromIDView(view)).setResultFun(this::setupView).show();
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_menu_layout;
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
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
    protected boolean fullScreen() {
        return true;
    }

    @Override
    protected void init() {
        this.menuPresenter = new MenuPresenter();
        this.menuPresenter.attachView(this);
        this.permissionUtils = PermissionUtils.init(this);
        this.setupView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == KEY_IMAGE_BG && resultCode == RESULT_OK) {
            CropImage.crop(data, this);
            return;
        }
        if (requestCode != com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            return;
        }
        if (resultCode == com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
            return;
        }
        if (resultCode == RESULT_OK) {
            com.theartofdev.edmodo.cropper.CropImage.ActivityResult result = com.theartofdev.edmodo.cropper.CropImage.getActivityResult(data);
            if (result == null) {
                return;
            }
            Uri resultUri = result.getUri();
            try {
                String path = PathUtil.getPath(this, resultUri);
                if (TextUtils.isEmpty(path)) {
                    return;
                }
                String pathCurren = moveFile(new File(path), this.getExternalFilesDir("bg_photo.jpg"));
                PrefUtil.get().postString(Pref.PHOTO_PATH_MENU_TOUCH, pathCurren);
                this.loadImage();
            } catch (URISyntaxException | IOException e) {
                Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private String moveFile(File file, File dir) throws IOException {
        File newFile = new File(dir, file.getName());
        try (FileChannel outputChannel = new FileOutputStream(newFile).getChannel(); FileChannel inputChannel = new FileInputStream(file).getChannel()) {
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
            file.delete();
        }
        return newFile.getPath();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        this.permissionUtils.permisionChange(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        this.menuPresenter.detachView();
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        Bundle bundle = intent.getBundleExtra(DATA_BUNDEL);
        if (bundle == null) {
            finish();
            return;
        }
        TypeEditMenu typeEditMenu = (TypeEditMenu) bundle.getSerializable(TYPE);
        if (typeEditMenu == null) {
            finish();
            return;
        }
        this.setupDataFromPer();
        switch (typeEditMenu) {
            case EDIT_BG_COLOR:
                ImageViewCompat.setImageTintList(this.btClose, ColorStateList.valueOf(getResources().getColor(R.color.black)));
                this.compatView.setVisibility(View.VISIBLE);
                this.tv.setText(getString(R.string.set_color));
                this.tv.setTextColor(getResources().getColor(R.color.black));
                this.editBgColor();
                break;
            case EDIT_BG_PHOTO:
                ImageViewCompat.setImageTintList(this.btClose, ColorStateList.valueOf(getResources().getColor(R.color.black)));
                this.ivBgMenu.setVisibility(View.VISIBLE);
                this.btnSelectPhoto.setVisibility(View.VISIBLE);
                this.blurView.setVisibility(View.VISIBLE);
                this.tv.setTextColor(getResources().getColor(R.color.black));
                this.tv.setText(getString(R.string.set_photo));
                this.loadImage();
                this.editBgPhoto();
                break;
            case EDIT_MENU_FUN:
                this.csColor.setVisibility(View.GONE);
                this.ivBg.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(R.drawable.bg_menu_layout).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
                        .into(this.ivBg);
                this.csHomeMenuTouch.setBackgroundResource(R.drawable.all_bg_stroke_white);
                break;
        }

    }

    private void loadImage() {
        String path = PrefUtil.get().getString(Pref.PHOTO_PATH_MENU_TOUCH, "");
        int blur = PrefUtil.get().getInt(Pref.PHOTO_BLUR_MENU_TOUCH, 25);
        Glide.with(this)
                .load(TextUtils.isEmpty(path) ? R.drawable.bg_menu_layout : path).diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.bg_menu_layout)
                .error(R.drawable.bg_menu_layout)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(blur, 3)))
                .into(this.ivBgMenu);
    }

    private void setupDataFromPer() {
        int boderMenuTouch = PrefUtil.get().getInt(Pref.BODER_MENU_TOUCH, 9);
        int alphaMenuTouch = PrefUtil.get().getInt(Pref.ALPHA_MENU_TOUCH, 250);
        int blurMenuTouch = PrefUtil.get().getInt(Pref.PHOTO_BLUR_MENU_TOUCH, 25);
        int anInt = PrefUtil.get().getInt(Pref.COLOR_MENU_TOUCH, ContextCompat.getColor(this, R.color.nau));
        this.compatView.setColor(anInt);
        this.compatView.setBoderLayout(PixelUtil.getDimenPixelFromPosition(boderMenuTouch));
        this.ivBgMenu.setCornerRadius(PixelUtil.getDimenPixelFromPosition(boderMenuTouch));
        this.compatView.setAlphaLayout(alphaMenuTouch);
        float alpha = (float) alphaMenuTouch / 255;
        this.ivBgMenu.setAlpha(alpha);
        this.boderSeek.setMax(30);
        this.boderSeek.setProgress(boderMenuTouch);
        this.alphaSeek.setMax(255);
        this.alphaSeek.setProgress(alphaMenuTouch);
        this.blurSeek.setMax(30);
        this.blurSeek.setProgress(blurMenuTouch);
    }

    private void editBgPhoto() {

        this.setBoderBgMenuTouch(false);

        this.setAlphaBgMenuTouch(false);

        this.blurSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i > 0) {
                    PrefUtil.get().postInt(Pref.PHOTO_BLUR_MENU_TOUCH, i);
                    loadImage();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    private void editBgColor() {

        this.colorSeek.setOnColorChangeListener(i -> {
            this.compatView.setColor(i);
            PrefUtil.get().postInt(Pref.COLOR_MENU_TOUCH, i);
        });

        this.setBoderBgMenuTouch(true);

        this.setAlphaBgMenuTouch(true);
    }

    private void setBoderBgMenuTouch(boolean is) {
        this.boderSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int dimenPixelFromPosition = PixelUtil.getDimenPixelFromPosition(i);
                if (is) {
                    compatView.setBoderLayout(dimenPixelFromPosition);
                } else {
                    ivBgMenu.setCornerRadius(dimenPixelFromPosition);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                PrefUtil.get().postInt(Pref.BODER_MENU_TOUCH, progress);
            }
        });
    }

    private void setAlphaBgMenuTouch(boolean is) {
        this.alphaSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (is) {
                    compatView.setAlphaLayout(i);
                } else {
                    float alpha = (float) i / 255;
                    ivBgMenu.setAlpha(alpha);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                PrefUtil.get().postInt(Pref.ALPHA_MENU_TOUCH, progress);
            }
        });
    }

    private void setupView() {
        int menuBottomToCenter = getFun(Pref.MENU_BOTTOM_TO_CENTER, MENU_HOME_SCREEN);
        int menuBottomToLeft = getFun(Pref.MENU_BOTTOM_TO_LEFT, MENU_STAR);
        int menuBottomToRight = getFun(Pref.MENU_BOTTOM_TO_RIGHT, ConstKey.MENU_CAPTURE_SCREEN_VIDEO);
        int menuTopToCenter = getFun(Pref.MENU_TOP_TO_CENTER, MENU_NOTIFICATION);
        int menuTopToLeft = getFun(Pref.MENU_TOP_TO_LEFT, MENU_CAPTURE_SCREEN);
        int menuTopToRight = getFun(Pref.MENU_TOP_TO_RIGHT, MENU_BACK);
        MenuModel menuModelBottomToCenter = getMenuModelPosition(menuBottomToCenter);
        MenuModel menuModelBottomToLeft = getMenuModelPosition(menuBottomToLeft);
        MenuModel menuModelBottomToRight = getMenuModelPosition(menuBottomToRight);
        MenuModel menuModelTopToCenter = getMenuModelPosition(menuTopToCenter);
        MenuModel menuModelTopToLeft = getMenuModelPosition(menuTopToLeft);
        MenuModel menuModelTopToRight = getMenuModelPosition(menuTopToRight);
        this.setUpMenuModel(this.tvBottomToCenter, this.ivBottomToCenter, menuModelBottomToCenter);
        this.setUpMenuModel(this.tvBottomToLeft, this.ivBottomToLeft, menuModelBottomToLeft);
        this.setUpMenuModel(this.tvBottomToRight, this.ivBottomToRight, menuModelBottomToRight);
        this.setUpMenuModel(this.tvTopToCenter, this.ivTopToCenter, menuModelTopToCenter);
        this.setUpMenuModel(this.tvTopToLeft, this.ivTopToLeft, menuModelTopToLeft);
        this.setUpMenuModel(this.tvTopToRight, this.ivTopToRight, menuModelTopToRight);
    }

    private int getFun(String menuTopToRight, int menu) {
        return PrefUtil.get().getInt(menuTopToRight, menu);
    }

    private MenuModel getMenuModelPosition(int menuTopToRight) {
        return getMenuModel(menuTopToRight);
    }

    private MenuModel getMenuModel(int menuBottomToCenter) {
        return this.listUtils.menuModelList().get(menuBottomToCenter);
    }

    private void setUpMenuModel(AppCompatTextView tv, AppCompatImageView iv, MenuModel menuModel) {
        tv.setText(menuModel.title);
        iv.setImageResource(menuModel.icon[1]);
    }

    private String getKeyFromIDView(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.ll_bottom_to_centter:
                return Pref.MENU_BOTTOM_TO_CENTER;
            case R.id.ll_bottom_to_left:
                return Pref.MENU_BOTTOM_TO_LEFT;
            case R.id.ll_bottom_to_right:
                return Pref.MENU_BOTTOM_TO_RIGHT;
            case R.id.ll_top_to_centter:
                return Pref.MENU_TOP_TO_CENTER;
            case R.id.ll_top_to_left:
                return Pref.MENU_TOP_TO_LEFT;
            case R.id.ll_top_to_right:
                return Pref.MENU_TOP_TO_RIGHT;
        }
        return "";
    }

    @Override
    public void onAllow(int rcode) {
        if (rcode == KEY_IMAGE_BG) {
            CropImage.album(this);
        }
    }

    @Override
    public void onDenied(int rcode) {
        if (rcode == KEY_IMAGE_BG) {
            Toast.makeText(this, getString(R.string.preadwrite_denied), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAskagain(int rcode) {
        this.permissionUtils.openAppSettings(this);
    }

    private boolean allReadWriteStogre() {
        return PermissionUtils.checkPerShowDialog(AppContext.get().getContext(), ConfigPer.READWRITE);
    }

    public void onBack(View view) {
        finish();
    }
}