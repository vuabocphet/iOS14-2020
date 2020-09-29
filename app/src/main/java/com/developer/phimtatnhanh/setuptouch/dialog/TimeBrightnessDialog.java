package com.developer.phimtatnhanh.setuptouch.dialog;


import android.content.Context;
import android.graphics.Typeface;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.setuptouch.utilities.SettingsUtil;
import com.github.florent37.viewanimator.ViewAnimator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.developer.phimtatnhanh.setuptouch.config.ConfigAll.fontMenuTouch;

public class TimeBrightnessDialog extends BaseDialog {

    private Context context;

    private int time = 60000;

    private List<AppCompatImageView> appCompatImageViews;

    @BindView(R.id.rd_15s)
    AppCompatImageView rd15s;
    @BindView(R.id.rd_30s)
    AppCompatImageView rd30s;
    @BindView(R.id.rd_1p)
    AppCompatImageView rd1p;
    @BindView(R.id.rd_2p)
    AppCompatImageView rd2p;
    @BindView(R.id.rd_10p)
    AppCompatImageView rd10p;
    @BindView(R.id.rd_30p)
    AppCompatImageView rd30p;
    @BindView(R.id.rd_never)
    AppCompatImageView rdNever;
    @BindView(R.id.cs_layout_all)
    ConstraintLayout csLayoutAll;
    @BindView(R.id.tv1)
    AppCompatTextView tv1;
    @BindView(R.id.tv2)
    AppCompatTextView tv2;
    @BindView(R.id.tv3)
    AppCompatTextView tv3;
    @BindView(R.id.tv4)
    AppCompatTextView tv4;
    @BindView(R.id.tv5)
    AppCompatTextView tv5;
    @BindView(R.id.tv6)
    AppCompatTextView tv6;
    @BindView(R.id.tv7)
    AppCompatTextView tv7;
    @BindView(R.id.tv)
    AppCompatTextView tv;

    @OnClick({R.id.bt_yes, R.id.bt_close})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.bt_yes) {
            SettingsUtil.setTimeout(this.context, this.time);
        }
        this.cancelTimeScreen();
    }

    @OnClick({R.id.ll_15s,
            R.id.ll_30s,
            R.id.ll_1p,
            R.id.ll_2p,
            R.id.ll_10p,
            R.id.ll_30p,
            R.id.ll_never})
    public void onClickView(View view) {
        int id = view.getId();
        this.set(id);
    }

    private void set(int id) {
        if (id == R.id.ll_15s || time == 15000) {
            setView(15000, rd15s);
        }
        if (id == R.id.ll_30s || time == 30000) {
            setView(30000, rd30s);
        }
        if (id == R.id.ll_1p || time == 60000) {
            setView(60000, rd1p);
        }
        if (id == R.id.ll_2p || time == 120000) {
            setView(120000, rd2p);
        }
        if (id == R.id.ll_10p || time == 600000) {
            setView(600000, rd10p);
        }
        if (id == R.id.ll_30p || time == 1800000) {
            setView(1800000, rd30p);
        }
        if (id == R.id.ll_never || time == Integer.MAX_VALUE) {
            setView(Integer.MAX_VALUE, rdNever);
        }
    }

    private void setView(int time, AppCompatImageView view) {
        this.time = time;
        for (AppCompatImageView appCompatImageView : this.appCompatImageViews) {
            if (appCompatImageView.getId() == view.getId()) {
                appCompatImageView.setImageResource(R.drawable.all_ic_radio_button_checked);
            } else
                appCompatImageView.setImageResource(R.drawable.all_ic_radio_button_unchecked);
        }
    }

    public static TimeBrightnessDialog init(Context context) {
        return new TimeBrightnessDialog(context);
    }

    public TimeBrightnessDialog(Context context) {
        this.context = context;
        this.createDialog();
    }

    @Override
    protected Context context() {
        return this.context;
    }

    @Override
    protected int onLayout() {
        return R.layout.time_bright;
    }

    @Override
    protected boolean fullscreen() {
        return true;
    }

    @Override
    public void init() {
        this.bindView();
    }

    private void bindView() {
        this.setUpFont(this.tv, this.tv1, this.tv2, this.tv3, this.tv4, this.tv5, this.tv6, this.tv7);
        this.appCompatImageViews = new ArrayList<>();
        this.appCompatImageViews.add(rd15s);
        this.appCompatImageViews.add(rd30s);
        this.appCompatImageViews.add(rd1p);
        this.appCompatImageViews.add(rd2p);
        this.appCompatImageViews.add(rd10p);
        this.appCompatImageViews.add(rd30p);
        this.appCompatImageViews.add(rdNever);
    }

    public void showTimeScreen() {
        if (this.dialog == null || this.dialog.isShowing()) {
            return;
        }
        this.time = SettingsUtil.getTimeScreenOut(this.context);
        this.set(0);
        ViewAnimator.animate(this.csLayoutAll).slideBottomIn().duration(500).start();
        this.dialog.show();
    }

    public void cancelTimeScreen() {
        if (this.dialog == null || !this.dialog.isShowing()) {
            return;
        }
        ViewAnimator.animate(this.csLayoutAll).fadeOut().duration(250).start();
        this.dialog.cancel();
    }

    private void setUpFont(AppCompatTextView... appCompatTextViews) {
        Typeface typeface = ResourcesCompat.getFont(this.context, fontMenuTouch);
        for (AppCompatTextView appCompatTextView : appCompatTextViews) {
            appCompatTextView.setTypeface(typeface);
        }
    }

}
