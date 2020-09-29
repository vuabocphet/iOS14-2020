package com.developer.phimtatnhanh.ui.menulayout;


import android.app.Activity;
import android.content.Intent;

import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.app.AppContext;
import com.developer.phimtatnhanh.setuptouch.config.ConfigAll;

public class CropImage implements ConfigAll {

    public static void album(Activity a) {
        if (a == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        a.startActivityForResult(Intent.createChooser(intent, AppContext.get().getContext().getResources().getString(R.string.luachon)), KEY_IMAGE_BG);
    }

    public static void crop(Intent d, Activity c) {
        com.theartofdev.edmodo.cropper.CropImage.activity(d.getData())
                .start(c);
    }
}
