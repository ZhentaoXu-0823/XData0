package com.custom.mdm;

import android.os.Parcel;
import android.os.Parcelable;

public class OtaResult implements Parcelable {
    private boolean isOta;
    private int otaResult;
    private String otaInfo;
    private String oldRomVersion, currentRomVersion;

    public OtaResult(boolean isOta, int otaResult, String otaInfo) {
        this.isOta = isOta;
        this.otaResult = otaResult;
        this.otaInfo = otaInfo;
    }

    protected OtaResult(Parcel in) {
        isOta = in.readByte() != 0;
        otaResult = in.readInt();
        otaInfo = in.readString();
    }

    public static final Creator<OtaResult> CREATOR = new Creator<OtaResult>() {
        @Override
        public OtaResult createFromParcel(Parcel in) {
            return new OtaResult(in);
        }

        @Override
        public OtaResult[] newArray(int size) {
            return new OtaResult[size];
        }
    };

    public String getOldRomVersion() {
        return oldRomVersion;
    }

    public void setOldRomVersion(String oldRomVersion) {
        this.oldRomVersion = oldRomVersion;
    }

    public String getCurrentRomVersion() {
        return currentRomVersion;
    }

    public void setCurrentRomVersion(String currentRomVersion) {
        this.currentRomVersion = currentRomVersion;
    }

    public boolean isOta() {
        return isOta;
    }

    public int getOtaResult() {
        return otaResult;
    }

    public String getOtaInfo() {
        return otaInfo;
    }

    public String toString() {
        return "Reboot from OTA: " + isOta
                + "OTA state: " + otaResult
                + "OTA result: " + otaInfo
                + "OTA info: " + "oldVersion:" + oldRomVersion + " currentVersion:" + currentRomVersion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isOta ? 1 : 0));
        dest.writeInt(otaResult);
        dest.writeString(otaInfo);
    }
}
