package com.developer.phimtatnhanh.ui.menulayout.dialogoptionfun;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.app.AppContext;
import com.developer.phimtatnhanh.data.MenuModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FunAdapter extends RecyclerView.Adapter<FunAdapter.FunHoler> {

    private List<MenuModel> menuModelList;

    private LayoutInflater inflater;

    private OnClickItem onClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public static FunAdapter create(List<MenuModel> menuModelList, Context context, RecyclerView recyclerView) {
        return new FunAdapter(menuModelList, context, recyclerView);
    }

    public FunAdapter(List<MenuModel> menuModelList, Context context, RecyclerView recyclerView) {
        AppContext.create(context);
        this.menuModelList = menuModelList;
        this.inflater = LayoutInflater.from(AppContext.get().getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(AppContext.get().getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(this);
    }

    @NonNull
    @Override
    public FunHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FunHoler(this.inflater.inflate(R.layout.item_fun, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FunHoler holder, int position) {
        MenuModel menuModel = menuModelList.get(position);
        holder.iv.setImageResource(menuModel.icon[0]);
        holder.tv.setText(menuModel.title);
        holder.csLayoutAll.setOnClickListener(view -> {
            if (this.onClickItem == null) {
                return;
            }
            this.onClickItem.onClickItem(position);
        });
    }

    @Override
    public int getItemCount() {
        return menuModelList.size();
    }

    public static class FunHoler extends RecyclerView.ViewHolder {
        @BindView(R.id.iv)
        public AppCompatImageView iv;
        @BindView(R.id.tv)
        public AppCompatTextView tv;
        @BindView(R.id.cs_layout_all)
        public View csLayoutAll;

        public FunHoler(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnClickItem {
        void onClickItem(int position);
    }
}
