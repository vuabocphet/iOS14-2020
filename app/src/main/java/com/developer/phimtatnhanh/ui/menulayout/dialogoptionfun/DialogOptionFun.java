package com.developer.phimtatnhanh.ui.menulayout.dialogoptionfun;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.data.ListUtils;
import com.developer.phimtatnhanh.data.PrefUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogOptionFun extends AlertDialog {

    @BindView(R.id.rcy_view)
    RecyclerView rcyView;

    private ListUtils listUtils;
    private Context context;
    private String key;
    private ResultFun resultFun;

    public DialogOptionFun setResultFun(ResultFun resultFun) {
        this.resultFun = resultFun;
        return this;
    }

    public static DialogOptionFun create(Context context, ListUtils listUtils, String key) {
        return new DialogOptionFun(context, listUtils, key);
    }

    public DialogOptionFun(Context context, ListUtils listUtils, String key) {
        super(context);
        this.key = key;
        this.context = context;
        this.listUtils = listUtils;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_option_fun);
        ButterKnife.bind(this);
        Window window = getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        FunAdapter.create(this.listUtils.menuModelList(), this.context, this.rcyView).setOnClickItem(position -> {
            cancel();
            PrefUtil.get().postInt(this.key, position);
            if (this.resultFun == null) {
                return;
            }
            this.resultFun.onResutlFun();
        });
    }

    public void showDialog() {
        if (isShowing()) {
            return;
        }
        show();
    }

    public interface ResultFun {
        void onResutlFun();
    }
}
