package com.developer.phimtatnhanh.ui.junk.viewjunk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.memory.junk.model.JunkInfo;
import com.developer.phimtatnhanh.R;

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

    }

    @Override
    public int getItemCount() {
        return this.junkInfo == null || this.junkInfo.mChildren == null || this.junkInfo.mChildren.isEmpty() ? 0 : this.junkInfo.mChildren.size();
    }
}
