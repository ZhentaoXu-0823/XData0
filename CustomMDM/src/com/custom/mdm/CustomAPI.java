package com.custom.mdm;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.RemoteException;
import android.os.IBinder;
import android.os.SystemClock;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.text.format.DateUtils;
import android.util.Log;

import com.custom.mdm.ICustomAPI;
import com.custom.mdm.OtaVerityReceiver;

import java.util.*;

public class CustomAPI {
    private static final String TAG = "CustomAPI";

    private static ICustomAPI iCustomAPI = null;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext = null;
    private static CustomServiceListener mListener = null;
    private static InstallReceiver installReceiver = null;
    private static OtaVerityReceiver otaVerityReceiver = null;

//    static IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
//        @Override
//        public void binderDied() {
//            Log.d(TAG, "binderDied");
//            if (iCustomAPI != null) {
//                iCustomAPI.asBinder().unlinkToDeath(this, 0);
//                iCustomAPI = null;
//            }
//        }
//    };

    private static final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
            iCustomAPI = null;
            if(installReceiver != null) installReceiver.unRegister(mContext);
            installReceiver = null;
            if(otaVerityReceiver != null) otaVerityReceiver.unRegister(mContext);
            otaVerityReceiver = null;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(mListener != null) mListener.onServiceDisconnected();
                }
            }, 2000);
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            iCustomAPI = ICustomAPI.Stub.asInterface(service);
            if(mListener != null) mListener.onServiceConnected();
            if(installReceiver != null) installReceiver.register(mContext);
            if(otaVerityReceiver != null) otaVerityReceiver.register(mContext);

//            try {
//                service.linkToDeath(deathRecipient, 0);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
        }
    };

    public interface CustomServiceListener {
        public void onServiceConnected();

        public default void onServiceDisconnected(){}
    }

    public static void init(Context context) {
        init(context, null);
    }

    public static void init(Context context, CustomServiceListener listener) {
        //Log.d(TAG, "init");
        mListener = listener;
        //if(mContext != null && iCustomAPI != null) return;
        mContext = context;
        Intent intent = new Intent();
        intent.setAction("com.custom.mdm.service");
        intent.setPackage("com.custom.mdm");
        mContext.startService(intent);
        mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        installReceiver = new InstallReceiver();
        otaVerityReceiver = new OtaVerityReceiver();
    }
    public static void release() {
        //Log.d(TAG, "release");
        if(isConnected()) {
            mContext.unbindService(mConnection);
            iCustomAPI = null;
            if(installReceiver!= null) installReceiver.unRegister(mContext);
            installReceiver = null;
        }
    }
    public static boolean isConnected() {
        return iCustomAPI != null;
    }

    public static void reboot() {
        try {
            iCustomAPI.reboot();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shutdown() {
        try {
            iCustomAPI.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void install(String apkFilePath) {
        install(apkFilePath, null);
    }

    public static void uninstall(String packageName) {
        uninstall(packageName, null);
    }

    public static void install(String apkFilePath, InstallReceiver.InstallListener listener) {
        try {
            iCustomAPI.install(apkFilePath);
            if(listener != null) installReceiver.setInstallListener(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void uninstall(String packageName, InstallReceiver.UninstallListener listener) {
        try {
            iCustomAPI.uninstall(packageName);
            if(listener != null) installReceiver.setUninstallListener(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void systemUpdate(String updateFilePath) {
        try {
            systemUpdate(updateFilePath, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void systemUpdate(String updateFilePath, OtaVerityReceiver.OtaVerityListener listener) {
        try {
            iCustomAPI.systemUpdate(updateFilePath);
            if (listener != null) otaVerityReceiver.setOtaVerityListener(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setWifiEnabled(boolean enabled) {
        try {
            iCustomAPI.setWifiEnabled(enabled);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setBluetoothEnabled(boolean enabled) {
        try {
            iCustomAPI.setBluetoothEnabled(enabled);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setLocationEnabled(boolean enabled) {
        try {
            iCustomAPI.setLocationEnabled(enabled);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setSimDataEnabled(boolean enabled) {
        try {
            iCustomAPI.setSimDataEnabled(enabled);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setVolumeKey(boolean enabled) {
        try {
            iCustomAPI.setVolumeKey(enabled);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int setStatusBar(boolean enabled) {
        try {
            iCustomAPI.setStatusBar(enabled);
            return getStatusBar();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int setNavigationBar(boolean enabled) {
        try {
            iCustomAPI.setNavigationBar(enabled);
            return getNavigationBar();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void setScreenBrightness(int brightness) {
        try {
            iCustomAPI.setScreenBrightness(brightness);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getScreenBrightness() {
        try {
            return iCustomAPI.getScreenBrightness();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void setScreenTimeOut(int ms) {
        try {
            iCustomAPI.setScreenTimeOut(ms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getScreenTimeOut() {
        try {
            return iCustomAPI.getScreenTimeOut();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void setVolume(int type, int volume, int flags) {
        try {
            iCustomAPI.setVolume(type, volume, flags);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getVolume(int type) {
        try {
            return iCustomAPI.getVolume(type);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void setLanguage(Locale locale) {
        try {
            iCustomAPI.setLanguage(locale.getLanguage(), locale.getCountry());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getLanguage() {
        try {
            return iCustomAPI.getLanguage();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void setTimeZone(String timeZone) {
        try {
            iCustomAPI.setTimeZone(timeZone);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getTimeZone() {
        try {
            return iCustomAPI.getTimeZone();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getWifiIP() {
        try {
            return iCustomAPI.getWifiIP();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void setWifiDHCP() {
        try {
            iCustomAPI.setWifiDHCP();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setWifiStatic(String wifiIp, String gateway, int networkNumberLength, String wifiDNS) {
        try {
            iCustomAPI.setWifiStatic(wifiIp, gateway, networkNumberLength, wifiDNS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean puk1write(String mPuk1) {
        try {
            return iCustomAPI.puk1write(mPuk1);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean puk2write(String mPuk2) {
        try {
            return iCustomAPI.puk2write(mPuk2);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static String puk1read() {
        try {
            return iCustomAPI.puk1read();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String puk2read() {
        try {
            return iCustomAPI.puk2read();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void setBackKey(boolean enabled) {
        try {
            iCustomAPI.setBackKey(enabled);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void setHomeKey(boolean enabled) {
        try {
            iCustomAPI.setHomeKey(enabled);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void setRecentKey(boolean enabled) {
        try {
            iCustomAPI.setRecentKey(enabled);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void disablePowerKey(boolean enabled) {
        try {
            iCustomAPI.disablePowerKey(enabled);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void disablePowerLongPressKey(boolean enabled) {
        try {
            iCustomAPI.disablePowerLongPressKey(enabled);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void setApkSignature(boolean enabled) {
        try {
            iCustomAPI.setApkSignature(enabled);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void setDevelopment(boolean enabled) {
        try {
            iCustomAPI.setDevelopment(enabled);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void setDebugMode(boolean enabled) {
        try {
            iCustomAPI.setDebugMode(enabled);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void setLauncher(String packageName, String launcherActivityName) {
        try {
            iCustomAPI.setLauncher(packageName, launcherActivityName);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void setAppsPwd(String pwd) {
        try {
            iCustomAPI.setAppsPwd(pwd);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void setStoragePwd(String pwd) {
        try {
            iCustomAPI.setStoragePwd(pwd);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void setResetPwd(String pwd) {
        try {
            iCustomAPI.setResetPwd(pwd);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void setDevelopmentPwd(String pwd) {
        try {
            iCustomAPI.setDevelopmentPwd(pwd);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void setSecurityPwd(String pwd) {
        try {
            iCustomAPI.setSecurityPwd(pwd);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void setSettingsPwd(String pwd) {
        try {
            iCustomAPI.setSettingsPwd(pwd);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static String getIMSI() {
        try {
            return iCustomAPI.getIMSI();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getICCID() {
        try {
            return iCustomAPI.getICCID();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int setAPN(ApnConfig apn) {
        try {
            return iCustomAPI.setAPN(apn);
        } catch(Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static ApnConfig getAPN(int id) {
        try {
            return iCustomAPI.getAPN(id);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<ApnConfig> getSimAPN() {
        try {
            return iCustomAPI.getSimAPN();
        } catch(Exception e) {
            e.printStackTrace();
            return new ArrayList<ApnConfig>();
        }
    }

    public static int deleteAPN(int id) {
        try {
            return iCustomAPI.deleteAPN(id);
        } catch(Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void setAirplaneMode(boolean enabled) {
        try {
            iCustomAPI.setAirplaneMode(enabled);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void setDateTime(String dateTime, String dateFormat) {
        try {
            iCustomAPI.setDateTime(dateTime, dateFormat);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void setSystemTime(int year, int month, int day, int hour, int minute, int second) {
        try {
            iCustomAPI.setSystemTime(year, month, day, hour, minute, second);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static String getRomVersion() {
        try {
            return iCustomAPI.getRomVersion();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getStorageSize() {
        try {
            return iCustomAPI.getStorageSize();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getUsedStorage() {
        try {
            return iCustomAPI.getUsedStorage();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getBrand() {
        try {
            return iCustomAPI.getBrand();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getManufacture() {
        try {
            return iCustomAPI.getManufacture();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getAndroidVersion() {
        try {
            return iCustomAPI.getAndroidVersion();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getSerialNo() {
        try {
            return iCustomAPI.getSerialNo();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getModel() {
        try {
            return iCustomAPI.getModel();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static AppInfo queryAppInfo(String pkgName) {
        try {
            return iCustomAPI.queryAppInfo(pkgName);
        } catch(Exception e) {
            e.printStackTrace();
            return new AppInfo(false, -1, "queryAppInfo error in CustomAPI");
        }
    }

    public static void setAppEnable(String packageName, boolean enabled) {
        try {
            iCustomAPI.setAppEnable(packageName, enabled);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void setPkgNotUninstallable(String pkgName, boolean notUninstallable){
        try {
            iCustomAPI.setPkgNotUninstallable(pkgName, notUninstallable);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCpu() {
        try {
            return iCustomAPI.getCpu();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getCpuPercent() {
        try {
            return iCustomAPI.getCpuPercent();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getMemory() {
        try {
            return iCustomAPI.getMemory();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getMemoryPercent() {
        try {
            return iCustomAPI.getMemoryPercent();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getFirmwareVersion() {
        try {
            return iCustomAPI.getFirmwareVersion();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getHardwareVersion() {
        try {
            return iCustomAPI.getHardwareVersion();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getSystemProperty(String propName) {
        try {
            return iCustomAPI.getSystemProperty(propName);
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getIMEI() {
        try {
            return iCustomAPI.getIMEI();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void sleep() {
        try {
            iCustomAPI.sleep();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static String getMac() {
        try {
            return iCustomAPI.getMac();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getIntensity() {
        try {
            return iCustomAPI.getIntensity();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void cleanAppCache(String pkgName) {
        try {
            iCustomAPI.cleanAppCache(pkgName);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void settingsPwd(String pwd) {
        try {
            iCustomAPI.settingsPwd(pwd);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void enabledBluetooth(boolean enabled) {
        try {
            iCustomAPI.enabledBluetooth(enabled);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static long getAppSize(String pkg) {
        try {
            return iCustomAPI.getAppSize(pkg);
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void allowInstall(boolean allow) {
        try {
            iCustomAPI.allowInstall(allow);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void factoryReset() {
        try {
            iCustomAPI.factoryReset();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void autoRotations(boolean auto) {
        try {
            iCustomAPI.autoRotations(auto);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void lockLandscape() {
        try {
            iCustomAPI.lockLandscape();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static OtaResult getOTAResult() {
        try {
            return iCustomAPI.getOTAResult();
        } catch(Exception e) {
            e.printStackTrace();
            return new OtaResult(false, -1, "Exception");
        }
    }

    public static String getIMEI(int slotIndex) {
        try {
            return iCustomAPI.getIMEIs(slotIndex);
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getIMSI(int subId) {
        try {
            return iCustomAPI.getIMSIs(subId);
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getICCID(int subId) {
        try {
            return iCustomAPI.getICCIDs(subId);
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getSubId(int slotIndex) {
        try {
            return iCustomAPI.getSubId(slotIndex);
        } catch(Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int getDataSimSlotIndex() {
        try {
            return iCustomAPI.getDataSimSlotIndex();
        } catch(Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void setFontSize(float fontSize) {
        try {
            iCustomAPI.setFontSize(fontSize);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static float getFontSize() {
        try {
            return iCustomAPI.getFontSize();
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static float getBatteryHealth() {
        try {
            return iCustomAPI.getBatteryHealth();
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getMdmVersionName() {
        try {
            return queryAppInfo("com.custom.mdm").getVersionName();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getUpTime() {
        return DateUtils.formatElapsedTime(SystemClock.elapsedRealtime() / 1000);
    }

    public static String getSn2() {
        try {
            return iCustomAPI.getSn2();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean setSn2(String sn2) {
        try {
            return iCustomAPI.setSn2(sn2);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean setWifiMac(String wifiMac) {
        try {
            return iCustomAPI.setWifiMac(wifiMac);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getWifiMac() {
        try {
            return iCustomAPI.getWifiMac();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean setBtMac(String btMac) {
        try {
            return iCustomAPI.setBtMac(btMac);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getBtMac() {
        try {
            return iCustomAPI.getBtMac();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setBootStartPkgName(String pkgName) {
        try {
            iCustomAPI.setBootStartPkgName(pkgName);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static String[] getSdcardsPath() {
        try {
            return iCustomAPI.getSdcardsPath();
        } catch(Exception e) {
            e.printStackTrace();
            return new String[]{};
        }
    }

    public static String getOtgPath() {
        try {
            return iCustomAPI.getOtgPath();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setAutoRotation(boolean enabled) {
        try {
            iCustomAPI.setAutoRotation(enabled);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void ctrlClearCache(String pkgName, boolean enable) {
        try {
            iCustomAPI.ctrlClearCache(pkgName, enable);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void ctrlClearStorage(String pkgName, boolean enable) {
        try {
            iCustomAPI.ctrlClearStorage(pkgName, enable);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAppIcon(String pkgName, boolean show) {
        try {
            iCustomAPI.showAppIcon(pkgName, show);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void uninstallThroughDragAndDrop(String pkgName, boolean enabled) {
        try {
            iCustomAPI.uninstallThroughDragAndDrop(pkgName, enabled);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static int getApiAvailable(String apiName) {
        int result = -1001;
        try {
            if (iCustomAPI.getApiAvailable("getApiAvailable") == 1) {
                result = iCustomAPI.getApiAvailable(apiName);
            }
        } catch(Exception e) {
            e.printStackTrace();
            result = -1;
        } finally {
            return result;
        }
    }

    public static void setPortIndex(boolean enabled) {
        try {
            iCustomAPI.setPortIndex(enabled);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static int getNavigationBar() {
        int result = -1001;
        try {
            if (getApiAvailable("getNavigationBar") == 1) {
                result = iCustomAPI.getNavigationBar();
            }
        } catch(Exception e) {
            e.printStackTrace();
            result = -1;
        } finally {
            return result;
        }
    }

    public static int getStatusBar() {
        int result = -1001;
        try {
            if (getApiAvailable("getStatusBar") == 1) {
                result = iCustomAPI.getStatusBar();
            }
        } catch(Exception e) {
            e.printStackTrace();
            result = -1;
        } finally {
            return result;
        }
    }

    public static String getScannerInfo() {
        try {
            return iCustomAPI.getScannerInfo();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCoinBatteryVoltage() {
        try {
            return iCustomAPI.getCoinBatteryVoltage();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setUsbCdc(boolean enabled) {
        try {
            iCustomAPI.setUsbCdc(enabled);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideQsEdit(boolean isHidden) {
        try {
            iCustomAPI.hideQsEdit(isHidden);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideHotspot(boolean isHidden) {
        try {
            iCustomAPI.hideHotspot(isHidden);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static int setStatusBarDisplay(boolean display) {
        try {
            iCustomAPI.setStatusBarDisplay(display);
            return getStatusBarDisplay();
        } catch(Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int getStatusBarDisplay() {
        int result = -1001;
        try {
            if (getApiAvailable("getStatusBarDisplay") == 1) {
                result = iCustomAPI.getStatusBarDisplay();
            }
        } catch(Exception e) {
            e.printStackTrace();
            result = -1;
        } finally {
            return result;
        }
    }

    public static int autoReboot(boolean enabled) {
        try {
            return iCustomAPI.autoReboot(enabled);
        } catch(Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int setAutoRebootTime(int time_h) {
        try {
            return iCustomAPI.setAutoRebootTime(time_h);
        } catch(Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}