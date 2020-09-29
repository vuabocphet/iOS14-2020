package com.developer.phimtatnhanh.setuptouch.dialog;


import android.content.Context;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.developer.phimtatnhanh.R;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.developer.phimtatnhanh.ui.touch.ConfigFloatTouch.BLUR_DEFAULT;

public class DevicePhoneDialog extends BaseDialog {

    @BindView(R.id.non_sliding_view)
    AppCompatImageView nonSlidingView;

    private Context context;

    public static DevicePhoneDialog create(Context context) {
        return new DevicePhoneDialog(context);
    }

    public DevicePhoneDialog(Context context) {
        this.context = context;
        this.createDialog();
    }

    @Override
    protected Context context() {
        return this.context;
    }

    @Override
    protected int onLayout() {
        return R.layout.device_phone_dialog;
    }

    @Override
    protected boolean fullscreen() {
        return true;
    }

    @Override
    public void init() {
        Glide.with(this.context)
                .load(R.drawable.bg_menu_layout)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(BLUR_DEFAULT, 3))).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(this.nonSlidingView);
    }
}
