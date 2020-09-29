package com.developer.phimtatnhanh.setuptouch.dialog;


import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;

import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.data.Pref;
import com.developer.phimtatnhanh.data.PrefUtil;
import com.developer.phimtatnhanh.delayclickview.PostDelayClick;
import com.developer.phimtatnhanh.setuptouch.config.ConfigBrightness;
import com.developer.phimtatnhanh.setuptouch.utilities.AudioUtil;
import com.developer.phimtatnhanh.setuptouch.utilities.BrightnessUtil;
import com.developer.phimtatnhanh.view.CustomSeekBarIOS;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.OnClick;

public class SeekBarDialog extends BaseDialog {

    @BindView(R.id.boxed_vertical)
    CustomSeekBarIOS boxedVertical;

    @BindView(R.id.ll_layout_all)
    LinearLayout llLayoutAll;
    @BindView(R.id.iv_type)
    AppCompatImageView ivType;

    @OnClick({R.id.iv_close})
    public void close(View view) {
        PostDelayClick.get().postDelayViewClick(view);
        cancel();
    }

    private Context context;
    private TypeSeekBar typeSeekBar;
    private Handler handler;

    public enum TypeSeekBar {
        AUTIO,
        BRIGHTNESS
    }

    public static SeekBarDialog create(Context context, TypeSeekBar typeSeekBar) {
        return new SeekBarDialog(context, typeSeekBar);
    }

    public SeekBarDialog(Context context, TypeSeekBar typeSeekBar) {
        this.context = context;
        this.typeSeekBar = typeSeekBar;
        this.createDialog();
    }

    @Override
    protected Context context() {
        return context;
    }

    @Override
    protected int onLayout() {
        return R.layout.seekbar_dialog;
    }

    @Override
    protected int gravity() {
        return Gravity.CENTER;
    }

    @Override
    protected boolean fullscreen() {
        return true;
    }

    @Override
    public void init() {
        this.handler = new Handler(Looper.getMainLooper());
        this.bindType();
        this.showSeekBarDialog();
    }

    public int getMaxBrightness(Context context, int defaultValue) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            Field[] fields = powerManager.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals("BRIGHTNESS_ON")) {
                    field.setAccessible(true);
                    try {
                        return (int) field.get(powerManager);
                    } catch (IllegalAccessException e) {
                        return defaultValue;
                    }
                }
            }
        }
        return defaultValue;
    }

    private void bindType() {
        if (this.typeSeekBar == TypeSeekBar.BRIGHTNESS) {
            boolean bool = PrefUtil.get().getBool(Pref.BRIGHTNESS, false);
            int maxBrightness = this.getMaxBrightness(this.context, 255);
            int anInt = PrefUtil.get().getInt(Pref.BRIGHTNESS_MAX, ConfigBrightness.BRIGHTNESS_MAX);
            int brightnessMax = bool ? maxBrightness : anInt;
            int brightness = BrightnessUtil.getBrightness(this.context);
            this.boxedVertical.setMax(brightnessMax);
            this.boxedVertical.setValue(brightness);
            this.boxedVertical.setOnBoxedPointsChangeListener(new CustomSeekBarIOS.OnValuesChangeListener() {
                @Override
                public void onPointsChanged(CustomSeekBarIOS boxedPoints, int points) {
                    try {
                        BrightnessUtil.setBrightness(context, points);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    handler.removeCallbacksAndMessages(null);
                }

                @Override
                public void onStartTrackingTouch(CustomSeekBarIOS boxedPoints) {

                }

                @Override
                public void onStopTrackingTouch(CustomSeekBarIOS boxedPoints) {
                    handler.postDelayed(() -> cancel(), 3000);
                }
            });
            return;
        }
        this.ivType.setImageResource(R.drawable.all_ic_speaker);
        AudioManager audioManager = (AudioManager) this.context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager == null) {
            cancel();
            return;
        }
        int maxAudio = AudioUtil.getMaxAudio(audioManager);
        int currenAudio = AudioUtil.getCurrenAudio(audioManager);
        this.boxedVertical.setMax(maxAudio);
        this.boxedVertical.setValue(currenAudio);
        this.boxedVertical.setStep(1);
        this.boxedVertical.setOnBoxedPointsChangeListener(new CustomSeekBarIOS.OnValuesChangeListener() {
            @Override
            public void onPointsChanged(CustomSeekBarIOS boxedPoints, int points) {
                AudioUtil.setAudio(audioManager, points);
                handler.removeCallbacksAndMessages(null);
            }

            @Override
            public void onStartTrackingTouch(CustomSeekBarIOS boxedPoints) {

            }

            @Override
            public void onStopTrackingTouch(CustomSeekBarIOS boxedPoints) {
                handler.postDelayed(() -> cancel(), 3000);
            }
        });
    }

    private void showSeekBarDialog() {
        if (this.dialog == null || this.dialog.isShowing()) {
            return;
        }
        this.dialog.show();
    }

}
