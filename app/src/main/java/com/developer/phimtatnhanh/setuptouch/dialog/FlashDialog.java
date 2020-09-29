package com.developer.phimtatnhanh.setuptouch.dialog;


import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.app.AppContext;
import com.developer.phimtatnhanh.data.Pref;
import com.developer.phimtatnhanh.data.PrefUtil;
import com.developer.phimtatnhanh.setuptouch.utilities.FlashlightUtil;
import com.xw.repo.BubbleSeekBar;

import butterknife.BindView;
import butterknife.OnClick;

import static com.developer.phimtatnhanh.setuptouch.config.ConfigAll.fontMenuTouch;

public class FlashDialog extends BaseDialog {

    @BindView(R.id.bubble_seekbar)
    BubbleSeekBar bubbleSeekbar;

    @BindView(R.id.btn_flash)
    AppCompatImageView btnFlash;

    @BindView(R.id.tv)
    AppCompatTextView tv;

    @BindView(R.id.tv1)
    AppCompatTextView tv1;


    @OnClick({R.id.btn_close, R.id.btn_flash})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_close:
                this.cancel();
                return;
            case R.id.btn_flash:
                this.flash();
        }
    }

    private Context context;
    private FlashlightUtil flashlightUtil;
    private MediaPlayer mediaPlayerOn;
    private MediaPlayer mediaPlayerOff;
    private Handler handler;
    private Runnable runnable;
    private int progressDelay = 800;
    private boolean isFlash;

    public static FlashDialog create(Context context) {
        return new FlashDialog(context);
    }

    public FlashDialog(Context context) {
        this.context = context;
        this.createDialog();
    }

    @Override
    protected boolean fullscreen() {
        return true;
    }

    @Override
    protected Context context() {
        return this.context;
    }

    @Override
    protected int onLayout() {
        return R.layout.flash_dialog;
    }

    @Override
    protected int gravity() {
        return Gravity.CENTER;
    }

    @Override
    public void init() {
        this.setUpFont(this.tv, this.tv1);
        this.mediaPlayerOn = MediaPlayer.create(this.context, R.raw.light_switch_on);
        this.mediaPlayerOff = MediaPlayer.create(this.context, R.raw.light_switch_off);
        this.flashlightUtil = FlashlightUtil.get(this.context);
        this.flash();
        this.handler = new Handler(Looper.getMainLooper());
        this.bubbleSeekbar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                set(progress);
            }
        });
        this.runnable = () -> {
            if (!this.isFlash) {
                this.handler.postDelayed(this.runnable, progressDelay);
                return;
            }
            this.flashSet();
            this.handler.postDelayed(this.runnable, progressDelay);
        };
        this.dialog.setOnCancelListener(dialogInterface -> this.remove(true));
        this.dialog.setOnDismissListener(dialogInterface -> this.remove(true));
    }

    private void set(int progress) {
        if (progress == 0) {
            this.remove(false);
            return;
        }
        if (this.handler == null || this.flashlightUtil == null) {
            return;
        }
        if (progress == 1) {
            progress = 800;
        }
        if (progress == 2) {
            progress = 600;
        }
        if (progress == 3) {
            progress = 400;
        }
        if (progress == 4) {
            progress = 200;
        }
        if (progress == 5) {
            progress = 100;
        }
        int progressDelay = progress;
        this.progressDelay = progressDelay;
        this.handler.removeCallbacks(this.runnable);
        this.handler.postDelayed(this.runnable, progressDelay);
    }

    private void remove(boolean offFlash) {
        if (this.handler == null || this.runnable == null || this.flashlightUtil == null) {
            return;
        }
        this.handler.removeCallbacks(this.runnable);
        this.handler.removeCallbacksAndMessages(null);
        if (offFlash) {
            this.flashlightUtil.lightOff();
        }
    }

    @Override
    protected boolean cancelable() {
        return false;
    }

    private void flash() {
        boolean bool = PrefUtil.get().getBool(Pref.AUDIO_FLASH, true);
        if (!this.isFlash) {
            this.flashlightUtil.lightOn();
            this.isFlash = true;
            if (bool) {
                this.mediaPlayerOn.start();
            }
            this.btnFlash.setImageResource(R.drawable.btn_switch_on);
        } else {
            this.flashlightUtil.lightOff();
            this.isFlash = false;
            if (bool) {
                this.mediaPlayerOff.start();
            }
            this.btnFlash.setImageResource(R.drawable.btn_switch_off);
        }
    }

    private void flashSet() {
        this.flashlightUtil.light();
    }

    private void setUpFont(AppCompatTextView... appCompatTextViews) {
        Typeface typeface = ResourcesCompat.getFont(AppContext.get().getContext(), fontMenuTouch);
        for (AppCompatTextView appCompatTextView : appCompatTextViews) {
            appCompatTextView.setTypeface(typeface);
        }
    }
}
