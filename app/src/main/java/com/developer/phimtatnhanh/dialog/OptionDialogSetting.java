package com.developer.phimtatnhanh.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.app.AppContext;
import com.developer.phimtatnhanh.data.PrefUtil;
import com.developer.phimtatnhanh.setuptouch.config.ConstKey;
import com.developer.phimtatnhanh.ui.settingcapturevideo.PreSetting;

import java.util.List;

public class OptionDialogSetting extends DialogFragment implements ConstKey {

    public interface SingleChoiceListener {
        void onPositiveButtonClicked(String text, String key);
    }

    private SingleChoiceListener mListener;
    private String title;
    private int position;
    private String key;
    private CharSequence[] list;
    private Context context;

    public OptionDialogSetting(SingleChoiceListener mListener, Context context, String title, String key, List<String> list) {
        this.mListener = mListener;
        this.context = context;
        this.list = list.toArray(new CharSequence[0]);
        this.title = title;
        this.key = key;
        this.position = Integer.parseInt(PreSetting.getFromKey(key));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle(this.title)
                .setSingleChoiceItems(this.list, position, (dialogInterface, i) -> this.position = i)
                .setPositiveButton(AppContext.get().getContext().getResources().getString(R.string.select), (dialogInterface, i) -> {
                    PrefUtil.get().postString(this.key, String.valueOf(this.position));
                    if (this.mListener == null) {
                        return;
                    }
                    this.mListener.onPositiveButtonClicked(this.list[this.position].toString(), this.key);
                })
                .setNegativeButton(AppContext.get().getContext().getResources().getString(R.string.cancel), (dialogInterface, i) -> {

                });
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog == null || dialog.getListView() == null || TextUtils.isEmpty(this.key)) {
            return;
        }
        dialog.getListView().setItemChecked(Integer.parseInt(PreSetting.getFromKey(this.key)), true);
    }

}
