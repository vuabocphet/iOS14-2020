package com.developer.phimtatnhanh.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.developer.phimtatnhanh.R;
import com.developer.phimtatnhanh.data.ListUtils;
import com.developer.phimtatnhanh.data.PrefUtil;
import com.developer.phimtatnhanh.setuptouch.config.ConstKey;

public class OptionDialog extends DialogFragment implements ConstKey {

    public interface SingleChoiceListener {

        void onPositiveButtonClicked(String text, String key);

        void onNegativeButtonClicked();

    }

    private SingleChoiceListener mListener;
    private Context context;
    private String title;
    private int position;
    private String key;
    private PrefUtil prefUtil;
    private ListUtils listUtils;

    public OptionDialog(SingleChoiceListener mListener, Context context, String title, String key, PrefUtil prefUtil, ListUtils listUtils) {
        this.context = context;
        this.mListener = mListener;
        this.title = title;
        this.key = key;
        this.listUtils = listUtils;
        this.prefUtil = prefUtil;
        this.position = prefUtil.getInt(key);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);

        final CharSequence[] list = this.listUtils.listStringClickTouch().toArray(new CharSequence[0]);

        builder.setTitle(this.title)
                .setSingleChoiceItems(list, position, (dialogInterface, i) -> {
                    position = i;
                })
                .setPositiveButton(this.listUtils.getString(R.string.select), (dialogInterface, i) -> {
                    this.prefUtil.postInt(key, position);
                    if (this.mListener == null) {
                        return;
                    }
                    this.mListener.onPositiveButtonClicked(list[position].toString(), this.key);
                })
                .setNegativeButton(ListUtils.get(this.context).getString(R.string.cancel), (dialogInterface, i) -> {
                    if (this.mListener == null) {
                        return;
                    }
                    this.mListener.onNegativeButtonClicked();
                });
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog == null || dialog.getListView() == null || this.prefUtil == null || TextUtils.isEmpty(this.key)) {
            return;
        }
        dialog.getListView().setItemChecked(this.prefUtil.getInt(this.key), true);
    }

    public static int getKey(int position) {
        switch (position) {
            case 0:
                return DIALOG_NOT_CLICK;
            case 1:
                return DIALOG_BACK_CLICK;
            case 2:
                return DIALOG_HOME_CLICK;
            case 3:
                return DIALOG_RECENT_CLICK;
            case 4:
                return DIALOG_MENU_CLICK;
            case 5:
                return DIALOG_NOTIFICATION_CLICK;
            case 6:
                return DIALOG_LOCK_OFF_CLICK;
            default:
                return DIALOG_NOT_CLICK;
        }
    }
}
