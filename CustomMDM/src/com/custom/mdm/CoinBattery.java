package com.custom.mdm;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CoinBattery {
    public static final String TAG = "CoinBattery";

    public static String getBatteryValue(final Context context) {
        SimpleDateFormat dateFormat = null;
        dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String currentDate = null;
        currentDate = dateFormat.format(new Date());
        if (currentDate.equals(getSavedDate(context))) {
            Log.d(TAG, "Multiple tests on " + currentDate + ", return saved data");
            return getSavedVoltage(context);
        }

        final String nodePath = "/sys/devices/platform/sp_pay/pay_battery_val";
        final String valueToWrite = "1";
        writeValueToNode(nodePath, valueToWrite);
        final String nodeValue = readValueFromNode(nodePath);
        String finalCurrentDate = currentDate;
        writeValueToNode(nodePath, "0");
        if (!nodeValue.equals("failed")) {
            saveVoltageAndDate(context, nodeValue, finalCurrentDate);
        }
        return nodeValue;
    }
    private static String getSavedVoltage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("BatteryVoltage", Context.MODE_PRIVATE);
        return preferences.getString("Voltage", "");
    }
    private static String getSavedDate(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("BatteryVoltage", Context.MODE_PRIVATE);
        return preferences.getString("Date", "");
    }
    private static void saveVoltageAndDate(Context context, String voltage, String date) {
        SharedPreferences preferences = context.getSharedPreferences("BatteryVoltage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Voltage", voltage);
        editor.putString("Date", date);
        editor.apply();
    }
    private static void writeValueToNode(String nodePath, String value) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(nodePath));
            writer.write(value);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static String readValueFromNode(String nodePath) {
        StringBuilder nodeValue = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(nodePath));
            String line;
            while ((line = reader.readLine()) != null) {
                nodeValue.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return nodeValue.toString();
    }
}