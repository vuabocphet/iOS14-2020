package com.developer.phimtatnhanh.setuptouch.utilities.capturescreen;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaScannerConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.app.AppContext;
import com.developer.phimtatnhanh.notui.PerActivityTransparent;
import com.developer.phimtatnhanh.setuptouch.config.ConfigAll;
import com.developer.phimtatnhanh.setuptouch.dialog.CountDownDialog;
import com.developer.phimtatnhanh.setuptouch.utilities.ConfigFile;
import com.developer.phimtatnhanh.setuptouch.utilities.SaveBitmapUtil;
import com.developer.phimtatnhanh.setuptouch.utilities.bus.EvenBusCaptureVideoUpdateTouch;
import com.developer.phimtatnhanh.setuptouch.utilities.capturescreen.video.LifeCaptureVideo;
import com.developer.phimtatnhanh.setuptouch.utilities.capturescreen.video.ScreenCaptureVideo;
import com.developer.phimtatnhanh.setuptouch.utilities.capturescreen.video.TypeError;
import com.developer.phimtatnhanh.setuptouch.utilities.permission.ConfigPer;
import com.developer.phimtatnhanh.util.DeviceUtils;
import com.developer.phimtatnhanh.util.PathUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MEDIA_PROJECTION_SERVICE;
import static android.content.Context.WINDOW_SERVICE;

public final class RxScreenCapture implements ConfigPer, LifeCaptureVideo {

    @SuppressLint("StaticFieldLeak")
    private static RxScreenCapture instance;

    public static void init() {
        if (instance == null) {
            instance = new RxScreenCapture();
        }
    }

    public static RxScreenCapture get() {
        return instance == null ? instance = new RxScreenCapture() : instance;
    }


    public void removeAll() {
        this.resultCode = -1010;
        this.resultData = null;
        this.lifeRxScreenCapture = null;
        this.screenCaptureVideo = null;
        this.contentValues = null;
        this.mUri = null;
        RxScreenCapture.instance = null;
    }

    private static final String TAG = "CCTVRxScreenCapture";
    private int resultCode = -1010;
    private Intent resultData;
    private LifeRxScreenCapture lifeRxScreenCapture;
    private ScreenCaptureVideo screenCaptureVideo;
    private WindowManager windowManager;
    private ContentValues contentValues;
    private Uri mUri;
    private MediaProjectionManager mediaProjectionManager;

    public RxScreenCapture setContext(Context context) {
        AppContext.create(context);
        return this;
    }

    public RxScreenCapture setResultCode(int resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    public int getResultCode() {
        return resultCode;
    }

    public Intent getResultData() {
        return resultData;
    }

    public void setResultData(Intent resultData) {
        this.resultData = resultData;
    }

    //region Screen Capture Image
    public RxScreenCapture setLifeRxScreenCapture(LifeRxScreenCapture lifeRxScreenCapture) {
        if (this.lifeRxScreenCapture == null) {
            this.lifeRxScreenCapture = lifeRxScreenCapture;
        }
        return this;
    }

    public void start() {
        if (AppContext.get().getContext() == null) {
            if (lifeRxScreenCapture != null) {
                lifeRxScreenCapture.onFailedCaptureScreen();
            }
            return;
        }

        if (this.windowManager == null) {
            this.windowManager = (WindowManager) AppContext.get().getContext().getSystemService(WINDOW_SERVICE);
        }

        if (this.mediaProjectionManager == null) {
            this.mediaProjectionManager = (MediaProjectionManager) AppContext.get().getContext().getSystemService(MEDIA_PROJECTION_SERVICE);
        }

        if (this.resultData == null || this.resultCode == -1010) {
            PerActivityTransparent.open(AppContext.get().getContext(), GET_MEDIA_PROJECTION_CODE, 0);
            return;
        }
        if (this.lifeRxScreenCapture != null) {
            this.lifeRxScreenCapture.onStartCaptureScreen();
        }
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            this.captureScreen().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
        }, ConfigAll.durationCaptureScreen);
    }

    public Single<Void> captureScreen() {

        return Single.create(emitter -> {
            try {
                this.capture(new Rect(0, 0, DeviceUtils.w(windowManager), DeviceUtils.h(windowManager)));
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        });
    }

    private void capture(Rect rect1) {
        AtomicReference<Bitmap> bitmapCaptureScreen = new AtomicReference<>();
        if (this.mediaProjectionManager == null || this.windowManager == null) {
            if (this.lifeRxScreenCapture != null) {
                this.lifeRxScreenCapture.onFailedCaptureScreen();
            }
            return;
        }
        final MediaProjection mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, resultData);
        int w = DeviceUtils.w(this.windowManager);
        int h = DeviceUtils.h(this.windowManager);
        @SuppressLint("WrongConstant") final ImageReader imageReader = ImageReader.newInstance(w, h, PixelFormat.RGBA_8888, 2);
        final VirtualDisplay virtualDisplay = mediaProjection.createVirtualDisplay(
                TAG, w, h, DeviceUtils.dpi(this.windowManager),
                DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                imageReader.getSurface(), null, null);
        imageReader.setOnImageAvailableListener(reader -> {
            Image image = imageReader.acquireLatestImage();
            if (image != null) {
                try {
                    Image.Plane[] planes = image.getPlanes();
                    ByteBuffer buffer = planes[0].getBuffer();
                    int pixelStride = planes[0].getPixelStride();
                    int rowStride = planes[0].getRowStride();
                    int rowPadding = rowStride - pixelStride * DeviceUtils.w(windowManager);
                    Bitmap bitmap = Bitmap.createBitmap(w + rowPadding / pixelStride, h, Bitmap.Config.ARGB_8888);
                    bitmap.copyPixelsFromBuffer(buffer);
                    bitmapCaptureScreen.set(Bitmap.createBitmap(bitmap, rect1.left, rect1.top, rect1.width(), rect1.height()));
                    if (this.lifeRxScreenCapture != null && bitmapCaptureScreen.get() != null) {
                        this.lifeRxScreenCapture.onResultCaptureScreen(bitmapCaptureScreen.get());
                        this.lifeRxScreenCapture.onStopCaptureScreen();
                    }
                    if (this.lifeRxScreenCapture != null && bitmapCaptureScreen.get() == null) {
                        this.lifeRxScreenCapture.onFailedCaptureScreen();
                    }
                    bitmap.recycle();
                    image.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (this.lifeRxScreenCapture != null) {
                        this.lifeRxScreenCapture.onFailedCaptureScreen();
                    }
                }
            }
            virtualDisplay.release();
            imageReader.close();
            mediaProjection.stop();
        }, new Handler(Looper.getMainLooper()));
    }

    //endregion

    //region Screen Capture Video
    public RxScreenCapture createRecorder() {
        if (this.screenCaptureVideo == null) {
            this.screenCaptureVideo = ScreenCaptureVideo.create().setLifeCaptureVideo(this);
        }
        return this;
    }

    public void startRecorder() {
        if (this.resultData == null || this.resultCode == -1010) {
            PerActivityTransparent.open(AppContext.get().getContext(), GET_MEDIA_PROJECTION_CODE_VIDEO, 0);
            return;
        }
        this.screenCaptureVideo.setmResultCode(this.resultCode).setmResultData(this.resultData);
        //Chuẩn bị quay màn hình
        CountDownDialog.create(AppContext.get().getContext()).startCountDown(new CountDownDialog.LifeCountDown() {
            @Override
            public void onStartCountDown() {
                EventBus.getDefault().post(new EvenBusCaptureVideoUpdateTouch().setPlay(EvenBusCaptureVideoUpdateTouch.LifeCaptureVideo.SETUP));
            }

            @Override
            public void onEndCountDown() {
                setOutputPath();
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    startCaptureVideo();
                    EventBus.getDefault().post(new EvenBusCaptureVideoUpdateTouch().setPlay(EvenBusCaptureVideoUpdateTouch.LifeCaptureVideo.START));
                }, 100);
            }
        });
    }

    private void startCaptureVideo() {
        this.screenCaptureVideo.setup();
        this.screenCaptureVideo.start();
    }

    public void stopCaptureVideo() {
        this.screenCaptureVideo.stopAll();
        EventBus.getDefault().post(new EvenBusCaptureVideoUpdateTouch().setPlay(EvenBusCaptureVideoUpdateTouch.LifeCaptureVideo.STOP));
    }

    private void setOutputPath() {
        String filename = "CCTVVideo_" + generateFileName();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = AppContext.get().getContext().getContentResolver();
            contentValues = new ContentValues();
            contentValues.put(MediaStore.Video.Media.RELATIVE_PATH, ConfigFile.PATH_CAPTURE_VIDEO_SCREEN);
            contentValues.put(MediaStore.Video.Media.TITLE, filename);
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, filename);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, ConfigFile.MIME_TYPE_VIDEO);
            mUri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
            this.screenCaptureVideo.setUriAndroidQ(mUri);
            try {
                String path = PathUtil.getPath(AppContext.get().getContext(), mUri);
                this.screenCaptureVideo.setFilePath(path);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            String folder = this.createFilePath(filename, SaveBitmapUtil.getPathBigVideo());
            this.screenCaptureVideo.setUriAndroidQ(null);
            this.screenCaptureVideo.setFilePath(folder);
        }
    }

    private String createFilePath(String name, String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            File f = File.createTempFile(name,
                    ".mp4",
                    file
            );
            return f.getAbsolutePath();
        } catch (IOException e) {
            return "";
        }
    }

    private String generateFileName() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate).replace(" ", "");
    }

    private void refreshGalleryFile(String filepath) {
        MediaScannerConnection.scanFile(AppContext.get().getContext(),
                new String[]{filepath}, null,
                (path, uri) -> {
                    Log.i("ExternalStorage", "Scanned " + path + ":");
                    Log.i("ExternalStorage", "-> uri=" + uri);
                });
    }

    @Override
    public void onError(TypeError typeError) {
        if (this.lifeRxScreenCapture == null) {
            return;
        }
        this.lifeRxScreenCapture.onErrorCaptureScreenVideo();
    }

    @Override
    public void onResult(String path) {
        if (this.screenCaptureVideo.getUriAndroidQ() == null && this.contentValues == null) {
            this.refreshGalleryFile(path);
        } else {
            try {
                this.contentValues.clear();
                this.contentValues.put(MediaStore.Video.Media.IS_PENDING, 0);
                AppContext.get().getContext().getContentResolver().update(this.mUri, this.contentValues, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (this.lifeRxScreenCapture == null) {
            return;
        }
        this.lifeRxScreenCapture.onResultCaptureScreenVideo(path);
    }

    @Override
    public void onStart() {
        if (this.lifeRxScreenCapture == null) {
            return;
        }
        this.lifeRxScreenCapture.onStartCaptureScreenVideo();
    }
    //endregion

}
