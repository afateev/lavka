package ru.sberbank.uposnative.evotor_driver;

import android.os.RemoteException;
import android.util.Log;
import ru.evotor.devices.drivers.IPaySystemDriverService;
import ru.evotor.devices.drivers.paysystem.PayInfo;
import ru.evotor.devices.drivers.paysystem.PayResult;

public class PaySystemDriverStub extends IPaySystemDriverService.Stub {
    private EvotorUsbService evotorUsbService;

    public PaySystemDriverStub(EvotorUsbService evotorUsbService2) {
        this.evotorUsbService = evotorUsbService2;
    }

    public PayResult payment(int i, PayInfo payInfo) throws RemoteException {
        Log.d("upos:", "PaySystemDriverStub - payment - index - " + i);
        return this.evotorUsbService.getPaymentRepository(i).payment(payInfo);
    }

    public PayResult cancelPayment(int i, PayInfo payInfo, String str) throws RemoteException {
        Log.d("upos:", "PaySystemDriverStub - cancelPayment - index - " + i);
        return this.evotorUsbService.getPaymentRepository(i).cancelPayment(payInfo, str);
    }

    public PayResult payback(int i, PayInfo payInfo, String str) throws RemoteException {
        Log.d("upos:", "PaySystemDriverStub - payback - index - " + i);
        return this.evotorUsbService.getPaymentRepository(i).payback(payInfo, str);
    }

    public PayResult cancelPayback(int i, PayInfo payInfo, String str) throws RemoteException {
        Log.d("upos:", "PaySystemDriverStub - cancelPayback - index - " + i);
        return this.evotorUsbService.getPaymentRepository(i).cancelPayback(payInfo, str);
    }

    public PayResult closeSession(int i) throws RemoteException {
        Log.d("upos:", "PaySystemDriverStub - closeSession - index - " + i);
        return this.evotorUsbService.getPaymentRepository(i).closeSession();
    }

    public void openServiceMenu(int i) throws RemoteException {
        Log.d("upos:", "PaySystemDriverStub - openServiceMenu - index - " + i);
        this.evotorUsbService.getPaymentRepository(i).openServiceMenu();
    }

    public String getBankName(int i) throws RemoteException {
        Log.d("upos:", "PaySystemDriverStub - getBankName - index - " + i);
        return this.evotorUsbService.getPaymentRepository(i).getBankName();
    }

    public int getTerminalNumber(int i) throws RemoteException {
        Log.d("upos:", "PaySystemDriverStub - getTerminalNumber - index - " + i);
        return this.evotorUsbService.getPaymentRepository(i).getTerminalNumber();
    }

    public String getTerminalID(int i) throws RemoteException {
        Log.d("c", "PaySystemDriverStub - getTerminalID - index - " + i);
        return this.evotorUsbService.getPaymentRepository(i).getTerminalID();
    }

    public String getMerchNumber(int i) throws RemoteException {
        Log.d("upos:", "PaySystemDriverStub - getMerchNumber - index - " + i);
        return this.evotorUsbService.getPaymentRepository(i).getMerchEngName();
    }

    public String getMerchCategoryCode(int i) throws RemoteException {
        Log.d("upos:", "PaySystemDriverStub - getMerchCategoryCode - index - " + i);
        return this.evotorUsbService.getPaymentRepository(i).getMerchCategoryCode();
    }

    public String getMerchEngName(int i) throws RemoteException {
        Log.d("upos:", "PaySystemDriverStub - getMerchEngName - index - " + i);
        return this.evotorUsbService.getPaymentRepository(i).getMerchEngName();
    }

    public String getCashier(int i) throws RemoteException {
        Log.d("upos:", "PaySystemDriverStub - getCashier - index - " + i);
        return this.evotorUsbService.getPaymentRepository(i).getCashier();
    }

    public String getServerIP(int i) throws RemoteException {
        Log.d("upos:", "PaySystemDriverStub - getServerIP - index - " + i);
        return this.evotorUsbService.getPaymentRepository(i).getServerIP();
    }

    public boolean isNotNeedRRN(int i) throws RemoteException {
        Log.d("upos:", "PaySystemDriverStub - isNotNeedRRN - index - " + i);
        return this.evotorUsbService.getPaymentRepository(i).isNotNeedRRN();
    }

    public String getTerminalNumberAsString(int i) throws RemoteException {
        Log.d("upos:", "PaySystemDriverStub - getTerminalNumberAsString - index - " + i);
        //TODO return this.evotorUsbService.getPaymentRepository(i).getTerminalNumberAsString();
        return "";
    }
}
