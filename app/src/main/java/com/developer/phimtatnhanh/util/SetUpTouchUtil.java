package com.developer.phimtatnhanh.util;


import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.developer.phimtatnhanh.app.AppContext;
import com.developer.phimtatnhanh.data.Pref;
import com.developer.phimtatnhanh.data.PrefUtil;
import com.developer.phimtatnhanh.ui.touch.ConfigFloatTouch;
import com.developer.phimtatnhanh.view.CompatView;

public class SetUpTouchUtil implements ConfigFloatTouch {

    private CompatView compatView;

    public SetUpTouchUtil create(CompatView compatView) {
        this.compatView = compatView;
        return this;
    }

    /*
     * getIcon
     * */
    private void setIcon() {
        if (isNullView()) return;
        int icon = PrefUtil.get().getInt(Pref.ICON_TOUCH, ICON_DEFAULT);
        Glide.with(AppContext.get().getContext()).load(icon).into(this.compatView);
    }

    /*
     * getAlpha
     * */
    private void setAlpha() {
        if (isNullView()) return;
        int alpha = PrefUtil.get().getInt(Pref.TOUCH_BG_ALPHA, ALPHA_DEFAULT);
        this.compatView.setAlphaLayout(alpha);
    }

    /*
     * getColor
     * */
    private void setColor() {
        if (isNullView()) return;
        int colorDefault = ContextCompat.getColor(AppContext.get().getContext(),COLOR_DEFAULT_ID);
        int color = PrefUtil.get().getInt(Pref.TOUCH_BG_COLOR, colorDefault);
        this.compatView.setColor(color);
    }

    /*
     * getBoder
     * */
    private void setBoder() {
        if (isNullView()) return;
        float boder = PrefUtil.get().getFloat(Pref.TOUCH_BG_BODER, BODER_DEFAULT);
        this.compatView.setBoderLayout(PixelUtil.getDimenPixelFromPosition((int) boder));
    }

    /*
     * getSize
     * */
    private void setSize() {
        if (isNullView()) return;
        float size = PrefUtil.get().getFloat(Pref.TOUCH_BG_SIZE, SIZE_DEFAULT);
        this.setSizeTouch(PixelUtil.getDimenPixelFromPosition((int) size));
    }

    private void setSizeTouch(int size) {
        ViewGroup.LayoutParams params = this.compatView.getLayoutParams();
        params.height = size;
        params.width = size;
        this.compatView.setLayoutParams(params);
    }

    private boolean isNullView() {
        return this.compatView == null;
    }

    public void setupAll() {
        this.setAlpha();
        this.setBoder();
        this.setColor();
        this.setIcon();
        this.setSize();
    }
}
