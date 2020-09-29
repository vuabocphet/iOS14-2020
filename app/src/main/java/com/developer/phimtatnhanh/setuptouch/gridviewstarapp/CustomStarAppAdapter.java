package com.developer.phimtatnhanh.setuptouch.gridviewstarapp;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.PopupMenu;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.data.PrefUtil;
import com.developer.phimtatnhanh.delayclickview.PostDelayClick;
import com.developer.phimtatnhanh.util.loadiconapp.GlideApp;

import java.util.List;

public class CustomStarAppAdapter extends BaseAdapter {

    private List<AppItem> list;
    private LayoutInflater layoutInflater;
    private Context context;
    private ClickAppItem clickAppItem;
    private boolean addApp = false;

    public void setClickAppItem(ClickAppItem clickAppItem) {
        this.clickAppItem = clickAppItem;
    }

    public CustomStarAppAdapter setAddApp(boolean addApp) {
        this.addApp = addApp;
        return this;
    }

    public static CustomStarAppAdapter init(Context aContext, List<AppItem> listPackName, GridView gridView) {
        return new CustomStarAppAdapter(aContext, listPackName, gridView);
    }

    public CustomStarAppAdapter(Context aContext, List<AppItem> listPackName, GridView gridView) {
        this.context = aContext;
        this.list = listPackName;
        this.layoutInflater = LayoutInflater.from(aContext);
        gridView.setAdapter(this);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int i) {
        return this.list.get(i);
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
            convertView = layoutInflater.inflate(R.layout.item_star_menu_touch, null);
            holder = new ViewHolder();
            holder.tv = convertView.findViewById(R.id.tv_app);
            holder.iv = convertView.findViewById(R.id.iv_app);
            holder.csLayout = convertView.findViewById(R.id.ll_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AppItem appItem = this.list.get(i);
        if (appItem.typeStar == TypeStar.NONE) {
            holder.tv.setVisibility(View.VISIBLE);
            holder.tv.setText(this.context.getString(R.string.none));
            holder.iv.setImageResource(R.drawable.all_click_none);
            holder.iv.setPadding(0, 0, 0, 0);
        }

        if (appItem.typeStar == TypeStar.BACK) {
            holder.iv.setImageResource(R.drawable.all_click_back_menu_touch);
            holder.tv.setText(this.context.getString(R.string.back));
            holder.iv.setPadding(22, 22, 22, 22);
        }

        if (appItem.typeStar == TypeStar.APP) {
            holder.tv.setVisibility(View.VISIBLE);
            holder.tv.setText(appItem.name);
            GlideApp.with(this.context)
                    .load(appItem.applicationInfo)
                    .into(holder.iv);
            holder.iv.setPadding(0, 0, 0, 0);
        }
        holder.csLayout.setOnClickListener(view -> {
            PostDelayClick.get().postDelayViewClick(view);
            if (this.clickAppItem != null) {
                this.clickAppItem.onClick(appItem, i);
            }
        });
        if (appItem.typeStar == TypeStar.APP && !this.addApp) {
            holder.csLayout.setOnLongClickListener(view -> {
                PopupMenu popupMenu = new PopupMenu(this.context, view);
                popupMenu.inflate(R.menu.menu_app_star);
                popupMenu.setOnMenuItemClickListener(menuItem -> {

                    int itemId = menuItem.getItemId();

                    switch (itemId) {
                        case R.id.mn_edit:
                            if (this.clickAppItem != null) {
                                this.clickAppItem.onClick(AppItem.init().setTypeStar(TypeStar.NONE), i);
                            }
                            break;
                        case R.id.mn_delete:
                            PrefUtil.get().postString("a" + i, "");
                            if (this.clickAppItem != null) {
                                this.clickAppItem.onUpdate();
                            }
                            break;
                        case R.id.mn_close:
                            popupMenu.dismiss();
                            break;
                    }

                    return false;
                });
                popupMenu.show();
                return false;
            });
        }
        return convertView;
    }

    static class ViewHolder {
        AppCompatImageView iv;
        AppCompatTextView tv;
        LinearLayoutCompat csLayout;
    }

    public interface ClickAppItem {
        void onClick(AppItem appItem, int position);

        void onUpdate();
    }

}
