package com.developer.phimtatnhanh.setuptouch.utilities;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.developer.phimtatnhanh.setuptouch.listen.OnHomePressedListener;

public class HomeWatcherUtil {
    private Context mContext;
    private IntentFilter mFilter;
    private OnHomePressedListener mListener;
    private InnerReceiver mReceiver;

    public HomeWatcherUtil(Context context) {
        mContext = context;
        mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        mFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mFilter.addAction(Intent.ACTION_SCREEN_ON);
    }

    public void setOnHomePressedListener(OnHomePressedListener listener) {
        mListener = listener;
        mReceiver = new InnerReceiver();
    }

    public boolean startWatch() {
        if (mReceiver != null) {
            mContext.registerReceiver(mReceiver, mFilter);
            return true;
        }
        return false;
    }

    public void stopWatch() {
        if (mReceiver != null) {
            if (startWatch()) {
                mContext.unregisterReceiver(mReceiver);
            }
        }
    }

    class InnerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS) || action.equals(Intent.ACTION_SCREEN_OFF) || action.equals(Intent.ACTION_SCREEN_ON))) {
                if (mListener == null) {
                    return;
                }
                mListener.onCancelMenuTouch();
            }
        }
    }
}
