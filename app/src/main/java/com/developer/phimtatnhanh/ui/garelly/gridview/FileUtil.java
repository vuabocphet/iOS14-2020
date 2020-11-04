package com.developer.phimtatnhanh.ui.garelly.gridview;


import com.developer.phimtatnhanh.setuptouch.utilities.SaveBitmapUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class FileUtil {

    public static Single<List<FileItem>> createGetFileGarelly() {
        return Single.create(emitter -> {
            try {
                List<FileItem> listFileFolder = getListFileFolder(SaveBitmapUtil.getPathBig());
                emitter.onSuccess(listFileFolder);
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        });
    }

    public static Single<List<FileItem>> createGetFileVideo() {
        return Single.create(emitter -> {
            try {
                List<FileItem> listFileFolder = getListFileFolder(SaveBitmapUtil.getPathBigVideo());
                emitter.onSuccess(listFileFolder);
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        });
    }

    private static List<FileItem> getListFileFolder(String pathBig) {
        List<FileItem> list = new ArrayList<>();
        File directory = new File(pathBig);
        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            return list;
        }
        for (File file : files) {
            if (file == null || file.length() == 0 || file.length() < 100) {
                continue;
            }
            FileItem fileItem = new FileItem();
            fileItem.path = file.getPath();
            list.add(0, fileItem);
        }
        return list;
    }

}
