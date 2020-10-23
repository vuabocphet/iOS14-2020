package com.developer.phimtatnhanh.ui.junk.viewjunk;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.memory.junk.model.JunkInfo;
import com.developer.phimtatnhanh.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewListJunk extends FrameLayout {

    @BindView(R.id.tv_name)
    AppCompatTextView tvName;
    @BindView(R.id.iv_check)
    AppCompatImageView ivCheck;
    @BindView(R.id.iv_rotate_cache)
    AppCompatImageView ivRotateCache;
    @BindView(R.id.tv_size)
    AppCompatTextView tvSize;
    @BindView(R.id.cs_layout_all)
    ConstraintLayout csLayoutAll;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.recycler_junk)
    RecyclerView recyclerJunk;
    @BindView(R.id.cs_layout1)
    ConstraintLayout csLayout1;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    public ViewListJunk(@NonNull Context context) {
        super(context);
        this.init();
    }

    public ViewListJunk(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public ViewListJunk(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init() {
        View view = inflate(this.getContext(), R.layout.view_list_junk, null);
        this.addView(view);
        ButterKnife.bind(this, view);
    }

    public ViewListJunk setTvName(String name) {
        this.tvName.setText(name);
        return this;
    }

    public ViewListJunk setTvSize(String size) {
        this.tvSize.setText(size);
        return this;
    }

    public ViewListJunk setListJunk(JunkInfo junkInfo) {
        JunkAdapter.create(this.getContext(), junkInfo, this.recyclerJunk);
        this.ivRotateCache.setVisibility(VISIBLE);
        this.ivCheck.setVisibility(VISIBLE);
        this.progressBar.setVisibility(GONE);
        if (junkInfo == null || junkInfo.mChildren == null || junkInfo.mChildren.isEmpty()) {
            this.csLayout1.setAlpha(0.5f);
            return this;
        }
        this.csLayout1.setAlpha(1f);
        this.csLayout1.setOnClickListener(view -> {
            if (this.recyclerJunk.getVisibility() == View.VISIBLE) {
                this.recyclerJunk.setVisibility(GONE);
                this.view.setVisibility(GONE);
                this.ivRotateCache.setRotation(0f);
                return;
            }
            this.recyclerJunk.setVisibility(VISIBLE);
            this.view.setVisibility(VISIBLE);
            this.ivRotateCache.setRotation(90f);
        });
        return this;
    }
}
