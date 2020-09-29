package com.developer.phimtatnhanh.setuptouch.utilities.permission;

public interface LifePermission {

    void onAllow(int rcode);

    void onDenied(int rcode);

    void onAskagain(int rcode);

}
