package com.custom.mdm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.media.AudioManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private Context mContext;
    private int mApnID;

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.shutdown:
                    // Util.shutdown(mContext);
                    CustomAPI.shutdown();
                    break;
                case R.id.reboot:
                    // Util.reboot(mContext);
                    CustomAPI.reboot();
                    break;
                case R.id.updateSystem:
                    String updatePath = getEditText(R.id.editUpdatePath, "/sdcard/Download/update.zip");
                    // Util.updateSystem(mContext, updatePath);
                    CustomAPI.systemUpdate(updatePath);
                    break;
                case R.id.installPkg:
                    String packagePath = getEditText(R.id.editPackagePath, "/sdcard/Download/package.apk");
                    // Util.install(mContext, packagePath);
                    CustomAPI.install(packagePath);
                    break;
                case R.id.deleteApk:
                    String packageName = getEditText(R.id.editPackageName, "com.custom.test");
                    // Util.uninstall(mContext, packageName);
                    CustomAPI.uninstall(packageName);
                    break;
                case R.id.vk_enable:
                    // Util.setVolumeKey(true);
                    CustomAPI.setVolumeKey(true);
                    break;
                case R.id.vk_disable:
                    // Util.setVolumeKey(false);
                    CustomAPI.setVolumeKey(false);
                    break;
                case R.id.airplane_mode_enable:
                    // Util.setAirplaneMode(mContext, true);
                    CustomAPI.setAirplaneMode(true);
                    break;
                case R.id.airplane_mode_disable:
                    // Util.setAirplaneMode(mContext, false);
                    CustomAPI.setAirplaneMode(false);
                    break;
                case R.id.wifi_enable:
                    // Util.setWifiEnabled(mContext, true);
                    CustomAPI.setWifiEnabled(true);
                    break;
                case R.id.wifi_disable:
                    // Util.setWifiEnabled(mContext, false);
                    CustomAPI.setWifiEnabled(false);
                    break;
                case R.id.gps_enable:
                    //Util.setGPSEnabled(mContext, true);
                    CustomAPI.setLocationEnabled(true);
                    break;
                case R.id.gps_disable:
                    //Util.setGPSEnabled(mContext, false);
                    CustomAPI.setLocationEnabled(false);
                    break;
                case R.id.sim_enable:
                    // Util.setSimDataEnabled(mContext, true);
                    CustomAPI.setSimDataEnabled(true);
                    break;
                case R.id.sim_disable:
                    // Util.setSimDataEnabled(mContext, false);
                    CustomAPI.setSimDataEnabled(false);
                    break;
                case R.id.bt_enable:
                    // Util.setBTEnabled(true);
                    CustomAPI.setBluetoothEnabled(true);
                    break;
                case R.id.bt_disable:
                    // Util.setBTEnabled(false);
                    CustomAPI.setBluetoothEnabled(false);
                    break;
                case R.id.set_scr_br: {
                    int brightness = Integer.parseInt(getEditText(R.id.edit_scr_br, "100"));
                    // Util.setScreenBrightness(mContext, brightness);
                    CustomAPI.setScreenBrightness(brightness);
                    break;
                }
                case R.id.get_scr_br: {
                    // int brightness = Util.getScreenBrightness(mContext);
                    int brightness = CustomAPI.getScreenBrightness();
                    Toast.makeText(mContext, "Screen brightness: " + brightness, Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.set_scr_time_out: {
                    int ms = Integer.parseInt(getEditText(R.id.edit_scr_time_out, "300000"));
                    // Util.setScreenTimeOut(mContext, ms);
                    CustomAPI.setScreenTimeOut(ms);
                    break;
                }
                case R.id.get_scr_time_out: {
                    // int ms = Util.getScreenTimeOut(mContext);
                    int ms = CustomAPI.getScreenTimeOut();
                    Toast.makeText(mContext, "Screen time out(ms) = " + ms, Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.set_volume: {
                    int volume = Integer.parseInt(getEditText(R.id.edit_volume, "10"));
                    // Util.setVolume(mContext, AudioManager.STREAM_RING, volume, 0);
                    CustomAPI.setVolume(AudioManager.STREAM_RING, volume, 0);
                    break;
                }
                case R.id.get_volume: {
                    // int volume = Util.getVolume(mContext, AudioManager.STREAM_RING);
                    int volume = CustomAPI.getVolume(AudioManager.STREAM_RING);
                    Toast.makeText(mContext, "Volume: " + volume, Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.set_sys_lan: {
                    String[] strs = getEditText(R.id.edit_sys_lan, "en").split("-");
                    String language = strs.length > 0 ? strs[0] : "en";
                    String country = strs.length > 1 ? strs[1] : "";
                    // Util.setLanguage(new Locale(language, country));
                    CustomAPI.setLanguage(new Locale(language, country));
                    break;
                }
                case R.id.get_sys_lan: {
                    // String language = Util.getLanguage();
                    String language = CustomAPI.getLanguage();
                    Toast.makeText(mContext, "Language: " + language, Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.set_time_zone: {
                    String timeZone = getEditText(R.id.edit_time_zone, "Asia/Tehran");
                    // Util.setTimeZone(mContext, timeZone);
                    CustomAPI.setTimeZone(timeZone);
                    break;
                }
                case R.id.get_time_zone: {
                    // String timeZone = Util.getTimeZone();
                    String timeZone = CustomAPI.getTimeZone();
                    Toast.makeText(mContext, "Time zone: " + timeZone, Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.set_time_str: {
                    String dateTime = getEditText(R.id.edit_time, "20230726140000");
                    // Util.setDateTime(mContext, "20230726140000", null); //2023.07.26 14:00:00
                    // Util.setDateTime(mContext, "AUTO_TIME", null); //Use network-provided time
                    if(dateTime.equals("auto time")) CustomAPI.setDateTime("AUTO_TIME", null); //Use network-provided time
                    else CustomAPI.setDateTime(dateTime, "yyyyMMddHHmmss"); //2023.07.26 14:00:00
                    break;
                }
                case R.id.set_time: {
                    //Util.setSystemTime(mContext, 2023, 7, 26, 14, 0, 0); //2023.07.26 14:00:00
                    CustomAPI.setSystemTime(2023, 7, 26, 14, 0, 0); //2023.07.26 14:00:00
                    break;
                }
                case R.id.auto_time: {
                    //Util.setSystemTime(mContext, 0, 0, 0, 0, 0, 0); //Use network-provided time
                    CustomAPI.setSystemTime(0, 0, 0, 0, 0, 0); //Use network-provided time
                    break;
                }
                case R.id.get_ip: {
                    // String wifiIp = Util.getWifiIP(mContext);
                    String wifiIp = CustomAPI.getWifiIP();
                    Toast.makeText(mContext, "Wifi ip: " + wifiIp, Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.get_imsi: {
                    // String imsi = Util.getIMSI(mContext);
                    String imsi = CustomAPI.getIMSI();
                    Toast.makeText(mContext, "IMSI: " + imsi, Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.get_imei: {
                    // String imei = Util2.getIMEI(mContext);
                    String imei = CustomAPI.getIMEI();
                    Toast.makeText(mContext, "IMEI: " + imei, Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.get_iccid: {
                    // String iccid = Util2.getICCID(mContext);
                    String iccid = CustomAPI.getICCID();
                    Toast.makeText(mContext, "ICCID: " + iccid, Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.static_enable:
                    // Util.setWifiStatic(mContext, "192.168.73.234", "192.168.73.1", 24, "8.8.8.4");
                    CustomAPI.setWifiStatic("192.168.73.234", "192.168.73.1", 24, "8.8.8.4");
                    break;
                case R.id.dhcp_enable:
                    // Util.setWifiDHCP(mContext);
                    CustomAPI.setWifiDHCP();
                    break;
                case R.id.sta_disable:
                    // Util.setStatusBar(mContext, false);
                    Toast.makeText(mContext, "StatusBar: " + CustomAPI.setStatusBar(false), Toast.LENGTH_SHORT).show();
                    //CustomAPI.setStatusBar(false);
                    break;
                case R.id.sta_enable:
                    // Util.setStatusBar(mContext, true);
                    Toast.makeText(mContext, "StatusBar: " + CustomAPI.setStatusBar(true), Toast.LENGTH_SHORT).show();
                    //CustomAPI.setStatusBar(true);
                    break;
                case R.id.sta_hide:
                    // Util.setStatusBarDisplay(mContext, false);
                    Toast.makeText(mContext, "StatusBarDisplay: " + CustomAPI.setStatusBarDisplay(false), Toast.LENGTH_SHORT).show();
                    //CustomAPI.setStatusBarDisplay(false);
                    break;
                case R.id.sta_display:
                    // Util.setStatusBarDisplay(mContext, true);
                    Toast.makeText(mContext, "StatusBarDisplay: " + CustomAPI.setStatusBarDisplay(true), Toast.LENGTH_SHORT).show();
                    //CustomAPI.setStatusBarDisplay(true);
                    break;
                case R.id.nav_disable:
                    // Util.setNavigationBar(mContext, false);
                    Toast.makeText(mContext, "NavigationBar: " + CustomAPI.setNavigationBar(false), Toast.LENGTH_SHORT).show();
                    //CustomAPI.setNavigationBar(false);
                    break;
                case R.id.nav_enable:
                    // Util.setNavigationBar(mContext, true);
                    Toast.makeText(mContext, "NavigationBar: " + CustomAPI.setNavigationBar(true), Toast.LENGTH_SHORT).show();
                    //CustomAPI.setNavigationBar(true);
                    break;
                case R.id.puk1read:
                    String puk1 = CustomAPI.puk1read();
                    Log.d(TAG, "puk1: " + puk1);
                    break;
                case R.id.puk1write:
                    String mPuk1 = "308202ef308201d7a003020102020900c2bde1ed64775a51300d06092a864886f70d01010b0500300d310b30090603550406130255533020170d3233303132343035353934345a180f32303530303631313035353934345a300d310b300906035504061302555330820122300d06092a864886f70d01010105000382010f003082010a0282010100dcdac13e4218b42da13c6f98a1e74bc2db4bae2c31026d547ce888e6746a8d2fb1a9da66cd23d67794e3e7d8a07022248ba5a8ccec07c49e98dc9a1280ef814501784ce625ee3ec5975d3d05d6d5ce2f0c4cfe4de7fbcf11fc61fcfa8e29cc5dbd6147fd5b65bf22590c75919055b90c97ddaf18948148b97c798fab4669e00fb74cd3cea2af972779c1273a8197a3c9a51eb929bff39e228cd66aa19e2989cdda64d5643c1da464d4d5f758a15a14b89cc496e5ff22c3489934b83dbb0e1cac15409f6c5bb0d32d870f52f3c5db1396620240269c4beca7deee423ccfc587a8ec54096ef60b9cca00ba94809cff704f3c70460d45b3cd61c8e55e732daad30f0203010001a350304e301d0603551d0e041604145d5a1ab878b4df89eef5f9c7a70ea3b8936aab5d301f0603551d230418301680145d5a1ab878b4df89eef5f9c7a70ea3b8936aab5d300c0603551d13040530030101ff300d06092a864886f70d01010b0500038201010008f712bac4238c8daf8395eb04858bccc22ecc5620b14568b379d434019c55f4bd6fb425a6867787ba259b4735fee41849d6f0faa95922ac8c10c9b57625ee95e07124275d67224fd19d1df1e05f2ed22b3f3d505142a8a447224e75abe927f13676b32e7d31485c4a792611cc84b769edaf1932ca7cb87a0ed514e0200f60c7601ef02325eff29f09660764c5853c69c2e2717109c532fcb6c3eb20117735f51f4a1e00fd4dd0d86050f4118307717facb43c00ce632563f46bbfa63f886917a608119f04a6f8ecfcdc8c0d3e3ce8b00d1a02a57c87d4dec185c68e22779cc41b7f898db7672b68205e948ad953f48c62789be5d7a0eb40ff2fc0333af15b44";
                    CustomAPI.puk1write(mPuk1);
                    break;
                case R.id.puk2read:
                    String puk2 = CustomAPI.puk2read();
                    Log.d(TAG, "puk2: " + puk2);
                    break;
                case R.id.puk2write:
                    String mPuk2 = "308202ef308201d7a003020102020900c2bde1ed64775a51300d06092a864886f70d01010b0500300d310b30090603550406130255533020170d3233303132343035353934345a180f32303530303631313035353934345a300d310b300906035504061302555330820122300d06092a864886f70d01010105000382010f003082010a0282010100dcdac13e4218b42da13c6f98a1e74bc2db4bae2c31026d547ce888e6746a8d2fb1a9da66cd23d67794e3e7d8a07022248ba5a8ccec07c49e98dc9a1280ef814501784ce625ee3ec5975d3d05d6d5ce2f0c4cfe4de7fbcf11fc61fcfa8e29cc5dbd6147fd5b65bf22590c75919055b90c97ddaf18948148b97c798fab4669e00fb74cd3cea2af972779c1273a8197a3c9a51eb929bff39e228cd66aa19e2989cdda64d5643c1da464d4d5f758a15a14b89cc496e5ff22c3489934b83dbb0e1cac15409f6c5bb0d32d870f52f3c5db1396620240269c4beca7deee423ccfc587a8ec54096ef60b9cca00ba94809cff704f3c70460d45b3cd61c8e55e732daad30f0203010001a350304e301d0603551d0e041604145d5a1ab878b4df89eef5f9c7a70ea3b8936aab5d301f0603551d230418301680145d5a1ab878b4df89eef5f9c7a70ea3b8936aab5d300c0603551d13040530030101ff300d06092a864886f70d01010b0500038201010008f712bac4238c8daf8395eb04858bccc22ecc5620b14568b379d434019c55f4bd6fb425a6867787ba259b4735fee41849d6f0faa95922ac8c10c9b57625ee95e07124275d67224fd19d1df1e05f2ed22b3f3d505142a8a447224e75abe927f13676b32e7d31485c4a792611cc84b769edaf1932ca7cb87a0ed514e0200f60c7601ef02325eff29f09660764c5853c69c2e2717109c532fcb6c3eb20117735f51f4a1e00fd4dd0d86050f4118307717facb43c00ce632563f46bbfa63f886917a608119f04a6f8ecfcdc8c0d3e3ce8b00d1a02a57c87d4dec185c68e22779cc41b7f898db7672b68205e948ad953f48c62789be5d7a0eb40ff2fc0333af15b33";
                    CustomAPI.puk2write(mPuk2);
                    break;
                case R.id.back_enable:
                    CustomAPI.setBackKey(true);
                    break;
                case R.id.back_disable:
                    CustomAPI.setBackKey(false);
                    break;
                case R.id.home_enable:
                    CustomAPI.setHomeKey(true);
                    break;
                case R.id.home_disable:
                    CustomAPI.setHomeKey(false);
                    break;
                case R.id.recent_enable:
                    CustomAPI.setRecentKey(true);
                    break;
                case R.id.recent_disable:
                    CustomAPI.setRecentKey(false);
                    break;
                case R.id.power_disable:
                    CustomAPI.disablePowerKey(true);
                    break;
                case R.id.power_enable:
                    CustomAPI.disablePowerKey(false);
                    break;
                case R.id.power_long_press_disable:
                    CustomAPI.disablePowerLongPressKey(true);
                    break;
                case R.id.power_long_press_enable:
                    CustomAPI.disablePowerLongPressKey(false);
                    break;
                case R.id.signature_enable:
                    CustomAPI.setApkSignature(true);
                    break;
                case R.id.signature_disable:
                    CustomAPI.setApkSignature(false);
                    break;
                case R.id.development_enable:
                    CustomAPI.setDevelopment(true);
                    break;
                case R.id.development_disable:
                    CustomAPI.setDevelopment(false);
                    break;
                case R.id.debug_mode_enable:
                    CustomAPI.setDebugMode(true);
                    break;
                case R.id.debug_mode_disable:
                    CustomAPI.setDebugMode(false);
                    break;
                case R.id.set_launcher:
                    CustomAPI.setLauncher("com.anddoes.launcher", "com.anddoes.launcher.Launcher");
                    break;
                case R.id.reset_launcher:
                    CustomAPI.setLauncher("reset", null);
                    break;
                case R.id.set_apps_pwd:
                    CustomAPI.setAppsPwd("t123456");
                    break;
                case R.id.reset_apps_pwd:
                    CustomAPI.setAppsPwd("reset");
                    break;
                case R.id.no_apps_pwd:
                    CustomAPI.setAppsPwd("nopwd");
                    break;
                case R.id.set_stg_pwd:
                    CustomAPI.setStoragePwd("t123456");
                    break;
                case R.id.reset_stg_pwd:
                    CustomAPI.setStoragePwd("reset");
                    break;
                case R.id.no_stg_pwd:
                    CustomAPI.setStoragePwd("nopwd");
                    break;
                case R.id.set_rst_pwd:
                    CustomAPI.setResetPwd("t123456");
                    break;
                case R.id.reset_rst_pwd:
                    CustomAPI.setResetPwd("reset");
                    break;
                case R.id.no_rst_pwd:
                    CustomAPI.setResetPwd("nopwd");
                    break;
                case R.id.set_dev_pwd:
                    CustomAPI.setDevelopmentPwd("t123456");
                    break;
                case R.id.reset_dev_pwd:
                    CustomAPI.setDevelopmentPwd("reset");
                    break;
                case R.id.no_dev_pwd:
                    CustomAPI.setDevelopmentPwd("nopwd");
                    break;
                case R.id.set_sec_pwd:
                    CustomAPI.setSecurityPwd("t123456");
                    break;
                case R.id.reset_sec_pwd:
                    CustomAPI.setSecurityPwd("reset");
                    break;
                case R.id.no_sec_pwd:
                    CustomAPI.setSecurityPwd("nopwd");
                    break;
                case R.id.set_sts_pwd:
                    CustomAPI.setSettingsPwd("t123456");
                    break;
                case R.id.reset_sts_pwd:
                    CustomAPI.setSettingsPwd("reset");
                    break;
                case R.id.no_sts_pwd:
                    CustomAPI.setSettingsPwd("nopwd");
                    break;
                case R.id.get_status_bar:
                    Toast.makeText(mContext, "StatusBar: " + CustomAPI.getStatusBar(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.get_navigation_bar:
                    Toast.makeText(mContext, "NavigationBar: " + CustomAPI.getNavigationBar(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.set_apn: {
                    ApnConfig apnConfig = new ApnConfig("XYZ", "3gnet", "460", "01", "default,supl,xcap");
                    int apn_id = CustomAPI.setAPN(apnConfig);
                    mApnID = apn_id;
                    if (apn_id != -1) Log.d(TAG, "Set APN successfully, apn id = " + apn_id);
                    break;
                }
                case R.id.get_apn: {
                    int id = 2172;
                    ApnConfig apnConfig = CustomAPI.getAPN(id);
                    Log.d(TAG, "Get APN by id(" + id + ") " + apnConfig);
                    List<ApnConfig> apns = CustomAPI.getSimAPN();
                    for (ApnConfig apn : apns) {
                        Log.d(TAG, "Get Current Sim APN " + apn);
                    }
                    break;
                }
                case R.id.delete_apn: {
                    int delete = CustomAPI.deleteAPN(mApnID);
                    Log.d(TAG, "Delete APN " + (delete == 1 ? "successfully":"failed") + ", apn id = " + mApnID);
                    break;
                }
                case R.id.get_up_time: {
                    String upTime = CustomAPI.getUpTime();
                    Toast.makeText(mContext, "Up time: " + upTime, Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.alatin_activity:
                    mContext.startActivity(new Intent(mContext, AlatinActivity.class));
                    break;
                case R.id.boot_start:
                    String bootstart_pkgname = getEditText(R.id.edit_bootstart_pkgname, "null");
                    CustomAPI.setBootStartPkgName(bootstart_pkgname);
                    break;
                case R.id.boot_start_no:
                    CustomAPI.setBootStartPkgName("null");
                    break;
                case R.id.uninstallable: {
                    String uninstallable_pkgname = getEditText(R.id.edit_uninstallable_pkgname, "null");
                    CustomAPI.setPkgNotUninstallable(uninstallable_pkgname, false);
                    break;
                }
                case R.id.uninstallable_no: {
                    String uninstallable_pkgname = getEditText(R.id.edit_uninstallable_pkgname, "null");
                    CustomAPI.setPkgNotUninstallable(uninstallable_pkgname, true);
                    break;
                }
                case R.id.clear_cache: {
                    String clear_cache_pkgname = getEditText(R.id.edit_clear_cache_pkgname, "null");
                    CustomAPI.ctrlClearCache(clear_cache_pkgname, true);
                    break;
                }
                case R.id.clear_cache_no: {
                    String clear_cache_pkgname = getEditText(R.id.edit_clear_cache_pkgname, "null");
                    CustomAPI.ctrlClearCache(clear_cache_pkgname, false);
                    break;
                }
                case R.id.clear_storage: {
                    String clear_storage_pkgname = getEditText(R.id.edit_clear_storage_pkgname, "null");
                    CustomAPI.ctrlClearStorage(clear_storage_pkgname, true);
                    break;
                }
                case R.id.clear_storage_no: {
                    String clear_storage_pkgname = getEditText(R.id.edit_clear_storage_pkgname, "null");
                    CustomAPI.ctrlClearStorage(clear_storage_pkgname, false);
                    break;
                }
                case R.id.auto_rotation_enabled: {
                    CustomAPI.setAutoRotation(true);
                    break;
                }
                case R.id.auto_rotation_disable: {
                    CustomAPI.setAutoRotation(false);
                    break;
                }
                case R.id.usb_cdc_enable:
                    CustomAPI.setUsbCdc(true);
                    break;
                case R.id.usb_cdc_disable:
                    CustomAPI.setUsbCdc(false);
                    break;
                case R.id.get_scanner_info:
                    String scanner = CustomAPI.getScannerInfo();
                    Toast.makeText(mContext, "Scanner: " + scanner, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.get_coin_battery:
                    String voltage = CustomAPI.getCoinBatteryVoltage();
                    Toast.makeText(mContext, "Coin Battery: " + voltage, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.display_qs_edit:
                    CustomAPI.hideQsEdit(false);
                    break;
                case R.id.hide_qs_edit:
                    CustomAPI.hideQsEdit(true);
                    break;
                case R.id.display_hotspot:
                    CustomAPI.hideHotspot(false);
                    break;
                case R.id.hide_hotspot:
                    CustomAPI.hideHotspot(true);
                    break;
                case R.id.auto_reboot_enable:
                    CustomAPI.autoReboot(true);
                    break;
                case R.id.auto_reboot_disable:
                    CustomAPI.autoReboot(false);
                    break;
                case R.id.auto_reboot_time:
                    int time = Integer.parseInt(getEditText(R.id.edit_auto_reboot_time, "24"));
                    CustomAPI.setAutoRebootTime(time);
                    break;
            }
        }
    };
    String getEditText(int id, String defaultText) {
        String text = getEditText(id);
        if(text == null || "".equals(text)) {
            text = defaultText;
        }
        return text;
    }
    String getEditText(int id) {
        EditText editText = (EditText)findViewById(id);
        return editText.getText().toString();
    }

    protected void initView() {
        setButtonClick(R.id.shutdown);
        setButtonClick(R.id.reboot);

        setButtonClick(R.id.updateSystem);

        setButtonClick(R.id.installPkg);

        setButtonClick(R.id.deleteApk);

        setButtonClick(R.id.vk_enable);
        setButtonClick(R.id.vk_disable);

        setButtonClick(R.id.airplane_mode_enable);
        setButtonClick(R.id.airplane_mode_disable);

        setButtonClick(R.id.wifi_enable);
        setButtonClick(R.id.wifi_disable);

        setButtonClick(R.id.gps_enable);
        setButtonClick(R.id.gps_disable);

        setButtonClick(R.id.sim_enable);
        setButtonClick(R.id.sim_disable);

        setButtonClick(R.id.bt_enable);
        setButtonClick(R.id.bt_disable);

        setButtonClick(R.id.set_scr_br);
        setButtonClick(R.id.get_scr_br);

        setButtonClick(R.id.set_scr_time_out);
        setButtonClick(R.id.get_scr_time_out);

        setButtonClick(R.id.set_volume);
        setButtonClick(R.id.get_volume);

        setButtonClick(R.id.set_sys_lan);
        setButtonClick(R.id.get_sys_lan);

        setButtonClick(R.id.set_time_zone);
        setButtonClick(R.id.get_time_zone);

        setButtonClick(R.id.set_time_str);
        setButtonClick(R.id.set_time);
        setButtonClick(R.id.auto_time);

        setButtonClick(R.id.dhcp_enable);
        setButtonClick(R.id.static_enable);
        setButtonClick(R.id.get_ip);

        setButtonClick(R.id.sta_enable);
        setButtonClick(R.id.sta_disable);

        setButtonClick(R.id.sta_display);
        setButtonClick(R.id.sta_hide);

        setButtonClick(R.id.nav_enable);
        setButtonClick(R.id.nav_disable);

        setButtonClick(R.id.puk1read);
        setButtonClick(R.id.puk1write);
        setButtonClick(R.id.puk2read);
        setButtonClick(R.id.puk2write);

        setButtonClick(R.id.back_enable);
        setButtonClick(R.id.back_disable);

        setButtonClick(R.id.home_enable);
        setButtonClick(R.id.home_disable);

        setButtonClick(R.id.recent_enable);
        setButtonClick(R.id.recent_disable);

        setButtonClick(R.id.power_disable);
        setButtonClick(R.id.power_enable);

        setButtonClick(R.id.power_long_press_disable);
        setButtonClick(R.id.power_long_press_enable);

        setButtonClick(R.id.signature_enable);
        setButtonClick(R.id.signature_disable);

        setButtonClick(R.id.development_enable);
        setButtonClick(R.id.development_disable);

        setButtonClick(R.id.debug_mode_enable);
        setButtonClick(R.id.debug_mode_disable);

        setButtonClick(R.id.set_launcher);
        setButtonClick(R.id.reset_launcher);

        setButtonClick(R.id.set_apps_pwd);
        setButtonClick(R.id.reset_apps_pwd);
        setButtonClick(R.id.no_apps_pwd);

        setButtonClick(R.id.set_stg_pwd);
        setButtonClick(R.id.reset_stg_pwd);
        setButtonClick(R.id.no_stg_pwd);

        setButtonClick(R.id.set_rst_pwd);
        setButtonClick(R.id.reset_rst_pwd);
        setButtonClick(R.id.no_rst_pwd);

        setButtonClick(R.id.set_dev_pwd);
        setButtonClick(R.id.reset_dev_pwd);
        setButtonClick(R.id.no_dev_pwd);

        setButtonClick(R.id.set_sec_pwd);
        setButtonClick(R.id.reset_sec_pwd);
        setButtonClick(R.id.no_sec_pwd);

        setButtonClick(R.id.set_sts_pwd);
        setButtonClick(R.id.reset_sts_pwd);
        setButtonClick(R.id.no_sts_pwd);

        setButtonClick(R.id.get_imsi);
        setButtonClick(R.id.get_imei);
        setButtonClick(R.id.get_iccid);

        setButtonClick(R.id.set_apn);
        setButtonClick(R.id.get_apn);
        setButtonClick(R.id.delete_apn);

        setButtonClick(R.id.get_up_time);
        setButtonClick(R.id.alatin_activity);

        setButtonClick(R.id.boot_start);
        setButtonClick(R.id.boot_start_no);

        setButtonClick(R.id.uninstallable);
        setButtonClick(R.id.uninstallable_no);

        setButtonClick(R.id.clear_cache);
        setButtonClick(R.id.clear_cache_no);

        setButtonClick(R.id.clear_storage);
        setButtonClick(R.id.clear_storage_no);

        setButtonClick(R.id.auto_rotation_enabled);
        setButtonClick(R.id.auto_rotation_disable);

        setButtonClick(R.id.usb_cdc_enable);
        setButtonClick(R.id.usb_cdc_disable);

        setButtonClick(R.id.get_status_bar);
        setButtonClick(R.id.get_navigation_bar);

        setButtonClick(R.id.get_scanner_info);
        setButtonClick(R.id.get_coin_battery);

        setButtonClick(R.id.display_qs_edit);
        setButtonClick(R.id.hide_qs_edit);

        setButtonClick(R.id.display_hotspot);
        setButtonClick(R.id.hide_hotspot);

        setButtonClick(R.id.auto_reboot_disable);
        setButtonClick(R.id.auto_reboot_enable);
        setButtonClick(R.id.auto_reboot_time);
    }
    void setButtonClick(int id){
        Button button = (Button)findViewById(id);
        button.setOnClickListener(mClickListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(TAG, "onCreate ..... ");

        mContext = MainActivity.this;
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart ..... ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        CustomAPI.init(mContext);
        Log.v(TAG, "onResume ..... ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        CustomAPI.release();
        Log.v(TAG, "onPause ..... ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop ..... ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy ..... ");
    }
}
