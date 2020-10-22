package com.developer.phimtatnhanh.ui.touch.gridview;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.developer.phimtatnhanh.R;

import java.util.List;

public class CustomIconAdapter extends BaseAdapter {

    private List<IconModel> iconModels;
    private LayoutInflater layoutInflater;
    private Context context;
    private ClickItem clickItem;
    private int iconCurent;

    public void setClickItem(ClickItem clickItem) {
        this.clickItem = clickItem;
    }

    public static CustomIconAdapter init(Context aContext, List<IconModel> iconModelList, GridView gridView) {
        return new CustomIconAdapter(aContext, iconModelList, gridView);
    }

    public CustomIconAdapter(Context aContext, List<IconModel> iconModelList, GridView gridView) {
        this.context = aContext;
        this.iconModels = iconModelList;
        this.layoutInflater = LayoutInflater.from(aContext);
        gridView.setAdapter(this);
    }

    @Override
    public int getCount() {
        return this.iconModels.size();
    }

    @Override
    public Object getItem(int i) {
        return this.iconModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        try {
            ViewHolder holder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.item_icon_touch, null);
                holder = new ViewHolder();
                holder.iv = convertView.findViewById(R.id.iv_icon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            IconModel iconModel = this.iconModels.get(i);
            if (iconModel.status) {
                this.iconCurent = i;
                holder.iv.setBackgroundResource(R.drawable.all_bg_stroke_red);
            } else {
                holder.iv.setBackground(new ColorDrawable(Color.TRANSPARENT));
            }
            Glide.with(this.context).load(iconModel.iconId).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv);
            holder.iv.setOnClickListener(view -> {
                if ((this.iconCurent == i)) {
                    return;
                }
                if (this.clickItem != null) {
                    iconModel.setStatus(true);
                    this.iconModels.set(i, iconModel);
                    this.iconModels.set(this.iconCurent, this.iconModels.get(this.iconCurent).setStatus(false));
                    this.clickItem.onClick(i);
                    notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TinhNv", "getView: " + e.toString());
        }
        return convertView;
    }

    static class ViewHolder {
        AppCompatImageView iv;
    }

    public interface ClickItem {
        void onClick(int appItemPosition);
    }
}
