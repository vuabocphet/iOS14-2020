package com.developer.phimtatnhanh.setuptouch.utilities;

public class ObservableTouchUtil {

    private static ObservableTouchUtil observableTouchUtil = null;

    public static ObservableTouchUtil get() {
        if (observableTouchUtil == null) {
            synchronized (ObservableTouchUtil.class) {
                if (observableTouchUtil == null) {
                    observableTouchUtil = new ObservableTouchUtil();
                }
            }
        }
        return observableTouchUtil;
    }

    private KeyServiceListener keyServiceListener;

    public KeyServiceListener getKeyServiceListener() {
        return keyServiceListener == null ? new KeyServiceListener() {
            @Override
            public void backKey() {

            }

            @Override
            public void homeKey() {

            }

            @Override
            public void recentKey() {

            }

            @Override
            public void settingKey() {

            }

            @Override
            public void powerDialog() {

            }

            @Override
            public void notificationKey() {

            }

            @Override
            public void onServicTouchStop() {

            }

            @Override
            public void onLockScreen() {

            }
        } : keyServiceListener;
    }

    public void setKeyServiceListener(KeyServiceListener keyServiceListener) {
        this.keyServiceListener = keyServiceListener;
    }

    public interface KeyServiceListener {

        void backKey();

        void homeKey();

        void recentKey();

        void settingKey();

        void powerDialog();

        void notificationKey();

        void onServicTouchStop();

        void onLockScreen();

    }

}
