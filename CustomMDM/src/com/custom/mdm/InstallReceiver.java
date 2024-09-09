package com.custom.mdm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInstaller;
import android.util.Log;
import android.content.Intent;

public class InstallReceiver extends BroadcastReceiver {
    private static final String TAG = "InstallReceiver";
    public static final String ACTION_INSTALL_PKG = "com.custom.mdm.install_package";
    public static final String ACTION_UNINSTALL_PKG = "com.custom.mdm.uninstall_package";
    private InstallListener installListener = null;
    private UninstallListener uninstallListener = null;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String pkgName = intent.getStringExtra(PackageInstaller.EXTRA_PACKAGE_NAME);
        String msg = intent.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE);
        //Log.d(TAG, "action: " + action + " pkgName: " + pkgName + " Status Message: " + msg);

        if (action.equals(ACTION_INSTALL_PKG) && installListener != null) {
            if (msg.contains("INSTALL_SUCCEEDED")) {
                installListener.onInstallSuccess(pkgName);
            } else if (msg.contains("INSTALL_FAILED")) {
                installListener.onInstallFail(pkgName, msg);
            }
        }

        if (action.equals(ACTION_UNINSTALL_PKG) && uninstallListener != null) {
            if (msg.contains("DELETE_SUCCEEDED")) {
                uninstallListener.onUninstallSuccess(pkgName);
            } else if (msg.contains("DELETE_FAILED")) {
                uninstallListener.onUninstallFail(pkgName, msg);
            }
        }
    }

    public void setInstallListener(InstallListener listener) {
        this.installListener = listener;
    }

    public void setUninstallListener(UninstallListener listener) {
        this.uninstallListener = listener;
    }

    public void register(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_INSTALL_PKG);
        filter.addAction(ACTION_UNINSTALL_PKG);
        context.registerReceiver(this, filter);
    }

    public void unRegister(Context context) {
        context.unregisterReceiver(this);
    }

    public interface InstallListener {
        void onInstallSuccess(String pkgName);
        void onInstallFail(String pkgName, String msg);
    }

    public interface UninstallListener {
        void onUninstallSuccess(String pkgName);
        void onUninstallFail(String pkgName, String msg);
    }
}
