package com.developer.phimtatnhanh.ui.junk.viewjunk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developer.memory.junk.model.JunkGroup;
import com.developer.memory.junk.model.JunkInfo;
import com.developer.memory.junk.util.CleanUtil;
import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.setuptouch.gridviewstarapp.AppItem;
import com.developer.phimtatnhanh.setuptouch.gridviewstarapp.AppUtil;
import com.developer.phimtatnhanh.util.loadiconapp.GlideApp;

public class JunkAdapter extends RecyclerView.Adapter<JunkHolder> {

    private JunkInfo junkInfo;

    private Context context;

    private LayoutInflater inflater;


    public static JunkAdapter create(Context context, JunkInfo junkInfo, RecyclerView recyclerView) {
        return new JunkAdapter(context, junkInfo, recyclerView);
    }

    public JunkAdapter(Context context, JunkInfo junkInfo, RecyclerView recyclerView) {
        this.context = context;
        this.junkInfo = junkInfo;
        this.inflater = LayoutInflater.from(this.context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(this);
    }

    @NonNull
    @Override
    public JunkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = inflater.inflate(R.layout.item_junk, parent, false);
        return new JunkHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull JunkHolder holder, int position) {
        JunkInfo junkInfo = this.junkInfo.mChildren.get(position);
        holder.tvName.setText(junkInfo.name);
        holder.tvSize.setText(CleanUtil.formatShortFileSize(this.context, junkInfo.mSize));
        if (this.junkInfo.typeJunk == JunkGroup.GROUP_TMP) {
            Glide.with(this.context).load(R.drawable.all_ic_tmp).into(holder.ivIcon);
        }
        if (this.junkInfo.typeJunk == JunkGroup.GROUP_OTHER) {
            Glide.with(this.context).load(R.drawable.all_ic_other).into(holder.ivIcon);
        }
        if (this.junkInfo.typeJunk == JunkGroup.GROUP_LOG) {
            Glide.with(this.context).load(R.drawable.all_ic_log).into(holder.ivIcon);
        }
        if (this.junkInfo.typeJunk == JunkGroup.GROUP_APK) {
            Glide.with(this.context).load(R.drawable.all_ic_log).into(holder.ivIcon);
        }
        if (this.junkInfo.typeJunk == JunkGroup.GROUP_CACHE) {
            GlideApp.with(this.context).load(AppUtil.init().getApplication(this.context.getPackageManager(),junkInfo.mPackageName).applicationInfo).into(holder.ivIcon);
        }
    }

    @Override
    public int getItemCount() {
        return this.junkInfo == null || this.junkInfo.mChildren == null || this.junkInfo.mChildren.isEmpty() ? 0 : this.junkInfo.mChildren.size();
    }
}
