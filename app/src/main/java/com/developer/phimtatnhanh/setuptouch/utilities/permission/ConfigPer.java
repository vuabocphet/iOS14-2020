package com.developer.phimtatnhanh.setuptouch.utilities.permission;

import android.Manifest;

public interface ConfigPer {

    String[] CAMERA = new String[]{Manifest.permission.CAMERA};
    String[] READWRITE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String[] RECORD_AUDIO = new String[]{Manifest.permission.RECORD_AUDIO};

    int GET_MEDIA_PROJECTION_CODE = 986;
    int GET_MEDIA_PROJECTION_CODE_VIDEO = 987;
    int RQCODE_READ_WRITE = 821;
    int RQCODE_CAMERA = 822;
    int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 823;
    int ACTION_ACCESSIBILITY_SETTINGS = 824;
    int ACTION_ADMIN = 825;
    int RQCODE_RECORD_AUDIO = 826;
    int RQCODE_PACKAGE_USAGE_STATS = 827;

    String KEYCODE = "keycode";
    String KEYMENU = "keymenu";

    String TYPE = "type";

    String DATA_BUNDEL = "data_bundel";

    int KEY_IMAGE_BG = 827;

    String CACHE_JUNK = "CACHE_JUNK";

    String CACHE_RAM = "CACHE_RAM";

}
