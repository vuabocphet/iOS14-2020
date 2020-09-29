package com.developer.phimtatnhanh.ui.garelly.gridview;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.delayclickview.PostDelayClick;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class CustomGarellyAdapter extends BaseAdapter {

    private List<FileItem> fileItems;
    private LayoutInflater layoutInflater;
    private Context context;
    private ClickItem clickItem;

    public void setClickItem(ClickItem clickItem) {
        this.clickItem = clickItem;
    }

    public void setFileItem(String path) {
        if (this.fileItems == null) {
            this.fileItems = new ArrayList<>();
        }
        FileItem fileItem = new FileItem();
        fileItem.path = path;
        this.fileItems.add(0, fileItem);
        this.notifyDataSetChanged();
    }

    public void deleteItem(String path) {
        if (this.fileItems == null) {
            this.fileItems = new ArrayList<>();
            return;
        }
        for (FileItem fileItem : this.fileItems) {
            if (fileItem.path.equals(path)){
                this.fileItems.remove(fileItem);
                break;
            }
        }
        this.notifyDataSetChanged();
    }

    public static CustomGarellyAdapter init(Context aContext, List<FileItem> fileItems, GridView gridView) {
        return new CustomGarellyAdapter(aContext, fileItems, gridView);
    }

    public CustomGarellyAdapter(Context aContext, List<FileItem> fileItems, GridView gridView) {
        this.context = aContext;
        this.fileItems = fileItems;
        this.layoutInflater = LayoutInflater.from(aContext);
        gridView.setAdapter(this);
    }

    @Override
    public int getCount() {
        return fileItems.size();
    }

    @Override
    public Object getItem(int i) {
        return fileItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_garelly, null);
            holder = new ViewHolder();
            holder.iv = convertView.findViewById(R.id.iv);
            ViewGroup.LayoutParams layoutParams = holder.iv.getLayoutParams();
            layoutParams.height = layoutParams.width * 2;
            holder.iv.setLayoutParams(layoutParams);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FileItem fileItem = fileItems.get(i);
        Glide.with(this.context).load(fileItem.path).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv);
        holder.iv.setOnClickListener(view -> {
            PostDelayClick.get().postDelayViewClick(view);
            if (this.clickItem != null) {
                this.clickItem.onClick(fileItem);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        RoundedImageView iv;
    }

    public interface ClickItem {
        void onClick(FileItem fileItem);
    }

}
