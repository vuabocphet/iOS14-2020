package com.developer.phimtatnhanh.ui.touch;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SeekBar;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.base.BaseActivity;
import com.developer.phimtatnhanh.data.ListUtils;
import com.developer.phimtatnhanh.data.Pref;
import com.developer.phimtatnhanh.data.PrefUtil;
import com.developer.phimtatnhanh.di.component.ActivityComponent;
import com.developer.phimtatnhanh.setuptouch.utilities.bus.UpdateIconTouch;
import com.developer.phimtatnhanh.ui.touch.gridview.CustomIconAdapter;
import com.developer.phimtatnhanh.ui.touch.gridview.IconModel;
import com.developer.phimtatnhanh.util.PixelUtil;
import com.developer.phimtatnhanh.view.CompatView;
import com.divyanshu.colorseekbar.ColorSeekBar;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class TouchActivity extends BaseActivity implements TouchView, ConfigFloatTouch {

    @Inject
    ListUtils listUtils;

    @BindView(R.id.grid_view)
    GridView gridView;

    @BindView(R.id.cv_touch)
    CompatView cvTouch;

    @BindView(R.id.color_seek)
    ColorSeekBar colorSeek;

    @BindView(R.id.boder_seek)
    AppCompatSeekBar boderSeek;

    @BindView(R.id.alpha_seek)
    AppCompatSeekBar alphaSeek;

    @BindView(R.id.cs_setup_bg_touch)
    ConstraintLayout csSetupBgTouch;
    @BindView(R.id.size_seek)
    AppCompatSeekBar sizeSeek;
    @BindView(R.id.iv_bg)
    AppCompatImageView ivBg;

    private TouchPresenter touchPresenter;

    @Override
    protected void init() {
        this.touchPresenter = new TouchPresenter();
        this.touchPresenter.attachView(this);
        this.setUp();
        Glide.with(this)
                .load(R.drawable.bg_menu_layout)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(BLUR_DEFAULT, 3))).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(this.ivBg);
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
        TypeEditTouch typeEditTouch = (TypeEditTouch) bundle.getSerializable(TYPE);
        if (typeEditTouch == null) {
            finish();
            return;
        }

        /*
         * Edit backgroud touch
         * */
        if (typeEditTouch == TypeEditTouch.EDIT_BG_TOUCH) {
            this.csSetupBgTouch.setVisibility(View.VISIBLE);
            this.setUpBgTouch();
            return;
        }

        /*
         * Edit icon touch
         * */
        if (typeEditTouch == TypeEditTouch.EDIT_ICON_TOUCH) {
            this.gridView.setVisibility(View.VISIBLE);
            CustomIconAdapter customIconAdapter = CustomIconAdapter.init(this, this.listUtils.getListIconTouch(), this.gridView);
            customIconAdapter.setClickItem(aIconModel -> {
                PrefUtil.get().postInt(Pref.ICON_TOUCH, aIconModel);
                Glide.with(this)
                        .load(this.listUtils.getListIconTouch().get(aIconModel).iconId)
                        .into(this.cvTouch);
                EventBus.getDefault().post(new UpdateIconTouch());
            });
        }
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_touch;
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void onDestroy() {
        this.touchPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected boolean fullScreen() {
        return true;
    }

    @Override
    public FragmentManager fragmentManager() {
        return getSupportFragmentManager();
    }

    private void setUp() {
        /*
         * getIcon
         * */
        int icon = PrefUtil.get().getInt(Pref.ICON_TOUCH, ICON_DEFAULT);
        if (icon > this.listUtils.getListIconTouch().size()) {
            icon = 0;
        }
        Glide.with(this).load(this.listUtils.getListIconTouch().get(icon).iconId).into(this.cvTouch);

        /*
         * getAlpha
         * */
        int alpha = PrefUtil.get().getInt(Pref.TOUCH_BG_ALPHA, ALPHA_DEFAULT);
        this.alphaSeek.setMax(ALPHA_MAX);
        this.alphaSeek.setProgress(alpha);
        this.cvTouch.setAlphaLayout(alpha);

        /*
         * getColor
         * */
        int colorDefault = ContextCompat.getColor(this, COLOR_DEFAULT_ID);
        int color = PrefUtil.get().getInt(Pref.TOUCH_BG_COLOR, colorDefault);
        this.cvTouch.setColor(color);

        /*
         * getBoder
         * */
        float boder = PrefUtil.get().getFloat(Pref.TOUCH_BG_BODER, BODER_DEFAULT);
        this.boderSeek.setMax(BODER_MAX);
        this.boderSeek.setProgress((int) boder);
        this.cvTouch.setBoderLayout(PixelUtil.getDimenPixelFromPosition((int) boder));

        /*
         * getSize
         * */
        float size = PrefUtil.get().getFloat(Pref.TOUCH_BG_SIZE, SIZE_DEFAULT);
        this.sizeSeek.setMax(SIZE_MAX);
        this.sizeSeek.setProgress((int) size);
        setSizeTouch(PixelUtil.getDimenPixelFromPosition((int) size));
    }

    private void setSizeTouch(int size) {
        ViewGroup.LayoutParams params = this.cvTouch.getLayoutParams();
        params.height = size;
        params.width = size;
        this.cvTouch.setLayoutParams(params);
    }

    private void setUpBgTouch() {
        this.sizeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 20) {
                    return;
                }
                setSizeTouch(PixelUtil.getDimenPixelFromPosition(i));
                PrefUtil.get().postFloat(Pref.TOUCH_BG_SIZE, i);
                EventBus.getDefault().post(new UpdateIconTouch());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        this.boderSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                cvTouch.setBoderLayout(PixelUtil.getDimenPixelFromPosition(i));
                PrefUtil.get().postFloat(Pref.TOUCH_BG_BODER, i);
                EventBus.getDefault().post(new UpdateIconTouch());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        this.colorSeek.setOnColorChangeListener(i -> {
            this.cvTouch.setColor(i);
            PrefUtil.get().postInt(Pref.TOUCH_BG_COLOR, i);
            EventBus.getDefault().post(new UpdateIconTouch());
        });
        this.alphaSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                cvTouch.setAlphaLayout(i);
                PrefUtil.get().postInt(Pref.TOUCH_BG_ALPHA, i);
                EventBus.getDefault().post(new UpdateIconTouch());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void onBack(View view) {
        finish();
    }
}