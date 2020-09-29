package com.developer.phimtatnhanh.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class GradientTextView extends AppCompatTextView {

    private String g1 = "#fc00f2";
    private String g2 = "#17009b";

    public GradientTextView(Context context) {
        super(context, null, -1);
    }

    public GradientTextView(Context context,
                            AttributeSet attrs) {
        super(context, attrs, -1);
    }

    public GradientTextView(Context context,
                            AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed,
                            int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            getPaint().setShader(new LinearGradient(
                    0, 0, 0, getHeight(),
                    Color.parseColor(g1), Color.parseColor(g2),
                    Shader.TileMode.CLAMP));
        }
    }
}

