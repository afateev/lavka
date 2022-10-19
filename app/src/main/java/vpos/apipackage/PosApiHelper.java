package vpos.apipackage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import com.cspos.PaySys;
import com.google.zxing.BarcodeFormat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import org.apache.commons.io.IOUtils;
import vpos.util.Util;

public class PosApiHelper {
    private static final String AAR_VERSION = "2.4.1";
    private static final int MAX_INTERVAL = 20000;
    private static final String MCU_NODE_FILE = "/sys/devices/platform/mcu_dev/mcudev_pwren";
    private static final int MIN_INTERVAL = 20000;
    private static final String NODE_BATT_STATUS = "/sys/class/power_supply/battery/status";
    private static final String NODE_BATT_VOL = "/sys/class/power_supply/battery/batt_vol";
    public static final int PRINT_MAX_LEN = 624;
    private static final String SET_LEFT_VOLUME_KEY_SCAN = "android.intent.action.SET_LEFT_VOLUME_KEY_SCAN";
    private static final String SET_RIGHT_VOLUME_KEY_SCAN = "android.intent.action.SET_RIGHT_VOLUME_KEY_SCAN";
    public static String TmpStr = "";
    private static Object mBCRService = getBCRService();
    private static PosApiHelper mInstance;
    private static final Object mLock = new Object();
    private int BatteryV;
    private int ret;
    private byte[] version = new byte[9];

    public String getAARVersion() {
        return AAR_VERSION;
    }

    public int SetMcuPowerMode(int i) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(MCU_NODE_FILE));
            fileOutputStream.write((i + "").getBytes());
            fileOutputStream.close();
            if (i != 1) {
                return 0;
            }
            long currentTimeMillis = System.currentTimeMillis();
            while (true) {
                int i2 = ((System.currentTimeMillis() - currentTimeMillis) > 20000 ? 1 : ((System.currentTimeMillis() - currentTimeMillis) == 20000 ? 0 : -1));
                if (i2 > 0 && i2 < 0) {
                    this.ret = SysGetVersion(this.version);
                    if (this.ret == 0) {
                        return 0;
                    }
                    Util.sleepMs(200);
                } else if (i2 > 0) {
                    return -2;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int Des(byte[] bArr, byte[] bArr2, byte[] bArr3, int i) {
        return Sys.Lib_Des(bArr, bArr2, bArr3, i);
    }

    public int getBatteryV() {
        return this.BatteryV;
    }

    public void setBatteryV(int i) {
        this.BatteryV = i;
    }

    public static PosApiHelper getInstance() {
        PosApiHelper posApiHelper;
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new PosApiHelper();
            }
            posApiHelper = mInstance;
        }
        return posApiHelper;
    }

    public int IccCheck(byte b) {
        return Icc.Lib_IccCheck(b);
    }

    public int IccOpen(byte b, byte b2, byte[] bArr) {
        return Icc.Lib_IccOpen(b, b2, bArr);
    }

    public int IccCommand(byte b, byte[] bArr, byte[] bArr2) {
        return Icc.Lib_IccCommand(b, bArr, bArr2);
    }

    public int IccApduCmd(byte b, byte[] bArr, short s, byte[] bArr2, byte[] bArr3) {
        return Icc.Lib_IccApduCmd(b, bArr, s, bArr2, bArr3);
    }

    public int IccClose(byte b) {
        return Icc.Lib_IccClose(b);
    }

    public int SleOpenAt24(byte b) {
        return Icc.Lib_SleOpenAt24(b);
    }

    public int SleCloseAt24(byte b) {
        return Icc.Lib_SleCloseAt24(b);
    }

    public int SleCheckAt24(byte b) {
        return Icc.Lib_SleCheckAt24(b);
    }

    public int SleReadMemAt24(byte b, int i, int i2, byte[] bArr) {
        return Icc.Lib_SleReadMemAt24(b, i, i2, bArr);
    }

    public int SleWriteMemAt24(byte b, int i, int i2, byte[] bArr) {
        return Icc.Lib_SleWriteMemAt24(b, i, i2, bArr);
    }

    public int PiccOpen() {
        return Picc.Lib_PiccOpen();
    }

    public int PiccCheck(byte b, byte[] bArr, byte[] bArr2) {
        return Picc.Lib_PiccCheck(b, bArr, bArr2);
    }

    public int PiccCommand(byte[] bArr, byte[] bArr2) {
        return Picc.Lib_PiccCommand(bArr, bArr2);
    }

    public int EntryPoint_Detect() {
        return Picc.Lib_EntryPoint();
    }

    public int EntryPoint_Open() {
        return Sys.Lib_SetEntryModeOpen();
    }

    public int EntryPoint_Close() {
        return Sys.Lib_SetEntryModeClose();
    }

    public int SysSetEntryMode(int i) {
        return Sys.Lib_SetEntryMode((byte) i);
    }

    public int PiccClose() {
        return Picc.Lib_PiccClose();
    }

    public int PiccFieldOn() {
        return Picc.Lib_PiccFieldOn();
    }

    public int PiccRemove() {
        return Picc.Lib_PiccRemove();
    }

    public int PiccHalt() {
        return Picc.Lib_PiccHalt();
    }

    public int PiccReset() {
        return Picc.Lib_PiccReset();
    }

    public int PiccNfc(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4) {
        return Picc.Lib_PiccNfc(bArr, bArr2, bArr3, bArr4);
    }

    public int PiccPoll(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5, byte[] bArr6) {
        return Picc.Lib_PiccPolling(bArr, bArr2, bArr3, bArr4, bArr5, bArr6);
    }

    public int MifareReprobe(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4) {
        return Picc.Lib_MifareReprobe(bArr, bArr2, bArr3, bArr4);
    }

    public int PiccPollingP3(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4) {
        return Picc.Lib_PiccPollingP3(bArr, bArr2, bArr3, bArr4);
    }

    public int PiccM1Authority(byte b, byte b2, byte[] bArr, byte[] bArr2) {
        return Picc.Lib_PiccM1Authority(b, b2, bArr, bArr2);
    }

    public int PiccM1ReadBlock(int i, byte[] bArr) {
        return Picc.Lib_PiccM1ReadBlock((byte) i, bArr);
    }

    public int PiccM1WriteBlock(int i, byte[] bArr) {
        return Picc.Lib_PiccM1WriteBlock((byte) i, bArr);
    }

    public int PiccM1Operate(byte b, byte b2, byte[] bArr, byte b3) {
        return Picc.Lib_PiccM1Operate(b, b2, bArr, b3);
    }

    public int PiccM1WriteValue(int i, byte[] bArr) {
        return Picc.Lib_PiccM1WriteValue(i, bArr);
    }

    public int PiccM1ReadValue(int i, byte[] bArr) {
        return Picc.Lib_PiccM1ReadValue(i, bArr);
    }

    public int McrClose() {
        return Mcr.Lib_McrClose();
    }

    public int McrOpen() {
        return Mcr.Lib_McrOpen();
    }

    public int McrReset() {
        return Mcr.Lib_McrReset();
    }

    public int McrCheck() {
        return Mcr.Lib_McrCheck();
    }

    public int McrRead(byte b, byte b2, byte[] bArr, byte[] bArr2, byte[] bArr3) {
        return Mcr.Lib_McrRead(b, b2, bArr, bArr2, bArr3);
    }

    public int PrintInit() throws PrintInitException {
        int PrintInit = PrintInit(3, 24, 24, 0);
        if (PrintInit == 0) {
            return PrintInit;
        }
        throw new PrintInitException(PrintInit);
    }

    public int PrintInit(int i, int i2, int i3, int i4) {
        int Lib_PrnInit = Print.Lib_PrnInit();
        if (Lib_PrnInit != 0) {
            return Lib_PrnInit;
        }
        int Lib_PrnSetGray = Print.Lib_PrnSetGray(i);
        if (Lib_PrnSetGray != 0) {
            return Lib_PrnSetGray;
        }
        int Lib_PrnSetFont = Print.Lib_PrnSetFont((byte) i2, (byte) i3, (byte) i4);
        if (Lib_PrnSetFont != 0) {
        }
        return Lib_PrnSetFont;
    }

    public int PrintSetVoltage(int i) {
        return Print.Lib_PrnSetVoltage(i);
    }

    public int PrintCheckStatus() {
        int parseInt = Integer.parseInt(readSysBattFile(NODE_BATT_VOL));
        if (!"Charging".equals(readSysBattFile(NODE_BATT_STATUS))) {
            Print.Lib_PrnIsCharge(0);
        } else {
            Print.Lib_PrnIsCharge(1);
        }
        int Lib_PrnSetVoltage = Print.Lib_PrnSetVoltage((parseInt * 2) / 100);
        if (Lib_PrnSetVoltage != 0) {
            return Lib_PrnSetVoltage;
        }
        return Print.Lib_PrnCheckStatus();
    }

    public int PrintCtnStart() {
        int PrintCheckStatus = PrintCheckStatus();
        return PrintCheckStatus != 0 ? PrintCheckStatus : Print.Lib_CTNPrnStart();
    }

    public int PrintClose() {
        return Print.Lib_PrnClose();
    }

    public int PrnConventional(int i) {
        return Print.Lib_PrnConventional(i);
    }

    public int PrnContinuous(int i) {
        return Print.Lib_PrnContinuous(i);
    }

    public int PrintOpen() throws PrintInitException {
        int PrintInit = PrintInit();
        if (PrintInit == 0) {
            PrintStr(IOUtils.LINE_SEPARATOR_UNIX);
            PrintStr(IOUtils.LINE_SEPARATOR_UNIX);
            PrintStr(IOUtils.LINE_SEPARATOR_UNIX);
            return PrintStart();
        }
        throw new PrintInitException(PrintInit);
    }

    public int PrintSetGray(int i) {
        return Print.Lib_PrnSetGray(i);
    }

    public int PrintSetAlign(int i) {
        return Print.Lib_PrnSetAlign(i);
    }

    public int PrintSetFont(byte b, byte b2, byte b3) {
        return Print.Lib_PrnSetFont(b, b2, b3);
    }

    public int PrintStr(String str) {
        return Print.Lib_PrnStr(str);
    }

    public int PrintSetLinPixelDis(char c) {
        return Print.Lib_SetLinPixelDis(c);
    }

    public int PrintStart() {
        int PrintCheckStatus = PrintCheckStatus();
        if (PrintCheckStatus != 0) {
            return PrintCheckStatus;
        }
        int Lib_PrnStart = Print.Lib_PrnStart();
        if (Lib_PrnStart != 0) {
        }
        return Lib_PrnStart;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0026 A[SYNTHETIC, Splitter:B:18:0x0026] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0033 A[SYNTHETIC, Splitter:B:24:0x0033] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String readSysBattFile(java.lang.String r3) {
        /*
            r0 = 0
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0020 }
            java.io.FileReader r2 = new java.io.FileReader     // Catch:{ IOException -> 0x0020 }
            r2.<init>(r3)     // Catch:{ IOException -> 0x0020 }
            r1.<init>(r2)     // Catch:{ IOException -> 0x0020 }
            java.lang.String r3 = r1.readLine()     // Catch:{ IOException -> 0x001b, all -> 0x0018 }
            r1.close()     // Catch:{ IOException -> 0x0013 }
            goto L_0x0030
        L_0x0013:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0030
        L_0x0018:
            r3 = move-exception
            r0 = r1
            goto L_0x0031
        L_0x001b:
            r3 = move-exception
            r0 = r1
            goto L_0x0021
        L_0x001e:
            r3 = move-exception
            goto L_0x0031
        L_0x0020:
            r3 = move-exception
        L_0x0021:
            r3.printStackTrace()     // Catch:{ all -> 0x001e }
            if (r0 == 0) goto L_0x002e
            r0.close()     // Catch:{ IOException -> 0x002a }
            goto L_0x002e
        L_0x002a:
            r3 = move-exception
            r3.printStackTrace()
        L_0x002e:
            java.lang.String r3 = ""
        L_0x0030:
            return r3
        L_0x0031:
            if (r0 == 0) goto L_0x003b
            r0.close()     // Catch:{ IOException -> 0x0037 }
            goto L_0x003b
        L_0x0037:
            r0 = move-exception
            r0.printStackTrace()
        L_0x003b:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: vpos.apipackage.PosApiHelper.readSysBattFile(java.lang.String):java.lang.String");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v12, resolved type: java.io.InputStreamReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v14, resolved type: java.io.InputStreamReader} */
    /* JADX WARNING: type inference failed for: r1v1, types: [java.io.InputStreamReader] */
    /* JADX WARNING: type inference failed for: r1v11 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0080 A[SYNTHETIC, Splitter:B:52:0x0080] */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x008a A[SYNTHETIC, Splitter:B:57:0x008a] */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0094 A[SYNTHETIC, Splitter:B:62:0x0094] */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x00a3 A[SYNTHETIC, Splitter:B:70:0x00a3] */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x00ad A[SYNTHETIC, Splitter:B:75:0x00ad] */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x00b7 A[SYNTHETIC, Splitter:B:80:0x00b7] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String readSysBattCat(java.lang.String r5) {
        /*
            r0 = 0
            java.lang.Runtime r1 = java.lang.Runtime.getRuntime()     // Catch:{ IOException -> 0x0077, all -> 0x0073 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0077, all -> 0x0073 }
            r2.<init>()     // Catch:{ IOException -> 0x0077, all -> 0x0073 }
            java.lang.String r3 = "cat "
            r2.append(r3)     // Catch:{ IOException -> 0x0077, all -> 0x0073 }
            r2.append(r5)     // Catch:{ IOException -> 0x0077, all -> 0x0073 }
            java.lang.String r5 = r2.toString()     // Catch:{ IOException -> 0x0077, all -> 0x0073 }
            java.lang.Process r5 = r1.exec(r5)     // Catch:{ IOException -> 0x0077, all -> 0x0073 }
            java.io.InputStream r5 = r5.getInputStream()     // Catch:{ IOException -> 0x0077, all -> 0x0073 }
            java.io.InputStreamReader r1 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0070, all -> 0x006a }
            r1.<init>(r5)     // Catch:{ IOException -> 0x0070, all -> 0x006a }
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0067, all -> 0x0061 }
            r2.<init>(r1)     // Catch:{ IOException -> 0x0067, all -> 0x0061 }
            java.lang.String r3 = r2.readLine()     // Catch:{ IOException -> 0x005f }
            if (r3 == 0) goto L_0x0049
            if (r5 == 0) goto L_0x0038
            r5.close()     // Catch:{ IOException -> 0x0034 }
            goto L_0x0038
        L_0x0034:
            r5 = move-exception
            r5.printStackTrace()
        L_0x0038:
            r1.close()     // Catch:{ IOException -> 0x003c }
            goto L_0x0040
        L_0x003c:
            r5 = move-exception
            r5.printStackTrace()
        L_0x0040:
            r2.close()     // Catch:{ IOException -> 0x0044 }
            goto L_0x0048
        L_0x0044:
            r5 = move-exception
            r5.printStackTrace()
        L_0x0048:
            return r3
        L_0x0049:
            if (r5 == 0) goto L_0x0053
            r5.close()     // Catch:{ IOException -> 0x004f }
            goto L_0x0053
        L_0x004f:
            r5 = move-exception
            r5.printStackTrace()
        L_0x0053:
            r1.close()     // Catch:{ IOException -> 0x0057 }
            goto L_0x005b
        L_0x0057:
            r5 = move-exception
            r5.printStackTrace()
        L_0x005b:
            r2.close()     // Catch:{ IOException -> 0x0098 }
            goto L_0x009c
        L_0x005f:
            r3 = move-exception
            goto L_0x007b
        L_0x0061:
            r2 = move-exception
            r4 = r0
            r0 = r5
            r5 = r2
            r2 = r4
            goto L_0x00a1
        L_0x0067:
            r3 = move-exception
            r2 = r0
            goto L_0x007b
        L_0x006a:
            r1 = move-exception
            r2 = r0
            r0 = r5
            r5 = r1
            r1 = r2
            goto L_0x00a1
        L_0x0070:
            r3 = move-exception
            r1 = r0
            goto L_0x007a
        L_0x0073:
            r5 = move-exception
            r1 = r0
            r2 = r1
            goto L_0x00a1
        L_0x0077:
            r3 = move-exception
            r5 = r0
            r1 = r5
        L_0x007a:
            r2 = r1
        L_0x007b:
            r3.printStackTrace()     // Catch:{ all -> 0x009d }
            if (r5 == 0) goto L_0x0088
            r5.close()     // Catch:{ IOException -> 0x0084 }
            goto L_0x0088
        L_0x0084:
            r5 = move-exception
            r5.printStackTrace()
        L_0x0088:
            if (r1 == 0) goto L_0x0092
            r1.close()     // Catch:{ IOException -> 0x008e }
            goto L_0x0092
        L_0x008e:
            r5 = move-exception
            r5.printStackTrace()
        L_0x0092:
            if (r2 == 0) goto L_0x009c
            r2.close()     // Catch:{ IOException -> 0x0098 }
            goto L_0x009c
        L_0x0098:
            r5 = move-exception
            r5.printStackTrace()
        L_0x009c:
            return r0
        L_0x009d:
            r0 = move-exception
            r4 = r0
            r0 = r5
            r5 = r4
        L_0x00a1:
            if (r0 == 0) goto L_0x00ab
            r0.close()     // Catch:{ IOException -> 0x00a7 }
            goto L_0x00ab
        L_0x00a7:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00ab:
            if (r1 == 0) goto L_0x00b5
            r1.close()     // Catch:{ IOException -> 0x00b1 }
            goto L_0x00b5
        L_0x00b1:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00b5:
            if (r2 == 0) goto L_0x00bf
            r2.close()     // Catch:{ IOException -> 0x00bb }
            goto L_0x00bf
        L_0x00bb:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00bf:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: vpos.apipackage.PosApiHelper.readSysBattCat(java.lang.String):java.lang.String");
    }

    public int PrintBmp(Bitmap bitmap) {
        return new Print().Lib_PrnBmp(bitmap);
    }

    public int PrintBarcode(String str, int i, int i2, BarcodeFormat barcodeFormat) {
        return new Print().Lib_PrnBarcode(str, i, i2, barcodeFormat);
    }

    public int PrintQrCode_Cut(String str, int i, int i2, BarcodeFormat barcodeFormat) {
        return new Print().printCutQrCode(str, i, i2, barcodeFormat);
    }

    public int PrintCutQrCode_Str(String str, String str2, int i, int i2, int i3, BarcodeFormat barcodeFormat) {
        return new Print().printCutQrCodeStr(str, str2, i, i2, i3, barcodeFormat);
    }

    public int PciWritePIN_MKey(byte b, byte b2, byte[] bArr, byte b3) {
        return Pci.Lib_PciWritePIN_MKey(b, b2, bArr, b3);
    }

    public int PciWriteMAC_MKey(byte b, byte b2, byte[] bArr, byte b3) {
        return Pci.Lib_PciWriteMAC_MKey(b, b2, bArr, b3);
    }

    public int PciWriteDES_MKey(byte b, byte b2, byte[] bArr, byte b3) {
        return Pci.Lib_PciWriteDES_MKey(b, b2, bArr, b3);
    }

    public int PciWriteDES_KLKKey(byte b, byte b2, byte[] bArr, byte b3, byte[] bArr2) {
        byte[] bArr3 = new byte[32];
        for (int i = 0; i < 32; i++) {
            bArr3[i] = 0;
        }
        Pci.Lib_PciWriteDES_MKey(b, b2, bArr, b3);
        Pci.Lib_PciGetTDES(bArr3, bArr);
        for (int i2 = 0; i2 < 32; i2++) {
            bArr2[i2] = bArr3[i2];
        }
        return 0;
    }

    public int PciWriteMAC_KLKKey(byte b, byte b2, byte[] bArr, byte b3, byte[] bArr2) {
        byte[] bArr3 = new byte[32];
        for (int i = 0; i < 32; i++) {
            bArr3[i] = 0;
        }
        Pci.Lib_PciWriteMAC_MKey(b, b2, bArr, b3);
        Pci.Lib_PciGetTDES(bArr3, bArr);
        for (int i2 = 0; i2 < 32; i2++) {
            bArr2[i2] = bArr3[i2];
        }
        return 0;
    }

    public int PciWritePIN_KLKKey(byte b, byte b2, byte[] bArr, byte b3, byte[] bArr2) {
        byte[] bArr3 = new byte[32];
        for (int i = 0; i < 32; i++) {
            bArr3[i] = 0;
        }
        Pci.Lib_PciWritePIN_MKey(b, b2, bArr, b3);
        Pci.Lib_PciGetTDES(bArr3, bArr);
        for (int i2 = 0; i2 < 32; i2++) {
            bArr2[i2] = bArr3[i2];
        }
        return 0;
    }

    public int PciWritePinKey(byte b, byte b2, byte[] bArr, byte b3, byte b4) {
        return Pci.Lib_PciWritePinKey(b, b2, bArr, b3, b4);
    }

    public int PciWritePinKey_HostMK(byte b, byte b2, byte[] bArr, byte b3, byte b4, byte[] bArr2) {
        byte[] bArr3 = new byte[32];
        byte[] bArr4 = new byte[32];
        for (int i = 0; i < 32; i++) {
            bArr3[i] = 0;
            bArr4[i] = 0;
        }
        Pci.Lib_PciWriteKLKPinKey(b, b2, bArr, b3, b4, bArr3, (byte) 0);
        Pci.Lib_PciGetTDES(bArr4, bArr3);
        for (int i2 = 0; i2 < 32; i2++) {
            bArr2[i2] = bArr4[i2];
        }
        return 0;
    }

    public int PciWriteDesKey_HostMK(byte b, byte b2, byte[] bArr, byte b3, byte b4, byte[] bArr2) {
        byte[] bArr3 = new byte[32];
        byte[] bArr4 = new byte[32];
        for (int i = 0; i < 32; i++) {
            bArr3[i] = 0;
            bArr4[i] = 0;
        }
        Pci.Lib_PciWriteKLKDesKey(b, b2, bArr, b3, b4, bArr3, (byte) 0);
        Pci.Lib_PciGetTDES(bArr4, bArr3);
        for (int i2 = 0; i2 < 32; i2++) {
            bArr2[i2] = bArr4[i2];
        }
        return 0;
    }

    public int PciWriteMacKey_HostMK(byte b, byte b2, byte[] bArr, byte b3, byte b4, byte[] bArr2) {
        byte[] bArr3 = new byte[32];
        byte[] bArr4 = new byte[32];
        for (int i = 0; i < 32; i++) {
            bArr3[i] = 0;
            bArr4[i] = 0;
        }
        Pci.Lib_PciWriteKLKMacKey(b, b2, bArr, b3, b4, bArr3, (byte) 0);
        Pci.Lib_PciGetTDES(bArr4, bArr3);
        for (int i2 = 0; i2 < 32; i2++) {
            bArr2[i2] = bArr4[i2];
        }
        return 0;
    }

    public int PciWriteMacKey(byte b, byte b2, byte[] bArr, byte b3, byte b4) {
        return Pci.Lib_PciWriteMacKey(b, b2, bArr, b3, b4);
    }

    public int PciWriteDesKey(byte b, byte b2, byte[] bArr, byte b3, byte b4) {
        return Pci.Lib_PciWriteDesKey(b, b2, bArr, b3, b4);
    }

    public int PciWriteMacKey_HostWK(byte b, byte b2, byte[] bArr, byte b3, byte b4, byte[] bArr2) {
        byte[] bArr3 = new byte[32];
        byte[] bArr4 = new byte[32];
        for (int i = 0; i < 32; i++) {
            bArr3[i] = 0;
            bArr4[i] = 0;
        }
        Pci.Lib_PciWriteKLKMacKey(b, b2, bArr, b3, b4, bArr3, (byte) 1);
        Pci.Lib_PciGetTDES(bArr4, bArr3);
        for (int i2 = 0; i2 < 32; i2++) {
            bArr2[i2] = bArr4[i2];
        }
        return 0;
    }

    public int PciWriteDesKey_HostWK(byte b, byte b2, byte[] bArr, byte b3, byte b4, byte[] bArr2) {
        byte[] bArr3 = new byte[32];
        byte[] bArr4 = new byte[32];
        for (int i = 0; i < 32; i++) {
            bArr3[i] = 0;
            bArr4[i] = 0;
        }
        Pci.Lib_PciWriteKLKDesKey(b, b2, bArr, b3, b4, bArr3, (byte) 1);
        Pci.Lib_PciGetTDES(bArr4, bArr3);
        for (int i2 = 0; i2 < 32; i2++) {
            bArr2[i2] = bArr4[i2];
        }
        return 0;
    }

    public int PciReadKcv(byte b, byte b2, byte[] bArr) {
        Pci.Lib_PciReadKcv(b, b2, bArr);
        return 0;
    }

    public int PciWritePinKey_HostWK(byte b, byte b2, byte[] bArr, byte b3, byte b4, byte[] bArr2) {
        byte[] bArr3 = new byte[32];
        byte[] bArr4 = new byte[32];
        for (int i = 0; i < 32; i++) {
            bArr3[i] = 0;
            bArr4[i] = 0;
        }
        Pci.Lib_PciWriteKLKPinKey(b, b2, bArr, b3, b4, bArr3, (byte) 1);
        Pci.Lib_PciGetTDES(bArr4, bArr3);
        for (int i2 = 0; i2 < 32; i2++) {
            bArr2[i2] = bArr4[i2];
        }
        return 0;
    }

    public int PciGetPin(byte b, byte b2, byte b3, byte b4, byte[] bArr, byte[] bArr2, byte[] bArr3, byte b5, byte b6, byte[] bArr4, byte b7, Context context) {
        return Pci.Lib_PciGetPin(b, b2, b3, b4, bArr, bArr2, bArr3, b5, b6, bArr4, b7, context);
    }

    public int PciGetKLKPin(byte b, byte b2, byte b3, byte b4, byte[] bArr, byte[] bArr2, byte[] bArr3, byte b5, byte b6, byte[] bArr4, byte b7, Context context) {
        return Pci.Lib_PciGetKLKPin(b, b2, b3, b4, bArr, bArr2, bArr3, b5, b6, bArr4, b7, context);
    }

    public int PciGetMac(byte b, short s, byte[] bArr, byte[] bArr2, byte b2) {
        return Pci.Lib_PciGetMac(b, s, bArr, bArr2, b2);
    }

    public int PciGetKLKMac(byte b, short s, byte[] bArr, byte[] bArr2, byte b2) {
        return Pci.Lib_PciGetKLKMac(b, s, bArr, bArr2, b2);
    }

    public int PciGetSelPalMac(byte b, short s, byte[] bArr, byte[] bArr2, byte b2) {
        return Pci.Lib_PciGetSelPalMac(b, s, bArr, bArr2, b2);
    }

    public int PciGetDes(byte b, short s, byte[] bArr, byte[] bArr2, byte b2) {
        return Pci.Lib_PciGetDes(b, s, bArr, bArr2, b2);
    }

    public int PciGetKLKDes(byte b, short s, byte[] bArr, byte[] bArr2, byte b2) {
        return Pci.Lib_PciGetKLKDes(b, s, bArr, bArr2, b2);
    }

    public int PciGetSelPalDes(byte b, short s, byte[] bArr, byte[] bArr2, byte b2) {
        return Pci.Lib_PciGetSelPalDes(b, s, bArr, bArr2, b2);
    }

    public int PciTriDes(int i, int i2, byte[] bArr, byte[] bArr2, int i3, byte[] bArr3, int i4, byte[] bArr4) {
        Log.e("PciTriDes", "PciTriDes: ---into");
        return Pci.Lib_PciTriDesHandle(i, i2, bArr, bArr2, i3, bArr3, i4, bArr4);
    }

    public int PciGetRnd(byte[] bArr) {
        return Pci.Lib_PciGetRnd(bArr);
    }

    public int PciGetKcv(byte[] bArr, byte[] bArr2) {
        return Pci.Lib_PciGetTDES(bArr, bArr2);
    }

    public int PciRsaGenKeyPair(int i, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4) {
        return Pci.Lib_PciRsaGenKeyPair(i, bArr, bArr2, bArr3, bArr4);
    }

    public int PciRsaEncrypt(int i, byte[] bArr, byte[] bArr2, byte[] bArr3) {
        return Pci.Lib_PciRsaEncrypt(i, bArr, bArr2, bArr3);
    }

    public int PciRsaDecrypt(int i, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4) {
        return Pci.Lib_PciRsaDecrypt(i, bArr, bArr2, bArr4, bArr3);
    }

    public int PciAesHandle(int i, int i2, byte[] bArr, byte[] bArr2, int i3, byte[] bArr3, int i4, byte[] bArr4) {
        return Pci.Lib_PciAesHandle(i, i2, bArr, bArr2, i3, bArr3, i4, bArr4);
    }

    public int SysUpdate() {
        return Sys.Lib_Update();
    }

    public int SysUpdateBoot() {
        return Sys.Lib_UpdateBoot();
    }

    public int SysSetLedMode(int i, int i2) {
        return Sys.Lib_SetLed((byte) i, (byte) i2);
    }

    public int SetCurrentBaudRate(byte b) {
        return Sys.Lib_SetCurrentBaudRate(b);
    }

    public int SysLogSwitch(int i) {
        PaySys.LogTurnOn(i);
        return Sys.Lib_LogSwitch(i);
    }

    public int SysBeep() {
        return Sys.Lib_Beep();
    }

    public int GetCurrentBaudRate() {
        return Sys.Lib_GetCurrentBaudRate();
    }

    public int SysReadChipID(byte[] bArr, int i) {
        return Sys.Lib_ReadChipID(bArr, i);
    }

    public int SysWriteSN(byte[] bArr) {
        return Sys.Lib_WriteSN(bArr);
    }

    public int RsaEncrypt(byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3) {
        return Sys.Lib_RsaEncrypt(bArr, i, bArr2, i2, bArr3);
    }

    public int RsaDecrypt(byte[] bArr, int i, byte[] bArr2, byte[] bArr3, int i2, byte[] bArr4) {
        return Sys.Lib_RsaDecrypt(bArr, i, bArr2, bArr3, i2, bArr4);
    }

    public int SysReadSN(byte[] bArr) {
        return Sys.Lib_ReadSN(bArr);
    }

    public int ReadRegister(byte[] bArr, byte[] bArr2) {
        return Sys.Lib_ReadRegister(bArr, bArr2);
    }

    public int WriteRegister(byte[] bArr) {
        return Sys.Lib_WriteRegister(bArr);
    }

    public int ReadEEprom(byte[] bArr, byte[] bArr2) {
        return Sys.Lib_ReadEEprom(bArr, bArr2);
    }

    public int WriteEEprom(byte[] bArr) {
        return Sys.Lib_WriteEEprom(bArr);
    }

    public int LoadRfConfig(byte[] bArr) {
        return Sys.Lib_LoadRfConfig(bArr);
    }

    public int UpdateRfConfig(byte[] bArr) {
        return Sys.Lib_UpdateRfConfig(bArr);
    }

    public int RetrieveRfConfiguration(byte[] bArr, byte[] bArr2) {
        return Sys.Lib_RetrieveRfConfiguration(bArr, bArr2);
    }

    public int RetrieveRfConfigurationSize(byte[] bArr, byte[] bArr2) {
        return Sys.Lib_RetrieveRfConfigurationSize(bArr, bArr2);
    }

    public int ReadAutoLoadRf(byte[] bArr) {
        return Sys.Lib_ReadAutoLoadRf(bArr);
    }

    public int CheckRfConfigStatus(byte[] bArr) {
        return Sys.Lib_CheckRfConfigStatus(bArr);
    }

    public int ReadAutoSetRf(byte[] bArr) {
        return Sys.Lib_ReadAutoSetRf(bArr);
    }

    public int SetAutoLoadRf(byte b) {
        return Sys.Lib_SetAutoLoadRf(b);
    }

    public int SetAutoSetRf(byte b) {
        return Sys.Lib_SetAutoSetRf(b);
    }

    public int PiccFirmwareDownload() {
        return Sys.Lib_PiccFirmwareDownload();
    }

    public int SysGetVersion(byte[] bArr) {
        return Sys.Lib_GetVersion(bArr);
    }

    public void SetKeyScanByLetfVolume(Context context, int i) {
        Intent intent = new Intent(SET_LEFT_VOLUME_KEY_SCAN);
        intent.putExtra("value", i);
        context.sendBroadcast(intent);
    }

    public void SetKeyScanByRightVolume(Context context, int i) {
        Intent intent = new Intent(SET_RIGHT_VOLUME_KEY_SCAN);
        intent.putExtra("value", i);
        context.sendBroadcast(intent);
    }

    public String getOSVersion(Context context) {
        return Settings.System.getString(context.getContentResolver(), "custom_build_version");
    }

    public String getMcuTargetVersion(Context context) {
        return Settings.System.getString(context.getContentResolver(), "mcu_target_version");
    }

    public static Object getBCRService() {
        try {
            Class<?> cls = Class.forName("android.os.ServiceManager");
            Class<?> cls2 = Class.forName("com.android.server.bcr.IBCRService$Stub");
            return cls2.getDeclaredMethod("asInterface", new Class[]{IBinder.class}).invoke(cls2, new Object[]{(IBinder) cls.getMethod("getService", new Class[]{String.class}).invoke(cls.newInstance(), new Object[]{"bcr_service"})});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean installRomPackage(Context context, String str) {
        Log.e("RomUtil", "installRomPackage - s");
        try {
            if (mBCRService == null) {
                mBCRService = getBCRService();
            }
            Method declaredMethod = mBCRService.getClass().getDeclaredMethod("installPackage", new Class[]{String.class});
            Log.e("RomUtil", "installPackage - ***********************" + ((Boolean) declaredMethod.invoke(mBCRService, new Object[]{str})));
            return ((Boolean) declaredMethod.invoke(mBCRService, new Object[]{str})).booleanValue();
        } catch (Exception e) {
            Log.e("RomUtil", "installRomPackage :" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public int PciEncryptPin(byte b, short s, byte[] bArr, byte[] bArr2) {
        return Pci.Lib_PciEncryptPin(b, s, bArr, bArr2);
    }
}
