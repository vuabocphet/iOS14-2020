package com.developer.phimtatnhanh.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.developer.phimtatnhanh.R;

public class CustomLayout extends LinearLayoutCompat {

    private String g1 = "fc00f2";
    private String g2 = "17009b";
    private String startColor = "";
    private String endColor = "";
    private float boderLayout = 0f;


    public CustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs);
    }

    public CustomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs);
    }

    @SuppressLint({"Recycle", "CustomViewStyleable"})
    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LayoutCustom);
        boolean isGradient = typedArray.getBoolean(R.styleable.LayoutCustom_is_gradient, false);
        this.boderLayout = typedArray.getFloat(R.styleable.LayoutCustom_boder, 0f);
        if (isGradient) {
            String startColor = typedArray.getString(R.styleable.LayoutCustom_start_color);
            String endColor = typedArray.getString(R.styleable.LayoutCustom_end_color);
            if (!TextUtils.isEmpty(startColor) && !TextUtils.isEmpty(endColor)) {
                this.startColor = startColor;
                this.endColor = endColor;
            }
        }

        if (isGradient) {
            this.initGradientDrawable();
        }else {
            this.initNotGradientDrawable();
        }
        typedArray.recycle();
    }

    private void initGradientDrawable() {
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BL_TR,
                new int[]{Color.parseColor("#" + startColor), Color.parseColor("#" + endColor)});
        gradientDrawable.setCornerRadius(this.boderLayout);
        this.setBackground(gradientDrawable);
    }

    private void initNotGradientDrawable() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(this.boderLayout);
        this.setBackground(gradientDrawable);
    }

}
