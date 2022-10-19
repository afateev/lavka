package vpos.apipackage;

import android.graphics.Bitmap;
import android.util.Log;
import com.google.zxing.BarcodeFormat;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Print {
    private final String tag = "Print";

    public static native int Lib_CTNPrnStart();

    public static native int Lib_PrnCheckStatus();

    public static native int Lib_PrnClose();

    public static native int Lib_PrnContinuous(int i);

    public static native int Lib_PrnConventional(int i);

    public static native int Lib_PrnCutPicture(byte[] bArr);

    public static native int Lib_PrnCutPictureStr(byte[] bArr, byte[] bArr2, int i);

    public static native int Lib_PrnFeedPaper(int i);

    public static native int Lib_PrnGetFont(byte[] bArr, byte[] bArr2, byte[] bArr3);

    public static native int Lib_PrnInit();

    public static native int Lib_PrnIsCharge(int i);

    public static native int Lib_PrnLogo(byte[] bArr);

    public static native int Lib_PrnSetAlign(int i);

    public static native int Lib_PrnSetCharSpace(int i);

    public static native int Lib_PrnSetFont(byte b, byte b2, byte b3);

    public static native int Lib_PrnSetGray(int i);

    public static native int Lib_PrnSetLeftIndent(int i);

    public static native int Lib_PrnSetLeftSpace(int i);

    public static native int Lib_PrnSetLineSpace(int i);

    public static native int Lib_PrnSetSpace(byte b, byte b2);

    public static native int Lib_PrnSetSpeed(int i);

    public static native int Lib_PrnSetVoltage(int i);

    public static native int Lib_PrnStart();

    public static native int Lib_PrnStep(int i);

    public static native int Lib_PrnStr(byte[] bArr);

    public static native int Lib_SetLinPixelDis(char c);

    static {
        System.loadLibrary("PosApi");
    }

    public static int Lib_PrnStr(String str) {
        byte[] bArr;
        try {
            PrintStream printStream = System.out;
            printStream.println("original string---" + str);
            bArr = str.getBytes("UnicodeBigUnmarked");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            bArr = null;
        }
        Lib_PrnStr(bArr);
        return 0;
    }

    public int Lib_PrnBarcode(String str, int i, int i2, BarcodeFormat barcodeFormat) {
        int Lib_PrnBmp = Lib_PrnBmp(BarcodeCreater.creatBarcode(str, i, i2, barcodeFormat));
        if (Lib_PrnBmp != 0) {
            Log.e("VPOS", "Lib_PrnSendBmp fail, iret = " + Lib_PrnBmp);
        }
        return Lib_PrnBmp;
    }

    public int printCutQrCode(String str, int i, int i2, BarcodeFormat barcodeFormat) {
        int prnCutQrCode = prnCutQrCode(BarcodeCreater.creatBarcode(str, i, i2, barcodeFormat));
        if (prnCutQrCode != 0) {
            Log.e("VPOS", "Lib_PrnSendBmp fail, iret = " + prnCutQrCode);
        }
        return prnCutQrCode;
    }

    public int printCutQrCodeStr(String str, String str2, int i, int i2, int i3, BarcodeFormat barcodeFormat) {
        int prnCutQrCodeStr = prnCutQrCodeStr(BarcodeCreater.creatBarcode(str, i2, i3, barcodeFormat), str2, i);
        if (prnCutQrCodeStr != 0) {
            Log.e("VPOS", "Lib_PrnSendBmp fail, iret = " + prnCutQrCodeStr);
        }
        return prnCutQrCodeStr;
    }

    public int Lib_PrnBmp(Bitmap bitmap) {
        PrinterBitmap Bitmap2PrintDot = Bitmap2PrintDot(bitmap);
        int i = Bitmap2PrintDot.m_iRowBytes * Bitmap2PrintDot.m_iHeight;
        byte[] bArr = new byte[(i + 5)];
        System.arraycopy(Bitmap2PrintDot.m_pDotByteBuffer, 0, bArr, 5, i);
        bArr[0] = (byte) (Bitmap2PrintDot.m_iWidth / 256);
        bArr[1] = (byte) (Bitmap2PrintDot.m_iWidth % 256);
        bArr[2] = (byte) (Bitmap2PrintDot.m_iHeight / 256);
        bArr[3] = (byte) (Bitmap2PrintDot.m_iHeight % 256);
        int Lib_PrnLogo = Lib_PrnLogo(bArr);
        if (Lib_PrnLogo != 0) {
        }
        return Lib_PrnLogo;
    }

    public int prnCutQrCode(Bitmap bitmap) {
        PrinterBitmap Bitmap2PrintDot = Bitmap2PrintDot(bitmap);
        int i = Bitmap2PrintDot.m_iRowBytes * Bitmap2PrintDot.m_iHeight;
        byte[] bArr = new byte[(i + 5)];
        System.arraycopy(Bitmap2PrintDot.m_pDotByteBuffer, 0, bArr, 5, i);
        bArr[0] = (byte) (Bitmap2PrintDot.m_iWidth / 256);
        bArr[1] = (byte) (Bitmap2PrintDot.m_iWidth % 256);
        bArr[2] = (byte) (Bitmap2PrintDot.m_iHeight / 256);
        bArr[3] = (byte) (Bitmap2PrintDot.m_iHeight % 256);
        int Lib_PrnCutPicture = Lib_PrnCutPicture(bArr);
        if (Lib_PrnCutPicture != 0) {
        }
        return Lib_PrnCutPicture;
    }

    public int prnCutQrCodeStr(Bitmap bitmap, String str, int i) {
        byte[] bArr;
        PrinterBitmap Bitmap2PrintDot = Bitmap2PrintDot(bitmap);
        int i2 = Bitmap2PrintDot.m_iRowBytes * Bitmap2PrintDot.m_iHeight;
        byte[] bArr2 = new byte[(i2 + 5)];
        System.arraycopy(Bitmap2PrintDot.m_pDotByteBuffer, 0, bArr2, 5, i2);
        bArr2[0] = (byte) (Bitmap2PrintDot.m_iWidth / 256);
        bArr2[1] = (byte) (Bitmap2PrintDot.m_iWidth % 256);
        bArr2[2] = (byte) (Bitmap2PrintDot.m_iHeight / 256);
        bArr2[3] = (byte) (Bitmap2PrintDot.m_iHeight % 256);
        try {
            PrintStream printStream = System.out;
            printStream.println("original string---" + str);
            bArr = str.getBytes("UnicodeBigUnmarked");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            bArr = null;
        }
        PrintStream printStream2 = System.out;
        printStream2.println("original string---strbytes.length" + bArr.length);
        int Lib_PrnCutPictureStr = Lib_PrnCutPictureStr(bArr2, bArr, i);
        if (Lib_PrnCutPictureStr != 0) {
        }
        return Lib_PrnCutPictureStr;
    }

    private PrinterBitmap Bitmap2PrintDot(Bitmap bitmap) {
        int i;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Log.i("iW = ", Integer.toString(width));
        Log.i("iH = ", Integer.toString(height));
        int i2 = 8;
        int i3 = (width + 7) / 8;
        Log.i("iRowBytes = ", Integer.toString(i3));
        int i4 = i3 * height;
        Log.i("iBufferSize = ", Integer.toString(i4));
        byte[] bArr = new byte[i4];
        for (int i5 = 0; i5 < i4; i5++) {
            bArr[i5] = 0;
        }
        int i6 = 0;
        while (i6 < height) {
            int i7 = 0;
            while (i7 < i3) {
                int i8 = 0;
                while (i8 < i2) {
                    int i9 = (i7 * 8) + i8;
                    if (width <= i9) {
                        break;
                    }
                    if (-16777216 == bitmap.getPixel(i9, i6)) {
                        int i10 = (i6 * i3) + i7;
                        i = i6;
                        bArr[i10] = (byte) ((int) (((double) bArr[i10]) + Math.pow(2.0d, (double) (7 - i8))));
                    } else {
                        i = i6;
                    }
                    i8++;
                    i6 = i;
                    i2 = 8;
                }
                Bitmap bitmap2 = bitmap;
                i7++;
                i6 = i6;
                i2 = 8;
            }
            Bitmap bitmap3 = bitmap;
            i6++;
            i2 = 8;
        }
        Bitmap bitmap4 = bitmap;
        PrinterBitmap printerBitmap = new PrinterBitmap();
        printerBitmap.m_pDotByteBuffer = bArr;
        printerBitmap.m_iRowBytes = i3;
        printerBitmap.m_iWidth = bitmap.getWidth();
        printerBitmap.m_iHeight = bitmap.getHeight();
        return printerBitmap;
    }

    private class PrinterBitmap {
        public int m_iHeight;
        public int m_iRowBytes;
        public int m_iWidth;
        public byte[] m_pDotByteBuffer;

        public PrinterBitmap() {
            this.m_pDotByteBuffer = null;
            this.m_iRowBytes = 0;
            this.m_iWidth = 0;
            this.m_iHeight = 0;
            this.m_pDotByteBuffer = null;
            this.m_iRowBytes = 0;
            this.m_iWidth = 0;
            this.m_iHeight = 0;
        }
    }
}
