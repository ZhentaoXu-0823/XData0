package com.custom.mdm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.timedetector.ManualTimeSuggestion;
import android.app.timedetector.TimeDetector;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.content.pm.IPackageManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.ComponentName;
import android.hardware.usb.UsbManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.IpConfiguration;
import android.net.IpConfiguration.IpAssignment;
import android.net.LinkAddress;
import android.net.NetworkUtils;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.StaticIpConfiguration;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RecoverySystem;
import android.os.SystemProperties;
import android.os.LocaleList;
import android.os.UserHandle;
import android.os.RemoteException;
import android.provider.Settings;
import android.text.TextUtils;
import android.telephony.TelephonyManager;
import android.telephony.SubscriptionManager;
import android.telephony.SubscriptionInfo;
import android.util.Log;
import com.android.internal.app.LocalePicker;
import com.android.internal.statusbar.IStatusBarService;
import java.io.File;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Inet4Address;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import com.xchengtech.ProjectConfig;

public class Util {
    public static final String TAG = "CustomInterfaceUtil";
    private static final String DEVICE_SCANNER_NAME = "/sys/devices/platform/device_info/MAIN2CAMERAINFO";

    // install apk sliently
    public static void install(Context context, String apkFilePath) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    PackageInstall.silentInstallApk(context, apkFilePath);
                }
            }).start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // uninstall package sliently
    public static void uninstall(Context context, String packageName) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    PackageInstall.uninstallAPK(context, packageName);
                }
            }).start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // update system
    public static void updateSystem(Context context, String srcPath) {
        if(!srcPath.endsWith(".zip")) {
            Log.e("UpdateSystem", "UpdateFile is no zip file");
            return;
        }
        SystemProperties.set("persist.sys.rom.version.before.ota", Util2.getRomVersion());
        RecoverySystem.ProgressListener listener = new RecoverySystem.ProgressListener() {
            @Override
            public void onProgress(int progress) {
                Log.d(TAG, "RecoverySystem.verifyPackage progress = " + progress);
                if(progress == 100) {
                    Intent intent = new Intent("android.intent.action.XC_ACTION_CUSTOM_UPDATE_SYSTEM");
                    intent.setPackage("com.android.settings");
                    intent.putExtra("update_file_path", srcPath);
                    intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    context.sendBroadcast(intent);
                }
            }
        };
        try {
            RecoverySystem.verifyPackage(new File(srcPath), listener, null);
        } catch (Exception e) {
            Intent intent = new Intent("android.intent.action.XC_ACTION_OTA_VERITY_FAILED");
            intent.setPackage("com.custom.mdm");
            intent.putExtra("update_file_path", srcPath);
            intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
            context.sendBroadcast(intent);
            e.printStackTrace();
        }
    }

    // reboot device
    public static void reboot(Context context) {
        IStatusBarService mBarService;
        mBarService = IStatusBarService.Stub.asInterface(ServiceManager.getService(Context.STATUS_BAR_SERVICE));
        try {
            mBarService.reboot(false);
        } catch (RemoteException e) {
        }
    }

    // shutdown
    public static void shutdown(Context context) {
        IStatusBarService mBarService;
        mBarService = IStatusBarService.Stub.asInterface(ServiceManager.getService(Context.STATUS_BAR_SERVICE));
        try {
            mBarService.shutdown();
        } catch (RemoteException e) {
        }
    }

    public static void factoryReset(Context context) {
        Intent intent = new Intent(Intent.ACTION_FACTORY_RESET);
        intent.setPackage("android");
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        intent.putExtra(Intent.EXTRA_REASON, "MasterClearConfirm");
        intent.putExtra(Intent.EXTRA_WIPE_EXTERNAL_STORAGE, false);
        intent.putExtra(Intent.EXTRA_WIPE_ESIMS, false);
        context.sendBroadcast(intent);
    }

    // open or close Wifi
    public static void setWifiEnabled(Context context, boolean enabled) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wm.setWifiEnabled(enabled);
    }

    // open or close bluetooth
    public static void setBTEnabled(boolean enabled) {
        BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
        if(enabled) ba.enable();
        else ba.disable();
    }

    // open or close GPS
    public static void setGPSEnabled(Context context, boolean enabled) {
        ContentResolver localContentResolver = context.getContentResolver();
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("name", "network_location_opt_in");
        localContentValues.put("value", enabled?1:0);
        localContentResolver.insert(Uri.parse("content://com.google.settings/partner"), localContentValues);

        int mode = Settings.Secure.LOCATION_MODE_OFF;
        if(enabled) {
            mode = Settings.Secure.LOCATION_MODE_HIGH_ACCURACY;
        }
        Settings.Secure.putInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE, mode);
    }

    // open or close SIM data
    public static void setSimDataEnabled(Context context, boolean enabled) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        tm.setDataEnabled(enabled);
    }

    // disable or enable volume key
    public static void setVolumeKey(boolean enabled) {
        SystemProperties.set("persist.sys.volume.key", enabled?"1":"0");
    }

    public static void setScreenBrightness(Context context, int brightness) {
        // Integer 0~255, suggest 10~255
        if(brightness < 10) brightness = 10;
        if(brightness > 255) brightness = 255;
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
    }
    public static int getScreenBrightness(Context context) {
        return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 100);
    }

    public static void setScreenTimeOut(Context context, int ms) {
        //18000000 display never off screen
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, ms);
    }
    public static int getScreenTimeOut(Context context) {
        return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 60000);
    }

    /**
     *type: 
     *  AudioManager.STREAM_SYSTEM
     *  AudioManager.STREAM_MUSIC
     *  AudioManager.STREAM_RING
     *  //AudioManager.STREAM_ALARM
     */
    public static void setVolume(Context context, int type, int volume, int flags) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(type);
        if(volume < 0) volume = 0;
        if(volume > maxVolume) volume = maxVolume;
        audioManager.setStreamVolume(type, volume, flags/*0, AudioManager.FLAG_SHOW_UI, AudioManager.FLAG_PLAY_SOUND*/);
    }
    public static int getVolume(Context context, int type) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamVolume(type);
    }

    /**
     *locale: 
     *  Locale.ENGLISH
     *  Locale.CHINA
     *  Locale.US
     *  Locale.GERMANY
     *  Locale locale = new Locale("en", "US");
     *  new Locale("en")
     *  ......
     */
    public static void setLanguage(Locale locale) {
        //LocalePicker.updateLocale(locale);
        LocaleList localeList = new LocaleList(locale, LocaleList.getDefault());
        LocalePicker.updateLocales(localeList);
    }
    public static String getLanguage() {
        return Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry();
    }

    /**
     *timeZone: 
     *  Asia/Shanghai
     *  Asia/Tehran
     *  America/New_York
     *  ......
     */
    public static void setTimeZone(Context context, String timeZone) {
        if (timeZone.equals("AUTO")) {
            setAutoTimeZone(context, true);
            return;
        }
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        try {
            alarmManager.setTimeZone(timeZone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setAutoTimeZone(context, false);
    }
    private static void setAutoTimeZone(Context context, boolean enabled) {
        Settings.Global.putInt(context.getContentResolver(), Settings.Global.AUTO_TIME_ZONE, enabled?1:0);
    }

    public static String getTimeZone() {
        return SystemProperties.get("persist.sys.timezone", "");
    }

    // Wifi settings
    public static String getWifiIP(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ipAddress = intIP2StrIP(wifiInfo.getIpAddress());
        return ipAddress;
    }
    public static void setWifiDHCP(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration wifiConfig = getWifiConfig(wifiManager);
        if(wifiConfig != null) {
            try {
                //wifiConfig.setIpAssignment(IpAssignment.DHCP);
                Method setIpAssignmentMethod = wifiConfig.getClass().getDeclaredMethod("setIpAssignment", IpAssignment.class);
                setIpAssignmentMethod.invoke(wifiConfig, IpAssignment.DHCP);
            } catch(Exception e) {
                e.printStackTrace();
            }
            setWifi(wifiManager, wifiConfig);
        }
    }
    public static void setWifiStatic(Context context, String wifiIp, String gateway, int networkNumberLength, String wifiDNS) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration wifiConfig = getWifiConfig(wifiManager);
        if(wifiConfig == null) return;

        try {
            //wifiConfig.setIpAssignment(IpAssignment.STATIC);
            Method setIpAssignmentMethod = wifiConfig.getClass().getDeclaredMethod("setIpAssignment", IpAssignment.class);
            setIpAssignmentMethod.invoke(wifiConfig, IpAssignment.STATIC);
            StaticIpConfiguration staticConfig = new StaticIpConfiguration();
            Inet4Address inetAddr = null;
            inetAddr = (Inet4Address) NetworkUtils.numericToInetAddress(wifiIp);
            staticConfig.ipAddress = new LinkAddress(inetAddr, networkNumberLength);
            staticConfig.gateway =(Inet4Address) NetworkUtils.numericToInetAddress(gateway);
            staticConfig.dnsServers.add((Inet4Address) NetworkUtils.numericToInetAddress(wifiDNS));
            //wifiConfig.setStaticIpConfiguration(staticConfig);
            Method setStaticIpConfigurationMethod = wifiConfig.getClass().getDeclaredMethod("setStaticIpConfiguration", staticConfig.getClass());
            setStaticIpConfigurationMethod.invoke(wifiConfig, staticConfig);
        } catch(Exception e) {
            e.printStackTrace();
        }
        setWifi(wifiManager, wifiConfig);
    }
    private static WifiConfiguration getWifiConfig(WifiManager wifiManager) {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int networkId = wifiInfo == null ? 0 : wifiInfo.getNetworkId();
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        if (configuredNetworks != null) {
            for (WifiConfiguration configuredNetwork : configuredNetworks) {
                if (configuredNetwork.networkId == networkId) {
                    try {
                        Method getIpAssignmentMethod = configuredNetwork.getClass().getDeclaredMethod("getIpAssignment");  //configuredNetwork.getIpAssignment()
                        Log.d(TAG, "Wifi IP mode = " + (getIpAssignmentMethod.invoke(configuredNetwork) == IpAssignment.STATIC ? "STATIC":"DHCP"));
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    return configuredNetwork;
                }
            }
        }
        return null;
    }
    public static int saveWifiConfig(Context context, String ssid, String password, int security) {
        WifiConfiguration wifiConfig = null;

        // Create WifiConfiguration Object
        wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = "\"" + ssid + "\""; // SSID
        wifiConfig.preSharedKey = "\"" + password + "\"";
        // Set security WPA/WPA2/NONE encryption type
        wifiConfig.allowedKeyManagement.set(security); // e.g. WifiConfiguration.KeyMgmt.NONE

        // save Wi-Fi Config
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return setWifi(wifiManager, wifiConfig);
    }
    private static int setWifi(WifiManager wifiManager, WifiConfiguration config) {
        //WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.updateNetwork(config);
        wifiManager.saveConfiguration();
        int netId = wifiManager.addNetwork(config);
        wifiManager.disableNetwork(netId);
        boolean flag = wifiManager.enableNetwork(netId, true);
        Log.d(TAG, "setWifi: netId = " + netId + ", flag = " + flag);
        return netId;
    }
    private static int strIp2IntIp(String ip) {
        String[] ips = ip.split("\\.");
        return Integer.parseInt(ips[0])<<24
                | Integer.parseInt(ips[1])<<16
                | Integer.parseInt(ips[2])<<8
                | Integer.parseInt(ips[3]);
    }
    private static String intIP2StrIP(int ip) {
        Log.d(TAG, "int IP = " + ip);
        return (ip & 0xFF) + "." +
                (ip >> 8 & 0xFF) + "." +
                (ip >> 16 & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    public static void setStatusBar(Context context, boolean enabled) {
        Settings.System.putInt(context.getContentResolver(), "status_bar", enabled?1:0);
    }

    public static void setNavigationBar(Context context, boolean enabled) {
        setNavigationBar(context, enabled?1:0);
        if (enabled) {
            setBackKey(context, enabled);
            setHomeKey(context, enabled);
            setRecentKey(context, enabled);
        }
    }
    public static int getStatusBar(Context context) {
        int apiAvailable = Util2.getApiAvailable("setStatusBar");
        if (apiAvailable != 1) {
            Log.d(TAG, "setStatusBar is not available");
            return -1001;
        } else {
            return Settings.System.getInt(context.getContentResolver(), "status_bar", 1);
        }
    }
    public static int getNavigationBar(Context context) {
        int apiAvailable = Util2.getApiAvailable("setNavigationBar");
        if (apiAvailable != 1) {
            Log.d(TAG, "setNavigationBar is not available");
            return -1001;
        } else {
            return Settings.System.getInt(context.getContentResolver(), "navbar_display", 1);
        }
    }
    private static void setNavigationBar(Context context, int display) {
        if(Settings.System.getInt(context.getContentResolver(), "navbar_display", 1) == display) return;
        Settings.System.putInt(context.getContentResolver(), "navbar_display", display);
    }

    public static void setBackKey(Context context, boolean enabled) {
        if(Settings.System.getInt(context.getContentResolver(), "backkey_enabled", 1) == (enabled?1:0)) return;
        Settings.System.putInt(context.getContentResolver(), "backkey_enabled", enabled?1:0);
        setNavigationBar(context, checkAllKeyEnabled(context)?1:0);
    }
    public static void setHomeKey(Context context, boolean enabled) {
        if(Settings.System.getInt(context.getContentResolver(), "homekey_enabled", 1) == (enabled?1:0)) return;
        Settings.System.putInt(context.getContentResolver(), "homekey_enabled", enabled?1:0);
        setNavigationBar(context, checkAllKeyEnabled(context)?1:0);
    }
    public static void setRecentKey(Context context, boolean enabled) {
        if(Settings.System.getInt(context.getContentResolver(), "recentkey_enabled", 1) == (enabled?1:0)) return;
        Settings.System.putInt(context.getContentResolver(), "recentkey_enabled", enabled?1:0);
        setNavigationBar(context, checkAllKeyEnabled(context)?1:0);
    }
    private static boolean checkAllKeyEnabled(Context context) {
        boolean res = false;
        res |= Settings.System.getInt(context.getContentResolver(), "backkey_enabled", 1)==1;
        res |= Settings.System.getInt(context.getContentResolver(), "homekey_enabled", 1)==1;
        res |= Settings.System.getInt(context.getContentResolver(), "recentkey_enabled", 1)==1;
        return res;
    }

    public static void disablePowerKey(Context context, boolean disable) {
        SystemProperties.set("persist.sys.power.key", disable?"0":"1");
        disableAutoScreenOff(context, disable);
    }

    public static void disablePowerLongPressKey(Context context, boolean disable) {
        SystemProperties.set("persist.sys.powerlongpress.key", disable?"0":"1");
    }
    private static WakeLock mWakeLock = null;
    private static WakeLock getWakeLock(Context context) {
        if(mWakeLock == null) {
            PowerManager powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "Custom_WakeLock");
            mWakeLock.setReferenceCounted(false);
        }
        return mWakeLock;
    }
    private static void disableAutoScreenOff(Context context, boolean disable) {
        WakeLock wakeLock = getWakeLock(context);
        if(disable) {
            wakeLock.acquire();
        } else {
            wakeLock.release();
        }
    }

    public static void setApkSignature(boolean enabled) {
        SystemProperties.set("persist.sys.device.mode", enabled?"1":"0");
        SystemProperties.set("persist.sys.zen.device.mode", enabled?"1":"0");
    }

    public static void setDevelopment(Context context, boolean enabled) {
        Settings.Global.putInt(context.getContentResolver(), Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, enabled?1:0);
        setAdbDebug(context, enabled);
    }

    public static void setAdbDebug(Context context, boolean enabled) {
        Settings.Global.putInt(context.getContentResolver(), Settings.Global.ADB_ENABLED, enabled?1:0);
        context.sendBroadcast(new Intent("Change.watermark"));
    }

    public static void setDebugMode(Context context, boolean enabled) {
        // String mode = SystemProperties.get("persist.sys.xc.demo");
        // String debug = "userdebug mode";
        // String user = "";
        // if(enabled == debug.equals(mode)) return;
        setDevelopment(context, enabled);
        setApkSignature(!enabled);
        // SystemProperties.set("persist.sys.xc.demo", enabled?debug:user);
        // reboot(context);
    }

    public static void setLauncher(String packageName, String launcherActivityName) {
        if("reset".equals(packageName)) {
            resetLauncher();
        }
        if(!TextUtils.isEmpty(packageName) && !TextUtils.isEmpty(packageName)) {
            setHomeActivity(packageName, launcherActivityName);
        }
        Log.e(TAG, "Parameter is empty");
    }
    private static void resetLauncher() {
        String launcher = "com.android.launcher3:com.android.launcher3.uioverrides.QuickstepLauncher";
        if(!TextUtils.isEmpty(ProjectConfig.XC_DEFAULT_LAUNCHER)) {
            launcher = ProjectConfig.XC_DEFAULT_LAUNCHER;
        }
        String[] launcherName = launcher.split(":");
        setHomeActivity(launcherName[0], launcherName[1]);
    }
    private static void setHomeActivity(String packageName, String launcherActivityName) {
        UserHandle user = android.os.Process.myUserHandle();
        final IPackageManager pm = IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
        final ComponentName component = new ComponentName(packageName, launcherActivityName);
        try {
            pm.setHomeActivity(component, user.getIdentifier());
        } catch(RemoteException e) {
            Log.e(TAG, "setHomeActivity: " + e);
        }
    }

    public static void setAppsPwd(Context context, String pwd) {
        if("nopwd".equals(pwd)) setSettingsMenuPwd(context, "apps", "no");
        else if("reset".equals(pwd)) setSettingsMenuPwd(context, "apps", "re");
        else if(!TextUtils.isEmpty(pwd)) setSettingsMenuPwd(context, "apps", pwd);
    }
    public static void setStoragePwd(Context context, String pwd) {
        if("nopwd".equals(pwd)) setSettingsMenuPwd(context, "storage", "no");
        else if("reset".equals(pwd)) setSettingsMenuPwd(context, "storage", "re");
        else if(!TextUtils.isEmpty(pwd)) setSettingsMenuPwd(context, "storage", pwd);
    }
    public static void setResetPwd(Context context, String pwd) {
        if("nopwd".equals(pwd)) setSettingsMenuPwd(context, "reset", "no");
        else if("reset".equals(pwd)) setSettingsMenuPwd(context, "reset", "re");
        else if(!TextUtils.isEmpty(pwd)) setSettingsMenuPwd(context, "reset", pwd);
    }
    public static void setDevelopmentPwd(Context context, String pwd) {
        if("nopwd".equals(pwd)) setSettingsMenuPwd(context, "development", "no");
        else if("reset".equals(pwd)) setSettingsMenuPwd(context, "development", "re");
        else if(!TextUtils.isEmpty(pwd)) setSettingsMenuPwd(context, "development", pwd);
    }
    public static void setSecurityPwd(Context context, String pwd) {
        if("nopwd".equals(pwd)) setSettingsMenuPwd(context, "security", "no");
        else if("reset".equals(pwd)) setSettingsMenuPwd(context, "security", "re");
        else if(!TextUtils.isEmpty(pwd)) setSettingsMenuPwd(context, "security", pwd);
    }
    private static void setSettingsMenuPwd(Context context, String key, String value) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.XC_ACTION_SETTINGS_MENU_PWD");
        intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        intent.putExtra(key, value);
        context.sendBroadcast(intent);
    }

    public static void setSettingsPwd(Context context, String pwd) {
        if("nopwd".equals(pwd)) setSettingsPwd(context, "settings_pwd", "no");
        else if("reset".equals(pwd)) setSettingsPwd(context, "settings_pwd", "re");
        else if(!TextUtils.isEmpty(pwd)) setSettingsPwd(context, "settings_pwd", pwd);
    }
    private static void setSettingsPwd(Context context, String key, String value) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.XC_ACTION_SETTINGS_PWD");
        intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        intent.putExtra(key, value);
        context.sendBroadcast(intent);
    }

    public static String getIMSI(Context context) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = tm.getSubscriberId();
        return imsi;
    }

    public static void setAirplaneMode(Context context, boolean enabled) {
        boolean airplaneModeEnabled = Settings.Global.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0)==1;
        if(enabled == airplaneModeEnabled) return;
        final ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mgr.setAirplaneMode(enabled);
    }

    static long MIN_DATE = 1194220800000L;
    public static void setSystemTime(Context context, int year, int month, int day, int hour, int minute, int second) {
        if(year == 0 && month == 0 && day == 0) {
            setAutoTime(context, true);
            return;
        }

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month-1);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        c.set(Calendar.MILLISECOND, 0);
        long when = Math.max(c.getTimeInMillis(), MIN_DATE);

        if (when / 1000 < Integer.MAX_VALUE) {
            setSystemTime(context, when);
        }
    }
    public static void setDateTime(Context context, String dateTime, String dateFormat) {
        if("AUTO_TIME".equals(dateTime)) {
            setAutoTime(context, true);
            return;
        }
        if(TextUtils.isEmpty(dateFormat)) dateFormat = "yyyyMMddHHmmss";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        try {
            Date date = formatter.parse(dateTime);
            assert date != null;
            setSystemTime(context, date.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void setSystemTime(Context context, long timeMillis) {
        if(timeMillis <= MIN_DATE) return;
        Log.d(TAG, "setSystemTime: " + timeMillis);
        setAutoTime(context, false);
        TimeDetector timeDetector = context.getSystemService(TimeDetector.class);
        ManualTimeSuggestion manualTimeSuggestion = TimeDetector.createManualTimeSuggestion(timeMillis, "CustomMDM: Set time");
        timeDetector.suggestManualTime(manualTimeSuggestion);
    }
    private static void setAutoTime(Context context, boolean enabled) {
        Settings.Global.putInt(context.getContentResolver(), Settings.Global.AUTO_TIME, enabled?1:0);
    }

    public static String getIMSI(Context context, int subId) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = tm.getSubscriberId(subId);
        return imsi;
    }

    public static String getIMEI(Context context, int slotIndex) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = "null";
        if (slotIndex == 0 || slotIndex == 1) {
            imei = tm.getDeviceId(slotIndex);
        } else {
            imei = "bad slotIndex";
        }
        return imei;
    }

    public static String getICCID(Context context, int subId) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String iccid = tm.getSimSerialNumber(subId);
        return iccid;
    }

    public static void shell(String cmd) {
        try {
            Runtime.getRuntime().exec(cmd);
            Log.d(TAG, "cmd: " + cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getSubId(Context context, int slotIndex) {
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        int subId = -1;
        if (subscriptionManager != null) {
            SubscriptionInfo subscriptionInfo = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(slotIndex);
            if (subscriptionInfo != null) {
                subId = subscriptionInfo.getSubscriptionId();
            }
        }
        return subId;
    }

    public static int getDataSimSlotIndex(Context context) {
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        int slotIndex = -1;
        if (subscriptionManager != null) {
            int defaultDataSubscriptionId = subscriptionManager.getDefaultDataSubscriptionId();
            slotIndex = subscriptionManager.getSlotIndex(defaultDataSubscriptionId);
        }
        return slotIndex;
    }

    public static String getScannerInfo() {
        return readValue(DEVICE_SCANNER_NAME);
    }

    private static String readValue(String path) {
        String value = "";
        try {
            FileInputStream fis = new FileInputStream(path);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr, 1024);

            StringBuilder buffer = new StringBuilder();
            String ch = null;
            while ((ch = br.readLine()) != null) {
                buffer.append(ch);
                buffer.append("\r\n");
                Log.d(TAG, path + ", ch:" + ch);
                return value = ch;
            }
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
        return value;
    }

    public static void setUsbCdc(Context context, boolean enabled) {
        Util2.setPortIndex(enabled);
        switchUsbMtp(context, !enabled);
    }
    private static void switchUsbMtp(Context context, boolean enabled) {
        UsbManager mUsbManager = (UsbManager) context.getApplicationContext().getSystemService(Context.USB_SERVICE);
        mUsbManager.setCurrentFunction(enabled?UsbManager.USB_FUNCTION_MTP:UsbManager.USB_FUNCTION_NONE, true);
    }

    public static void hideQsEdit(boolean isHidden) {
        SystemProperties.set("persist.sys.matriz.qs.edit.visible", isHidden?"0":"1");
    }

    public static void hideHotspot(boolean isHidden) {
        SystemProperties.set("persist.sys.zen.hide.hotspot.option", isHidden?"1":"0");
    }

    public static void setStatusBarDisplay(Context context, boolean display) {
        Settings.System.putInt(context.getContentResolver(), "status_bar_display", display?1:0);
    }

    public static int getStatusBarDisplay(Context context) {
        int apiAvailable = Util2.getApiAvailable("setStatusBarDisplay");
        if (apiAvailable != 1) {
            Log.d(TAG, "setStatusBarDisplay is not available");
            return -1001;
        } else {
            return Settings.System.getInt(context.getContentResolver(), "status_bar_display", 1);
        }
    }

    public static int autoReboot(Context context, boolean enabled) {
        setAutoRebootTime(context, enabled?"24":"0");
        return 1;
    }
    public static int setAutoRebootTime(Context context, long time_h) {
        if (time_h <= 0 || time_h > 24) return 0;
        setAutoRebootTime(context, String.valueOf(time_h));
        return 1;
    }
    private static void setAutoRebootTime(Context context, String time_h) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.XC_ACTION_REBOOT_INTIME");
        intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        intent.putExtra("reboot_time", time_h);
        context.sendBroadcast(intent);
    }

    public static int simDataSwitch(Context context, int index) {
        SubscriptionManager subscriptionManager = SubscriptionManager.from(context);
        List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
        if (subscriptionManager != null && subscriptionInfoList.size() > 0) {
            int subId = subscriptionInfoList.get(index).getSubscriptionId();
            subscriptionManager.setDefaultDataSubId(subId);
            if (subscriptionManager.getDefaultDataSubscriptionId() == subId) {
                return index;
            } else {
                return -99; //set default data simcard faile.
            }
        } else {
            return -100; //subscriptionManager error or subscriptionInfoList is null.
        }
    }

    public static String getPhoneNumber(Context context, int slotIndex) {
        String phoneNumber = "SlotIndex should be 0 or 1.";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        SubscriptionManager subscriptionManager = SubscriptionManager.from(context);

        List<SubscriptionInfo> subscriptionInfos = subscriptionManager.getActiveSubscriptionInfoList();
        if (subscriptionInfos != null) {
            for (SubscriptionInfo subscriptionInfo : subscriptionInfos) {
                if (subscriptionInfo.getSimSlotIndex() == slotIndex) {
                    int subscriptionId = subscriptionInfo.getSubscriptionId();
                    TelephonyManager specificManager = telephonyManager.createForSubscriptionId(subscriptionId);
                    phoneNumber = ((specificManager.getLine1Number() == null)
                                    || specificManager.getLine1Number().isEmpty()) ? "Sim yet unregistered!" : specificManager.getLine1Number();
                }
            }
        } else {
            phoneNumber = "100";
            Log.d(TAG, "subscriptionInfos is null");
        }
        return phoneNumber;
    }
}
