package vpos.apipackage;

public class Picc {
    public static native int Lib_1PiccM1AuthorizeSec(int i, int i2, byte[] bArr);

    public static native int Lib_EntryPoint();

    public static native int Lib_MifareReprobe(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4);

    public static native int Lib_PiccApduCmd(byte[] bArr, short s, byte[] bArr2, byte[] bArr3);

    public static native int Lib_PiccCheck(byte b, byte[] bArr, byte[] bArr2);

    public static native int Lib_PiccClose();

    public static native int Lib_PiccCommand(byte[] bArr, byte[] bArr2);

    public static native int Lib_PiccDataExchange(byte[] bArr, short s, byte[] bArr2, byte[] bArr3);

    public static native int Lib_PiccFieldOn();

    public static native int Lib_PiccHalt();

    public static native int Lib_PiccHwConfig(short s, short s2);

    public static native int Lib_PiccHwModeSet(int i);

    public static native int Lib_PiccKeyStore(byte[] bArr, byte b, byte b2, byte b3, byte b4);

    public static native int Lib_PiccM1Authority(byte b, byte b2, byte[] bArr, byte[] bArr2);

    public static native int Lib_PiccM1Init();

    public static native int Lib_PiccM1Operate(byte b, byte b2, byte[] bArr, byte b3);

    public static native int Lib_PiccM1ReadBlock(byte b, byte[] bArr);

    public static native int Lib_PiccM1ReadValue(int i, byte[] bArr);

    public static native int Lib_PiccM1RestoreTransfer(byte b, byte b2);

    public static native int Lib_PiccM1WriteBlock(byte b, byte[] bArr);

    public static native int Lib_PiccM1WriteValue(int i, byte[] bArr);

    public static native int Lib_PiccMfpActivateCard();

    public static native int Lib_PiccMfpAuthenticateClassicSL2(byte b, byte b2, short s, short s2);

    public static native int Lib_PiccMfpAuthenticateSL(byte b, byte b2, short s, short s2, short s3, byte b3, byte[] bArr, byte b4, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5);

    public static native int Lib_PiccMfpChangeKey(byte b, short s, short s2, short s3, byte b2, byte[] bArr);

    public static native int Lib_PiccMfpCommitPerso();

    public static native int Lib_PiccMfpMultiBlockRead(byte b, byte b2, byte[] bArr);

    public static native int Lib_PiccMfpMultiBlockWrite(byte b, byte b2, byte[] bArr);

    public static native int Lib_PiccMfpProximityCheck(int i, byte[] bArr, byte b, byte b2, byte[] bArr2);

    public static native int Lib_PiccMfpRead(byte b, byte b2, byte b3, short s, byte b4, byte[] bArr);

    public static native int Lib_PiccMfpReadValue(int i, byte b, byte b2, short s, byte[] bArr, byte[] bArr2);

    public static native int Lib_PiccMfpResetAuth();

    public static native int Lib_PiccMfpResetSecMsgState();

    public static native int Lib_PiccMfpWrite(byte b, byte b2, short s, byte b3, byte[] bArr);

    public static native int Lib_PiccMfpWritePerso(short s, byte[] bArr);

    public static native int Lib_PiccMfpWriteValue(byte b, byte b2, short s, byte[] bArr, byte b3);

    public static native int Lib_PiccMfulActivateCard();

    public static native int Lib_PiccMfulIncrCnt(int i, byte[] bArr);

    public static native int Lib_PiccMfulPwdAuth(byte[] bArr, byte[] bArr2);

    public static native int Lib_PiccMfulRead(int i, byte[] bArr);

    public static native int Lib_PiccMfulReadCnt(int i, byte[] bArr);

    public static native int Lib_PiccMfulReadSign(int i, byte[] bArr);

    public static native int Lib_PiccMfulUlcAuthenticate(short s, short s2);

    public static native int Lib_PiccMfulWrite(int i, byte[] bArr);

    public static native int Lib_PiccNfc(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4);

    public static native int Lib_PiccOpen();

    public static native int Lib_PiccOriDataExchange(byte b, byte b2, byte b3, byte b4, byte[] bArr, short s, byte[] bArr2, byte[] bArr3);

    public static native int Lib_PiccPolling(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5, byte[] bArr6);

    public static native int Lib_PiccPollingP3(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4);

    public static native int Lib_PiccRemove();

    public static native int Lib_PiccReset();

    public static native int Lib_PiccSamAv2Ini(byte b, byte b2, byte[] bArr, byte b3, byte b4, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5);

    public static native int Lib_PiccSamAv2Init(int i, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4);

    public static native int Lib_PiccSamAv2Initi(byte b, byte[] bArr, byte b2, byte b3, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5);

    public static native int Lib_PiccSamClose(int i);

    public static native int Lib_PiccSamMfcAuth(byte b, byte b2, byte b3, byte b4);

    public static native int Lib_PiccSamOpen(int i, byte[] bArr);

    public static native int Lib_PiccWriSl1KeyToAv2(byte[] bArr, byte b, byte b2, byte b3);

    static {
        System.loadLibrary("PosApi");
    }
}
