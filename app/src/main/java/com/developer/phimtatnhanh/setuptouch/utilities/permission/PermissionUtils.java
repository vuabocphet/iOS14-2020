package com.developer.phimtatnhanh.setuptouch.utilities.permission;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.developer.phimtatnhanh.service.MyAdmin;
import com.developer.phimtatnhanh.service.TouchService;
import com.suke.widget.SwitchButton;

import java.util.List;

import static android.content.Context.DEVICE_POLICY_SERVICE;

/*
 * Camera
 * Setting
 *Read Write Data
 * */

public class PermissionUtils implements ConfigPer {

    private Activity activity;
    private LifePermission lifePermission;


    public static PermissionUtils init(Activity activity) {
        return new PermissionUtils(activity);
    }

    public PermissionUtils(Activity context) {
        this.lifePermission = (LifePermission) context;
        this.setActivity(context);
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }


    private boolean hasPermissionRationale(String... permission) {
        if (getActivity() == null) {
            return false;
        }
        for (String s : permission) {
            boolean result = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), s);
            if (!result) {
                return false;
            }
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermissions(int code, String... permission) {
        if (getActivity() == null) {
            return;
        }
        getActivity().requestPermissions(permission, code);
    }

    public static boolean perCaptureScreen(Activity activity, int rQcode) {
        if (activity == null) {
            return false;
        }
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        if (mediaProjectionManager == null) {
            return false;
        }
        activity.startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), rQcode);
        return true;
    }

    public static boolean perAccessibility(Activity activity) {
        if (activity == null) {
            return false;
        }
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivityForResult(intent, ACTION_ACCESSIBILITY_SETTINGS);
        return true;
    }

    public void permisionChange(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (this.lifePermission == null) {
            return;
        }
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            this.lifePermission.onAllow(requestCode);
            return;
        }
        boolean isCheckRationale = this.hasPermissionRationale(permissions);
        if (isCheckRationale) {
            this.lifePermission.onDenied(requestCode);
            return;
        }
        this.lifePermission.onAskagain(requestCode);
    }

    public void openAppSettings(Activity activity) {
        if (activity == null) {
            return;
        }
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity.getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(myAppSettings);
    }

    //region permission setting

    public static void requestSetting(Context context) {
        if (checkPerSystemSetting(context)) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            openPermissionsSetting(context);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void openPermissionsSetting(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    //endregion

    //region check permission
    public static boolean checkPerSystemSetting(Context context) {
        boolean retVal = true;
        if (context == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = Settings.System.canWrite(context);
        }
        return retVal;
    }

    public static boolean checkPerSystemAler(Context context) {
        if (context == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        }
        return true;
    }

    public static boolean checkPerShowDialog(Context context, String... permission) {
        if (context == null || permission == null || permission.length == 0) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String s : permission) {
                return context.checkSelfPermission(s) == PackageManager.PERMISSION_GRANTED;
            }
        }
        return true;
    }
    //endregion

    public static void isRunningService(Activity activity, Class<?> serviceClass, SwitchButton switchButton) {
        TouchService.start(activity);
        if (activity.isFinishing() || activity.isDestroyed() || switchButton == null) {
            return;
        }
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                switchButton.setChecked(true);
                return;
            }
        }
        switchButton.setChecked(false);
    }

    public static boolean isRunningService(Context activity, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAccessibilityServiceEnabled(Context context, Class<? extends AccessibilityService> service) {
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);

        for (AccessibilityServiceInfo enabledService : enabledServices) {
            ServiceInfo enabledServiceInfo = enabledService.getResolveInfo().serviceInfo;
            if (enabledServiceInfo.packageName.equals(context.getPackageName()) && enabledServiceInfo.name.equals(service.getName()))
                return true;
        }
        return false;
    }

    public static boolean isAdmin(Context context) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(DEVICE_POLICY_SERVICE);
        ComponentName compName = new ComponentName(context, MyAdmin.class);
        return devicePolicyManager.isAdminActive(compName);
    }

}
