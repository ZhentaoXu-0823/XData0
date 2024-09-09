package com.custom.mdm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInstaller;
import android.util.Log;
import android.content.Intent;

public class OtaVerityReceiver extends BroadcastReceiver {
    private static final String TAG = "OtaVerityReceiver";
    public static final String ACTION_VERITY_FAILED = "android.intent.action.XC_ACTION_OTA_VERITY_FAILED";
    private OtaVerityListener otaVerityListener = null;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String path = intent.getStringExtra("update_file_path");
        Log.d(TAG, "xzt otaVerity path: " + path);
        if (action.equals(ACTION_VERITY_FAILED) && otaVerityListener != null) {
            otaVerityListener.onOtaVerityFailed(path);
        }
    }

    public void setOtaVerityListener(OtaVerityListener listener) {
        this.otaVerityListener = listener;
    }

    public void register(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_VERITY_FAILED);
        context.registerReceiver(this, filter);
    }

    public void unRegister(Context context) {
        context.unregisterReceiver(this);
    }

    public interface OtaVerityListener {
        void onOtaVerityFailed(String path);
    }
}
