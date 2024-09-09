package com.custom.mdm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MDMBootReceiver extends BroadcastReceiver {
    private static final String TAG = "MDMBootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //Log.d(TAG, "onReceive action=" + action);

        if (action.equals(Intent.ACTION_LOCKED_BOOT_COMPLETED)
                || action.equals(Intent.ACTION_BOOT_COMPLETED)) {
        }
    }
}
