package vpos.apipackage;

public class Icc {
    public static native int Lib_IccApduCmd(byte b, byte[] bArr, short s, byte[] bArr2, byte[] bArr3);

    public static native int Lib_IccCheck(byte b);

    public static native int Lib_IccClose(byte b);

    public static native int Lib_IccCommand(byte b, byte[] bArr, byte[] bArr2);

    public static native int Lib_IccDetectSYN(byte b);

    public static native int Lib_IccDetect_4428(byte b);

    public static native int Lib_IccOpen(byte b, byte b2, byte[] bArr);

    public static native int Lib_IccSelectEtu(byte b);

    public static native int Lib_SleChangeSecCode4442(byte b, byte[] bArr);

    public static native int Lib_SleCheckAt24(byte b);

    public static native int Lib_SleClose4428(byte b);

    public static native int Lib_SleClose4442(byte b);

    public static native int Lib_SleCloseAt24(byte b);

    public static native int Lib_SleInit4428(byte b, byte[] bArr);

    public static native int Lib_SleInit4442(byte b, byte[] bArr);

    public static native int Lib_SleOpen4428(byte b);

    public static native int Lib_SleOpen4442(byte b);

    public static native int Lib_SleOpenAt24(byte b);

    public static native int Lib_SleReadErrorCount4442(byte b);

    public static native int Lib_SleReadMem4442(byte b, byte b2, int i, byte[] bArr);

    public static native int Lib_SleReadMemAt24(byte b, int i, int i2, byte[] bArr);

    public static native int Lib_SleReadPinCounter4428(byte b, byte[] bArr);

    public static native int Lib_SleReadProMem4442(byte b, byte[] bArr);

    public static native int Lib_SleReadWithPB4428(byte b, byte b2, int i, byte[] bArr);

    public static native int Lib_SleReadWithoutPB4428(byte b, byte b2, int i, byte[] bArr);

    public static native int Lib_SleReset4428(byte b, byte[] bArr);

    public static native int Lib_SleReset4442(byte b, byte[] bArr);

    public static native int Lib_SleVerSecCode4442(byte b, byte[] bArr);

    public static native int Lib_SleVerifyPin4428(byte b, byte[] bArr);

    public static native int Lib_SleWriteMem4442(byte b, byte b2, int i, byte[] bArr);

    public static native int Lib_SleWriteMemAt24(byte b, int i, int i2, byte[] bArr);

    public static native int Lib_SleWritePB4428(byte b, byte b2, int i, byte[] bArr);

    public static native int Lib_SleWritePin4428(byte b, byte[] bArr);

    public static native int Lib_SleWritePinCounter4428(byte b, byte b2);

    public static native int Lib_SleWriteProMem4442(byte b, byte b2, int i, byte[] bArr);

    public static native int Lib_SleWriteWithPB4428(byte b, byte b2, int i, byte[] bArr);

    public static native int Lib_SleWriteWithoutPB4428(byte b, byte b2, int i, byte[] bArr);

    static {
        System.loadLibrary("PosApi");
    }
}
