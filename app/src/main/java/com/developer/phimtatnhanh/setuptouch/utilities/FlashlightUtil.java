package com.developer.phimtatnhanh.setuptouch.utilities;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;

public class FlashlightUtil {

    private static final String TAG = "FlashlightUtil";
    private Camera mCamera;
    private Camera.Parameters parameters;
    private CameraManager camManager;
    private Context context;

    public static FlashlightUtil get(Context context) {
        return new FlashlightUtil(context);
    }

    private boolean isOnFlash = false;

    public boolean isOnFlash() {
        return isOnFlash;
    }

    public FlashlightUtil(Context context) {
        this.context = context;
    }

    private void createCamera() {
        this.mCamera = Camera.open();
        this.parameters = mCamera.getParameters();
    }

    public void light() {
        if (isOnFlash()) {
            this.lightOff();
            return;
        }
        this.lightOn();
    }

    public void lightOn() {
        this.isOnFlash = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                this.camManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                String cameraId;
                if (this.camManager != null) {
                    cameraId = this.camManager.getCameraIdList()[0];
                    this.camManager.setTorchMode(cameraId, true);
                }
            } catch (CameraAccessException e) {
                try {
                    this.openFlash();
                } catch (Exception ee) {
                    e.printStackTrace();
                }
            }
        } else {
            this.openFlash();
        }
    }

    public void lightOff() {
        this.isOnFlash = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                String cameraId;
                this.camManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                if (this.camManager != null) {
                    cameraId = this.camManager.getCameraIdList()[0];
                    this.camManager.setTorchMode(cameraId, false);
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
                try {
                    stopFlash();
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        } else {
            stopFlash();
        }
    }

    private void openFlash() {
        this.createCamera();
        this.parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        this.mCamera.setParameters(parameters);
        this.mCamera.startPreview();
    }

    private void stopFlash() {
        this.createCamera();
        this.parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        this.mCamera.setParameters(parameters);
        this.mCamera.stopPreview();
    }


}
