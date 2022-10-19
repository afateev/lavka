package com.cspos;

import android.content.Context;

public class PaySys {
    public static native int CallKeyPad(byte[] bArr, int i);

    public static native int EmvAddOneAIDS(byte[] bArr, int i);

    public static native int EmvAddOneCAPK(byte[] bArr, int i);

    public static native int EmvAddOneCAPKString(String str);

    public static native int EmvClearAllAIDS();

    public static native int EmvClearAllCapks();

    public static native int EmvClearOneAIDS(byte[] bArr, int i);

    public static native int EmvClearOneCapks(byte[] bArr, int i);

    public static native int EmvConfigErase();

    public static native int EmvConfigExist();

    public static native int EmvConfigRead(int i, byte[] bArr, int i2);

    public static native int EmvConfigWrite(int i, byte[] bArr, int i2);

    public static native int EmvContextInit(int i, int i2);

    public static native int EmvFinal();

    public static native int EmvGetTagData(byte[] bArr, int i, int i2);

    public static native int EmvGetVersion(byte[] bArr);

    public static native int EmvModifyTermParasTag(byte[] bArr, int i);

    public static native int EmvParaInit();

    public static native int EmvPrePare55Field(byte[] bArr, int i);

    public static native int EmvProcess(int i, int i2);

    public static native int EmvReadTermPar(byte[] bArr, int i, byte[] bArr2, int i2);

    public static native int EmvSaveTermParas(byte[] bArr, int i, int i2);

    public static native int EmvSetCardType(int i);

    public static native int EmvSetExtAmount(int[] iArr);

    public static native int EmvSetOnlineResult(byte[] bArr, byte[] bArr2, int i);

    public static native int EmvSetTransAmount(int i);

    public static native int EmvSetTransAmountBack(int i);

    public static native int EmvSetTransType(int i);

    public static native int GetKLKpinblock(int i, int i2, byte[] bArr, byte[] bArr2, byte[] bArr3);

    public static native int GetSelPalpinblock(int i, int i2, byte[] bArr, byte[] bArr2, byte[] bArr3);

    public static native int Getpinblock(int i, int i2, byte[] bArr, byte[] bArr2, byte[] bArr3);

    public static native int LibAdapterUartBaud(byte[] bArr);

    public static native int LogTurnOn(int i);

    public static native int OfflinePinCipher(byte b, int i, int i2, byte[] bArr, int i3, byte[] bArr2, byte[] bArr3, int i4, byte[] bArr4);

    public static native int OfflinePinPlain(byte b, int i, int i2, byte[] bArr);

    public static native int PapassAidSet(String str);

    public static native int PapassCapkSet(String str);

    public static native int PapassKernelSet(String str);

    public static native int PapassReaderSet(String str);

    public static native int PapassTransSet(String str, int i);

    public static native int PayWaveClearAllAIDS();

    public static native int PayWaveClearAllCapk();

    public static native int PayWaveClearAllTerm();

    public static native int PayWaveDownloadAIDS(String str);

    public static native int PayWaveDownloadCapks(String str);

    public static native int PayWaveDownloadTerm(String str);

    public static native int PayWaveFinal();

    public static native int PayWaveGetTagData(byte[] bArr, int i, int i2);

    public static native int PayWaveKernelInit();

    public static native int PayWaveSetTransAmount(int i);

    public static native int PayWaveSetTransType(int i);

    public static native int PayWaveTrans();

    public static native int PaypassGetTagValue(byte[] bArr, int i, int i2);

    public static native int PaypassKernelInit();

    public static native int PaypassOff();

    public static native int PaypassTest();

    public static native int QvsdcSetOnlineResult(byte[] bArr);

    public static native int SDKAuthorization();

    public static native int SetPadTime(int i);

    public static native int SetPinType(int i);

    public static native int poskeypad(Context context);

    public static native int possetkeypad(int i, int i2, int i3, int i4, byte[] bArr, byte[] bArr2, String str);

    public static native void postest();

    static {
        System.loadLibrary("PosApi");
        System.loadLibrary("PaypassApi");
        System.loadLibrary("VisaLib");
    }
}
