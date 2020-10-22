package com.developer.phimtatnhanh.ui.junk.viewjunk;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

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
        return this;
    }

}
