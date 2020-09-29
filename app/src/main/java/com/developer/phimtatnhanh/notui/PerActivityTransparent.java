package com.developer.phimtatnhanh.notui;




/*
 * Create By Nguyễn Tình
 * */

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.service.MyAdmin;
import com.developer.phimtatnhanh.service.TouchService;
import com.developer.phimtatnhanh.setuptouch.config.ConstKey;
import com.developer.phimtatnhanh.setuptouch.utilities.bus.EvenBusCaptureStart;
import com.developer.phimtatnhanh.setuptouch.utilities.bus.EvenBusCaptureStartVideo;
import com.developer.phimtatnhanh.setuptouch.utilities.bus.EvenBusFlash;
import com.developer.phimtatnhanh.setuptouch.utilities.bus.ShowFloatTouch;
import com.developer.phimtatnhanh.setuptouch.utilities.capturescreen.RxScreenCapture;
import com.developer.phimtatnhanh.setuptouch.utilities.permission.ConfigPer;
import com.developer.phimtatnhanh.setuptouch.utilities.permission.LifePermission;
import com.developer.phimtatnhanh.setuptouch.utilities.permission.PermissionUtils;

import org.greenrobot.eventbus.EventBus;

public class PerActivityTransparent extends AppCompatActivity implements ConfigPer, LifePermission {

    public static void open(Context context, int keyCode, int keyMenu) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, PerActivityTransparent.class);
        intent.putExtra(KEYCODE, keyCode);
        intent.putExtra(KEYMENU, keyMenu);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private PermissionUtils permissionUtils;
    private DevicePolicyManager devicePolicyManager;
    private int keyMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        this.permissionUtils = PermissionUtils.init(this);
        int keyCode = intent.getIntExtra(KEYCODE, -1);
        this.keyMenu = intent.getIntExtra(KEYMENU, -1);
        if (keyCode == -1) {
            finish();
            return;
        }
        sw(keyCode);
    }

    private void sw(int keyCode) {
        switch (keyCode) {
            case GET_MEDIA_PROJECTION_CODE:
                boolean b = PermissionUtils.perCaptureScreen(this, GET_MEDIA_PROJECTION_CODE);
                if (!b) {
                    finish();
                }
                break;
            case GET_MEDIA_PROJECTION_CODE_VIDEO:
                boolean b_1 = PermissionUtils.perCaptureScreen(this, GET_MEDIA_PROJECTION_CODE_VIDEO);
                if (!b_1) {
                    finish();
                }
                break;
            case RQCODE_RECORD_AUDIO:
                boolean rc = PermissionUtils.checkPerShowDialog(this, ConfigPer.RECORD_AUDIO);
                if (rc) {
                    finish();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    this.permissionUtils.requestPermissions(ConfigPer.RQCODE_RECORD_AUDIO, ConfigPer.RECORD_AUDIO);
                }
                break;

            case RQCODE_READ_WRITE:
                boolean rw = PermissionUtils.checkPerShowDialog(this, ConfigPer.READWRITE);
                if (rw) {
                    finish();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    this.permissionUtils.requestPermissions(ConfigPer.RQCODE_READ_WRITE, ConfigPer.READWRITE);
                }
                break;

            case RQCODE_CAMERA:
                boolean c = PermissionUtils.checkPerShowDialog(this, ConfigPer.CAMERA);
                if (c) {
                    finish();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    this.permissionUtils.requestPermissions(RQCODE_CAMERA, ConfigPer.CAMERA);
                }
                break;

            case ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Intent it = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(it, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
                }
                break;
            case ACTION_ACCESSIBILITY_SETTINGS:
                boolean b1 = PermissionUtils.perAccessibility(this);
                if (!b1) {
                    finish();
                }
                break;
            case ACTION_ADMIN:
                if (PermissionUtils.isAdmin(this)) {
                    finish();
                    return;
                }
                devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
                ComponentName compName = new ComponentName(this, MyAdmin.class);
                Intent admin = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                admin.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
                admin.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.desadmin));
                admin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(admin, ACTION_ADMIN);
                break;
            default:
                finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_MEDIA_PROJECTION_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                EventBus.getDefault().post(new ShowFloatTouch());
                Toast.makeText(this, getString(R.string.pmedia_denied), Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            RxScreenCapture.get().setResultCode(resultCode).setResultData(data);
            EventBus.getDefault().post(new EvenBusCaptureStart());
            finish();
            return;
        }

        if (requestCode == GET_MEDIA_PROJECTION_CODE_VIDEO) {
            if (resultCode != Activity.RESULT_OK) {
                EventBus.getDefault().post(new ShowFloatTouch());
                Toast.makeText(this, getString(R.string.pmedia_denied), Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            RxScreenCapture.get().setResultCode(resultCode).setResultData(data);
            EventBus.getDefault().post(new EvenBusCaptureStartVideo());
            finish();
            return;
        }

        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            TouchService.start(this);
        }
        if (requestCode == ACTION_ACCESSIBILITY_SETTINGS) {
            if (resultCode != Activity.RESULT_OK) {
                EventBus.getDefault().post(new ShowFloatTouch());
                finish();
                return;
            }
        }
        if (requestCode == ACTION_ADMIN) {
            if (resultCode != Activity.RESULT_OK) {
                EventBus.getDefault().post(new ShowFloatTouch());
                finish();
                return;
            }
            if (devicePolicyManager == null) {
                return;
            }
            devicePolicyManager.lockNow();
        }
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        this.permissionUtils.permisionChange(requestCode, permissions, grantResults);
    }

    @Override
    public void onAllow(int rcode) {
        //Quyền camera được cấp
        if (rcode == RQCODE_CAMERA) {
            EventBus.getDefault().post(new EvenBusFlash());
            finish();
        }

        //Quyền đọc ghi bộ nhớ hoặc micro được cấp và ứng dụng cho quay video màn hình
        if ((rcode == RQCODE_READ_WRITE || rcode == RQCODE_RECORD_AUDIO) && this.keyMenu == ConstKey.MENU_CAPTURE_SCREEN_VIDEO) {
            boolean recordEnabled = PermissionUtils.checkPerShowDialog(this, ConfigPer.RECORD_AUDIO);
            if (recordEnabled) {
                if (RxScreenCapture.get().getResultCode() == -1010 || RxScreenCapture.get().getResultData() == null) {
                    this.sw(GET_MEDIA_PROJECTION_CODE_VIDEO);
                    return;
                }
                EventBus.getDefault().post(new EvenBusCaptureStartVideo());
                finish();
            } else {
                this.sw(RQCODE_RECORD_AUDIO);
                return;
            }
        }

        //Quyền đọc ghi bộ nhớ được cấp và ứng dụng cho chụp ảnh màn hình
        if (rcode == RQCODE_READ_WRITE && this.keyMenu == ConstKey.MENU_CAPTURE_SCREEN) {
            if (RxScreenCapture.get().getResultCode() == -1010 || RxScreenCapture.get().getResultData() == null) {
                this.sw(GET_MEDIA_PROJECTION_CODE);
                return;
            }
            EventBus.getDefault().post(new EvenBusCaptureStart());
            finish();
        }
        finish();
    }

    @Override
    public void onDenied(int rcode) {
        EventBus.getDefault().post(new ShowFloatTouch());
        if (rcode == RQCODE_CAMERA) {
            Toast.makeText(this, getString(R.string.pcamera_denied), Toast.LENGTH_SHORT).show();
            finish();
        }
        if (rcode == RQCODE_READ_WRITE) {
            Toast.makeText(this, getString(R.string.preadwrite_denied), Toast.LENGTH_SHORT).show();
            finish();
        }
        if (rcode == RQCODE_RECORD_AUDIO) {
            Toast.makeText(this, getString(R.string.precord_denied), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onAskagain(int rcode) {
        EventBus.getDefault().post(new ShowFloatTouch());
        this.permissionUtils.openAppSettings(this);
        finish();
    }

}
