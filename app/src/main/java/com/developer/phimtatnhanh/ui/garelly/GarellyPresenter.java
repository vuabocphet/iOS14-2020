package com.developer.phimtatnhanh.ui.garelly;


import android.annotation.SuppressLint;

import com.developer.phimtatnhanh.base.BaseMvpPresenter;
import com.developer.phimtatnhanh.ui.garelly.gridview.FileUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class GarellyPresenter implements BaseMvpPresenter<GarellyView> {

    private GarellyView garellyView;
    private Disposable disposable;

    @Override
    public void attachView(GarellyView view) {
        this.garellyView = view;
    }

    @Override
    public void detachView() {
        this.garellyView = null;
    }

    @SuppressLint("CheckResult")
    public void createRxGetFileGarelly() {
        this.disposable = FileUtil
                .createGetFileGarelly()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((fileItems, throwable) -> this.garellyView.onResult(fileItems));
    }

    @SuppressLint("CheckResult")
    public void createRxGetFileVideo() {
        this.disposable = FileUtil
                .createGetFileVideo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((fileItems, throwable) -> this.garellyView.onResult(fileItems));
    }

    public void onStopRxGetFileGarelly() {
        if (this.disposable == null) {
            return;
        }
        this.disposable.dispose();
    }

}
