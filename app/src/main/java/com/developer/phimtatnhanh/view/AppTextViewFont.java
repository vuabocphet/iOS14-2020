package com.developer.phimtatnhanh.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.FontRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

public class AppTextViewFont extends AppCompatTextView {
    public AppTextViewFont(@NonNull Context context) {
        super(context);
    }

    public AppTextViewFont(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AppTextViewFont(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("ResourceType")
    public void setFont(@FontRes int idFonts) {
        this.setTypeface(ResourcesCompat.getFont(this.getContext(), idFonts));
    }

}
