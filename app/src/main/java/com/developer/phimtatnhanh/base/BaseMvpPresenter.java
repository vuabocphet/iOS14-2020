package com.developer.phimtatnhanh.base;

public interface BaseMvpPresenter <V extends BaseView> {

    void attachView(V view);

    void detachView();

}
