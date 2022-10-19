package ru.sberbank.cashboxevotor.data.model.response;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u0002\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e¨\u0006\u000f"}, d2 = {"Lru/sberbank/cashboxevotor/data/model/response/UposResponse;", "", "responseCode", "", "responseBody", "Lru/sberbank/cashboxevotor/data/model/response/UposResponseBody;", "(ILru/sberbank/cashboxevotor/data/model/response/UposResponseBody;)V", "getResponseBody", "()Lru/sberbank/cashboxevotor/data/model/response/UposResponseBody;", "setResponseBody", "(Lru/sberbank/cashboxevotor/data/model/response/UposResponseBody;)V", "getResponseCode", "()I", "setResponseCode", "(I)V", "app_evotortestRelease_stub"}, k = 1, mv = {1, 1, 13})
/* compiled from: UposResponse.kt */
public final class UposResponse {
    private UposResponseBody responseBody;
    private int responseCode;

    public UposResponse(int i, UposResponseBody uposResponseBody) {
        this.responseCode = i;
        this.responseBody = uposResponseBody;
    }

    public final UposResponseBody getResponseBody() {
        return this.responseBody;
    }

    public final int getResponseCode() {
        return this.responseCode;
    }

    public final void setResponseBody(UposResponseBody uposResponseBody) {
        this.responseBody = uposResponseBody;
    }

    public final void setResponseCode(int i) {
        this.responseCode = i;
    }
}
