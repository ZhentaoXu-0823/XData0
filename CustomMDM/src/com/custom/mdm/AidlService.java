package com.custom.mdm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.*;

public class AidlService extends Service {
    public static final String TAG = "AidlService";

    public AidlService() {
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
    }

    ICustomAPI.Stub stub = new ICustomAPI.Stub() {
        @Override
        public void reboot() throws RemoteException {
            //Util.factoryReset(AidlService.this);
            Util.reboot(AidlService.this);
        }

        @Override
        public void shutdown() throws RemoteException {
            Util.shutdown(AidlService.this);
        }

        @Override
        public void install(String apkFilePath) throws RemoteException {
            Util.install(AidlService.this, apkFilePath);
        }

        @Override
        public void uninstall(String packageName) throws RemoteException {
            Util.uninstall(AidlService.this, packageName);
        }

        @Override
        public void systemUpdate(String updateFilePath) throws RemoteException {
            Util.updateSystem(AidlService.this, updateFilePath);
        }

        @Override
        public void setWifiEnabled(boolean enabled) throws RemoteException {
            Util.setWifiEnabled(AidlService.this, enabled);
        }

        @Override
        public void setBluetoothEnabled(boolean enabled) throws RemoteException {
            Util.setBTEnabled(enabled);
        }

        @Override
        public void setLocationEnabled(boolean enabled) throws RemoteException {
            Util.setGPSEnabled(AidlService.this, enabled);
        }

        @Override
        public void setSimDataEnabled(boolean enabled) throws RemoteException {
            Util.setSimDataEnabled(AidlService.this, enabled);
        }

        @Override
        public void setVolumeKey(boolean enabled) throws RemoteException {
            Util.setVolumeKey(enabled);
        }

        @Override
        public void setStatusBar(boolean enabled) throws RemoteException {
            Util.setStatusBar(AidlService.this, enabled);
        }

        @Override
        public void setNavigationBar(boolean enabled) throws RemoteException {
            Util.setNavigationBar(AidlService.this, enabled);
        }

        @Override
        public int getNavigationBar() throws RemoteException {
           return Util.getNavigationBar(AidlService.this);
        }

        @Override
        public int getStatusBar() throws RemoteException {
           return Util.getStatusBar(AidlService.this);
        }

        @Override
        public void setScreenBrightness(int brightness) throws RemoteException {
            Util.setScreenBrightness(AidlService.this, brightness);
        }

        @Override
        public int getScreenBrightness() throws RemoteException {
            return Util.getScreenBrightness(AidlService.this);
        }

        @Override
        public void setScreenTimeOut(int ms) throws RemoteException {
            Util.setScreenTimeOut(AidlService.this, ms);
        }

        @Override
        public int getScreenTimeOut() throws RemoteException {
            return Util.getScreenTimeOut(AidlService.this);
        }

        @Override
        public void setVolume(int type, int volume, int flags) throws RemoteException {
            Util.setVolume(AidlService.this, type, volume, flags);
        }

        @Override
        public int getVolume(int type) throws RemoteException {
            return Util.getVolume(AidlService.this, type);
        }

        @Override
        public void setLanguage(String language, String country) throws RemoteException {
            Locale locale = new Locale(language, country);
            Util.setLanguage(locale);
        }

        @Override
        public String getLanguage() throws RemoteException {
            return Util.getLanguage();
        }

        @Override
        public void setTimeZone(String timeZone) throws RemoteException {
            Util.setTimeZone(AidlService.this, timeZone);
        }

        @Override
        public String getTimeZone() throws RemoteException {
            return Util.getTimeZone();
        }

        @Override
        public String getWifiIP() throws RemoteException {
            return Util.getWifiIP(AidlService.this);
        }

        @Override
        public void setWifiDHCP() throws RemoteException {
            Util.setWifiDHCP(AidlService.this);
        }

        @Override
        public void setWifiStatic(String wifiIp, String gateway, int networkNumberLength, String wifiDNS) throws RemoteException {
            Util.setWifiStatic(AidlService.this, wifiIp, gateway, networkNumberLength, wifiDNS);
        }

        @Override
        public boolean puk1write(String mPuk1) throws RemoteException {
            return PUKRW.puk1write(mPuk1);
        }

        @Override
        public boolean puk2write(String mPuk2) throws RemoteException {
            return PUKRW.puk2write(mPuk2);
        }

        @Override
        public String puk1read() throws RemoteException {
            return PUKRW.puk1read();
        }

        @Override
        public String puk2read() throws RemoteException {
            return PUKRW.puk2read();
        }

        @Override
        public void setBackKey(boolean enabled) throws RemoteException {
            Util.setBackKey(AidlService.this, enabled);
        }

        @Override
        public void setHomeKey(boolean enabled) throws RemoteException {
            Util.setHomeKey(AidlService.this, enabled);
        }

        @Override
        public void setRecentKey(boolean enabled) throws RemoteException {
            Util.setRecentKey(AidlService.this, enabled);
        }

        @Override
        public void disablePowerKey(boolean enabled) throws RemoteException {
            Util.disablePowerKey(AidlService.this, enabled);
        }

        @Override
        public void disablePowerLongPressKey(boolean enabled) throws RemoteException {
            Util.disablePowerLongPressKey(AidlService.this, enabled);
        }

        @Override
        public void setApkSignature(boolean enabled) throws RemoteException {
            Util.setApkSignature(enabled);
        }

        @Override
        public void setDevelopment(boolean enabled) throws RemoteException {
            Util.setDevelopment(AidlService.this, enabled);
        }

        @Override
        public void setDebugMode(boolean enabled) throws RemoteException {
            Util.setDebugMode(AidlService.this, enabled);
        }

        @Override
        public void setLauncher(String packageName, String launcherActivityName) throws RemoteException {
            Util.setLauncher(packageName, launcherActivityName);
        }

        @Override
        public void setAppsPwd(String pwd) throws RemoteException {
            Util.setAppsPwd(AidlService.this, pwd);
        }

        @Override
        public void setStoragePwd(String pwd) throws RemoteException {
            Util.setStoragePwd(AidlService.this, pwd);
        }

        @Override
        public void setResetPwd(String pwd) throws RemoteException {
            Util.setResetPwd(AidlService.this, pwd);
        }

        @Override
        public void setDevelopmentPwd(String pwd) throws RemoteException {
            Util.setDevelopmentPwd(AidlService.this, pwd);
        }

        @Override
        public void setSecurityPwd(String pwd) throws RemoteException {
            Util.setSecurityPwd(AidlService.this, pwd);
        }

        @Override
        public void setSettingsPwd(String pwd) throws RemoteException {
            Util.setSettingsPwd(AidlService.this, pwd);
        }

        @Override
        public String getIMSI() throws RemoteException {
            return Util.getIMSI(AidlService.this);
        }

        @Override
        public String getICCID() throws RemoteException {
            return Util2.getICCID(AidlService.this);
        }

        @Override
        public int setAPN(ApnConfig apn) throws RemoteException {
            return ApnUtils.setAPN(AidlService.this, apn);
        }

        @Override
        public ApnConfig getAPN(int id) throws RemoteException {
            return ApnUtils.getAPN(AidlService.this, id);
        }

        @Override
        public List<ApnConfig> getSimAPN() throws RemoteException {
            return ApnUtils.getSimAPN(AidlService.this);
        }

        @Override
        public void setAirplaneMode(boolean enabled) throws RemoteException {
            Util.setAirplaneMode(AidlService.this, enabled);
        }

        @Override
        public void setDateTime(String dateTime, String dateFormat) throws RemoteException {
            Util.setDateTime(AidlService.this, dateTime, dateFormat);
        }

        @Override
        public void setSystemTime(int year, int month, int day, int hour, int minute, int second) throws RemoteException {
            Util.setSystemTime(AidlService.this, year, month, day, hour, minute, second);
        }

        @Override
        public int deleteAPN(int id) throws RemoteException {
            return ApnUtils.deleteAPN(AidlService.this, id);
        }

        @Override
        public String getRomVersion() throws RemoteException {
            return Util2.getRomVersion();
        }

        @Override
        public String getStorageSize() throws RemoteException {
            return Util2.getStorageSize();
        }

        @Override
        public String getUsedStorage() throws RemoteException {
            return Util2.getUsedStorage();
        }

        @Override
        public String getBrand() throws RemoteException {
            return Util2.getBrand();
        }

        @Override
        public String getManufacture() throws RemoteException {
            return Util2.getManufacture();
        }

        @Override
        public String getAndroidVersion() throws RemoteException {
            return Util2.getAndroidVersion();
        }

        @Override
        public String getSerialNo() throws RemoteException {
            return Util2.getSerialNo();
        }

        @Override
        public String getModel() throws RemoteException {
            return Util2.getModel();
        }

        @Override
        public AppInfo queryAppInfo(String pkgName) throws RemoteException {
            return Util2.queryAppInfo(AidlService.this, pkgName);
        }

        @Override
        public void setAppEnable(String packageName, boolean enabled) throws RemoteException {
            Util2.setAppEnable(packageName, enabled);
        }

        @Override
        public void setPkgNotUninstallable(String pkgName, boolean notUninstallable) throws RemoteException {
            Util2.setPkgNotUninstallable(AidlService.this, pkgName, notUninstallable);
        }

        @Override
        public String getCpu() throws RemoteException {
            return Util2.getCpu();
        }

        @Override
        public String getCpuPercent() throws RemoteException {
            return Util2.getCpuPercent();
        }

        @Override
        public String getMemory() throws RemoteException {
            return Util2.getMemory(AidlService.this);
        }

        @Override
        public String getMemoryPercent() throws RemoteException {
            return Util2.getMemoryPercent(AidlService.this);
        }

        @Override
        public String getFirmwareVersion() throws RemoteException {
            return Util2.getFirmwareVersion();
        }

        @Override
        public String getHardwareVersion() throws RemoteException {
            return Util2.getHardwareVersion();
        }

        @Override
        public String getSystemProperty(String propName) throws RemoteException {
            return Util2.getSystemProperty(propName);
        }

        @Override
        public String getIMEI() throws RemoteException {
            return Util2.getIMEI(AidlService.this);
        }

        @Override
        public void sleep() throws RemoteException {
            Util2.sleep();
        }

        @Override
        public String getMac() throws RemoteException {
            return Util2.getMac();
        }

        @Override
        public String getIntensity() throws RemoteException {
            return Util2.getIntensity(AidlService.this);
        }

        @Override
        public void cleanAppCache(String pkgName) throws RemoteException {
            Util2.cleanAppCache(pkgName);
        }

        @Override
        public void settingsPwd(String pwd) throws RemoteException {
            Util2.settingPwd(AidlService.this, pwd);
        }

        @Override
        public void enabledBluetooth(boolean enabled) throws RemoteException {
            Util2.enabledBluetooth(enabled);
        }

        @Override
        public long getAppSize(String pkg) throws RemoteException {
            return Util2.getAppSize(AidlService.this, pkg);
        }

        @Override
        public void allowInstall(boolean allow) throws RemoteException {
            Util2.allowInstall(allow);
        }

        @Override
        public void factoryReset() throws RemoteException {
            Util.factoryReset(AidlService.this);
        }

        @Override
        public void autoRotations(boolean auto) throws RemoteException {
            Util2.autoRotations(auto);
        }

        @Override
        public void lockLandscape() throws RemoteException {
            Util2.lockLandscape();
        }

        @Override
        public OtaResult getOTAResult() throws RemoteException {
            return Util2.getOTAResult();
        }

        @Override
        public String getIMEIs(int slotIndex) throws RemoteException {
            return Util.getIMEI(AidlService.this, slotIndex);
        }

        @Override
        public String getIMSIs(int subId) throws RemoteException {
            return Util.getIMSI(AidlService.this, subId);
        }

        @Override
        public String getICCIDs(int subId) throws RemoteException {
            return Util.getICCID(AidlService.this, subId);
        }

        @Override
        public int getSubId(int slotIndex) throws RemoteException {
            return Util.getSubId(AidlService.this, slotIndex);
        }

        @Override
        public int getDataSimSlotIndex() throws RemoteException {
            return Util.getDataSimSlotIndex(AidlService.this);
        }

        @Override
        public void setFontSize(float fontSize) throws RemoteException {
            Util2.setFontSize(AidlService.this, fontSize);
        }

        @Override
        public float getFontSize() throws RemoteException {
            return Util2.getFontSize(AidlService.this);
        }

        @Override
        public String getEthIp() throws RemoteException {
            return "";
        }

        @Override
        public String getEthGateway() throws RemoteException {
            return "";
        }

        @Override
        public int getEthUseDhcpOrStaticIp() throws RemoteException {
            return -1;
        }

        @Override
        public float getBatteryHealth() throws RemoteException {
            return Util2.getBatteryHealth();
        }

        @Override
        public String getSn2() throws RemoteException {
            return Util2.getSn2(AidlService.this);
        }

        @Override
        public boolean setSn2(String sn2) throws RemoteException {
            return Util2.setSn2(AidlService.this, sn2);
        }

        @Override
        public boolean setWifiMac(String wifiMac) throws RemoteException {
            return Util2.setWifiMac(AidlService.this, wifiMac);
        }

        @Override
        public String getWifiMac() throws RemoteException {
            return Util2.getWifiMac(AidlService.this);
        }

        @Override
        public boolean setBtMac(String btMac) throws RemoteException {
            return Util2.setBtMac(AidlService.this, btMac);
        }

        @Override
        public String getBtMac() throws RemoteException {
            return Util2.getBtMac(AidlService.this);
        }

        @Override
        public void setBootStartPkgName(String pkgName) throws RemoteException {
            Util2.setBootStartPkgName(AidlService.this, pkgName);
        }

        @Override
        public String[] getSdcardsPath() throws RemoteException {
            return Util2.getSdcardsPath(AidlService.this);
        }

        @Override
        public String getOtgPath() throws RemoteException {
            return Util2.getOtgPath(AidlService.this);
        }

        @Override
        public void setAutoRotation(boolean enabled) throws RemoteException {
            Util2.setAutoRotation(AidlService.this, enabled);
        }

        @Override
        public void ctrlClearCache(String pkgName, boolean enable) throws RemoteException {
            Util2.ctrlClearCache(AidlService.this, pkgName, enable);
        }

        @Override
        public void ctrlClearStorage(String pkgName, boolean enable) throws RemoteException {
            Util2.ctrlClearStorage(AidlService.this, pkgName, enable);
        }

        @Override
        public void showAppIcon(String pkgName, boolean show) throws RemoteException {
            Util2.showAppIcon(AidlService.this, pkgName, show);
        }

        @Override
        public void uninstallThroughDragAndDrop(String pkgName, boolean enabled) throws RemoteException {
            Util2.uninstallThroughDragAndDrop(AidlService.this, pkgName, enabled);
        }

        @Override
        public int getApiAvailable(String apiName) throws RemoteException {
            return Util2.getApiAvailable(apiName);
        }

        @Override
        public void setPortIndex(boolean enabled) throws RemoteException {
            Util2.setPortIndex(enabled);
        }

        @Override
        public String getScannerInfo() throws RemoteException {
            return Util.getScannerInfo();
        }

        @Override
        public String getCoinBatteryVoltage() throws RemoteException {
            return CoinBattery.getBatteryValue(AidlService.this);
        }

        @Override
        public void setUsbCdc(boolean enabled) throws RemoteException {
            Util.setUsbCdc(AidlService.this, enabled);
        }

        @Override
        public void hideQsEdit(boolean isHidden) throws RemoteException {
            Util.hideQsEdit(isHidden);
        }

        @Override
        public void hideHotspot(boolean isHidden) throws RemoteException {
            Util.hideHotspot(isHidden);
        }

        @Override
        public void setStatusBarDisplay(boolean display) throws RemoteException {
            Util.setStatusBarDisplay(AidlService.this, display);
        }

        @Override
        public int getStatusBarDisplay() throws RemoteException {
            return Util.getStatusBarDisplay(AidlService.this);
        }

        @Override
        public int autoReboot(boolean enabled) throws RemoteException {
            return Util.autoReboot(AidlService.this, enabled);
        }

        @Override
        public int setAutoRebootTime(long time_h) throws RemoteException {
            return Util.setAutoRebootTime(AidlService.this, time_h);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return stub;
    }
}