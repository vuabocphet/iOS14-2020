package com.developer.phimtatnhanh.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.developer.phimtatnhanh.R;

public class CompatLayout extends RelativeLayout {

    private float boder;
    private int isGradient;
    private int alpha = 255;
    private String startColor = "252421";
    private String endColor = "252421";

    public CompatLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs);
    }

    public CompatLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs);
    }

    @SuppressLint("Recycle")
    private void init(AttributeSet attrs) {

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CompatView);
        boolean isGradient = typedArray.getBoolean(R.styleable.CompatView_isgradient, false);
        this.boder = typedArray.getDimensionPixelSize(R.styleable.CompatView_boderview, 0);
        this.alpha = typedArray.getInt(R.styleable.CompatView_alphaview, 255);
        String startColor = typedArray.getString(R.styleable.CompatView_startcolor);
        String endColor = typedArray.getString(R.styleable.CompatView_endcolor);
        if (!TextUtils.isEmpty(startColor)) {
            this.startColor = startColor;
        }
        if (!TextUtils.isEmpty(endColor)) {
            this.endColor = endColor;
        }
        this.update();
        typedArray.recycle();
    }

    private void update() {
        this.setBackground(shape());
    }

    private GradientDrawable shape() {
        GradientDrawable shape = new GradientDrawable(GradientDrawable.Orientation.BR_TL, new int[]{Color.parseColor("#" + startColor), Color.parseColor("#" + endColor)});
        shape.setCornerRadius(this.boder);
        shape.setAlpha(this.alpha);
        return shape;
    }

    public void setStartColor(String color) {
        if (TextUtils.isEmpty(color)) {
            return;
        }
        this.startColor = color;
        this.update();
    }

    public void setEndColor(String color) {
        if (TextUtils.isEmpty(color)) {
            return;
        }
        this.endColor = color;
        this.update();
    }

    public void setAlphaLayout(int alpha) {
        if (alpha < 0 || alpha > 255) {
            return;
        }
        this.alpha = alpha;
        this.update();
    }

    public void setBoderLayout(float boder) {
        if (boder < 0) {
            return;
        }
        this.boder = boder;
        this.update();
    }

}
