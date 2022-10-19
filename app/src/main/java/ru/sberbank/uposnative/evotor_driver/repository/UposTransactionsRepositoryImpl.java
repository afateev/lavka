package ru.sberbank.uposnative.evotor_driver.repository;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.subjects.Subject;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Unit;
import kotlin.concurrent.ThreadsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;
import ru.evotor.devices.drivers.paysystem.IPaySystem;
import ru.evotor.devices.drivers.paysystem.PayInfo;
import ru.evotor.devices.drivers.paysystem.PayResult;
import ru.sberbank.cashboxevotor.data.model.response.UposResponse;
import ru.sberbank.evotoruposdriver.repository.UposTransactionsRepository;
import ru.sberbank.uposnative.UposVspClientAidlInterface;
import ru.sberbank.uposnative.UposVspClientCallbackListener;
import ru.sberbank.uposnative.evotor_driver.presentation.UposViewCallbackListener;

public final class UposTransactionsRepositoryImpl implements ServiceConnection, UposTransactionsRepository, IPaySystem {

    public static final String UPOS_ACTION_AIDL = "ru.sberbank.uposnative.UposVspClientAidlInterface";
    public static final String uposPackageName = "ru.sberbank.upos_driver_test";
    public static final String uposPatchService = "ru.sberbank.uposnative.aidl.UposVspClientAidlService";
    public static UposTransactionsRepository INSTANCE = null;
    public static final AtomicBoolean initialized = new AtomicBoolean();
    public Function0<Unit> connectListener;
    private final Application context;
    private final int indexRep;
    public boolean isSystemMenu;
    public Integer currentRequestId;
    static final  KProperty[] $$delegatedProperties = {Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(UposTransactionsRepositoryImpl.class), "gson", "getGson()Lcom/google/gson/Gson;"))};
    //private final Lazy gson$delegate = LazyKt.lazy(UposTransactionsRepositoryImpl$gson$2.INSTANCE);
    private final Lazy gson$delegate = LazyKt.lazy(new Function0<Gson>() {
        @Override
        public Gson invoke() {
            return new Gson();
        }
    });
    public Subject<UposResponse> terminalSlipCheck;
    public UposViewCallbackListener uposViewCallbackListener;
    public UposVspClientAidlInterface uposVspClientAidlInterface;

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Log.d("upos:", "UposTransactionsRepositoryImpl - onServiceConnected");
        uposVspClientAidlInterface = UposVspClientAidlInterface.Stub.asInterface(iBinder);
        Function0<Unit> function0 = connectListener;
        if (function0 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("connectListener");
        }
        function0.invoke();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        Log.d("upos:", "UposTransactionsRepositoryImpl - onServiceDisconnected");
        uposVspClientAidlInterface = null;
    }

    @Override
    public void initService(Function0<Unit> function0) {
        Intrinsics.checkParameterIsNotNull(function0, "connectListener");
        Log.d("upos:", "UposTransactionsRepositoryImpl - initService");
        connectListener = function0;
        if (this.uposVspClientAidlInterface != null) {
            Log.d("upos:", "UposTransactionsRepositoryImpl - initService 1");
            function0.invoke();
        } else {
            Log.d("upos:", "UposTransactionsRepositoryImpl - initService 2");
        }

        Intent intent = new Intent(UPOS_ACTION_AIDL).setComponent(new ComponentName("ru.sberbank.upos_driver_test", uposPatchService));
        //искать в своём пакете а не в чужом
        //Intent intent = new Intent(UPOS_ACTION_AIDL).setComponent(new ComponentName("ru.ip_fateev.lavka", uposPatchService));
        //Intent intent = new Intent(UPOS_ACTION_AIDL);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> intentServices = pm.queryIntentServices(intent, 0);
        boolean res = context.bindService(intent, this, Context.BIND_AUTO_CREATE);
        if (res) {
            Log.d("upos:", "UposTransactionsRepositoryImpl - bind service ok");
        }
        else {
            Log.d("upos:", "UposTransactionsRepositoryImpl - bind service ERROR");
        }
    }

    @Override
    public void subscribeUposViewCallbackListener(UposViewCallbackListener uposViewCallbackListener) {
        //TODO
    }

    @Override
    public void unBindService() {
        Log.d("upos:", "UposTransactionsRepositoryImpl - unBindService");
        context.unbindService(this);
    }

    public final void registerCallback() {
        Log.d("upos:", "UposTransactionsRepositoryImpl - registerCallback");
        /*UposVspClientAidlInterface uposVspClientAidlInterface2 = this.uposVspClientAidlInterface;
        if (uposVspClientAidlInterface2 != null) {
            uposVspClientAidlInterface2.registerUposClientCallbackListener(new UposTransactionsRepositoryImpl$registerCallback$1(this));
        }*/
        try {
            uposVspClientAidlInterface.registerUposClientCallbackListener(new UposVspClientCallbackListener.Stub() {
                @Override
                public void onAdditionalAbstractResponse(int i, byte[] bArr) throws RemoteException {
                    Intrinsics.checkParameterIsNotNull(bArr, "response");
                }

                @Override
                public void onFullTransactionResponse(int i, byte[] bArr, String str) throws RemoteException {

                }

                @Override
                public void onMasterCallTransactionResponse(int i, byte[] bArr, String str) throws RemoteException {
                    Intrinsics.checkParameterIsNotNull(bArr, "response");
                    Intrinsics.checkParameterIsNotNull(str, "json");
                }

                @Override
                public void onTransactionArrayResponse(int i, byte[] bArr) throws RemoteException {

                }

                @Override
                public void onTransactionResponse(int transactionCode, String response) throws RemoteException {
                    //TODO последние параметры 30 и null не применены
                    ThreadsKt.thread(true, false, null, null, 0, (Function0<Unit>) () -> {
                        String str;
                        Log.d("upos:", "UposTransactionsRepositoryImpl uposResponse_ transactionCode:" + transactionCode + " response:" + response);
                        StringBuilder sb = new StringBuilder();
                        sb.append("UposTransactionsRepositoryImpl registerCallback - terminalSlipCheck:");
                        sb.append(terminalSlipCheck);
                        Log.d("upos:", sb.toString());
                        /*TODO
                        UposResponse uposResponse = new UposResponse(transactionCode, (UposResponseBody) getGson().fromJson(response, UposResponseBody.class));
                        Log.d("upos:", "UposTransactionsRepositoryImpl registerCallback - isSystemMenu:" + isSystemMenu);
                        Log.d("upos:", "UposTransactionsRepositoryImpl registerCallback - indexRep:" + getIndexRep());
                        Log.d("upos:", "UposTransactionsRepositoryImpl registerCallback - current_request_id: " + currentRequestId);
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("UposTransactionsRepositoryImpl registerCallback - REQUEST_ID:");
                        UposResponseBody responseBody = uposResponse.getResponseBody();
                        Integer num = null;
                        sb2.append(responseBody != null ? responseBody.getREQUEST_ID() : null);
                        Log.d("upos:", sb2.toString());
                        boolean z = true;
                        if (!isSystemMenu) {
                            Integer access$getCurrentRequestId$p = currentRequestId;
                            UposResponseBody responseBody2 = uposResponse.getResponseBody();
                            if (responseBody2 != null) {
                                num = responseBody2.getREQUEST_ID();
                            }
                            if (!Intrinsics.areEqual((Object) access$getCurrentRequestId$p, (Object) num)) {
                                uposResponse.setResponseCode(-1);
                                UposResponseBody uposResponseBody = new UposResponseBody((String) null, (String) null, (String) null, (String) null, (String) null, (String) null, (String) null, (String) null, (Integer) null, FrameMetricsAggregator.EVERY_DURATION, (DefaultConstructorMarker) null);
                                uposResponseBody.setMESSAGE(getContext().getString(R.string.unknown_operation));
                                uposResponse.setResponseBody(uposResponseBody);
                            }
                            Subject access$getTerminalSlipCheck$p = terminalSlipCheck;
                            if (access$getTerminalSlipCheck$p != null) {
                                access$getTerminalSlipCheck$p.onNext(uposResponse);
                            }
                            Subject access$getTerminalSlipCheck$p2 = terminalSlipCheck;
                            if (access$getTerminalSlipCheck$p2 != null) {
                                access$getTerminalSlipCheck$p2.onComplete();
                                return;
                            }
                            return;
                        }
                        UposViewCallbackListener access$getUposViewCallbackListener$p = uposViewCallbackListener;
                        if (access$getUposViewCallbackListener$p != null) {
                            access$getUposViewCallbackListener$p.onFinishProcess();
                        }
                        uposViewCallbackListener = null;
                        UposResponseBody responseBody3 = uposResponse.getResponseBody();
                        CharSequence cheque = responseBody3 != null ? responseBody3.getCHEQUE() : null;
                        if (!(cheque == null || cheque.length() == 0)) {
                            z = false;
                        }
                        if (!z) {
                            Integer access$getCurrentRequestId$p2 = currentRequestId;
                            UposResponseBody responseBody4 = uposResponse.getResponseBody();
                            if (responseBody4 != null) {
                                num = responseBody4.getREQUEST_ID();
                            }
                            if (Intrinsics.areEqual((Object) access$getCurrentRequestId$p2, (Object) num)) {
                                PrinterRepository access$getPrinterRepository$p = printerRepository;
                                UposResponseBody responseBody5 = uposResponse.getResponseBody();
                                if (responseBody5 == null || (str = responseBody5.getCHEQUE()) == null) {
                                    str = "";
                                }
                                access$getPrinterRepository$p.print(str);
                            }
                        }*/

                        return null;
                    });
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    @Override
    public PayResult payment(PayInfo payInfo) {
        //TODO
        return null;
     }
    /*
    @Override
    public PayResult payment(PayInfo payInfo) {
        this.isSystemMenu = false;
        Ref.ObjectRef objectRef = new Ref.ObjectRef();
        objectRef.element = (String) null;
        //Object blockingSingle = Observable.fromCallable(new UposTransactionsRepositoryImpl$payment$1(this, payInfo, objectRef)).flatMap(new UposTransactionsRepositoryImpl$payment$2(this)).map(new UposTransactionsRepositoryImpl$payment$3(objectRef)).flatMap(new UposTransactionsRepositoryImpl$payment$4(this)).blockingSingle();
        Object blockingSingle = Observable.fromCallable(new Callable<Object>() {
            @Override
            public void call() throws Exception {
                initService(() -> {
                    registerCallback();
                    if (uposVspClientAidlInterface != null) {
                        try {
                            AmountUtills amountUtills = AmountUtills.INSTANCE;
                            uposVspClientAidlInterface.startPayment(String.valueOf(amountUtills.calcInMinValue(payInfo != null ? payInfo.getPrice() : null)), generateRequestJson((String) objectRef.element));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                });
            }
        }).flatMap(new Function<Object, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Object o) throws Throwable {
                return null;
            }
        }).map(new Function<Object, Object>() {
            @Override
            public Object apply(Object o) throws Throwable {
                return null;
            }
        }).flatMap(new Function<Object, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Object o) throws Throwable {
                return null;
            }
        }).blockingSingle();
        Intrinsics.checkExpressionValueIsNotNull(blockingSingle, "Observable.fromCallable …        .blockingSingle()");
        return (PayResult) blockingSingle;
    }*/

    @Override
    public PayResult cancelPayment(PayInfo payInfo, String rrn) {
        //TODO
        return null;
    }

    @Override
    public PayResult payback(PayInfo payInfo, String rrn) {
        //TODO
        return null;
    }

    @Override
    public PayResult cancelPayback(PayInfo payInfo, String rrn) {
        //TODO
        return null;
    }

    @Override
    public PayResult closeSession() {
        //TODO
        return null;
    }

    @Override
    public void openServiceMenu() {
        this.isSystemMenu = true;
        initService(() -> {
            registerCallback();
            if (uposVspClientAidlInterface != null) {
                try {
                    uposVspClientAidlInterface.startCoreServiceMenu(generateRequestJson((String) null));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            return null;
        });
    }

    @Override
    public String getBankName() {
        //TODO
        return null;
    }

    @Override
    public int getTerminalNumber() {
        //TODO
        return 0;
    }

    @Override
    public String getTerminalID() {
        return "12345678";
    }

    @Override
    public String getMerchNumber() {
        return "123";
    }

    @Override
    public String getMerchCategoryCode() {
        return "123";
    }

    @Override
    public String getMerchEngName() {
        return "123";
    }

    @Override
    public String getCashier() {
        return "123";
    }

    @Override
    public String getServerIP() {
        return "123";
    }

    @Override
    public boolean isNotNeedRRN() {
        return false;
    }

    public final Gson getGson() {
        Lazy lazy = this.gson$delegate;
        KProperty kProperty = $$delegatedProperties[0];
        return (Gson) lazy.getValue();
    }

    public final int getIndexRep() {
        return this.indexRep;
    }

    public UposTransactionsRepositoryImpl(Application application, int i) {
        Intrinsics.checkParameterIsNotNull(application, "context");
        context = application;
        indexRep = i;
    }

    public static final /* synthetic */ UposTransactionsRepository access$getINSTANCE$cp() {
        UposTransactionsRepository uposTransactionsRepository = INSTANCE;
        if (uposTransactionsRepository == null) {
            Intrinsics.throwUninitializedPropertyAccessException("INSTANCE");
        }
        return uposTransactionsRepository;
    }

    public static final class Constants {
        private Constants() {
        }

        public /* synthetic */ Constants(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @JvmStatic
        public final UposTransactionsRepository getInstance(Application application) {
            Intrinsics.checkParameterIsNotNull(application, "application");
            if (UposTransactionsRepositoryImpl.initialized.getAndSet(true)) {
                UposTransactionsRepositoryImpl.INSTANCE = new UposTransactionsRepositoryImpl(application, -13);
            }
            return UposTransactionsRepositoryImpl.access$getINSTANCE$cp();
        }
    }

    private final int generateRandomInt() {
        return new Random().nextInt(200000000) + 1;
    }

    public final String generateRequestJson(String str) {
        this.currentRequestId = Integer.valueOf(generateRandomInt());
        Log.d("upos:", "generateRequestIdJson: " + this.currentRequestId);
        Log.d("upos:", "hash: " + str);
        HashMap hashMap = new HashMap();
        Map map = hashMap;
        Integer num = this.currentRequestId;
        map.put("REQUEST_ID", num != null ? String.valueOf(num.intValue()) : null);
        if (str != null) {
            map.put("TRACK2", str);
        }
        String json = getGson().toJson((Object) hashMap);
        Intrinsics.checkExpressionValueIsNotNull(json, "json");
        return json;
    }
}
