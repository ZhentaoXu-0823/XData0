package serial.utils;
/**
 * This class define Scanner native interface, will description Scanner native interface
 */
public class ComportNative {
    static {
        System.loadLibrary("comportjni_midtest");
    }

    /**
     * Open Scanner device, call before scanning barcode/QRcode etc.
     *
     * @return (true,success; false, failed)
     */
    public static native boolean comPortTest();
    public static native boolean comPortTestDevice(int device);
}
