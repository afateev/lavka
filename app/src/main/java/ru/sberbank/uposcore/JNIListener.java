package ru.sberbank.uposcore;

import java.util.Map;

public interface JNIListener {
    void onCreateScreen(int i, String str);

    void onDeleteKey(int i);

    void onDriverRequest(byte[] bArr);

    String onGetHwStatus(int i);

    boolean onLoadKey(int i, boolean z, int i2, byte[] bArr);

    void onMasterCallData(byte[] bArr);

    void onRelayCallData(byte[] bArr);

    void onRunResult(int i, Map<Integer, String> map);

    byte[] onSecureCertificateRequest();

    void onUpdateScreen(int i, String str);
}
