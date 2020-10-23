package com.developer.memory.junk;


import android.annotation.SuppressLint;
import android.app.usage.StorageStats;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Build;
import android.os.Environment;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.developer.memory.R;
import com.developer.memory.junk.callback.IScanCallback;
import com.developer.memory.junk.model.JunkGroup;
import com.developer.memory.junk.model.JunkInfo;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class JunkScanRx {

    private IScanCallback mCallback;

    private JunkInfo mApkInfo;
    private JunkInfo mLogInfo;
    private JunkInfo mTmpInfo;
    private JunkInfo mOtherInfo;
    private JunkInfo mCacheInfo;

    private boolean completeJunkApk = false;
    private boolean completeJunkLog = false;
    private boolean completeJunkTmp = false;
    private boolean completeJunkOther = false;

    private boolean isRunning = false;
    private Context context;

    private int mScanCount;
    private int mTotalCount;

    private ArrayList<JunkInfo> mSysCaches;

    private HashMap<String, String> mAppNames;

    private Disposable disposable1;
    private Disposable disposable2;
    private Disposable disposable3;
    private Disposable disposable4;
    private Disposable disposable5;

    public JunkInfo getmApkInfo() {
        return mApkInfo;
    }

    public JunkInfo getmLogInfo() {
        return mLogInfo;
    }

    public JunkInfo getmTmpInfo() {
        return mTmpInfo;
    }

    public JunkInfo getmOtherInfo() {
        return mOtherInfo;
    }

    public JunkInfo getmCacheInfo() {
        return mCacheInfo;
    }

    public JunkScanRx(Context context, IScanCallback callback) {
        mCallback = callback;
        this.context = context;
        mApkInfo = new JunkInfo(this.context, JunkGroup.GROUP_APK);
        mLogInfo = new JunkInfo(this.context, JunkGroup.GROUP_LOG);
        mTmpInfo = new JunkInfo(this.context, JunkGroup.GROUP_TMP);
        mOtherInfo = new JunkInfo(this.context, JunkGroup.GROUP_OTHER);
        mCacheInfo = new JunkInfo(this.context, JunkGroup.GROUP_CACHE);
        mSysCaches = new ArrayList<>();
    }

    private void travelPath(File root, int junkType) {

        if (root == null || !root.exists()) {
            return;
        }

        File[] lists = root.listFiles();

        if (lists == null || lists.length == 0) {
            return;
        }

        for (File file : lists) {
            if (!this.isRunning) {
                return;
            }
            if (file.isFile()) {
                String name = file.getName();
                if (junkType == JunkGroup.GROUP_APK && name.endsWith(".apk")) {
                    this.createJunkApk(file);
                } else if (junkType == JunkGroup.GROUP_LOG && name.endsWith(".log")) {
                    this.createJunkLog(file);
                } else if (junkType == JunkGroup.GROUP_TMP && (name.endsWith(".tmp") || name.endsWith(".temp"))) {
                    this.createJunkTmp(file);
                } else if (junkType == JunkGroup.GROUP_OTHER &&
                        (name.endsWith(".zip") ||
                                name.endsWith(".oob") ||
                                name.endsWith(".txt") ||
                                name.endsWith(".rar") ||
                                name.endsWith(".dat") ||
                                name.endsWith(".os") ||
                                name.endsWith(".xml") ||
                                name.endsWith(".json") ||
                                name.endsWith(".exo")
                        )) {
                    this.createJunkOther(file);
                }
            } else {
                travelPath(file, junkType);
            }
        }

        return;
    }

    private void createJunkApk(File file) {
        JunkInfo info = new JunkInfo(this.context, JunkGroup.GROUP_APK);
        long length = file.length();
        info.mSize = length < 100 ? 1024 * 7 : length;
        info.name = file.getName();
        info.mPath = file.getAbsolutePath();
        info.mIsChild = false;
        info.mIsVisible = true;
        mApkInfo.mChildren.add(info);
        mApkInfo.mSize += info.mSize;
        if (this.mCallback != null && this.isRunning) {
            this.mCallback.onProgressApk(info);
        }
    }

    private void createJunkLog(File file) {
        JunkInfo info = new JunkInfo(this.context, JunkGroup.GROUP_LOG);
        long length = file.length();
        info.mSize = length < 100 ? 1024 * 7 : length;
        info.name = file.getName();
        info.mPath = file.getAbsolutePath();
        info.mIsChild = false;
        info.mIsVisible = true;
        mLogInfo.mChildren.add(info);
        mLogInfo.mSize += info.mSize;
        if (this.mCallback != null && this.isRunning) {
            this.mCallback.onProgressLog(info);
        }
    }

    private void createJunkTmp(File file) {
        JunkInfo info = new JunkInfo(this.context, JunkGroup.GROUP_TMP);
        long length = file.length();
        info.mSize = length < 100 ? 1024 * 7 : length;
        info.name = file.getName();
        info.mPath = file.getAbsolutePath();
        info.mIsChild = false;
        info.mIsVisible = true;
        mTmpInfo.mChildren.add(info);
        mTmpInfo.mSize += info.mSize;
        if (this.mCallback != null && this.isRunning) {
            this.mCallback.onProgressTmp(info);
        }
    }

    private void createJunkOther(File file) {
        JunkInfo info = new JunkInfo(this.context, JunkGroup.GROUP_OTHER);
        long length = file.length();
        info.mSize = length < 100 ? 1024 * 7 : length;
        info.name = file.getName();
        info.mPath = file.getAbsolutePath();
        info.mIsChild = false;
        info.mIsVisible = true;
        mOtherInfo.mChildren.add(info);
        mOtherInfo.mSize += info.mSize;
        if (this.mCallback != null && this.isRunning) {
            this.mCallback.onProgressOther(info);
        }
    }

    public void startScanJunkRx() {
        this.isRunning = true;
        if (mCallback != null) {
            mCallback.onStartJunk();
        }
        File externalDir = Environment.getExternalStorageDirectory();

        this.disposable1 = this.createJunkRx(externalDir, JunkGroup.GROUP_APK).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        this.disposable2 = this.createJunkRx(externalDir, JunkGroup.GROUP_LOG).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        this.disposable3 = this.createJunkRx(externalDir, JunkGroup.GROUP_TMP).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        this.disposable4 = this.createJunkRx(externalDir, JunkGroup.GROUP_OTHER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        this.disposable5 = this.createCacheRx().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void stopScanJunkRx() {
        this.isRunning = false;
        if (this.disposable1 != null) {
            this.disposable1.dispose();
        }
        if (this.disposable2 != null) {
            this.disposable2.dispose();
        }
        if (this.disposable3 != null) {
            this.disposable3.dispose();
        }
        if (this.disposable4 != null) {
            this.disposable4.dispose();
        }
        if (this.disposable5 != null) {
            this.disposable5.dispose();
        }
    }

    private Single<String> createJunkRx(File externalDir, int typeJunk) {
        return Single.create(emitter -> {
            try {
                this.getJunkAll(externalDir, typeJunk);
                this.scanJunkRxComplete(typeJunk);
            } catch (Exception e) {
                onErrorJunk(e);
                this.scanJunkRxComplete(typeJunk);
            }
        });
    }

    private void scanJunkRxComplete(int typeJunk) {
        if (typeJunk == JunkGroup.GROUP_APK) {
            this.completeJunkApk = true;
        }
        if (typeJunk == JunkGroup.GROUP_LOG) {
            this.completeJunkLog = true;
        }
        if (typeJunk == JunkGroup.GROUP_TMP) {
            this.completeJunkTmp = true;
        }
        if (typeJunk == JunkGroup.GROUP_OTHER) {
            this.completeJunkOther = true;
        }
        this.finishAll();
    }

    private void onErrorJunk(Exception e) {
        if (this.mCallback != null) {
            this.mCallback.onErrorJunk(e);
        }
    }

    private void finishAll() {
        if (this.mCallback != null &&
                this.completeJunkApk &&
                this.completeJunkLog &&
                this.completeJunkTmp &&
                this.completeJunkOther &&
                this.isRunning &&
                (this.mScanCount == this.mTotalCount)) {
            this.mCallback.onFinish();
            this.mCallback.onStopJunk();
            this.stopScanJunkRx();
        }
    }

    private void getJunkAll(File externalDir, int typeJunk) {
        if (externalDir != null) {
            this.travelPath(externalDir, typeJunk);
        }
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
                mAppNames = new HashMap<>();
                for (int i = 0; i < mTotalCount; i++) {
                    if (!this.isRunning) {
                        return;
                    }
                    ApplicationInfo info = installedPackages.get(i);
                    mAppNames.put(info.packageName, pm.getApplicationLabel(info).toString());
                    getPackageInfo(info.packageName, observer);
                }
            } catch (Exception e) {
                onErrorJunk(e);
            }
        });
    }

    public void getPackageInfo(String packageName, IPackageStatsObserver.Stub observer) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            this.getSizeBelow26(packageName, observer);
            return;
        }
        this.getSizeAbove25(packageName);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant")
    private void getSizeAbove25(String packageName) {
        final StorageStatsManager storageStatsManager = (StorageStatsManager) context.getSystemService(Context.STORAGE_STATS_SERVICE);
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(packageName, 0);
            StorageStats storageStats = storageStatsManager.queryStatsForUid(ai.storageUuid, ai.uid);
            mScanCount++;
            {
                JunkInfo info = new JunkInfo(context, JunkGroup.GROUP_CACHE);
                info.mPackageName = ai.packageName;
                info.name = mAppNames.get(info.mPackageName);
                info.mSize = storageStats.getCacheBytes();
                if (info.mSize > 0 && !info.mPackageName.equals(context.getPackageName())) {
                    mSysCaches.add(info);
                    mCacheInfo.mChildren.add(info);
                    if (mCallback != null && isRunning) {
                        mCallback.onProgressCache(info);
                        mCallback.onProgress(info);
                    }
                }
            }
            if (mScanCount == mTotalCount) {
                Collections.sort(mSysCaches);
                Collections.reverse(mSysCaches);
                finishAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
            onErrorJunk(e);
        }
    }

    private void getSizeBelow26(String packageName, IPackageStatsObserver.Stub observer) {
        try {
            PackageManager pm = this.context.getPackageManager();

            Method getPackageSizeInfo = pm.getClass()
                    .getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);

            getPackageSizeInfo.invoke(pm, packageName, observer);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            onErrorJunk(e);
        }
    }

    private class PackageStatsObserver extends IPackageStatsObserver.Stub {
        @Override
        public void onGetStatsCompleted(PackageStats packageStats, boolean succeeded)
                throws RemoteException {
            mScanCount++;
            if (packageStats == null || !succeeded) {
            } else {
                JunkInfo info = new JunkInfo(context, JunkGroup.GROUP_CACHE);
                info.mPackageName = packageStats.packageName;
                info.name = mAppNames.get(info.mPackageName);
                info.mSize = packageStats.cacheSize + packageStats.externalCacheSize;
                if (info.mSize > 0 && !info.mPackageName.equals(context.getPackageName())) {
                    mSysCaches.add(info);
                    mCacheInfo.mChildren.add(info);
                    if (mCallback != null && isRunning) {
                        mCallback.onProgress(info);
                        mCallback.onProgressCache(info);
                    }
                }
            }

            if (mScanCount == mTotalCount) {
                Collections.sort(mSysCaches);
                Collections.reverse(mSysCaches);
                finishAll();
            }
        }
    }

}
