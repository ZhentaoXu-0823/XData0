package com.custom.mdm.util;

import android.util.Log;

import com.example.android_socketcan.android_socketcan;

public class Gpio {
    public static final String TAG = "Gpio";

    private static android_socketcan mGpio;

    static {
        mGpio = new android_socketcan();
    }

    public static int pin_set_direction(int direction) {
        int tag = mGpio.setGpioDirection(direction);
        Log.i(TAG, "setGpioDirection tag = " + tag);
        return tag;
    }

    public static int pin_set_output(int level) {
        int tag = mGpio.writeGpioStatus(level);
        Log.i(TAG, "writeGpioStatus tag = " + tag);
        return tag;
    }

    public static int pin_get_level() {
        int level = mGpio.readGpioStatus();
        Log.i(TAG, "readGpioStatus level = " + level);
        return level;
    }
}
