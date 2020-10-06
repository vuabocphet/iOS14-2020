package com.developer.memory.junk;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Environment;
import android.os.RemoteException;

import com.developer.memory.R;
import com.developer.memory.junk.callback.IScanCallback;
import com.developer.memory.junk.model.JunkInfo;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class JunkScanRx {

    private IScanCallback mCallback;
    private final int SCAN_LEVEL = 4;
    private JunkInfo mApkInfo;
    private JunkInfo mLogInfo;
    private JunkInfo mTmpInfo;
    private Disposable subscribe;
    private boolean isRunning = false;
    private Context context;

    private int mScanCount;
    private int mTotalCount;
    private ArrayList<JunkInfo> mSysCaches;
    private ArrayList<JunkInfo> junkInfos;
    private HashMap<String, String> mAppNames;
    private long mTotalSize = 0L;
    private Disposable subscribe1;
    private boolean isScanJunk = false;
    private boolean isScanCache = false;

    public JunkScanRx(Context context, IScanCallback callback) {
        mCallback = callback;
        this.context = context;
        mApkInfo = new JunkInfo(this.context);
        mLogInfo = new JunkInfo(this.context);
        mTmpInfo = new JunkInfo(this.context);
    }

    private void travelPath(File root, int level) {
        if (root == null || !root.exists() || level > SCAN_LEVEL) {
            return;
        }

        File[] lists = root.listFiles();
        for (File file : lists) {
            if (!this.isRunning) {
                return;
            }
            if (file.isFile()) {
                String name = file.getName();
                JunkInfo info = null;
                if (name.endsWith(".apk")) {
                    info = new JunkInfo(this.context);
                    info.mSize = file.length();
                    info.name = name;
                    info.mPath = file.getAbsolutePath();
                    info.mIsChild = false;
                    info.mIsVisible = true;
                    mApkInfo.mChildren.add(info);
                    mApkInfo.mSize += info.mSize;
                } else if (name.endsWith(".log")) {
                    info = new JunkInfo(this.context);
                    info.mSize = file.length();
                    info.name = name;
                    info.mPath = file.getAbsolutePath();
                    info.mIsChild = false;
                    info.mIsVisible = true;
                    mLogInfo.mChildren.add(info);
                    mLogInfo.mSize += info.mSize;
                } else if (name.endsWith(".tmp") || name.endsWith(".temp")) {
                    info = new JunkInfo(this.context);
                    info.mSize = file.length();
                    info.name = name;
                    info.mPath = file.getAbsolutePath();
                    info.mIsChild = false;
                    info.mIsVisible = true;
                    mTmpInfo.mChildren.add(info);
                    mTmpInfo.mSize += info.mSize;
                }
                if (info != null && this.mCallback != null && isRunning) {
                    this.mCallback.onProgress(info);
                }
            } else {
                if (level < SCAN_LEVEL) {
                    travelPath(file, level + 1);
                }
            }
        }
    }

    public void startScanJunkRx() {
        this.isRunning = true;
        if (mCallback != null) {
            mCallback.onStartJunk();
        }
        this.subscribe = this.createJunkRx().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        this.subscribe1 = this.createCacheRx().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void stopScanJunkRx() {
        this.isRunning = false;
        if (this.subscribe != null) {
            this.subscribe.dispose();
        }
        if (this.subscribe1 != null) {
            this.subscribe1.dispose();
        }
    }

    private Single<String> createJunkRx() {
        return Single.create(emitter -> {
            try {
                ArrayList<JunkInfo> junk = this.getJunk();
                emitter.onSuccess("onSuccess");
                this.isScanJunk = true;
                junkInfos.addAll(junk);
                if (this.mCallback != null && this.isScanCache && isRunning) {
                    this.mCallback.onFinish(junkInfos, mSysCaches);
                    this.mCallback.onStopJunk();
                }
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
                if (this.mCallback != null) {
                    this.mCallback.onErrorJunk();
                }
            }
        });
    }

    private ArrayList<JunkInfo> getJunk() {
        File externalDir = Environment.getExternalStorageDirectory();
        if (externalDir != null) {
            this.travelPath(externalDir, 0);
        }
        ArrayList<JunkInfo> list = new ArrayList<>();

        if (mApkInfo.mSize > 0L) {
            Collections.sort(mApkInfo.mChildren);
            Collections.reverse(mApkInfo.mChildren);
            list.add(mApkInfo);
        }

        if (mLogInfo.mSize > 0L) {
            Collections.sort(mLogInfo.mChildren);
            Collections.reverse(mLogInfo.mChildren);
            list.add(mLogInfo);
        }

        if (mTmpInfo.mSize > 0L) {
            Collections.sort(mTmpInfo.mChildren);
            Collections.reverse(mTmpInfo.mChildren);
            list.add(mTmpInfo);
        }
        return list;
    }

    private Single<String> createCacheRx() {
        return Single.create(emitter -> {
            try {
                PackageManager pm = this.context.getPackageManager();
                @SuppressLint("WrongConstant")
                List<ApplicationInfo> installedPackages = pm.getInstalledApplications(PackageManager.GET_GIDS);
                IPackageStatsObserver.Stub observer = new PackageStatsObserver();
                mScanCount = 0;
                mTotalCount = installedPackages.size();
                mSysCaches = new ArrayList<>();
                junkInfos = new ArrayList<>();
                mAppNames = new HashMap<>();

                for (int i = 0; i < mTotalCount; i++) {
                    if (!this.isRunning) {
                        return;
                    }
                    ApplicationInfo info = installedPackages.get(i);
                    mAppNames.put(info.packageName, pm.getApplicationLabel(info).toString());
                    getPackageInfo(info.packageName, observer);
                }
                emitter.onSuccess("onSuccess");
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        });
    }

    public void getPackageInfo(String packageName, IPackageStatsObserver.Stub observer) {
        try {
            PackageManager pm = this.context.getPackageManager();
            Method getPackageSizeInfo = pm.getClass()
                    .getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);

            getPackageSizeInfo.invoke(pm, packageName, observer);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private class PackageStatsObserver extends IPackageStatsObserver.Stub {
        @Override
        public void onGetStatsCompleted(PackageStats packageStats, boolean succeeded)
                throws RemoteException {
            mScanCount++;
            if (packageStats == null || !succeeded) {
            } else {
                JunkInfo info = new JunkInfo(context);
                info.mPackageName = packageStats.packageName;
                info.name = mAppNames.get(info.mPackageName);
                info.mSize = packageStats.cacheSize + packageStats.externalCacheSize;
                if (info.mSize > 0) {
                    mSysCaches.add(info);
                    mTotalSize += info.mSize;
                }
                if (mCallback != null && isRunning) {
                    mCallback.onProgress(info);
                }
            }

            if (mScanCount == mTotalCount) {
                JunkInfo info = new JunkInfo(context);
                info.name = context.getString(R.string.system_cache);
                info.mSize = mTotalSize;
                Collections.sort(mSysCaches);
                Collections.reverse(mSysCaches);
                info.mChildren = mSysCaches;
                info.mIsVisible = true;
                info.mIsChild = false;
                ArrayList<JunkInfo> list = new ArrayList<>();
                list.add(info);
                isScanCache = true;
                if (mCallback != null && isScanJunk && isRunning) {
                    mCallback.onFinish(junkInfos, mSysCaches);
                    mCallback.onStopJunk();
                }
            }
        }
    }

}
