package com.custom.mdm;

import android.os.Parcel;
import android.os.Parcelable;

public class AppInfo implements Parcelable {
    //is or not installed
    private boolean isInstalled;
    //-1 -- getAppInfo error  ||  0 -- is not installed  ||  >0 -- is installed
    private int versionCode;
    //null -- is not installed  ||  not null -- versionName or error Message
    private String versionName;

    public AppInfo(boolean isInstalled) {
        new AppInfo(isInstalled, -1, "");
    }

    public AppInfo(boolean isInstalled, int versionCode, String versionName) {
        this.isInstalled = isInstalled;
        this.versionCode = versionCode;
        this.versionName = versionName;
    }

    protected AppInfo(Parcel in) {
        isInstalled = in.readByte() != 0;
        versionCode = in.readInt();
        versionName = in.readString();
    }

    public static final Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
        @Override
        public AppInfo createFromParcel(Parcel in) {
            return new AppInfo(in);
        }

        @Override
        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };

    public boolean isInstalled() {
        return isInstalled;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public String toString() {
        if (isInstalled) {
            return "isInstalled: " + isInstalled + " versionCode: " + versionCode + " versionName: " + versionName;
        } else {
            return "isInstalled: " + isInstalled;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isInstalled ? 1 : 0));
        dest.writeInt(versionCode);
        dest.writeString(versionName);
    }
}
