package vpos.apipackage;

public class Mcr {
    public static native int Lib_McrCheck();

    public static native int Lib_McrClose();

    public static native int Lib_McrOpen();

    public static native int Lib_McrRead(byte b, byte b2, byte[] bArr, byte[] bArr2, byte[] bArr3);

    public static native int Lib_McrReset();

    static {
        System.loadLibrary("PosApi");
    }
}
