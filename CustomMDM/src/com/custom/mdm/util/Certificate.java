package com.custom.mdm.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Certificate {
    public static final String TAG = "Certificate";

    /**
     * 获取所有CA证书
     * @param type
     *  0   all
     *  1   system
     *  2   user
     * @return
     */
    public static List<String> getCertificates(int type) {
        Log.d(TAG, "getCertificates type = " + type);
        List<String> list = new ArrayList<>();
        try {
            KeyStore ks = KeyStore.getInstance("AndroidCAStore");
            if (ks != null) {
                ks.load(null, null);
                Enumeration<String> aliases = ks.aliases();
                int i = 0;
                while (aliases.hasMoreElements()) {
                    String alias = (String) aliases.nextElement();
                    X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
                    String caInfo = cert.getIssuerDN().getName();
                    // To print System Certs only
                    if (type!=2 && alias.startsWith("system")) {
                        list.add(alias + "<-->" + caInfo);
                    }
                    // To print User Certs only
                    if (type != 1 && alias.startsWith("user")) {
                        list.add(alias + "<-->" + caInfo);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void installCA(Context context, String path) {
        Log.d(TAG, "installCA path = " + path);
        doModifyCA(context, path, true);
    }

    public static void uninstallCA(Context context, String name) {
        Log.d(TAG, "uninstallCA name = " + name);
        doModifyCA(context, name, false);
    }

    /**
     * 发送广播安装、卸载CA证书
     * @param context
     * @param path
     * @param flag
     *  true    安装
     *  false   卸载
     */
    private static void doModifyCA(Context context, String path, boolean flag) {
        Intent caIntent = new Intent("android.intent.action.XC_OPERATE_CA");
        caIntent.putExtra("ca_path", path);
        caIntent.putExtra("ca_ooperator", flag);
        caIntent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        context.sendBroadcast(caIntent);
    }
}
