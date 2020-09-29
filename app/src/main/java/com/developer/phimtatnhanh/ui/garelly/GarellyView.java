package com.developer.phimtatnhanh.ui.garelly;


import com.developer.phimtatnhanh.base.BaseView;
import com.developer.phimtatnhanh.ui.garelly.gridview.FileItem;

import java.util.List;

public interface GarellyView extends BaseView {

    void onResult(List<FileItem> fileItems);

    String TYPE = "type";
    String IMG = "img";
    String VIDEO = "video";

}
