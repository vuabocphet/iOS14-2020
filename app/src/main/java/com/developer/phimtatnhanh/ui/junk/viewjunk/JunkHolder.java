package com.developer.phimtatnhanh.ui.junk.viewjunk;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.phimtatnhanh.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JunkHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_icon)
    public AppCompatImageView ivIcon;
    @BindView(R.id.tv_title)
    public AppCompatTextView tvName;
    @BindView(R.id.iv_check)
    public AppCompatImageView ivCheck;
    @BindView(R.id.tv_size)
    public AppCompatTextView tvSize;
    @BindView(R.id.cs_layout_all)
    public ConstraintLayout csLayoutAll;

    public JunkHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
