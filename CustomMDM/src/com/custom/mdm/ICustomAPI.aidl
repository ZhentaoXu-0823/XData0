// ICustomAPI.aidl
package com.custom.mdm;

// Declare any non-default types here with import statements
import com.custom.mdm.ApnConfig;
import com.custom.mdm.AppInfo;
import com.custom.mdm.OtaResult;

interface ICustomAPI {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void reboot();

    void shutdown();

    void install(String apkFilePath);

    void uninstall(String packageName);

    void systemUpdate(String updateFilePath);

    void setWifiEnabled(boolean enabled);

    void setBluetoothEnabled(boolean enabled);

    void setLocationEnabled(boolean enabled);

    void setSimDataEnabled(boolean enabled);

    void setVolumeKey(boolean enabled);

    void setStatusBar(boolean enabled);

    void setNavigationBar(boolean enabled);

    void setScreenBrightness(int brightness);

    int getScreenBrightness();

    void setVolume(int type, int volume, int flags);

    int getVolume(int type);

    void setLanguage(String language, String country);

    String getLanguage();

    void setTimeZone(String timeZone);

    String getTimeZone();

    String getWifiIP();

    void setWifiDHCP();

    void setWifiStatic(String wifiIp, String gateway, int networkNumberLength, String wifiDNS);

    boolean puk1write(String mPuk1);

    boolean puk2write(String mPuk2);

    String puk1read();

    String puk2read();

    void setBackKey(boolean enabled);

    void setHomeKey(boolean enabled);

    void setRecentKey(boolean enabled);

    void disablePowerKey(boolean enabled);

    void setApkSignature(boolean enabled);

    void setDevelopment(boolean enabled);

    void setDebugMode(boolean enabled);

    void setLauncher(String packageName, String launcherActivityName);

    void setAppsPwd(String pwd);

    void setStoragePwd(String pwd);

    void setResetPwd(String pwd);

    void setDevelopmentPwd(String pwd);

    void setSecurityPwd(String pwd);

    void setSettingsPwd(String pwd);

    void setScreenTimeOut(int brightness);

    int getScreenTimeOut();

    String getIMSI();

    int setAPN(in ApnConfig apn);

    ApnConfig getAPN(int id);

    List<ApnConfig> getSimAPN();

    void setAirplaneMode(boolean enabled);

    void setDateTime(String dateTime, String dateFormat);

    void setSystemTime(int year, int month, int day, int hour, int minute, int second);

    int deleteAPN(int id);

    String getRomVersion();

    String getStorageSize();

    String getUsedStorage();

    String getBrand();

    String getManufacture();

    String getAndroidVersion();

    String getSerialNo();

    String getModel();

    AppInfo queryAppInfo(String pkgName);

    void setAppEnable(String packageName, boolean enabled);

    void setPkgNotUninstallable(String pkgName, boolean notUninstallable);

    String getCpu();

    String getCpuPercent();

    String getMemory();

    String getMemoryPercent();

    String getFirmwareVersion();

    String getHardwareVersion();

    String getSystemProperty(String propName);

    String getIMEI();

    void sleep();

    String getMac();

    String getIntensity();

    void cleanAppCache(String pkgName);

    void settingsPwd(String pwd);

    void enabledBluetooth(boolean enabled);

    long getAppSize(String pkg);

    void allowInstall(boolean allow);

    void factoryReset();

    void autoRotations(boolean auto);

    void lockLandscape();

    OtaResult getOTAResult();

    String getICCID();

    String getIMEIs(int slotIndex);

    String getIMSIs(int subId);

    String getICCIDs(int subId);

    int getSubId(int slotIndex);

    int getDataSimSlotIndex();

    void setFontSize(float fontSize);

    float getFontSize();

    int getEthUseDhcpOrStaticIp();

    String getEthIp();

    String getEthGateway();

    float getBatteryHealth();

    String getSn2();

    boolean setSn2(String sn2);

    String getWifiMac();

    boolean setWifiMac(String wifiMac);

    String getBtMac();

    boolean setBtMac(String btMac);

    void disablePowerLongPressKey(boolean enabled);

    void setBootStartPkgName(String pkgName);

    String[] getSdcardsPath();

    String getOtgPath();

    void setAutoRotation(boolean enabled);

    void ctrlClearCache(String pkgName, boolean enable);

    void ctrlClearStorage(String pkgName, boolean enable);

    void showAppIcon(String pkgName, boolean show);

    void uninstallThroughDragAndDrop(String pkgName, boolean enabled);

    int getApiAvailable(String apiName);

    void setPortIndex(boolean enabled);

    int getNavigationBar();

    int getStatusBar();

    String getScannerInfo();

    String getCoinBatteryVoltage();

    void setUsbCdc(boolean enabled);

    void hideQsEdit(boolean isHidden);

    void hideHotspot(boolean isHidden);

    void setStatusBarDisplay(boolean display);

    int getStatusBarDisplay();

    int autoReboot(boolean enabled);

    int setAutoRebootTime(long time_h);
}
