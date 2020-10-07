package com.developer.memory.junk.util;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.developer.memory.R;
import com.developer.memory.junk.model.JunkInfo;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CleanUtil {
    public static String formatShortFileSize(Context context, long number) {
        if (context == null) {
            return "";
        }

        float result = number;
        int suffix = R.string.byte_short;
        if (result > 900) {
            suffix = R.string.kilo_byte_short;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = R.string.mega_byte_short;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = R.string.giga_byte_short;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = R.string.tera_byte_short;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = R.string.peta_byte_short;
            result = result / 1024;
        }
        String value;
        if (result < 1) {
            value = String.format("%.2f", result);
        } else if (result < 10) {
            value = String.format("%.2f", result);
        } else if (result < 100) {
            value = String.format("%.1f", result);
        } else {
            value = String.format("%.0f", result);
        }
        return context.getResources().
                getString(R.string.clean_file_size_suffix,
                        value, context.getString(suffix));
    }

    public static boolean deleteFile(File file) {
        if (file.isDirectory()) {
            String[] children = file.list();
            for (String name : children) {
                boolean suc = deleteFile(new File(file, name));
                if (!suc) {
                    return false;
                }
            }
        }
        return file.delete();
    }

    public static void killAppProcesses(Context context, String packageName) {
        if (packageName == null || packageName.isEmpty()) {
            return;
        }

        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        am.killBackgroundProcesses(packageName);
    }

    public static Disposable freeJunkInfos(Context context, ArrayList<JunkInfo> junks, ArrayList<JunkInfo> sysCaches, CleanCallBack cleanCallBack) {
        return Single.create((SingleOnSubscribe<Void>) emitter -> {
            for (JunkInfo info : junks) {
                try {
                    File file = new File(info.mPath);
                    if (file.exists()) {
                        boolean delete = file.delete();
                        if (delete) {
                            cleanCallBack.onProgressClean(info);
                        } else {
                            cleanCallBack.onProgressClean(info);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    cleanCallBack.onError(e);
                }
                if (junks.size() < 50) {
                    SystemClock.sleep(50);
                }
            }
            for (JunkInfo info : sysCaches) {
                if (info == null || TextUtils.isEmpty(info.name)) {
                    continue;
                }
                cleanCallBack.onProgressClean(info);
                if (sysCaches.size() < 50) {
                    SystemClock.sleep(50);
                }
            }
            cleanCallBack.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public static void freeAllAppsCache(Context context, CleanCallBack cleanCallBack) {

        File externalDir = context.getExternalCacheDir();
        if (externalDir == null) {
            return;
        }
        PackageManager pm = context.getPackageManager();
        @SuppressLint("WrongConstant") List<ApplicationInfo>
                installedPackages = pm.getInstalledApplications(PackageManager.GET_GIDS);
        for (ApplicationInfo info : installedPackages) {
            String externalCacheDir = externalDir.getAbsolutePath()
                    .replace(context.getPackageName(), info.packageName);
            File externalCache = new File(externalCacheDir);
            if (externalCache.exists() && externalCache.isDirectory()) {
                deleteFile(externalCache);
            }
        }
        try {
            Method freeStorageAndNotify = pm.getClass()
                    .getMethod("freeStorageAndNotify", long.class, IPackageDataObserver.class);
            long freeStorageSize = Long.MAX_VALUE;

            freeStorageAndNotify.invoke(pm, freeStorageSize, new IPackageDataObserver.Stub() {
                @Override
                public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
                    /*cleanCallBack.onProgressClean(packageName, freeStorageSize);*/
                }
            });
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            cleanCallBack.onError(e);
        }
        cleanCallBack.onComplete();
    }

    public interface CleanCallBack {

        void onComplete();

        void onProgressClean(JunkInfo junkInfo);

        void onError(Throwable throwable);
    }
}
