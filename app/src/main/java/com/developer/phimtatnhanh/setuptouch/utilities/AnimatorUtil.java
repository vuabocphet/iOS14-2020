package com.developer.phimtatnhanh.setuptouch.utilities;


import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.developer.phimtatnhanh.R;

public class AnimatorUtil {

    public static void startAnimationScaleIn(View view) {
        if (view == null) {
            return;
        }
        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.start));
    }

    public static void startAnimationScaleOut(View view) {
        if (view == null) {
            return;
        }
        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.stop);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

}
