package com.developer.phimtatnhanh.setuptouch.utilities;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;

import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.app.AppContext;
import com.developer.phimtatnhanh.data.Pref;
import com.developer.phimtatnhanh.data.PrefUtil;
import com.developer.phimtatnhanh.dialog.OptionDialog;
import com.developer.phimtatnhanh.setuptouch.config.ConfigAll;
import com.developer.phimtatnhanh.setuptouch.config.ConfigParamsWindownManager;
import com.developer.phimtatnhanh.setuptouch.dialog.FlashDialog;
import com.developer.phimtatnhanh.setuptouch.dialog.MenuTouchDialog;
import com.developer.phimtatnhanh.setuptouch.dialog.ReviewScreenDialog;
import com.developer.phimtatnhanh.setuptouch.listen.EvenClick;
import com.developer.phimtatnhanh.setuptouch.listen.OnHomePressedListener;
import com.developer.phimtatnhanh.setuptouch.utilities.bus.EvenBusCaptureVideoUpdateTouch;
import com.developer.phimtatnhanh.setuptouch.utilities.capturescreen.RxScreenCapture;
import com.developer.phimtatnhanh.ui.garelly.EventBusPathScreenShot;
import com.developer.phimtatnhanh.util.SetUpTouchUtil;
import com.developer.phimtatnhanh.view.CompatLayout;
import com.developer.phimtatnhanh.view.CompatView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.WINDOW_SERVICE;

public class ViewManagerUtil implements ConfigAll, EvenClick, OnHomePressedListener, MenuTouchDialog.LifeDialog {

    @BindView(R.id.cv_touch)
    CompatView cvTouch;

    @BindView(R.id.tv_time_video)
    AppCompatTextView tvTimeVideo;

    @BindView(R.id.cpll_time_video)
    CompatLayout cpllTimeVideo;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private View view;
    private ViewManagerAnimateOnTouchUtil viewManagerAnimateOnTouchUtil;
    private MenuTouchDialog menuTouchDialog;
    private HomeWatcherUtil homeWatcherUtil;
    public EvenBusCaptureVideoUpdateTouch.LifeCaptureVideo lifeCaptureVideo = EvenBusCaptureVideoUpdateTouch.LifeCaptureVideo.STOP;
    private Handler handler;
    private long longtime = 0;
    private Runnable runnable;
    private Unbinder unbinder;
    private SetUpTouchUtil setUpTouchUtil;

    public static ViewManagerUtil init(Context context) {
        return new ViewManagerUtil(context);
    }

    public ViewManagerUtil(Context context) {
        AppContext.create(context);
        this.initView();
    }

    public ViewManagerUtil setLifeCaptureVideo(EvenBusCaptureVideoUpdateTouch.LifeCaptureVideo lifeCaptureVideo) {
        this.lifeCaptureVideo = lifeCaptureVideo;
        return this;
    }

    @SuppressLint({"InflateParams", "RtlHardcoded"})
    private void initView() {
        this.windowManager = (WindowManager) AppContext.get().getContext().getSystemService(WINDOW_SERVICE);
        this.homeWatcherUtil = new HomeWatcherUtil(AppContext.get().getContext());
        this.homeWatcherUtil.setOnHomePressedListener(this);
        this.layoutParams = ConfigParamsWindownManager.init();
        this.layoutParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        if (PrefUtil.get().getInt(Pref.X) != 4 || PrefUtil.get().getInt(Pref.Y) != 4 || PrefUtil.get().getInt(Pref.G) != 4) {
            this.layoutParams.gravity = PrefUtil.get().getInt(Pref.G);
            this.layoutParams.x = PrefUtil.get().getInt(Pref.X);
            this.layoutParams.y = PrefUtil.get().getInt(Pref.Y);
        }
        this.view = LayoutInflater.from(AppContext.get().getContext()).inflate(R.layout.touch, null);
        this.unbinder = ButterKnife.bind(this, view);
        this.addView();
        this.cvTouch.setVisibility(View.GONE);
        this.showView(true);
        this.setUpTouchUtil = new SetUpTouchUtil().create(this.cvTouch);
        this.updateIconTouch();
        if (this.getWindowManager() == null || this.getView() == null || this.getLayoutParams() == null) {
            return;
        }
        this.viewManagerAnimateOnTouchUtil = ViewManagerAnimateOnTouchUtil.init(this);
        this.viewManagerAnimateOnTouchUtil.setEvenClick(this);
        this.menuTouchDialog = MenuTouchDialog.init(AppContext.get().getContext(), this);
        this.handler = new Handler(Looper.getMainLooper());
    }

    public WindowManager getWindowManager() {
        return windowManager;
    }

    public View getView() {
        return this.view;
    }

    public WindowManager.LayoutParams getLayoutParams() {
        return this.layoutParams;
    }

    private void addView() {
        this.windowManager.addView(this.view, this.layoutParams);
    }

    public void removeView() {

        if (this.unbinder != null) {
            this.unbinder.unbind();
        }

        if (this.menuTouchDialog != null) {
            this.menuTouchDialog.removeAll();
            this.menuTouchDialog = null;
        }

        if (this.windowManager != null && this.view != null) {
            this.windowManager.removeView(this.view);
            this.windowManager = null;
            this.view = null;
        }

        if (this.viewManagerAnimateOnTouchUtil != null) {
            this.viewManagerAnimateOnTouchUtil.cleanAll();
        }

        if (this.homeWatcherUtil != null) {
            this.homeWatcherUtil.stopWatch();
        }

        if (this.setUpTouchUtil != null) {
            this.setUpTouchUtil = null;
        }
    }

    public void showView(boolean anitation) {
        if (this.cvTouch == null) {
            return;
        }
        this.cvTouch.setVisibility(View.VISIBLE);
        if (!anitation) {
            return;
        }
        AnimatorUtil.startAnimationScaleIn(this.cvTouch);
    }

    public void hideView(boolean anitation) {
        if (this.cvTouch == null) {
            return;
        }
        if (!anitation) {
            this.cvTouch.setVisibility(View.GONE);
            return;
        }
        AnimatorUtil.startAnimationScaleOut(this.cvTouch);
    }

    public void evenBusCaptureScreen() {
        RxScreenCapture.get().setContext(AppContext.get().getContext()).setLifeRxScreenCapture(this.menuTouchDialog).start();
    }

    public void evenBusCaptureScreenVideo() {
        RxScreenCapture.get().setContext(AppContext.get().getContext()).setLifeRxScreenCapture(this.menuTouchDialog).createRecorder().startRecorder();
    }

    public void evenBusFlash() {
        if (this.menuTouchDialog == null || AppContext.get().getContext() == null) {
            return;
        }
        FlashDialog.create(AppContext.get().getContext()).show();
    }

    private void canMenuTouch() {
        if (this.menuTouchDialog == null) {
            return;
        }
        this.menuTouchDialog.cancelMenuTouch(true);
    }

    @Override
    public void onSingleClick() {
        int position = PrefUtil.get().getInt(Pref.KEY_CLICK_SINGLE);
        this.checkKeyEventTouch(position);
    }

    @Override
    public void onDoubleClick() {
        int position = PrefUtil.get().getInt(Pref.KEY_CLICK_SINGLE);
        if (position == 4) {
            return;
        }
        int positionDouble = PrefUtil.get().getInt(Pref.KEY_CLICK_DOUBLE);
        this.checkKeyEventTouch(positionDouble);
    }

    @Override
    public void onLongClick() {
        int positionLong = PrefUtil.get().getInt(Pref.KEY_CLICK_LONG);
        this.checkKeyEventTouch(positionLong);
    }

    private void checkKeyEventTouch(int positionDouble) {
        if (this.menuTouchDialog == null) {
            return;
        }
        int key = OptionDialog.getKey(positionDouble);
        if (key == DIALOG_MENU_CLICK) {
            this.menuTouchDialog.showMenuTouch();
            this.hideView(true);
            return;
        }
        FunUtil funUtil = this.menuTouchDialog.getFunUtil();
        if (funUtil == null) {
            return;
        }
        funUtil.select(key);
    }

    @Override
    public void onCancelMenuTouch() {
        this.canMenuTouch();
    }

    @Override
    public void onCancel(boolean is) {
        if (this.homeWatcherUtil != null) {
            this.homeWatcherUtil.stopWatch();
        }
        if (!is || this.lifeCaptureVideo == EvenBusCaptureVideoUpdateTouch.LifeCaptureVideo.START || this.lifeCaptureVideo == EvenBusCaptureVideoUpdateTouch.LifeCaptureVideo.SETUP) {
            return;
        }
        this.showView(true);
        if (this.viewManagerAnimateOnTouchUtil == null) {
            return;
        }
        this.viewManagerAnimateOnTouchUtil.alphaTouch();
    }

    @Override
    public void onShow() {
        if (this.homeWatcherUtil == null) {
            return;
        }
        this.homeWatcherUtil.startWatch();
    }

    //region capture screen
    private Disposable subscribe;

    @SuppressLint("CheckResult")
    @Override
    public void onResultCaptureScreen(Bitmap bitmap) {
        if (AppContext.get().getContext() == null) {
            return;
        }
        try {
            ReviewScreenDialog reviewScreenDialog = ReviewScreenDialog.init(AppContext.get().getContext(), bitmap, "");
            this.subscribe = SaveBitmapUtil
                    .createSaveFile(bitmap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((s, throwable) -> {
                        reviewScreenDialog.setFile(new File(s));
                        EventBus.getDefault().post(new EventBusPathScreenShot().setPath(s));
                        if (subscribe != null) {
                            subscribe.dispose();
                        }
                    });
            boolean bool = PrefUtil.get().getBool(Pref.CAPTURE_SCREEN_PREVIEW, true);
            if (bool) {
                reviewScreenDialog.showReviewScreen();
            } else {
                Toast.makeText(AppContext.get().getContext(), AppContext.get().getContext().getString(R.string.capture_sussce), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailedCaptureScreen() {
        this.showView(true);
        if (this.viewManagerAnimateOnTouchUtil == null) {
            return;
        }
        this.viewManagerAnimateOnTouchUtil.alphaTouch();
    }

    @Override
    public void onStartCaptureScreen() {
        this.hideView(false);
    }

    @Override
    public void onStopCaptureScreen() {
        this.showView(true);
        if (this.viewManagerAnimateOnTouchUtil == null) {
            return;
        }
        this.viewManagerAnimateOnTouchUtil.alphaTouch();
    }

    @Override
    public void onStartCaptureScreenVideo() {

    }

    @Override
    public void onResultCaptureScreenVideo(String path) {
        this.lifeCaptureVideo = EvenBusCaptureVideoUpdateTouch.LifeCaptureVideo.STOP;
        Log.e("TinhNv", "onResultCaptureScreenVideo: " + path);
    }

    @Override
    public void onErrorCaptureScreenVideo() {
        this.lifeCaptureVideo = EvenBusCaptureVideoUpdateTouch.LifeCaptureVideo.STOP;
        RxScreenCapture.get().stopCaptureVideo();
    }
    //endregion

    //region capture video update touch
    @SuppressLint("SetTextI18n")
    public void captureVideoTouchSetUp() {
        this.longtime = 0;
        this.tvTimeVideo.setText("00:00");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        Date date = new Date();
        this.cvTouch.setVisibility(View.GONE);
        this.cpllTimeVideo.setVisibility(View.VISIBLE);
        this.runnable = () -> {
            this.longtime = this.longtime + 1000;
            date.setTime(this.longtime);
            this.tvTimeVideo.setText(format.format(date));
            this.handler.postDelayed(this.runnable, 1000);
        };
        this.viewManagerAnimateOnTouchUtil.alphaTouch();
    }

    public void captureVideoTouchStart() {
        if (this.handler == null) {
            this.cvTouch.setVisibility(View.VISIBLE);
            this.cpllTimeVideo.setVisibility(View.GONE);
            return;
        }
        this.handler.postDelayed(this.runnable, 1000);
    }

    public void captureVideoTouchStop() {
        if (this.handler != null) {
            this.handler.removeCallbacks(this.runnable);
            this.handler.removeCallbacksAndMessages(null);
        }
        this.cvTouch.setVisibility(View.VISIBLE);
        this.cpllTimeVideo.setVisibility(View.GONE);
    }

    public void updateIconTouch() {
        if (this.setUpTouchUtil == null) {
            return;
        }
        this.setUpTouchUtil.setupAll();

        if (this.viewManagerAnimateOnTouchUtil != null) {
            this.viewManagerAnimateOnTouchUtil.updateFloatingTouchView(true);
        }
    }
    //endregion


}
