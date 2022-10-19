package ru.sberbank.evotoruposdriver.repository;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import ru.evotor.devices.drivers.paysystem.IPaySystem;
import ru.sberbank.uposnative.evotor_driver.presentation.UposViewCallbackListener;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005H&J\u0012\u0010\u0006\u001a\u00020\u00032\b\u0010\u0007\u001a\u0004\u0018\u00010\bH&J\b\u0010\t\u001a\u00020\u0003H&¨\u0006\n"}, d2 = {"Lru/sberbank/evotoruposdriver/repository/UposTransactionsRepository;", "Lru/evotor/devices/drivers/paysystem/IPaySystem;", "initService", "", "connectListener", "Lkotlin/Function0;", "subscribeUposViewCallbackListener", "uposViewCallbackListener", "Lru/sberbank/uposnative/evotor_driver/presentation/UposViewCallbackListener;", "unBindService", "app_evotortestRelease_stub"}, k = 1, mv = {1, 1, 13})
/* compiled from: UposTransactionsRepository.kt */
public interface UposTransactionsRepository extends IPaySystem {
    void initService(Function0<Unit> function0);

    void subscribeUposViewCallbackListener(UposViewCallbackListener uposViewCallbackListener);

    void unBindService();
}
