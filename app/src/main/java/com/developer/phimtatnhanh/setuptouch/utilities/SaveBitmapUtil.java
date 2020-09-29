package com.developer.phimtatnhanh.setuptouch.utilities;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import androidx.core.content.FileProvider;

import com.developer.phimtatnhanh.BuildConfig;
import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.app.AppContext;
import com.developer.phimtatnhanh.util.PathUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.Single;

public class SaveBitmapUtil implements ConfigFile {

    public static Single<String> createSaveFile(Bitmap bmp) {
        return Single.create(emitter -> {
            try {
                String path = path(bmp);
                emitter.onSuccess(path);
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        });
    }

    public static String path(Bitmap bmp) {
        if (bmp == null) {
            return "";
        }
        File save = save(bmp);
        if (save == null) {
            return "";
        }
        return save.getAbsolutePath();
    }

    public static File save(Bitmap bmp) {
        try {
            return new File(saveImage(bmp));
        } catch (Exception e) {
            return null;
        }
    }

    public static String saveImage(Bitmap bitmap) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        String path = "";
        @SuppressLint("SimpleDateFormat")
        String format = "Screenshot_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date());
        OutputStream fos = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = AppContext.get().getContext().getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, format);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, MIME_TYPE);
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, PATH_CAPTURE_SCREEN);
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            try {
                path = PathUtil.getPath(AppContext.get().getContext(), imageUri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            if (imageUri != null) {
                fos = resolver.openOutputStream(imageUri);
            }
        } else {
            String imagesDir = getPathBig();
            File file = new File(imagesDir);
            if (!file.exists()) {
                file.mkdir();
            }
            File f = File.createTempFile(format,
                    ".jpg",
                    file
            );
            path = f.getAbsolutePath();
            fos = new FileOutputStream(f);
        }
        if (fos != null) {
            fos.write(bytes.toByteArray());
            fos.close();
            Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(new File(path));
            media.setData(contentUri);
            AppContext.get().getContext().sendBroadcast(media);
        }
        return path;
    }

    /*@SuppressLint("SimpleDateFormat")
    private static String getFilename() {
        String format = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date());
        File pix = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File storageDir = new File(pix, "Screenshots");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        try {
            File file = File.createTempFile(
                    "img" + format,
                    ".jpg",
                    storageDir
            );
            return file.getAbsolutePath();
        } catch (Exception e) {
            return "";
        }
    }*/

    public static String getPathBig() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + NAME_FODEL_CAPTURE_SCREEN;
    }

    public static String getPathBigVideo() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + File.separator + NAME_FODEL_CAPTURE_VIDEO_SCREEN;
    }

    public static void openCaptureScreen(Context context, File file, String type) {
        if (file == null) {
            return;
        }
        Uri uri;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(file);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.setDataAndType(uri, type + "/*");
        context.startActivity(intent);
    }

    public static void sendMessenger(Context context, File newFile) {
        try {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String ext = newFile.getName().substring(newFile.getName().lastIndexOf(".") + 1);
            String type = mime.getMimeTypeFromExtension(ext);
            Intent sharingIntent = new Intent();
            sharingIntent.setAction(Intent.ACTION_SEND);
            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            sharingIntent.setType(type);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                sharingIntent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", newFile));
            } else {
                sharingIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(newFile));
            }
            sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Intent chooser = Intent.createChooser(sharingIntent, context.getString(R.string.chonapp));
            chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(chooser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
