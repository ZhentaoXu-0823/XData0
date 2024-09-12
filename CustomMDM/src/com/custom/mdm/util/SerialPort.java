package com.custom.mdm.util;

import android.util.Log;

import java.io.IOException;
import java.util.Arrays;

import serial.utils.SerialHelper;

public class SerialPort {
    public static final String TAG = "SerialPort";

    private static SerialHelper serialHelper;
    private static byte[] receivedData = null;

    public static boolean open(String port, int baud, int data_bits, int parity, int stop_bits) {
        serialHelper = new SerialHelper(port, baud, parity, data_bits, stop_bits) {
            @Override
            protected void onDataReceived(byte[] buff) {
                try{
                    receivedData = buff;
                    Log.i(TAG, "接收：" + Arrays.toString(buff));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onSendDataReceived(byte[] buff) {
                try{
                    Log.d(TAG, "发送：" + Arrays.toString(buff));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        try {
            serialHelper.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serialHelper.isOpen();
    }

    public static boolean close() {
        if (serialHelper == null) return false;
        serialHelper.close();
        return !serialOpened();
    }

    public static boolean write(byte[] bytes) {
        if (!serialOpened()) {
            return false;
        }
        serialHelper.send(bytes);
        return true;
    }

    public static int read(byte[] bytes) {
        if (bytes == null) return 0;
        if (receivedData == null) return -1;

        int dataLen = receivedData.length;
        int len = dataLen < bytes.length ? dataLen : bytes.length;
        for (int i = 0; i < len ; i++) {
            bytes[i] = receivedData[i];
        }
        receivedData = null;
        return dataLen;
    }

    private static boolean serialOpened() {
        if (serialHelper == null) return false;
        return serialHelper.isOpen();
    }
}
