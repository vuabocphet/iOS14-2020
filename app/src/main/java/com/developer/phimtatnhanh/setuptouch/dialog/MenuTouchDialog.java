package com.developer.phimtatnhanh.setuptouch.dialog;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.app.AppContext;
import com.developer.phimtatnhanh.data.ListUtils;
import com.developer.phimtatnhanh.data.MenuModel;
import com.developer.phimtatnhanh.data.Pref;
import com.developer.phimtatnhanh.data.PrefUtil;
import com.developer.phimtatnhanh.setuptouch.config.ConfigAll;
import com.developer.phimtatnhanh.setuptouch.config.ConstKey;
import com.developer.phimtatnhanh.setuptouch.utilities.FunUtil;
import com.developer.phimtatnhanh.setuptouch.utilities.capturescreen.LifeRxScreenCapture;
import com.developer.phimtatnhanh.setuptouch.utilities.capturescreen.RxScreenCapture;
import com.developer.phimtatnhanh.util.PixelUtil;
import com.developer.phimtatnhanh.view.CompatView;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class MenuTouchDialog implements ConfigAll, ConstKey, LifeRxScreenCapture {

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
    @BindView(R.id.grid_star_app_menu_touch)
    public GridView gridStarAppMenuTouch;
    @BindView(R.id.cs_home_menu_touch)
    public ConstraintLayout csHomeMenuTouch;
    @BindView(R.id.cs_star_app_menu_touch)
    public ConstraintLayout csStarAppMenuTouch;
    @BindView(R.id.progress_load)
    public ProgressBar progressLoad;
    @BindView(R.id.cs_layout_all)
    ConstraintLayout csLayoutAll;
    @BindView(R.id.iv_bg)
    RoundedImageView ivBg;
    @BindView(R.id.compatView)
    CompatView compatView;

    private int menuBottomToCenter;
    private int menuBottomToLeft;
    private int menuBottomToRight;
    private int menuTopToCenter;
    private int menuTopToLeft;
    private int menuTopToRight;
    private FunUtil funUtil;

    @OnClick({R.id.ll_bottom_to_centter,
            R.id.ll_bottom_to_left,
            R.id.ll_bottom_to_right,
            R.id.ll_top_to_centter,
            R.id.ll_top_to_left,
            R.id.ll_top_to_right})
    public void onClickV(View view) {
        this.onClickView(view);
    }

    public LifeDialog lifeDialog;
    public Dialog menuDialog;
    public ListUtils listUtils;
    private boolean cancel = true;

    public void removeAll() {
        this.lifeDialog = null;
        this.funUtil = null;
        this.menuDialog = null;
        RxScreenCapture.get().removeAll();
    }

    public static MenuTouchDialog init(Context context, LifeDialog lifeDialog) {
        return new MenuTouchDialog(context, lifeDialog);
    }

    public Dialog getMenuDialog() {
        return menuDialog;
    }

    public FunUtil getFunUtil() {
        return funUtil;
    }

    public MenuTouchDialog(@NonNull Context context, LifeDialog lifeDialog) {
        AppContext.create(context);
        this.listUtils = ListUtils.get(context);
        this.lifeDialog = lifeDialog;
        this.menuDialog = create();
        this.funUtil = new FunUtil(this);
    }

    @SuppressLint("InflateParams")
    public Dialog create() {
        if (AppContext.get().getContext() == null) {
            return null;
        }
        Dialog dialog = new Dialog(AppContext.get().getContext(), R.style.BottomDialog);
        View viewTouchDialog = LayoutInflater.from(AppContext.get().getContext()).inflate(R.layout.menu_touch, null);
        dialog.setContentView(viewTouchDialog);
        ButterKnife.bind(this, viewTouchDialog);
        ViewGroup.MarginLayoutParams paramsOpenTouch = (ViewGroup.MarginLayoutParams) viewTouchDialog.getLayoutParams();
        paramsOpenTouch.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsOpenTouch.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        Window window = dialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.Dialog_Animation);
            this.onAttachedToWindow(window);
        }
        viewTouchDialog.setLayoutParams(paramsOpenTouch);
        dialog.setOnCancelListener(dialogInterface -> {
            if (this.lifeDialog != null) {
                this.lifeDialog.onCancel(this.cancel);
            }
        });
        dialog.setOnDismissListener(dialogInterface -> {
            if (this.lifeDialog != null) {
                this.lifeDialog.onCancel(this.cancel);
            }
        });
        this.setUpFont(this.tvTopToCenter,
                this.tvTopToLeft,
                this.tvTopToRight,
                this.tvBottomToCenter,
                this.tvBottomToLeft,
                this.tvBottomToRight);
        Glide.with(AppContext.get().getContext())
                .load(R.drawable.bg_menu_layout).diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
                .into(ivBg);
        return dialog;
    }

    public void onAttachedToWindow(Window window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        }
        window.setType(LAYOUT_FLAG);
    }

    @Override
    public void onResultCaptureScreen(Bitmap bitmap) {
        if (this.lifeDialog == null) {
            return;
        }
        this.lifeDialog.onResultCaptureScreen(bitmap);
    }

    @Override
    public void onFailedCaptureScreen() {
        if (this.lifeDialog == null) {
            return;
        }
        this.lifeDialog.onFailedCaptureScreen();
    }

    @Override
    public void onStartCaptureScreen() {
        if (this.lifeDialog == null) {
            return;
        }
        this.lifeDialog.onStartCaptureScreen();
    }

    @Override
    public void onStopCaptureScreen() {
        if (this.lifeDialog == null) {
            return;
        }
        this.lifeDialog.onStopCaptureScreen();
    }

    @Override
    public void onResultCaptureScreenVideo(String path) {
        if (this.lifeDialog == null) {
            return;
        }
        this.lifeDialog.onResultCaptureScreenVideo(path);
    }

    @Override
    public void onStartCaptureScreenVideo() {
        if (this.lifeDialog == null) {
            return;
        }
        this.lifeDialog.onStartCaptureScreenVideo();
    }

    @Override
    public void onErrorCaptureScreenVideo() {
        if (this.lifeDialog == null) {
            return;
        }
        this.lifeDialog.onErrorCaptureScreenVideo();
    }

    public interface LifeDialog {

        void onCancel(boolean is);

        void onShow();

        void onResultCaptureScreen(Bitmap bitmap);

        void onFailedCaptureScreen();

        void onStartCaptureScreen();

        void onStopCaptureScreen();

        void onStartCaptureScreenVideo();

        void onResultCaptureScreenVideo(String path);

        void onErrorCaptureScreenVideo();

    }

    public void showMenuTouch() {
        if (this.getMenuDialog() == null || this.lifeDialog == null || this.getMenuDialog().isShowing()) {
            return;
        }
        this.csLayoutAll.setVisibility(View.VISIBLE);
        this.cancel = true;
        this.lifeDialog.onShow();
        this.setUpView();
        this.getMenuDialog().show();
    }

    public void cancelMenuTouch(boolean cancel) {
        if (this.getMenuDialog() == null || !this.getMenuDialog().isShowing()) {
            return;
        }
        this.cancel = cancel;
        this.getMenuDialog().cancel();
    }

    private void setUpFont(AppCompatTextView... appCompatTextViews) {
        Typeface typeface = ResourcesCompat.getFont(AppContext.get().getContext(), fontMenuTouch);
        for (AppCompatTextView appCompatTextView : appCompatTextViews) {
            appCompatTextView.setTypeface(typeface);
        }
    }

    private void setUpView() {
        this.menuBottomToCenter = getFun(Pref.MENU_BOTTOM_TO_CENTER, MENU_HOME_SCREEN);
        this.menuBottomToLeft = getFun(Pref.MENU_BOTTOM_TO_LEFT, MENU_STAR);
        this.menuBottomToRight = getFun(Pref.MENU_BOTTOM_TO_RIGHT, ConstKey.MENU_CAPTURE_SCREEN_VIDEO);
        this.menuTopToCenter = getFun(Pref.MENU_TOP_TO_CENTER, MENU_NOTIFICATION);
        this.menuTopToLeft = getFun(Pref.MENU_TOP_TO_LEFT, MENU_CAPTURE_SCREEN);
        this.menuTopToRight = getFun(Pref.MENU_TOP_TO_RIGHT, MENU_BACK);
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

        int boderMenuTouch = PrefUtil.get().getInt(Pref.BODER_MENU_TOUCH, 9);
        int alphaMenuTouch = PrefUtil.get().getInt(Pref.ALPHA_MENU_TOUCH, 250);
        int blurMenuTouch = PrefUtil.get().getInt(Pref.PHOTO_BLUR_MENU_TOUCH, 25);
        int color = PrefUtil.get().getInt(Pref.COLOR_MENU_TOUCH, ContextCompat.getColor(AppContext.get().getContext(), R.color.nau));
        String path = PrefUtil.get().getString(Pref.PHOTO_PATH_MENU_TOUCH, "");
        String type = PrefUtil.get().getString(Pref.BG_MENU, Pref.BG_COLOR);
        this.compatView.setBoderLayout(PixelUtil.getDimenPixelFromPosition(boderMenuTouch));
        this.ivBg.setCornerRadius(PixelUtil.getDimenPixelFromPosition(boderMenuTouch));
        this.compatView.setAlphaLayout(alphaMenuTouch);
        float alpha = (float) alphaMenuTouch / 255;
        this.ivBg.setAlpha(alpha);
        if (type.equals(Pref.BG_COLOR)) {
            this.compatView.setColor(color);
            this.compatView.setVisibility(View.VISIBLE);
            this.ivBg.setVisibility(View.GONE);
        } else {
            this.ivBg.setVisibility(View.VISIBLE);
            Glide.with(AppContext.get().getContext())
                    .load(TextUtils.isEmpty(path) ? R.drawable.bg_menu_layout : path).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.bg_menu_layout)
                    .error(R.drawable.bg_menu_layout)
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(blurMenuTouch, 3)))
                    .into(this.ivBg);
        }
    }

    private int getFun(String menuTopToRight, int menuBack) {
        return PrefUtil.get().getInt(menuTopToRight, menuBack);
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

    private void onClickView(View view) {

        if (this.funUtil == null) {
            return;
        }
        int id = view.getId();
        switch (id) {
            case R.id.ll_bottom_to_centter:
                this.funUtil.select(this.menuBottomToCenter);
                this.isCancelMenuDialog(this.menuBottomToCenter);
                return;
            case R.id.ll_bottom_to_left:
                this.funUtil.select(this.menuBottomToLeft);
                this.isCancelMenuDialog(this.menuBottomToLeft);
                return;
            case R.id.ll_bottom_to_right:
                this.funUtil.select(this.menuBottomToRight);
                this.isCancelMenuDialog(this.menuBottomToRight);
                return;
            case R.id.ll_top_to_centter:
                this.funUtil.select(this.menuTopToCenter);
                this.isCancelMenuDialog(this.menuTopToCenter);
                return;
            case R.id.ll_top_to_left:
                this.funUtil.select(this.menuTopToLeft);
                this.isCancelMenuDialog(this.menuTopToLeft);
                return;
            case R.id.ll_top_to_right:
                this.funUtil.select(this.menuTopToRight);
                this.isCancelMenuDialog(this.menuTopToRight);
        }
    }

    private void isCancelMenuDialog(int i) {
        boolean is = false;
        for (int cancelDialogTouch : notCancelDialogTouch) {
            if (cancelDialogTouch == i) {
                is = true;
                break;
            }
        }
        if (!is) {
            if (i == MENU_CAPTURE_SCREEN || i == MENU_CAPTURE_SCREEN_VIDEO) {
                this.csLayoutAll.setVisibility(View.GONE);
                this.cancel = false;
                this.cancelMenuTouch(false);
                return;
            }
            this.cancelMenuTouch(true);
        }
    }
}

