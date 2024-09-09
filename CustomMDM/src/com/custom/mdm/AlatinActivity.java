package com.custom.mdm;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.custom.mdm.CustomAPI;
import com.custom.mdm.InstallReceiver.InstallListener;
import com.custom.mdm.InstallReceiver.UninstallListener;

import java.util.*;

import com.custom.mdm.OtaVerityReceiver;
public class AlatinActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "AlatinActivity";

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_alatin);
        Log.v(TAG, "onCreate ..... ");

        mContext = AlatinActivity.this;
        initButton();
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

    private void initButton() {
        setButtonOnclick(R.id.btn_get_rom_version);
        setButtonOnclick(R.id.btn_get_manufacture);
        setButtonOnclick(R.id.btn_get_storage_size);
        setButtonOnclick(R.id.btn_get_used_storage);
        setButtonOnclick(R.id.btn_get_memory);
        setButtonOnclick(R.id.btn_get_memory_precent);
        setButtonOnclick(R.id.btn_get_brand);
        setButtonOnclick(R.id.btn_get_sn);
        setButtonOnclick(R.id.btn_get_model);
        setButtonOnclick(R.id.btn_shutdown);
        setButtonOnclick(R.id.btn_reboot);
        setButtonOnclick(R.id.btn_sleep);
        setButtonOnclick(R.id.btn_android_version);
        setButtonOnclick(R.id.btn_get_mac);
        setButtonOnclick(R.id.btn_get_firmware);
        setButtonOnclick(R.id.btn_get_hardware);
        setButtonOnclick(R.id.btn_get_imei);
        setButtonOnclick(R.id.btn_get_imei2);
        setButtonOnclick(R.id.btn_get_intensity);
        setButtonOnclick(R.id.btn_get_cpu);
        setButtonOnclick(R.id.btn_get_cpu_percent);
        setButtonOnclick(R.id.btn_factory_reset);
        setButtonOnclick(R.id.btn_get_app_info);
        setButtonOnclick(R.id.btn_get_app_size);
        setButtonOnclick(R.id.btn_clear_cache);
        setButtonOnclick(R.id.btn_set_app_enable);
        setButtonOnclick(R.id.btn_set_app_disable);
        setButtonOnclick(R.id.btn_uninstallable);
        setButtonOnclick(R.id.btn_not_uninstallable);
        setButtonOnclick(R.id.btn_uninstall);
        setButtonOnclick(R.id.btn_install);
        setButtonOnclick(R.id.btn_update_system);
        setButtonOnclick(R.id.btn_get_ota_result);
        setButtonOnclick(R.id.btn_get_prop);
        setButtonOnclick(R.id.btn_set_settings_pwd);
        setButtonOnclick(R.id.btn_enable_sim_data);
        setButtonOnclick(R.id.btn_disable_sim_data);
        setButtonOnclick(R.id.btn_enable_bt);
        setButtonOnclick(R.id.btn_disable_bt);
        setButtonOnclick(R.id.btn_allow_install);
        setButtonOnclick(R.id.btn_limit_install);
        setButtonOnclick(R.id.btn_auto_rotations);
        setButtonOnclick(R.id.btn_lock_rotations);
        setButtonOnclick(R.id.btn_lock_landspace);
        setButtonOnclick(R.id.btn_get_imeis);
        setButtonOnclick(R.id.btn_get_imsis);
        setButtonOnclick(R.id.btn_get_iccids);
        setButtonOnclick(R.id.btn_get_subid);
        setButtonOnclick(R.id.btn_get_dataSimSlotIndex);
        setButtonOnclick(R.id.btn_set_font_size);
        setButtonOnclick(R.id.btn_get_font_size);
        setButtonOnclick(R.id.btn_get_battery_health);
        setButtonOnclick(R.id.btn_get_version_name);
        setButtonOnclick(R.id.btn_set_sn2);
        setButtonOnclick(R.id.btn_get_sn2);
        setButtonOnclick(R.id.btn_set_wifi_mac);
        setButtonOnclick(R.id.btn_get_wifi_mac);
        setButtonOnclick(R.id.btn_set_bt_mac);
        setButtonOnclick(R.id.btn_get_bt_mac);
        setButtonOnclick(R.id.btn_get_otg_path);
        setButtonOnclick(R.id.btn_get_sdcards_path);
        setButtonOnclick(R.id.btn_enable_clear_cache);
        setButtonOnclick(R.id.btn_disable_clear_cache);
        setButtonOnclick(R.id.btn_enable_clear_storage);
        setButtonOnclick(R.id.btn_disable_clear_storage);
        setButtonOnclick(R.id.btn_show_icon);
        setButtonOnclick(R.id.btn_hide_icon);
        setButtonOnclick(R.id.btn_uninstall_through_drag_and_drop);
        setButtonOnclick(R.id.btn_not_uninstall_through_drag_and_drop);
        setButtonOnclick(R.id.btn_get_api);
    }

    private String getEditText(int id) {
        EditText et = (EditText) findViewById(id);
        return et.getText().toString();
    }

    private void setButtonOnclick(int id) {
        Button btn = (Button) findViewById(id);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get_rom_version:
                String romVersion = CustomAPI.getRomVersion();
                Toast.makeText(mContext, "romVersion: " + romVersion, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_manufacture:
                String manufacture = CustomAPI.getManufacture();
                Toast.makeText(mContext, "manufacture: " + manufacture, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_storage_size:
                String storageSize = CustomAPI.getStorageSize();
                Toast.makeText(mContext, "storageSize: " + storageSize, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_used_storage:
                String usedStorage = CustomAPI.getUsedStorage();
                Toast.makeText(mContext, "usedStorage: " + usedStorage, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_memory:
                String memory = CustomAPI.getMemory();
                Toast.makeText(mContext, "memory: " + memory, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_memory_precent:
                String memoryPrecent = CustomAPI.getMemoryPercent();
                Toast.makeText(mContext, "memoryPrecent" + memoryPrecent, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_brand:
                String brand = CustomAPI.getBrand();
                Toast.makeText(mContext, "brand: " + brand, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_sn:
                String sn = CustomAPI.getSerialNo();
                Toast.makeText(mContext, "SN: " + sn, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_model:
                String model = CustomAPI.getModel();
                Toast.makeText(mContext, "model: " + model, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_shutdown:
                CustomAPI.shutdown();
                break;
            case R.id.btn_reboot:
                CustomAPI.reboot();
                break;
            case R.id.btn_sleep:
                CustomAPI.sleep();
                break;
            case R.id.btn_android_version:
                String androidVersion = CustomAPI.getAndroidVersion();
                Toast.makeText(mContext, "androidVersion: " + androidVersion, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_mac:
                String mac = CustomAPI.getMac();
                Toast.makeText(mContext, "mac: " + mac, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_firmware:
                String firmware = CustomAPI.getFirmwareVersion();
                Toast.makeText(mContext, "firmwareVersion: " + firmware, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_hardware:
                String hardware = CustomAPI.getHardwareVersion();
                Toast.makeText(mContext, "hardwareVersion: " + hardware, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_imei:
                String imei = CustomAPI.getIMEI(0);
                Toast.makeText(mContext, "imei_sim1: " + imei, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_imei2:
                String imei2 = CustomAPI.getIMEI(1);
                Toast.makeText(mContext, "imei_sim2: " + imei2, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_intensity:
                String intensity = CustomAPI.getIntensity();
                Toast.makeText(mContext, "intensity: " + intensity, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_cpu:
                String cpu = CustomAPI.getCpu();
                Toast.makeText(mContext, "cpu: " + cpu, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_cpu_percent:
                String cpuPercent = CustomAPI.getCpuPercent();
                Toast.makeText(mContext, "cpuPercent: " + cpuPercent, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_factory_reset:
                CustomAPI.factoryReset();
                break;
            case R.id.btn_get_app_info:
                String appInfoPkgName = getEditText(R.id.et_pkg_name);
                AppInfo appInfo = CustomAPI.queryAppInfo(appInfoPkgName);
                Toast.makeText(mContext, "queryAppInfo: " + appInfo.toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_app_size:
                String appSizePkgName = getEditText(R.id.et_pkg_name);
                long appSize = CustomAPI.getAppSize(appSizePkgName);
                Toast.makeText(mContext, "appSize: " + appSize, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_clear_cache:
                String clearCachePkgName = getEditText(R.id.et_pkg_name);
                CustomAPI.cleanAppCache(clearCachePkgName);
                break;
            case R.id.btn_set_app_enable:
                String appEnablePkgName = getEditText(R.id.et_pkg_name);
                CustomAPI.setAppEnable(appEnablePkgName, true);
                break;
            case R.id.btn_set_app_disable:
                String appDisablePkgName = getEditText(R.id.et_pkg_name);
                CustomAPI.setAppEnable(appDisablePkgName, false);
                break;
            case R.id.btn_uninstallable:
                String uninstallablePkgName = getEditText(R.id.et_pkg_name);
                CustomAPI.setPkgNotUninstallable(uninstallablePkgName, false);
                break;
            case R.id.btn_not_uninstallable:
                String notUninstallablePkgName = getEditText(R.id.et_pkg_name);
                CustomAPI.setPkgNotUninstallable(notUninstallablePkgName, true);
                break;
            case R.id.btn_uninstall:
                String uninstallPkgName = getEditText(R.id.et_pkg_name);
                CustomAPI.uninstall(uninstallPkgName, new UninstallListener() {
                    @Override
                    public void onUninstallSuccess(String pkgName) {
                        Log.d(TAG, "++++UNINSTALL\n" + "pkgName: " + pkgName);
                        Toast.makeText(mContext, "pkgName: " + pkgName, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUninstallFail(String pkgName, String msg) {
                        Log.d(TAG, "----UNINSTALL\n" + "pkgName: " + pkgName + " Status Message: " + msg);
                        Toast.makeText(mContext, "pkgName: " + pkgName + " Status Message: " + msg, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btn_install:
                String installApkPath = getEditText(R.id.et_file_path);
                CustomAPI.install(installApkPath, new InstallListener() {
                    @Override
                    public void onInstallSuccess(String pkgName) {
                        Log.d(TAG, "++++INSTALL\n" + "pkgName: " + pkgName);
                        Toast.makeText(mContext, "pkgName: " + pkgName, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onInstallFail(String pkgName, String msg) {
                        Log.d(TAG, "----INSTALL\n" + "pkgName: " + pkgName + " Status Message: " + msg);
                        Toast.makeText(mContext, "pkgName: " + pkgName + " Status Message: " + msg, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btn_update_system:
                String updateSystemFilePath = getEditText(R.id.et_file_path);
                if (updateSystemFilePath == null || updateSystemFilePath.equals("")) {
                    updateSystemFilePath = "sdcard/Download/update.zip";
                }
                // Util.updateSystem(mContext, updatePath);
                //CustomAPI.systemUpdate(updatePath);
                CustomAPI.systemUpdate(updateSystemFilePath, new OtaVerityReceiver.OtaVerityListener() {
                    @Override
                    public void onOtaVerityFailed(String path) {
                        Log.d(TAG, "++++OTA VERITY FAILED\n" + "path: " + path);
                        Toast.makeText(mContext, "PATH: " + path, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btn_get_ota_result:
                OtaResult otaResult = CustomAPI.getOTAResult();
                Toast.makeText(mContext, otaResult.toString(), Toast.LENGTH_SHORT).show();
            case R.id.btn_get_prop:
                String propName = getEditText(R.id.et_property_name);
                String propResult = CustomAPI.getSystemProperty(propName);
                Toast.makeText(mContext, "propResult: " + propResult, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_set_settings_pwd:
                String settingsPwd = getEditText(R.id.et_settings_pwd);
                CustomAPI.settingsPwd(settingsPwd);
                break;
            case R.id.btn_enable_sim_data:
                CustomAPI.setSimDataEnabled(true);
                break;
            case R.id.btn_disable_sim_data:
                CustomAPI.setSimDataEnabled(false);
                break;
            case R.id.btn_enable_bt:
                CustomAPI.enabledBluetooth(true);
                break;
            case R.id.btn_disable_bt:
                CustomAPI.enabledBluetooth(false);
                break;
            case R.id.btn_allow_install:
                CustomAPI.allowInstall(true);
                break;
            case R.id.btn_limit_install:
                CustomAPI.allowInstall(false);
                break;
            case R.id.btn_auto_rotations:
                CustomAPI.autoRotations(true);
                break;
            case R.id.btn_lock_rotations:
                CustomAPI.autoRotations(false);
                break;
            case R.id.btn_lock_landspace:
                CustomAPI.lockLandscape();
                break;
            case R.id.btn_get_imeis:
                int slot_imei = Integer.parseInt(getEditText(R.id.et_get_imeis));
                String imeis = CustomAPI.getIMEI(slot_imei);
                Toast.makeText(mContext, "IMEI" + slot_imei + ": " + imeis, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_imsis:
                int slot_imsi = Integer.parseInt(getEditText(R.id.et_get_imsis));
                String imsis = CustomAPI.getIMSI(slot_imsi);
                Toast.makeText(mContext, "IMSI" + slot_imsi + ": " + imsis, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_iccids:
                int slot_iccid = Integer.parseInt(getEditText(R.id.et_get_iccids));
                String iccids = CustomAPI.getICCID(slot_iccid);
                Toast.makeText(mContext, "ICCID" + slot_iccid + ": " + iccids, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_subid:
                int slot_subid = Integer.parseInt(getEditText(R.id.et_get_subid));
                int subid = CustomAPI.getSubId(slot_subid);
                Toast.makeText(mContext, "SUBID" + slot_subid + ": " + subid, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_dataSimSlotIndex:
                int sindex = CustomAPI.getDataSimSlotIndex();
                Toast.makeText(mContext, "DATA SIM SLOT INDEX: " + sindex, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_set_font_size:
                float fontSize = Float.parseFloat(getEditText(R.id.et_set_font_size));
                CustomAPI.setFontSize(fontSize);
                break;
            case R.id.btn_get_font_size:
                float getFontSize = CustomAPI.getFontSize();
                Toast.makeText(mContext, "font size: " + getFontSize, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_battery_health:
                Toast.makeText(mContext, "battery health: " + CustomAPI.getBatteryHealth(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_version_name:
                Toast.makeText(mContext, "CustomMDM version name: " + CustomAPI.getMdmVersionName(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_sn2:
                String getSn2 = CustomAPI.getSn2();
                Toast.makeText(mContext, "SN2: " + getSn2, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_set_sn2:
                String setSn2 = getEditText(R.id.et_set_sn2);
                if (CustomAPI.setSn2(setSn2)) {
                    Toast.makeText(mContext, "set success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "set failed", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_get_wifi_mac:
                Toast.makeText(mContext, "WIFI MAC: " + CustomAPI.getWifiMac(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_set_wifi_mac:
                String setWifiMac = getEditText(R.id.et_set_wifi_mac);
                if (CustomAPI.setWifiMac(setWifiMac)) {
                    Toast.makeText(mContext, "SET WIFI MAC SUCCESS", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "SET WIFI MAC FAILED", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_get_bt_mac:
                Toast.makeText(mContext, "BT MAC: " + CustomAPI.getBtMac(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_set_bt_mac:
                String setBtMac = getEditText(R.id.et_set_bt_mac);
                if (CustomAPI.setBtMac(setBtMac)) {
                    Toast.makeText(mContext, "SET BT MAC SUCCESS", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "SET BT MAC FAILED", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_get_sdcards_path:
                StringBuilder sbd_sdcardsPath = new StringBuilder();
                for (String s : CustomAPI.getSdcardsPath()) {
                    sbd_sdcardsPath.append(s).append("\n");
                }
                Toast.makeText(mContext, "SDCARDS PATH: \n" + sbd_sdcardsPath.toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_get_otg_path:
                Toast.makeText(mContext, CustomAPI.getOtgPath() != null && !CustomAPI.getOtgPath().equals("") ? CustomAPI.getOtgPath() : "DONE EXIST OTG", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_enable_clear_cache:
                CustomAPI.ctrlClearCache(getEditText(R.id.et_ctrl_clear_cache), true);
                break;
            case R.id.btn_disable_clear_cache:
                CustomAPI.ctrlClearCache(getEditText(R.id.et_ctrl_clear_cache), false);
                break;
            case R.id.btn_enable_clear_storage:
                CustomAPI.ctrlClearStorage(getEditText(R.id.et_ctrl_clear_storage), true);
                break;
            case R.id.btn_disable_clear_storage:
                CustomAPI.ctrlClearStorage(getEditText(R.id.et_ctrl_clear_storage), false);
                break;
            case R.id.btn_show_icon:
                CustomAPI.showAppIcon(getEditText(R.id.et_ctrl_app_icon), true);
                break;
            case R.id.btn_hide_icon:
                CustomAPI.showAppIcon(getEditText(R.id.et_ctrl_app_icon), false);
                break;
            case R.id.btn_uninstall_through_drag_and_drop:
                CustomAPI.uninstallThroughDragAndDrop(getEditText(R.id.et_pkg_name), true);
                break;
            case R.id.btn_not_uninstall_through_drag_and_drop:
                CustomAPI.uninstallThroughDragAndDrop(getEditText(R.id.et_pkg_name), false);
                break;
            case R.id.btn_get_api:
                String apiName = getEditText(R.id.et_get_api);
                switch (CustomAPI.getApiAvailable(apiName)) {
                    case -1:
                        Toast.makeText(mContext, apiName + " is not exist", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        Toast.makeText(mContext, apiName + " is not realized", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(mContext, apiName + " is realized", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            default:
                Toast.makeText(mContext, "view.id error", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

