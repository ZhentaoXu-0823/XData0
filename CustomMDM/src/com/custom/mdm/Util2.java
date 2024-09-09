package com.custom.mdm;

import com.android.internal.util.HexDump;

import android.app.ActivityManager;
import android.app.usage.StorageStats;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.provider.Settings;
import android.os.StatFs;
import android.os.SystemProperties;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.os.storage.VolumeInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.util.ArrayList;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.*;

import vendor.mediatek.hardware.nvram.V1_0.INvram;

import com.pos.sdk.accessory.POIGeneralAPI;

import com.xchengtech.ProjectConfig;

public class Util2 {
    //Util for Alatin
    //2023/07/31 changed by zhentao.xu begin
    public static final String TAG = "CustomInterfaceUtil2";
    private static final String XC_CUSTOM_NVRAM_NAME = "/mnt/vendor/nvdata/APCFG/APRDEB/XC_CUSTOM";
    private static final String API_INFORMATION_PATH = "system/etc/custom_mdm_config.xml";

    //getSystemProperty("");
    public static String getSystemProperty(String propName) {
        return SystemProperties.get(propName, "cannot getSystemProperty ---- " + propName);
    }

    public static String getRomVersion() {
        return getSystemProperty("ro.build.display.id");
    }

    public static String getBrand() {
        return getSystemProperty("ro.product.brand");
    }

    public static String getManufacture() {
        return getSystemProperty("ro.product.manufacturer");
    }

    public static String getAndroidVersion() {
        return getSystemProperty("ro.build.version.release");
    }

    public static String getSerialNo() {
        return getSystemProperty("ro.serialno");
    }

    public static String getModel() {
        return getSystemProperty("ro.product.model");
    }

    public static void cleanAppCache(String pkgName) {
        Util.shell("pm clear " + pkgName);
    }

    public static void settingsPwd(Context context, String pwd) {
        if (pwd == null || pwd.length() == 0) {
            Util.setSettingsPwd(context, "123456");
        } else {
            Util.setSettingsPwd(context, pwd);
        }
    }

    public static void enabledBluetooth(boolean enabled) {
        SystemProperties.set("persist.sys.enabled.bluetooth", enabled?"1":"0");
        Log.d(TAG, "enabledBluetooth have not realized");
    }

    public static String getFirmwareVersion() {
        String firmwareVersion = POIGeneralAPI.getDefault().getSpVersion();
        return firmwareVersion;
    }

    public static String getHardwareVersion() {
        return getSystemProperty("ro.board.platform");
    }

    public static void setAppEnable(String pkgName, boolean enabled) {
        if (enabled) {
            Util.shell("pm enable " + pkgName);
        } else {
            Util.shell("pm disable-user " + pkgName);
        }
    }

    public static void setPkgNotUninstallable(Context context, String pkgName, boolean notUninstallable) {
        SystemProperties.set("persist.sys.notuninstall." + pkgName, notUninstallable?"1":"0");
    }

    public static String getCpu() {
        return "MTK";
    }

    public static String getCpuPercent() {
        BufferedReader reader = null;
        int totle = 0;
        int totleR = 0;
        try {
            for (int i = 0 ; i < 4 ; i++){
                reader = new BufferedReader(new InputStreamReader(new FileInputStream("/sys/devices/system/cpu/cpu" + i + "/cpufreq/cpuinfo_max_freq")), 100);
                String totleRead = reader.readLine();
                totle += Integer.parseInt(totleRead);
            }
            for (int i = 0 ; i < 4 ; i++) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream("/sys/devices/system/cpu/cpu" + i + "/cpufreq/scaling_cur_freq")), 100);
                String totleReadC = reader.readLine();
                totleR += Integer.parseInt(totleReadC);
            }
            return "" + totleR * 100 / totle + "%";
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static AppInfo queryAppInfo(Context context, String pkgName) {
        try {
            PackageInfo pkgInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
            boolean isInstalled = pkgInfo != null;
            if (isInstalled) {
                int versionCode = pkgInfo.versionCode;
                String versionName = pkgInfo.versionName;
                return new AppInfo(isInstalled, versionCode, versionName);
            } else {
                return new AppInfo(isInstalled, 0, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new AppInfo(false, -1, "getAppInfo error");
        }
    }

    public static String getMemory(Context context) {
        try {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
            manager.getMemoryInfo(info);
            return formatFileSize(info.totalMem);
        } catch (Exception e) {
            e.printStackTrace();
            return "getMemory error";
        }
    }

    public static String getMemoryPercent(Context context) {
        try {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
            manager.getMemoryInfo(info);
            return (info.availMem * 100 / info.totalMem) + "%";
        } catch (Exception e) {
            e.printStackTrace();
            return "getMemoryPercent error";
        }
    }

    public static String getStorageSize(){
        try {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            long totalCounts = statFs.getBlockCountLong();
            long size = statFs.getBlockSizeLong();
            long totalROMSize = totalCounts * size;
            return formatFileSize(totalROMSize);
        } catch (Exception e) {
            e.printStackTrace();
            return "getStorageSize error";
        }
    }

    public static String getUsedStorage(){
        try {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            long availCounts = statFs.getAvailableBlocksLong();
            long size = statFs.getBlockSizeLong();
            long availROMSize = availCounts * size;
            return formatFileSize(availROMSize);
        } catch (Exception e) {
            e.printStackTrace();
            return "getUsedStorage error";
        }
    }

    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        return imei;
    }

    public static String getICCID(Context context) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String iccid = tm.getSimSerialNumber();
        return iccid;
    }

    public static void sleep() {
        Util.shell("input keyevent " + 26);
    }

    public static String getMac() {
        final int START_INDEX = 4;
        final int LENGTH = 6;
        return byte2HexStringWithSymbol(readHexData("/vendor/nvdata/APCFG/APRDEB/WIFI", START_INDEX, LENGTH));
    }

    private static String simIntensity = "";
    public static String getIntensity(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //WIFI or MOBILE
            String type = networkInfo.getTypeName();
            if (type.equals("WIFI")) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                return "Connected WIFI, WIFI Intensity: " + wifiInfo.getRssi();
            } else if (type.equals("MOBILE")) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (telephonyManager != null) {
                    telephonyManager.listen(new PhoneStateListener(){
                        @Override
                        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                            super.onSignalStrengthsChanged(signalStrength);
                            int asu = signalStrength.getGsmSignalStrength();
                            int lastSignal = -113 + 2 * asu;
                            simIntensity = "Connected SIM, SIM Intensity: " + lastSignal;
                        }
                    }, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
                    return simIntensity;
                }
            }
        }
        return "XXX";
    }

    public static byte[] readHexData(String nvName, int st, int len) {
        Log.d(TAG, "readHexData.nvName : " + nvName);
        byte[] data = null;
        String buff = null;
        try {
            INvram agent = INvram.getService();
            if(agent != null) {
                buff = agent.readFileByName(nvName, st + len);
                byte[] newBuff = HexDump.hexStringToByteArray(buff.substring(0, buff.length() - 1));

                if(newBuff != null) {
                    data = new byte[len];
                    for(int i = 0; i < len; i++) {
                        data[i] = newBuff[st + i];
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return data;
    }

    public static String byte2HexStringWithSymbol(byte[] data) {
        if (data == null || data.length <= 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            String hexStr = (Integer.toHexString(data[i] & 0xff)).toUpperCase();
            if (hexStr.length() < 2) {
                sb.append("0");
            }
            sb.append(hexStr);
            if (i != data.length - 1) {
                sb.append(":");
            }
        }
        return sb.toString();
    }

    public static String byte2HexString(byte[] data) {
        if (data == null || data.length <= 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            String hexStr = (Integer.toHexString(data[i] & 0xff)).toUpperCase();
            if (hexStr.length() < 2) {
                sb.append("0");
            }
            sb.append(hexStr);
        }
        return sb.toString();
    }

    public static void settingPwd(Context context, String pwd) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.XC_ACTION_SETTINGS_PWD");
        intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        intent.putExtra("settings_pwd", pwd);
        context.sendBroadcast(intent);
    }

    public static long getAppSize(Context context, String pkg) {
        StorageStatsManager storageStatsManager = (StorageStatsManager) context.getSystemService(Context.STORAGE_STATS_SERVICE);
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        List<StorageVolume> storageVolumes = storageManager.getStorageVolumes();
        long appTotalSize = 0;
        for (StorageVolume s : storageVolumes) {
            UUID uuid = (s.getUuid() == null) ? StorageManager.UUID_DEFAULT : UUID.fromString(s.getUuid());
            int uid = -1;
            try {
                PackageManager pm = context.getPackageManager();
                ApplicationInfo ai = pm.getApplicationInfo(pkg, PackageManager.GET_META_DATA);
                uid = ai.uid;
                StorageStats storageStats = storageStatsManager.queryStatsForUid(uuid, uid);
                appTotalSize = storageStats.getAppBytes() + storageStats.getCacheBytes() + storageStats.getDataBytes();
                Log.d(TAG, "AppBytes:" + formatFileSize(storageStats.getAppBytes()) +" CacheBytes:" + formatFileSize(storageStats.getCacheBytes())
                            + " DataBytes:" + formatFileSize(storageStats.getDataBytes()) + " Total:" + formatFileSize(appTotalSize));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return appTotalSize;
    }

    public static void allowInstall(boolean allow) {
        SystemProperties.set("persist.sys.allow.install", allow?"1":"0");
    }

    private static String formatFileSize(long d) {
        if(d > 1024) {
            d = d / 1024;
            if(d > 1024) {
                d = d / 1024;
                if(d > 1024) {
                    d = d / 1024;
                    return "" + d + "GB";
                } else {
                    return "" + d + "MB";
                }
            } else {
                return "" + d + "KB";
            }
        } else {
            return "" + d + "B";
        }
    }
    //2023/07/31 changed by zhentao.xu end
    //2023/08/03 for alatin changed by zhentao.xu start
    public static void autoRotations(boolean auto) {
        //persist.sys.auto.rotations      auto--1    lock--0
        //persist.sys.lock.lockLandscape      lock--1    auto--0
        if (auto) {
            SystemProperties.set("persist.sys.auto.rotations", "1");
            SystemProperties.set("persist.sys.lock.lockLandscape", "0");
        } else {
            SystemProperties.set("persist.sys.auto.rotations", "0");
        }
    }
    public static void lockLandscape() {
        //persist.sys.lock.lockLandscape      lock--1    auto--0
        SystemProperties.set("persist.sys.lock.lockLandscape", "1");
    }
    //2023/08/03 for alatin changed by zhentao.xu start
    public static OtaResult getOTAResult() {
        OtaResult otaResult = null;
        if(SystemProperties.get("sys.boot.reason", "null").contains("ota") || SystemProperties.get("sys.boot.reason", "null").contains("recovery")) {
            if(getOtaResult1() == -1) {
                otaResult = new OtaResult(true, -1, "oldRomVersion is null");
            } else if(getOtaResult1() == 0) {
                otaResult = new OtaResult(true, 0, "OTA FAILED");
                otaResult.setOldRomVersion(SystemProperties.get("persist.sys.rom.version.before.ota", "null"));
                otaResult.setCurrentRomVersion(getRomVersion());
            } else if(getOtaResult1() == 1) {
                otaResult = new OtaResult(true, 1, "OTA SUCCESS");
                otaResult.setOldRomVersion(SystemProperties.get("persist.sys.rom.version.before.ota", "null"));
                otaResult.setCurrentRomVersion(getRomVersion());
            }
        } else {
            otaResult = new OtaResult(false, -1, "NOT OTA");
        }
        return otaResult;
    }

    public static int getOtaResult1() {
        String oldRomVersion = SystemProperties.get("persist.sys.rom.version.before.ota", "null");
        String currentRomVersion = getRomVersion();
        if (oldRomVersion == null || oldRomVersion.equals("null")) {
            return -1;
        } else {
            return oldRomVersion.equals(currentRomVersion)?0:1;
        }
    }

    public static void setFontSize(Context context, float fontSize) {
        Settings.System.putFloat(context.getContentResolver(), Settings.System.FONT_SCALE, fontSize);
    }

    public static float getFontSize(Context context) {
        return Settings.System.getFloat(context.getContentResolver(), Settings.System.FONT_SCALE, 0);
    }

    public static float getBatteryHealth() {
        int batteryHealth = Integer.parseInt(getSystemProperty("persist.sys.health.loop"));
        float result = (1 - 0.2f*batteryHealth/800) * 100;
        return result;
    }

    private static final int SN2_NV_START_POSITION = 301;
    private static final int SN2_AND_SN3_MAX_LENGTH = 50;

    public static String getSn2(Context context) {
        return doReadFromNVByIndex(XC_CUSTOM_NVRAM_NAME, SN2_NV_START_POSITION, SN2_AND_SN3_MAX_LENGTH);
    }

    public static boolean setSn2(Context context, String sn2) {
        String readSn2 = getSn2(context);
        if (sn2 == null || sn2.equals("") || readSn2.equals(sn2)) {
            return false;
        }
        byte[] buff = null;
        byte[] mSn2 = stringToByteArray(stringToHex(sn2));
        INvram agent = null;
        try {
            agent = INvram.getService();
            String data = agent.readFileByName(XC_CUSTOM_NVRAM_NAME, SN2_NV_START_POSITION + SN2_AND_SN3_MAX_LENGTH);
            buff = HexDump.hexStringToByteArray(data.substring(0, data.length() - 1));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (buff != null) {
            for (int i = SN2_NV_START_POSITION; i < SN2_NV_START_POSITION + mSn2.length; i++) {
                buff[i] = mSn2[i - SN2_NV_START_POSITION];
            }
            for (int j = SN2_NV_START_POSITION + mSn2.length; j < SN2_NV_START_POSITION + SN2_AND_SN3_MAX_LENGTH; j++) {
                buff[j] = 0x00;
            }
            ArrayList<Byte> dataArray = new ArrayList<Byte>(SN2_NV_START_POSITION + SN2_AND_SN3_MAX_LENGTH);
            for (int j = 0; j < SN2_NV_START_POSITION + SN2_AND_SN3_MAX_LENGTH; j++) {
                dataArray.add(j, new Byte(buff[j]));
            }
            try {
                agent.writeFileByNamevec(XC_CUSTOM_NVRAM_NAME, SN2_NV_START_POSITION + SN2_AND_SN3_MAX_LENGTH, dataArray);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private static String doReadFromNVByIndex(String path, int start, int length) {
        byte[] buff = readHexData(path, start, length);
        String result = hexStringToString(byte2HexString(buff));
        return result;
    }

    private static byte[] stringToByteArray(String buff) {
        byte[] buffer = new byte[buff.length() / 2];
        buffer = HexDump.hexStringToByteArray(buff);;
        return buffer;
    }

    public static String hexStringToString(String hexString) {
    StringBuilder output = new StringBuilder();
    for (int i = 0; i < hexString.length(); i += 2) {
        String str = hexString.substring(i, i + 2);
        output.append((char) Integer.parseInt(str, 16));
    }
    return output.toString();
}

    public static String stringToHex(String str) {
        char[] strChars = str.toCharArray();
        StringBuffer hexBuffer = new StringBuffer();
        for(int i=0; i<strChars.length; i++) {
            hexBuffer.append(Integer.toHexString((int)strChars[i]));
        }
        return hexBuffer.toString();
    }

    public static byte[] hexStringToByteArrayBySymbol(String hexString) {
        String[] hexValues = hexString.split(":");
        byte[] byteArray = new byte[hexValues.length];
        for (int i = 0; i < hexValues.length; i++) {
            byteArray[i] = (byte) Integer.parseInt(hexValues[i], 16);
        }
        return byteArray;
    }

    private static final String WIFI_NVRAM_NAME = "/vendor/nvdata/APCFG/APRDEB/WIFI";
    public static String getWifiMac(Context context) {
        final int START_INDEX = 4;
        final int LENGTH = 6;
        return byte2HexStringWithSymbol(readHexData(WIFI_NVRAM_NAME, START_INDEX, LENGTH));
    }

    public static boolean setWifiMac(Context context, String wifiMac) {
        final int START_INDEX = 4;
        final int LENGTH = 6;
        String readWifiMac = getWifiMac(context);
        if (wifiMac == null || wifiMac.equals("") || wifiMac.equals(readWifiMac)) {
            return false;
        }
        byte[] buff = null;
        byte[] mMac = hexStringToByteArrayBySymbol(wifiMac);
        INvram agent = null;
        try {
            agent = INvram.getService();
            String data = agent.readFileByName(WIFI_NVRAM_NAME, START_INDEX + LENGTH);
            buff = HexDump.hexStringToByteArray(data.substring(0,data.length() - 1));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (buff != null) {
            for (int i = START_INDEX; i < START_INDEX + LENGTH; i++) {
                buff[i] = mMac[i - START_INDEX];
            }
            ArrayList<Byte> dataArray = new ArrayList<Byte>(START_INDEX + LENGTH);
            for (int j = 0; j < START_INDEX + LENGTH; j++) {
                dataArray.add(j, new Byte(buff[j]));
            }
            try {
                agent.writeFileByNamevec(WIFI_NVRAM_NAME,START_INDEX + LENGTH, dataArray);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private static final String BT_NVRAM_NAME = "/vendor/nvdata/APCFG/APRDEB/BT_Addr";
    public static String getBtMac(Context context) {
        final int START_INDEX = 0;
        final int LENGTH = 6;
        return byte2HexStringWithSymbol(readHexData(BT_NVRAM_NAME, START_INDEX, LENGTH));
    }

    public static boolean setBtMac(Context context, String btMac) {
        final int START_INDEX = 0;
        final int LENGTH = 6;
        String readBtMac = getBtMac(context);
        if (btMac == null || btMac.equals("") || btMac.equals(readBtMac)) {
            return false;
        }
        byte[] buff = null;
        byte[] mMac = hexStringToByteArrayBySymbol(btMac);
        INvram agent = null;
        try {
            agent = INvram.getService();
            String data = agent.readFileByName(BT_NVRAM_NAME, START_INDEX + LENGTH);
            buff = HexDump.hexStringToByteArray(data.substring(0,data.length() - 1));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (buff != null) {
            for (int i = START_INDEX; i < START_INDEX + LENGTH; i++) {
                buff[i] = mMac[i - START_INDEX];
            }
            ArrayList<Byte> dataArray = new ArrayList<Byte>(START_INDEX + LENGTH);
            for (int j = 0; j < START_INDEX + LENGTH; j++) {
                dataArray.add(j, new Byte(buff[j]));
            }
            try {
                agent.writeFileByNamevec(BT_NVRAM_NAME,START_INDEX + LENGTH, dataArray);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static void setBootStartPkgName(Context context, String pkgName) {
        SystemProperties.set("persist.sys.bootstart", pkgName);
    }

    public static String[] getSdcardsPath(Context context) {
        File[] externalFilesDir = context.getExternalFilesDirs(Environment.DIRECTORY_DOWNLOADS);
        String[] paths = new String[2];
        int paths_length = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0 ; i < externalFilesDir.length ; i++) {
            String p = externalFilesDir[i].getAbsolutePath();
            int index = p.indexOf("Android/data");
            if (index != -1) {
                String result = p.substring(0, index);
                Log.d(TAG, "path: " + result);
                paths[paths_length++] = result;
            }
        }
        return paths;
    }

    public static String getOtgPath(Context context) {
        String otgPath = null;
        StorageManager mStorageManager = (StorageManager) context.getSystemService(StorageManager.class);
        final List<VolumeInfo> volumes = mStorageManager.getVolumes();
        if (volumes.size() < 0) {
            return "NO OTG";
        }
        for (VolumeInfo volume : volumes) {
            String diskID = volume.getDiskId();
            if (diskID != null) {
                String[] idSplit = diskID.split(":");
                if (idSplit != null && idSplit.length == 2) {
                    if (idSplit[1].startsWith("8,")) {
                        otgPath = volume.path;
                    }
                }
            }
        }
        return otgPath;
    }

    public static void setAutoRotation(Context context, boolean enabled) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION,enabled ? 1:0);
    }

    public static void ctrlClearCache(Context context, String pkgName, boolean enable) {
        SystemProperties.set("persist.sys.hide.clear.cache." + pkgName, enable ? "0" : "1");
    }

    public static void ctrlClearStorage(Context context, String pkgName, boolean enable) {
        SystemProperties.set("persist.sys.hide.clear.storage." + pkgName, enable ? "0" : "1");
    }

    public static void showAppIcon(Context context, String pkgName, boolean show) {
        SystemProperties.set("persist.sys.show.icon." + pkgName, show ? "1" : "0");
    }

    public static void uninstallThroughDragAndDrop(Context context, String pkgName, boolean enabled) {
        SystemProperties.set("persist.sys.uninstall.through.drag_drop." + pkgName, enabled ? "1" : "0");
    }

    private static Map<String, String> getApiList() {
        Map<String, String> map = new HashMap<>();
        try {
            File inputFile = new File(API_INFORMATION_PATH);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("item");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String key = eElement.getAttribute("key");
                    String value = eElement.getTextContent();
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static int getApiAvailable(String apiName) {
        int status = -1;
        Map<String, String> map = getApiList();
        for (String key : map.keySet()) {
            if (apiName.equals(key)) {
                status = Integer.parseInt(map.get(apiName));
            }
        }
        return status;
    }

    public static void setPortIndex(boolean enabled) {
        SystemProperties.set("persist.vendor.radio.port_index", enabled ? "1" : "0");
    }
}
