package ru.sberbank.uposcore;

import android.content.res.AssetManager;
import java.util.Map;

public class NativeInterface {
    private static native void uposNativeDone();

    private static native String uposNativeGetConnectionSettings(int i);

    private static native int uposNativeInit(Map<Settings, String> map, AssetManager assetManager, JNIListener jNIListener);

    private static native void uposNativeOnDriverResponse(byte[] bArr);

    private static native void uposNativeOnRelayResponse(int i, byte[] bArr);

    private static native int uposNativeRun(Map<Params, String> map, JNIListener jNIListener);

    private static native int uposNativeSetControlText(int i, int i2, String str);

    private static native void uposNativeSetMasterCallData(byte[] bArr);

    private static native int uposNativeSetScreenResult(int i, int i2);

    static {
        System.loadLibrary("upos-jni");
    }

    public static void onDriverResponse(byte[] bArr) {
        uposNativeOnDriverResponse(bArr);
    }

    public static int uposInit(Map<Settings, String> map, AssetManager assetManager, JNIListener jNIListener) {
        return uposNativeInit(map, assetManager, jNIListener);
    }

    public static int uposRun(Map<Params, String> map, JNIListener jNIListener) {
        return uposNativeRun(map, jNIListener);
    }

    public static void uposDone() {
        uposNativeDone();
    }

    public static int setScreenResult(int i, int i2) {
        return uposNativeSetScreenResult(i, i2);
    }

    public static int setControlText(int i, int i2, String str) {
        return uposNativeSetControlText(i, i2, str);
    }

    public static String getConnectionSettings(int i) {
        return uposNativeGetConnectionSettings(i);
    }

    public static void setMasterCallData(byte[] bArr) {
        uposNativeSetMasterCallData(bArr);
    }

    public static void setRelayCallData(int i, byte[] bArr) {
        uposNativeOnRelayResponse(i, bArr);
    }
}
