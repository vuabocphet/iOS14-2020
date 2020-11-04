package com.developer.phimtatnhanh.setuptouch.utilities;


import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import com.developer.memory.junk.RamMaster;
import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.app.AppContext;
import com.developer.phimtatnhanh.data.PrefUtil;
import com.developer.phimtatnhanh.notui.PerActivityTransparent;
import com.developer.phimtatnhanh.service.CustomAccessibilityService;
import com.developer.phimtatnhanh.setuptouch.config.ConstKey;
import com.developer.phimtatnhanh.setuptouch.dialog.FlashDialog;
import com.developer.phimtatnhanh.setuptouch.dialog.MenuTouchDialog;
import com.developer.phimtatnhanh.setuptouch.dialog.SeekBarDialog;
import com.developer.phimtatnhanh.setuptouch.dialog.TimeBrightnessDialog;
import com.developer.phimtatnhanh.setuptouch.utilities.capturescreen.RxScreenCapture;
import com.developer.phimtatnhanh.setuptouch.utilities.permission.ConfigPer;
import com.developer.phimtatnhanh.setuptouch.utilities.permission.PermissionUtils;
import com.developer.phimtatnhanh.ui.cleanjunk.CleanJunkActivity;
import com.developer.phimtatnhanh.ui.junk.JunkActivity;
import com.developer.phimtatnhanh.ui.ram.RamActivity;

import static android.content.Context.DEVICE_POLICY_SERVICE;
import static android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS;
import static com.developer.phimtatnhanh.setuptouch.utilities.permission.ConfigPer.CACHE_JUNK;
import static com.developer.phimtatnhanh.setuptouch.utilities.permission.ConfigPer.CACHE_RAM;

public class FunUtil implements ConstKey {

    private MenuTouchDialog menuTouchDialog;
    private Handler handler;

    public FunUtil(MenuTouchDialog menuTouchDialog) {
        this.menuTouchDialog = menuTouchDialog;
        this.handler = new Handler(Looper.getMainLooper());
        ObservableTouchUtil.get();
        RxScreenCapture.init();
    }

    public void select(int i) {
        switch (i) {
            case MENU_NOT_CLICK:
                return;
            case MENU_HOME_SCREEN:
                this.onHome();
                return;
            case MENU_RECENT_SCREEN:
                this.onRecent();
                return;
            case MENU_BACK:
                this.onBack();
                return;
            case MENU_AIR:
                this.onAir();
                return;
            case MENU_BLUETOOTH:
                this.onBluetooth();
                return;
            case MENU_LIGHT_SCREEN:
                this.onBrightness();
                return;
            case MENU_SETTING:
                this.onSetting();
                return;
            case MENU_NOTIFICATION:
                this.onNotification();
                return;
            case MENU_FLASH_LIGHT:
                this.onFlash();
                return;
            case MENU_OFF_SCREEN:
                this.onOffScreen();
                return;
            case MENU_ROTATE_SCREEN:
                this.onRotateScreen();
                return;
            case MENU_VOLUE_UP:
                this.onVolueUp();
                return;
            case MENU_VOLUE_OUT:
                this.onVolueOut();
                return;
            case MENU_WIFI:
                this.onWifi();
                return;
            case MENU_NETWORK:
                this.onNetWork();
                return;
            case MENU_STAR:
                this.onStar();
                return;
            case MENU_TIME_LIGHT_SCREEN:
                this.onTimeBrightness();
                return;
            case MENU_PHONE:
                this.onPhoneDevice();
                return;
            case MENU_CAPTURE_SCREEN_VIDEO:
                this.onCaptureVideo();
                return;
            case MENU_CAPTURE_SCREEN:
                this.onCaptureScreen();
                return;
            case MENU_POWER_DIALOG:
                this.onPowerDialog();
                return;
            case MENU_SCAN_JUNK:
                this.junk();
                return;
            case MENU_SCAN_RAM:
                this.ram();
        }
    }


    private void onHome() {
        if (dialogempty()) return;
       /* Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter",
                "com.miui.permcenter.permissions.PermissionsEditorActivity");
        intent.putExtra("extra_pkgname", getPackageName());
        startActivity(intent);
        */
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.get().getContext().startActivity(startMain);
    }

    private void onRecent() {
        if (dialogempty()) return;
        if (allAccessibility()) return;
        this.handler.postDelayed(() -> ObservableTouchUtil.get().getKeyServiceListener().recentKey(), 255);
    }

    private void onBack() {
        if (dialogempty()) return;
        if (allAccessibility()) return;
        this.handler.postDelayed(() -> ObservableTouchUtil.get().getKeyServiceListener().backKey(), 255);
    }

    private void onAir() {
        if (dialogempty()) return;
        Intent panelIntent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
        panelIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.get().getContext().startActivity(panelIntent);
    }

    private void onBluetooth() {
        if (dialogempty()) return;
        Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.get().getContext().startActivity(intent);
    }

    private boolean dialogempty() {
        return this.menuTouchDialog == null || AppContext.get().getContext() == null;
    }

    private void onBrightness() {
        if (dialogempty()) return;
        if (allSetting()) return;
        SeekBarDialog.create(AppContext.get().getContext(), SeekBarDialog.TypeSeekBar.BRIGHTNESS);
    }

    private void onSetting() {
        if (dialogempty()) return;
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.get().getContext().startActivity(intent);
    }

    private void onNotification() {
        if (dialogempty()) return;
        if (allAccessibility()) return;
        this.handler.postDelayed(() -> ObservableTouchUtil.get().getKeyServiceListener().notificationKey(), 255);
    }

    private void onFlash() {
        if (dialogempty()) return;
        if (allCamera()) return;
        FlashDialog.create(AppContext.get().getContext()).show();
    }

    @SuppressLint("WakelockTimeout")
    private void onOffScreen() {
        if (dialogempty()) return;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            boolean bool = PermissionUtils.isAdmin(AppContext.get().getContext());
            if (!bool) {
                PerActivityTransparent.open(AppContext.get().getContext(), ConfigPer.ACTION_ADMIN, 0);
                return;
            }
            DevicePolicyManager devicePolicyManager = (DevicePolicyManager) AppContext.get().getContext().getSystemService(DEVICE_POLICY_SERVICE);
            if (devicePolicyManager == null) {
                return;
            }
            devicePolicyManager.lockNow();
            return;
        }
        if (allAccessibility()) return;
        this.handler.postDelayed(() -> ObservableTouchUtil.get().getKeyServiceListener().onLockScreen(), 255);
    }

    private void onRotateScreen() {
        if (dialogempty()) return;
        if (allSetting()) return;
        ScreenOrientationUtil.setScreenOrientation(AppContext.get().getContext());
    }

    private void onVolueUp() {
        if (dialogempty()) return;
        if (allSetting()) return;
        SeekBarDialog.create(AppContext.get().getContext(), SeekBarDialog.TypeSeekBar.AUTIO);
    }

    private void onVolueOut() {
        if (dialogempty()) return;
        if (allSetting()) return;
        SeekBarDialog.create(AppContext.get().getContext(), SeekBarDialog.TypeSeekBar.AUTIO);
    }

    private void onWifi() {
        if (dialogempty()) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Intent panelIntent = new Intent(Settings.Panel.ACTION_WIFI);
            panelIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            AppContext.get().getContext().startActivity(panelIntent);
            return;
        }
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.get().getContext().startActivity(intent);
    }

    private void onNetWork() {
        if (dialogempty()) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Intent panelIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
            panelIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            AppContext.get().getContext().startActivity(panelIntent);
            return;
        }
        Intent intent = new Intent(ACTION_DATA_ROAMING_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.get().getContext().startActivity(intent);
    }

    private void onStar() {
        if (dialogempty()) return;
        StarAppUtil.init()
                .setContext(AppContext.get().getContext())
                .setListUtils(this.menuTouchDialog.listUtils)
                .setCsStarAppMenuTouch(this.menuTouchDialog.csStarAppMenuTouch)
                .setCsHomeMenuTouch(this.menuTouchDialog.csHomeMenuTouch)
                .setGridStarAppMenuTouch(this.menuTouchDialog.gridStarAppMenuTouch)
                .setProgressBar(this.menuTouchDialog.progressLoad)
                .setMenuTouchDialog(this.menuTouchDialog)
                .show();
    }

    private void onTimeBrightness() {
        if (dialogempty()) return;
        if (allSetting()) return;
        TimeBrightnessDialog.init(AppContext.get().getContext()).showTimeScreen();
    }

    private void onPhoneDevice() {
        if (dialogempty()) return;
        Toast.makeText(AppContext.get().getContext(), AppContext.get().getContext().getString(R.string.developer), Toast.LENGTH_SHORT).show();
    }

    private void onCaptureVideo() {
        if (dialogempty()) return;
        if (allReadWriteStogre(MENU_CAPTURE_SCREEN_VIDEO)) return;
        if (allAudio()) return;
        RxScreenCapture.get().setContext(AppContext.get().getContext()).setLifeRxScreenCapture(this.menuTouchDialog).createRecorder().startRecorder();
    }

    private void onCaptureScreen() {
        if (dialogempty()) return;
        if (allReadWriteStogre(MENU_CAPTURE_SCREEN)) return;
        RxScreenCapture.get().setContext(AppContext.get().getContext()).setLifeRxScreenCapture(this.menuTouchDialog).start();
    }

    private void onPowerDialog() {
        if (dialogempty()) return;
        if (allAccessibility()) return;
        this.handler.postDelayed(() -> ObservableTouchUtil.get().getKeyServiceListener().powerDialog(), 255);
    }

    private void junk() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (allJunk(MENU_SCAN_JUNK)) return;
        }
        if (allReadWriteStogre(MENU_SCAN_JUNK)) return;
        if (System.currentTimeMillis() <= PrefUtil.get().getLong(CACHE_JUNK, 0L) + (15 * 60 * 1000)) {
            CleanJunkActivity.open(AppContext.get().getContext(), true);
            return;
        }
        JunkActivity.open(AppContext.get().getContext());
    }

    private void ram() {
        if (allJunk(MENU_SCAN_RAM)) return;
        if (System.currentTimeMillis() <= PrefUtil.get().getLong(CACHE_RAM, 0L) + (15 * 60 * 1000)) {
            RamActivity.open(AppContext.get().getContext(), true);
            return;
        }
        RamActivity.open(AppContext.get().getContext(), false);
    }

    private boolean allAccessibility() {
        this.menuTouchDialog.cancelMenuTouch(true);
        boolean accessibilityServiceEnabled = PermissionUtils.isAccessibilityServiceEnabled(AppContext.get().getContext(), CustomAccessibilityService.class);
        if (!accessibilityServiceEnabled) {
            PerActivityTransparent.open(AppContext.get().getContext(), ConfigPer.ACTION_ACCESSIBILITY_SETTINGS, 0);
            return true;
        }
        return false;
    }

    private boolean allSetting() {
        this.menuTouchDialog.cancelMenuTouch(true);
        boolean settingEnabled = PermissionUtils.checkPerSystemSetting(AppContext.get().getContext());
        if (!settingEnabled) {
            PermissionUtils.requestSetting(AppContext.get().getContext());
            return true;
        }
        return false;
    }

    private boolean allCamera() {
        this.menuTouchDialog.cancelMenuTouch(true);
        boolean cameraEnabled = PermissionUtils.checkPerShowDialog(AppContext.get().getContext(), ConfigPer.CAMERA);
        if (!cameraEnabled) {
            PerActivityTransparent.open(AppContext.get().getContext(), ConfigPer.RQCODE_CAMERA, 0);
            return true;
        }
        return false;
    }

    private boolean allReadWriteStogre(int keyCode) {
        boolean cameraEnabled = PermissionUtils.checkPerShowDialog(AppContext.get().getContext(), ConfigPer.READWRITE);
        if (!cameraEnabled) {
            PerActivityTransparent.open(AppContext.get().getContext(), ConfigPer.RQCODE_READ_WRITE, keyCode);
            return true;
        }
        return false;
    }

    private boolean allAudio() {
        this.menuTouchDialog.cancelMenuTouch(true);
        boolean cameraEnabled = PermissionUtils.checkPerShowDialog(AppContext.get().getContext(), ConfigPer.RECORD_AUDIO);
        if (!cameraEnabled) {
            PerActivityTransparent.open(AppContext.get().getContext(), ConfigPer.RQCODE_RECORD_AUDIO, MENU_CAPTURE_SCREEN_VIDEO);
            return true;
        }
        return false;
    }

    private boolean allJunk(int menuType) {
        this.menuTouchDialog.cancelMenuTouch(true);
        boolean permission = RamMaster.hasPermission(AppContext.get().getContext());
        if (!permission) {
            PerActivityTransparent.open(AppContext.get().getContext(), ConfigPer.RQCODE_PACKAGE_USAGE_STATS, menuType);
            return true;
        }
        return false;
    }

}
