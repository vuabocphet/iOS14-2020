package com.developer.phimtatnhanh.service;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.data.PrefUtil;
import com.developer.phimtatnhanh.setuptouch.utilities.ObservableTouchUtil;
import com.developer.phimtatnhanh.setuptouch.utilities.ViewManagerUtil;
import com.developer.phimtatnhanh.setuptouch.utilities.bus.EvenBusCaptureStart;
import com.developer.phimtatnhanh.setuptouch.utilities.bus.EvenBusCaptureStartVideo;
import com.developer.phimtatnhanh.setuptouch.utilities.bus.EvenBusCaptureVideoUpdateTouch;
import com.developer.phimtatnhanh.setuptouch.utilities.bus.EvenBusFlash;
import com.developer.phimtatnhanh.setuptouch.utilities.bus.ShowFloatTouch;
import com.developer.phimtatnhanh.setuptouch.utilities.bus.UpdateIconTouch;
import com.developer.phimtatnhanh.setuptouch.utilities.permission.PermissionUtils;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class TouchService extends Service {

    private ViewManagerUtil viewManagerUtil;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        ObservableTouchUtil.get().getKeyServiceListener().onServicTouchStop();
    }

    public static void start(Context context) {
        if (context == null) {
            return;
        }
        if (!PermissionUtils.checkPerSystemAler(context)) {
            return;
        }
        if (PermissionUtils.isRunningService(context, TouchService.class)) return;
        Intent intent = new Intent(context, TouchService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
            return;
        }
        context.startService(intent);
    }

    public static void stop(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, TouchService.class);
        context.stopService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        MMKV.initialize(this);
        PrefUtil.init();
        this.viewManagerUtil = ViewManagerUtil.init(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String NOTIFICATION_CHANNEL_ID = this.getPackageName();
            String channelName = this.getString(R.string.app_name);
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setContentTitle("App is running in background")
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            startForeground(2, notification);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (this.viewManagerUtil != null) {
            this.viewManagerUtil.removeView();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EvenBusCaptureStart event) {
        if (this.viewManagerUtil != null) {
            this.viewManagerUtil.evenBusCaptureScreen();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EvenBusCaptureStartVideo event) {
        if (this.viewManagerUtil != null) {
            this.viewManagerUtil.evenBusCaptureScreenVideo();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EvenBusFlash event) {
        if (this.viewManagerUtil != null) {
            this.viewManagerUtil.evenBusFlash();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EvenBusCaptureVideoUpdateTouch event) {
        if (this.viewManagerUtil != null) {
            if (event.lifeCaptureVideo == EvenBusCaptureVideoUpdateTouch.LifeCaptureVideo.SETUP) {
                this.viewManagerUtil.setLifeCaptureVideo(EvenBusCaptureVideoUpdateTouch.LifeCaptureVideo.SETUP).captureVideoTouchSetUp();
            }
            if (event.lifeCaptureVideo == EvenBusCaptureVideoUpdateTouch.LifeCaptureVideo.START) {
                this.viewManagerUtil.setLifeCaptureVideo(EvenBusCaptureVideoUpdateTouch.LifeCaptureVideo.START).captureVideoTouchStart();
            }
            if (event.lifeCaptureVideo == EvenBusCaptureVideoUpdateTouch.LifeCaptureVideo.STOP) {
                this.viewManagerUtil.setLifeCaptureVideo(EvenBusCaptureVideoUpdateTouch.LifeCaptureVideo.STOP).captureVideoTouchStop();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdateIconTouch event) {
        if (this.viewManagerUtil != null) {
            this.viewManagerUtil.updateIconTouch();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ShowFloatTouch event) {
        if (this.viewManagerUtil != null) {
            this.viewManagerUtil.showView(true);
        }
    }

}
