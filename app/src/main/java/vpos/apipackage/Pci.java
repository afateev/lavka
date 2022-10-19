package vpos.apipackage;

import android.content.Context;

public class Pci {
    public static native int Lib_PciAesHandle(int i, int i2, byte[] bArr, byte[] bArr2, int i3, byte[] bArr3, int i4, byte[] bArr4);

    public static native int Lib_PciEncryptPin(byte b, short s, byte[] bArr, byte[] bArr2);

    public static native int Lib_PciGetDes(byte b, short s, byte[] bArr, byte[] bArr2, byte b2);

    public static native int Lib_PciGetKLKDes(byte b, short s, byte[] bArr, byte[] bArr2, byte b2);

    public static native int Lib_PciGetKLKMac(byte b, short s, byte[] bArr, byte[] bArr2, byte b2);

    public static native int Lib_PciGetKLKPin(byte b, byte b2, byte b3, byte b4, byte[] bArr, byte[] bArr2, byte[] bArr3, byte b5, byte b6, byte[] bArr4, byte b7, Context context);

    public static native int Lib_PciGetMac(byte b, short s, byte[] bArr, byte[] bArr2, byte b2);

    public static native int Lib_PciGetPin(byte b, byte b2, byte b3, byte b4, byte[] bArr, byte[] bArr2, byte[] bArr3, byte b5, byte b6, byte[] bArr4, byte b7, Context context);

    public static native int Lib_PciGetRnd(byte[] bArr);

    public static native int Lib_PciGetSelPalDes(byte b, short s, byte[] bArr, byte[] bArr2, byte b2);

    public static native int Lib_PciGetSelPalMac(byte b, short s, byte[] bArr, byte[] bArr2, byte b2);

    public static native int Lib_PciGetTDES(byte[] bArr, byte[] bArr2);

    public static native int Lib_PciOffLineEncPin(byte b, byte b2, byte b3, short s, byte[] bArr);

    public static native int Lib_PciOffLinePlainPin(byte b, byte b2, byte b3, short s);

    public static native int Lib_PciReadKcv(byte b, byte b2, byte[] bArr);

    public static native int Lib_PciRsaDecrypt(int i, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4);

    public static native int Lib_PciRsaEncrypt(int i, byte[] bArr, byte[] bArr2, byte[] bArr3);

    public static native int Lib_PciRsaGenKeyPair(int i, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4);

    public static native int Lib_PciTriDesHandle(int i, int i2, byte[] bArr, byte[] bArr2, int i3, byte[] bArr3, int i4, byte[] bArr4);

    public static native int Lib_PciWriteDES_MKey(byte b, byte b2, byte[] bArr, byte b3);

    public static native int Lib_PciWriteDesKey(byte b, byte b2, byte[] bArr, byte b3, byte b4);

    public static native int Lib_PciWriteKLKDesKey(byte b, byte b2, byte[] bArr, byte b3, byte b4, byte[] bArr2, byte b5);

    public static native int Lib_PciWriteKLKMacKey(byte b, byte b2, byte[] bArr, byte b3, byte b4, byte[] bArr2, byte b5);

    public static native int Lib_PciWriteKLKPinKey(byte b, byte b2, byte[] bArr, byte b3, byte b4, byte[] bArr2, byte b5);

    public static native int Lib_PciWriteMAC_MKey(byte b, byte b2, byte[] bArr, byte b3);

    public static native int Lib_PciWriteMacKey(byte b, byte b2, byte[] bArr, byte b3, byte b4);

    public static native int Lib_PciWritePIN_MKey(byte b, byte b2, byte[] bArr, byte b3);

    public static native int Lib_PciWritePinKey(byte b, byte b2, byte[] bArr, byte b3, byte b4);

    static {
        System.loadLibrary("PosApi");
    }
}
