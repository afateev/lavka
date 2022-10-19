package ru.sberbank.cashboxevotor.data.model.response;

import androidx.core.app.FrameMetricsAggregator;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0016\u0018\u00002\u00020\u0001Bq\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f¢\u0006\u0002\u0010\rR\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u0013\u0010\u0007\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000fR\u001c\u0010\b\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u000f\"\u0004\b\u0014\u0010\u0011R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000fR\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u000f\"\u0004\b\u0017\u0010\u0011R\u001c\u0010\t\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u000f\"\u0004\b\u0019\u0010\u0011R\u001e\u0010\u000b\u001a\u0004\u0018\u00010\fX\u000e¢\u0006\u0010\n\u0002\u0010\u001e\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u000fR\u001c\u0010\n\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u000f\"\u0004\b!\u0010\u0011¨\u0006\""}, d2 = {"Lru/sberbank/cashboxevotor/data/model/response/UposResponseBody;", "", "RRN", "", "CHEQUE", "MESSAGE", "IS_OWN", "ERROR", "HASH", "PAN", "TID", "REQUEST_ID", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;)V", "getCHEQUE", "()Ljava/lang/String;", "setCHEQUE", "(Ljava/lang/String;)V", "getERROR", "getHASH", "setHASH", "getIS_OWN", "getMESSAGE", "setMESSAGE", "getPAN", "setPAN", "getREQUEST_ID", "()Ljava/lang/Integer;", "setREQUEST_ID", "(Ljava/lang/Integer;)V", "Ljava/lang/Integer;", "getRRN", "getTID", "setTID", "app_evotortestRelease_stub"}, k = 1, mv = {1, 1, 13})
/* compiled from: UposResponseBody.kt */
public final class UposResponseBody {
    private String CHEQUE;
    private final String ERROR;
    private String HASH;
    private final String IS_OWN;
    private String MESSAGE;
    private String PAN;
    private Integer REQUEST_ID;
    private final String RRN;
    private String TID;

    public UposResponseBody() {
        this((String) null, (String) null, (String) null, (String) null, (String) null, (String) null, (String) null, (String) null, (Integer) null, FrameMetricsAggregator.EVERY_DURATION, (DefaultConstructorMarker) null);
    }

    public UposResponseBody(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, Integer num) {
        this.RRN = str;
        this.CHEQUE = str2;
        this.MESSAGE = str3;
        this.IS_OWN = str4;
        this.ERROR = str5;
        this.HASH = str6;
        this.PAN = str7;
        this.TID = str8;
        this.REQUEST_ID = num;
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ UposResponseBody(java.lang.String r11, java.lang.String r12, java.lang.String r13, java.lang.String r14, java.lang.String r15, java.lang.String r16, java.lang.String r17, java.lang.String r18, java.lang.Integer r19, int r20, kotlin.jvm.internal.DefaultConstructorMarker r21) {
        /*
            r10 = this;
            r0 = r20
            r1 = r0 & 1
            r2 = 0
            if (r1 == 0) goto L_0x000b
            r1 = r2
            java.lang.String r1 = (java.lang.String) r1
            goto L_0x000c
        L_0x000b:
            r1 = r11
        L_0x000c:
            r3 = r0 & 2
            if (r3 == 0) goto L_0x0014
            r3 = r2
            java.lang.String r3 = (java.lang.String) r3
            goto L_0x0015
        L_0x0014:
            r3 = r12
        L_0x0015:
            r4 = r0 & 4
            if (r4 == 0) goto L_0x001d
            r4 = r2
            java.lang.String r4 = (java.lang.String) r4
            goto L_0x001e
        L_0x001d:
            r4 = r13
        L_0x001e:
            r5 = r0 & 8
            if (r5 == 0) goto L_0x0026
            r5 = r2
            java.lang.String r5 = (java.lang.String) r5
            goto L_0x0027
        L_0x0026:
            r5 = r14
        L_0x0027:
            r6 = r0 & 16
            if (r6 == 0) goto L_0x002f
            r6 = r2
            java.lang.String r6 = (java.lang.String) r6
            goto L_0x0030
        L_0x002f:
            r6 = r15
        L_0x0030:
            r7 = r0 & 32
            if (r7 == 0) goto L_0x0038
            r7 = r2
            java.lang.String r7 = (java.lang.String) r7
            goto L_0x003a
        L_0x0038:
            r7 = r16
        L_0x003a:
            r8 = r0 & 64
            if (r8 == 0) goto L_0x0042
            r8 = r2
            java.lang.String r8 = (java.lang.String) r8
            goto L_0x0044
        L_0x0042:
            r8 = r17
        L_0x0044:
            r9 = r0 & 128(0x80, float:1.794E-43)
            if (r9 == 0) goto L_0x004c
            r9 = r2
            java.lang.String r9 = (java.lang.String) r9
            goto L_0x004e
        L_0x004c:
            r9 = r18
        L_0x004e:
            r0 = r0 & 256(0x100, float:3.59E-43)
            if (r0 == 0) goto L_0x0056
            r0 = r2
            java.lang.Integer r0 = (java.lang.Integer) r0
            goto L_0x0058
        L_0x0056:
            r0 = r19
        L_0x0058:
            r11 = r10
            r12 = r1
            r13 = r3
            r14 = r4
            r15 = r5
            r16 = r6
            r17 = r7
            r18 = r8
            r19 = r9
            r20 = r0
            r11.<init>(r12, r13, r14, r15, r16, r17, r18, r19, r20)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ru.sberbank.cashboxevotor.data.model.response.UposResponseBody.<init>(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    public final String getRRN() {
        return this.RRN;
    }

    public final String getCHEQUE() {
        return this.CHEQUE;
    }

    public final void setCHEQUE(String str) {
        this.CHEQUE = str;
    }

    public final String getMESSAGE() {
        return this.MESSAGE;
    }

    public final void setMESSAGE(String str) {
        this.MESSAGE = str;
    }

    public final String getIS_OWN() {
        return this.IS_OWN;
    }

    public final String getERROR() {
        return this.ERROR;
    }

    public final String getHASH() {
        return this.HASH;
    }

    public final void setHASH(String str) {
        this.HASH = str;
    }

    public final String getPAN() {
        return this.PAN;
    }

    public final void setPAN(String str) {
        this.PAN = str;
    }

    public final String getTID() {
        return this.TID;
    }

    public final void setTID(String str) {
        this.TID = str;
    }

    public final Integer getREQUEST_ID() {
        return this.REQUEST_ID;
    }

    public final void setREQUEST_ID(Integer num) {
        this.REQUEST_ID = num;
    }
}
