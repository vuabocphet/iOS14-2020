package com.developer.phimtatnhanh.ads;


import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.view.CompatView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;


public class NativeAdView extends LinearLayout {

    private TextView tvNativeTitle;
    private TextView tvNativeBody;
    private TextView btNativeCta;
    private LinearLayout viewIcon;
    private FrameLayout layoutAdChoice;
    private FrameLayout layoutContentAd;
    private LinearLayout layoutRootAd;
    private LinearLayout layoutMediaView;
    private Style adStyle = Style.NORMAL;
    private boolean adAttached;

    public NativeAdView(Context context) {
        super(context);
        initView(null);
    }

    public NativeAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public NativeAdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NativeAdView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {

        inflate(getContext(), R.layout.ex_ad_view, this);

        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.NativeAdView, 0, 0);

        if (typedArray.hasValue(R.styleable.NativeAdView_ad_style)) {
            int style = typedArray.getInt(R.styleable.NativeAdView_ad_style, 0);
            this.adStyle = Style.fromId(style);
        }

        this.viewIcon = this.findViewById(R.id.native_ad_ad_icon_layout);
        this.layoutMediaView = this.findViewById(R.id.layout_media_view);
        this.layoutMediaView.setVisibility(this.adStyle == Style.MEDIA_VIEW ? View.VISIBLE : GONE);

        this.tvNativeTitle = this.findViewById(R.id.native_ad_title);
        this.tvNativeBody = this.findViewById(R.id.native_ad_body);
        this.btNativeCta = this.findViewById(R.id.native_cta);
        this.showLoadingState(tvNativeTitle, tvNativeBody, btNativeCta, viewIcon, layoutMediaView);

        this.layoutAdChoice = this.findViewById(R.id.native_adchoice_view);
        this.layoutContentAd = this.findViewById(R.id.layout_content_ad);
        this.layoutRootAd = this.findViewById(R.id.root_ad_view);
        this.setVisibility(VISIBLE);
    }

    private void showLoadingState(View... views) {
        activeView(views, false);
    }

    private void hideLoadingState(View... views) {
        activeView(views, true);
    }

    private void activeView(View[] views, boolean active) {
        for (View view : views) {
            view.setActivated(active);
        }
    }

    private enum Style {
        NORMAL(0), MEDIA_VIEW(1);
        int id;

        Style(int id) {
            this.id = id;
        }

        static Style fromId(int id) {
            for (Style f : values()) {
                if (f.id == id) return f;
            }
            throw new IllegalArgumentException();
        }
    }

    public boolean isAdAttached() {
        return adAttached;
    }

    public void show(UnifiedNativeAd unifiedNativeAd) {
        if (unifiedNativeAd == null) {
            setVisibility(GONE);
            return;
        }
        this.adAttached = true;
        this.layoutContentAd.setVisibility(VISIBLE);
        this.viewIcon.removeAllViews();
        this.layoutAdChoice.removeAllViews();
        this.layoutRootAd.removeAllViews();

        this.tvNativeTitle.setText(unifiedNativeAd.getHeadline());
        this.tvNativeBody.setText(unifiedNativeAd.getBody());
        this.btNativeCta.setText(unifiedNativeAd.getCallToAction());
        this.hideLoadingState(tvNativeTitle, tvNativeBody, btNativeCta, viewIcon, layoutMediaView);

        UnifiedNativeAdView unifiedNativeAdView = new UnifiedNativeAdView(getContext());
        unifiedNativeAdView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        if (this.adStyle == Style.MEDIA_VIEW) {
            com.google.android.gms.ads.formats.MediaView admobMediaView = new com.google.android.gms.ads.formats.MediaView(getContext());
            this.layoutMediaView.addView(admobMediaView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            this.layoutMediaView.setVisibility(VISIBLE);
            unifiedNativeAdView.setMediaView(admobMediaView);
        }

        CompatView admobIcon =new CompatView(getContext(),null);
        admobIcon.setBoderLayout(R.dimen._5sdp);
        com.google.android.gms.ads.formats.NativeAd.Image icon = unifiedNativeAd.getIcon();
        if (icon != null && icon.getDrawable() != null) {
            admobIcon.setImageDrawable(icon.getDrawable());
        }
        this.viewIcon.addView(admobIcon, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        unifiedNativeAdView.setHeadlineView(tvNativeTitle);
        unifiedNativeAdView.setCallToActionView(btNativeCta);
        unifiedNativeAdView.setBodyView(tvNativeBody);
        unifiedNativeAdView.setIconView(admobIcon);
        unifiedNativeAdView.setNativeAd(unifiedNativeAd);

        ViewGroup parent = (ViewGroup) layoutContentAd.getParent();
        if (parent != null) {
            parent.removeView(layoutContentAd);
        }
        unifiedNativeAdView.addView(layoutContentAd);
        this.layoutRootAd.addView(unifiedNativeAdView);
        this.layoutRootAd.setVisibility(VISIBLE);
        setVisibility(VISIBLE);
    }
}
