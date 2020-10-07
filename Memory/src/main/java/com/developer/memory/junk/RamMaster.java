package com.developer.memory.junk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Process;
import android.provider.Settings;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class RamMaster {

     /*   <uses-permission
    android:name="android.permission.PACKAGE_USAGE_STATS"
    tools:ignore="ProtectedPermissions" />

    <uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES" />*/

    public static List<String> printForegroundTask(Context context) {
        List<String> list = new ArrayList<>();
        long time = System.currentTimeMillis();
        @SuppressLint("WrongConstant") UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
        List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, 0, time);
        if (appList != null && appList.size() > 0) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
            for (UsageStats usageStats : appList) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (!mySortedMap.isEmpty()) {
                Set<Long> longs = mySortedMap.keySet();
                for (Long aLong : longs) {
                    UsageStats usageStats = mySortedMap.get(aLong);
                    if (usageStats != null) {
                        String packageName = usageStats.getPackageName();
                        if (list.contains(packageName)
                                || packageName.startsWith("com.google.android")
                                || packageName.startsWith("com.android")
                                || packageName.startsWith("com.miui")
                                || packageName.startsWith("com.samsung")
                                || packageName.startsWith("com.coloros")
                                || packageName.startsWith("com.huawei")
                                || packageName.startsWith("com.oneplus")
                                || packageName.startsWith("com.vivo")) {
                            continue;
                        }
                        list.add(packageName);
                    }
                }
            }
        }
        return list;
    }

    public static boolean needPermissionForBlocking(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode != AppOpsManager.MODE_ALLOWED);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void requestPerRamMaster(Activity activity, int rqCode) {
        if (activity == null) {
            return;
        }
        activity.startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), rqCode);
    }

    public static boolean hasPermission(@NonNull final Context context) {
        boolean granted = false;
        AppOpsManager appOps = (AppOpsManager) context
                .getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(), context.getPackageName());

        if (mode == AppOpsManager.MODE_DEFAULT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                granted = (context.checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
            }
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }
        return granted;
    }

    public static boolean killPackageProcesses(ActivityManager am, String packagename) {
        boolean result = false;
        if (am != null) {
            am.killBackgroundProcesses(packagename);
            result = !isPackageRunning(am, packagename);
        }
        return result;
    }

    public static boolean isPackageRunning(ActivityManager am, String packagename) {
        return findPIDbyPackageName(am, packagename) != -1;
    }

    public static int findPIDbyPackageName(ActivityManager am, String packagename) {
        int result = -1;
        if (am != null) {
            for (ActivityManager.RunningAppProcessInfo pi : am.getRunningAppProcesses()) {
                if (pi.processName.equalsIgnoreCase(packagename)) {
                    result = pi.pid;
                }
                if (result != -1) break;
            }
        }
        return result;
    }

    public static void autoStartRunningApplication(Context context) {
        if (context == null || !isDeviceChina()) {
            return;
        }
        try {
            Intent intent = new Intent();
            String manufacturer = Build.MANUFACTURER;
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else if ("oneplus".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.oneplus.security", "com.oneplus.security.chainlaunch.view.ChainLaunchAppListAct‌​ivity"));
            }
            List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (list.size() > 0) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public static boolean isDeviceChina() {
        try {
            String manufacturer = Build.MANUFACTURER;
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                return true;
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                return true;
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                return true;
            } else if ("oneplus".equalsIgnoreCase(manufacturer)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
