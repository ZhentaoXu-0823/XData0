package com.example.android_socketcan;

public class android_socketcan {
    static {
        System.loadLibrary("android_socketcan");
    }

    public static native int setGpioDirection(int direction);
    public static native int writeGpioStatus(int value);
    public static native int readGpioStatus();
}
