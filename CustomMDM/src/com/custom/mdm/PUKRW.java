package com.custom.mdm;

import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import com.android.internal.util.HexDump;
import vendor.mediatek.hardware.nvram.V1_0.INvram;

public class PUKRW {

    private static final String TAG = "PUKRW";

    private static final String XC_SIGN_NVRAM_NAME = "/mnt/vendor/nvdata/APCFG/APRDEB/XC_SIGNATURE";
    private static final int CUSTOM_PUK1_START = 0;
    private static final int CUSTOM_PUK1_END = 2048;
    private static final int CUSTOM_PUK2_START = 2048;
    private static final int CUSTOM_PUK2_END = 4096;

    public static boolean puk1write(String mPuk1) {
        return doWritePuk1ToNV(mPuk1);
    }

    public static boolean puk2write(String mPuk2) {
        return doWritePuk2ToNV(mPuk2);
    }

    public static String puk1read() {
        return doReadPuk1FormNV();
    }

    public static String puk2read() {
        return doReadPuk2FormNV();
    }

    //read puk1 form NV
    private static String doReadPuk1FormNV() {
        byte[] buff = null;
        INvram agent = null;
        StringBuffer buf = new StringBuffer();
        try {
            agent = INvram.getService();
            String data = agent.readFileByName(XC_SIGN_NVRAM_NAME, CUSTOM_PUK1_END);
            buff = HexDump.hexStringToByteArray(
                                data.substring(0, data.length() - 1));
            for(int i = CUSTOM_PUK1_START; i < CUSTOM_PUK1_END; i++){
                buf.append(hexStringToString(Integer.toHexString(buff[i])));
            }
            return buf.toString();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String doReadPuk2FormNV() {
        byte[] buff = null;
        INvram agent = null;
        StringBuffer buf = new StringBuffer();
        try {
            agent = INvram.getService();
            String data = agent.readFileByName(XC_SIGN_NVRAM_NAME, CUSTOM_PUK2_END);
            buff = HexDump.hexStringToByteArray(
                                data.substring(0, data.length() - 1));
            for(int i = CUSTOM_PUK2_START; i < CUSTOM_PUK2_END; i++){
                buf.append(hexStringToString(Integer.toHexString(buff[i])));
            }
            return buf.toString();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean doWritePuk1ToNV(String mPuk) {
        String pukNV = doReadPuk1FormNV();
        Log.d(TAG, "mPuk1 = " + mPuk);
        if (pukNV.equals(mPuk)) {
            return false;
        }
        byte[] buff = null;
        byte[] puk = stringToByteArray(stringToHex(mPuk));
        INvram agent = null;
        try {
            agent = INvram.getService();
            String data = agent.readFileByName(XC_SIGN_NVRAM_NAME, CUSTOM_PUK1_END);
            buff = HexDump.hexStringToByteArray(
                                data.substring(0, data.length() - 1));
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        if(buff != null) {
            //clear data start with BUILD_FWVERSION_IN_NV_RESERVE_LENGTH
            for (int i = CUSTOM_PUK1_START; i < CUSTOM_PUK1_END; i++) {
                buff[i] = (byte) 0x00;
            }
            for(int j = CUSTOM_PUK1_START; j < puk.length; j++) {
                buff[j] = puk[j];
            }

            ArrayList<Byte> dataArray = new ArrayList<Byte>(CUSTOM_PUK1_END);
            for (int k = 0; k < CUSTOM_PUK1_END; k++) {
                dataArray.add(k, new Byte(buff[k]));
            }

            try {
                agent.writeFileByNamevec(XC_SIGN_NVRAM_NAME, CUSTOM_PUK1_END, dataArray);
                Log.d(TAG, "savePuk1FWVersionToNv---->successful!");
                return true;
            } catch(Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private static boolean doWritePuk2ToNV(String mPuk) {
        String pukNV = doReadPuk2FormNV();
        Log.d(TAG, "mPuk2 = " + mPuk);
        if (pukNV.equals(mPuk)) {
            return false;
        }
        byte[] buff = null;
        byte[] puk = stringToByteArray(stringToHex(mPuk));
        INvram agent = null;
        try {
            agent = INvram.getService();
            String data = agent.readFileByName(XC_SIGN_NVRAM_NAME, CUSTOM_PUK2_END);
            buff = HexDump.hexStringToByteArray(
                                data.substring(0, data.length() - 1));
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        if(buff != null) {
            for (int i = CUSTOM_PUK2_START; i < CUSTOM_PUK2_END; i++) {
                buff[i] = (byte) 0x00;
            }
            for(int j = CUSTOM_PUK2_START; j < CUSTOM_PUK2_START + puk.length; j++) {
                buff[j] = puk[j - CUSTOM_PUK2_START];
            }

            ArrayList<Byte> dataArray = new ArrayList<Byte>(CUSTOM_PUK2_END);
            for (int k = 0; k < CUSTOM_PUK2_END; k++) {
                dataArray.add(k, new Byte(buff[k]));
            }

            try {
                agent.writeFileByNamevec(XC_SIGN_NVRAM_NAME, CUSTOM_PUK2_END, dataArray);
                Log.d(TAG, "savePuk2FWVersionToNv---->successful!");
                return true;
            } catch(Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static String stringToHex(String str) {
        char[] strChars = str.toCharArray();
        StringBuffer hexBuffer = new StringBuffer();
        for(int i=0; i<strChars.length; i++) {
            hexBuffer.append(Integer.toHexString((int)strChars[i]));
        }
        return hexBuffer.toString();
    }

    private static byte[] stringToByteArray(String buff) {
        byte[] buffer = new byte[buff.length() / 2];
        buffer = HexDump.hexStringToByteArray(buff);;
        return buffer;
    }

    private static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    private static String byte2HexString(byte[] data) {
        if (data == null || data.length <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            String hexStr = Integer.toHexString(data[i] & 0xff);
            if (hexStr.length() < 2) {
                sb.append("0");
            }
            sb.append(hexStr);
        }
        return sb.toString();
    }
}
