package com.custom.mdm;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.text.TextUtils;
import android.util.Log;

import com.custom.mdm.ApnConfig;

import java.util.ArrayList;
import java.util.List;

public class ApnUtils {
    public static final String TAG = "ApnUtils";

    public static final Uri APN_URI = Uri.parse("content://telephony/carriers");
    public static final Uri CURRENT_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

    public static int getSelectAPN(Context context, String apn, String mcc, String mnc) {
        ContentResolver resolver = context.getContentResolver();
        String apn_condition = "apn=? AND mcc=? AND mnc=?";
        String[] args = new String[]{apn, mcc, mnc};
        Cursor c = resolver.query(APN_URI, ApnConfig.sProjection, apn_condition, args, null);
        if (c != null && c.moveToNext()) {
            ApnConfig apnConfig = new ApnConfig(new ApnConfig.ApnData(null, c), null);
            Log.e(TAG, "getSelectAPN " + apnConfig);
            return apnConfig.getId();
        }
        return -1;
    }

    public static ApnConfig getAPN(Context context, int id) {
        ApnConfig apn = null;
        ContentResolver resolver = context.getContentResolver();
        String apn_condition = "_id=?";
        String[] args = new String[]{String.valueOf(id)};
        Cursor c = resolver.query(APN_URI, ApnConfig.sProjection, apn_condition, args, null);
        if (c != null && c.moveToNext()) {
            apn = new ApnConfig(new ApnConfig.ApnData(null, c), null);
            Log.e(TAG, "getAPN " + apn);
        }
        return apn;
    }

    public static void getCurrentAPN(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor c = resolver.query(CURRENT_APN_URI, ApnConfig.sProjection, null, null, null);
        while(c.moveToNext()) {
            ApnConfig apnConfig = new ApnConfig(new ApnConfig.ApnData(null, c), null);
            Log.d(TAG, "getCurrentAPN " + apnConfig.toString());
        }
    }

    public static List<ApnConfig> getSimAPN(Context context) {
        List<ApnConfig> apns = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        StringBuilder where = new StringBuilder("NOT (type='ia' AND (apn=\"\" OR apn IS NULL)) AND " + "user_visible!=0");
        Cursor c = resolver.query(Telephony.Carriers.SIM_APN_URI, ApnConfig.sProjection, where.toString(), null, null);
        while(c.moveToNext()) {
            ApnConfig apnConfig = new ApnConfig(new ApnConfig.ApnData(null, c));
            apns.add(apnConfig);
            Log.d(TAG, "getSimAPN " + apnConfig.toString());
        }
        return apns;
    }

    public static int settingAddAPN(Context context, String apnName, String apn, String mcc, String mnc, String type) {
        ApnConfig apnConfig = new ApnConfig(apnName, apn, mcc, mnc, type);
        return settingAddAPN(context, apnConfig);
    }

    public static int settingAddAPN(Context context, ApnConfig apn) {
        int id = -1;
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = apn.getContentValues();
        Cursor c = null;
        Uri newRow = resolver.insert(APN_URI, values);
        if (newRow != null) {
            c = resolver.query(newRow, null, null, null, null);
            int idIndex = c.getColumnIndex(Telephony.Carriers._ID);
            c.moveToFirst();
            id = c.getShort(idIndex);
        }
        if (c != null) c.close();
        Log.d(TAG, "Add APN " + id);
        return id;
    }

    public static int updateAPN(Context context, ApnConfig apn) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = apn.getContentValues();
        Uri updateUri = Uri.withAppendedPath(APN_URI, String.valueOf(apn.getId()));
        int update = resolver.update(updateUri, values, null, null);
        Log.d(TAG, "Update APN " + update);
        return update;
    }

    public static int selectAPN(Context context, int id) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put("apn_id", id);
        int update = resolver.update(CURRENT_APN_URI, values, null, null);
        return update;
    }

    public static int setAPN(Context context, ApnConfig apn) {
        int apn_id = -1;
        if (apn.getId() == apn_id) {
            apn_id = settingAddAPN(context, apn);
        } else if (updateAPN(context, apn) > 0) {
            apn_id = apn.getId();
        }
        if (apn_id != -1) selectAPN(context, apn_id);
        return apn_id;
    }

    public static int deleteAPN(Context context, int id) {
        ContentResolver resolver = context.getContentResolver();
        String apn_condition = "_id=?";
        String[] args = new String[]{String.valueOf(id)};
        int delete = resolver.delete(APN_URI, apn_condition, args);
        return delete;
    }
}
