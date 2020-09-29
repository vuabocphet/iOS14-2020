package com.developer.phimtatnhanh.setuptouch.dialog;


import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.developer.phimtatnhanh.R;

import butterknife.BindView;

public class CountDownDialog extends BaseDialog {

    private Context context;
    private LifeCountDown lifeCountDown;

    public static CountDownDialog create(Context context) {
        return new CountDownDialog(context);
    }

    public CountDownDialog(Context context) {
        this.context = context;
        this.createDialog();
    }

    @BindView(R.id.tv_count_down)
    AppCompatTextView tvCountDown;

    @Override
    protected Context context() {
        return context;
    }

    @Override
    protected int onLayout() {
        return R.layout.count_down;
    }

    @Override
    protected boolean fullscreen() {
        return false;
    }

    @Override
    protected int gravity() {
        return Gravity.CENTER;
    }

    @Override
    public void init() {
        this.tvCountDown.setTypeface(ResourcesCompat.getFont(this.context, R.font.thin_ios));
    }

    @Override
    protected boolean cancelable() {
        return false;
    }

    public void startCountDown(LifeCountDown lifeCountDown) {
        this.lifeCountDown = lifeCountDown;
        this.show();
    }

    @Override
    public void show() {
        super.show();
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(3, 0);
        animator.addUpdateListener(animation -> {
            Object animatedValue = animation.getAnimatedValue();
            if (Integer.parseInt(animatedValue.toString()) == 0){
                if (this.lifeCountDown!=null){
                    this.lifeCountDown.onEndCountDown();
                }
                this.cancel();
                animator.cancel();
                return;
            }
            this.tvCountDown.setText(String.valueOf(animation.getAnimatedValue()));
        });
        animator.setEvaluator((TypeEvaluator<Integer>) (fraction, startValue, endValue) -> Math.round(startValue + (endValue - startValue) * fraction));
        animator.setDuration(6000);
        animator.start();
        if (this.lifeCountDown!=null){
            this.lifeCountDown.onStartCountDown();
        }
    }

    public interface LifeCountDown {
        void onStartCountDown();

        void onEndCountDown();
    }
}
