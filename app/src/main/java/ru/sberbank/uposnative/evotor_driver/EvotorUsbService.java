package ru.sberbank.uposnative.evotor_driver;

import android.app.Service;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.IBinder;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import ru.evotor.devices.drivers.Constants;
import ru.sberbank.evotoruposdriver.repository.UposTransactionsRepository;
import ru.sberbank.uposnative.evotor_driver.repository.UposTransactionsRepositoryImpl;

public class EvotorUsbService extends Service {
    private static UsbDeviceConnection connection;
    private static final Map<Integer, UposTransactionsRepository> instances = new HashMap();
    private static UsbManager usbManager;
    private String LOG_TAG = "USB service";
    private volatile AtomicInteger newDeviceIndex = new AtomicInteger(0);

    public IBinder onBind(Intent intent) {
        String action;
        if (intent == null || (action = intent.getAction()) == null) {
            return null;
        }
        char c = 65535;
        int hashCode = action.hashCode();
        if (hashCode != -1396074500) {
            if (hashCode == 910888167 && action.equals(Constants.INTENT_FILTER_DRIVER_MANAGER)) {
                c = 0;
            }
        } else if (action.equals(Constants.INTENT_FILTER_PAY_SYSTEM)) {
            c = 1;
        }
        if (c == 0) {
            usbManager = (UsbManager) getSystemService("usb");
            return new PaySystemUsbDriverManagerStub(this);
        } else if (c != 1) {
            return null;
        } else {
            return new PaySystemDriverStub(this);
        }
    }

    public static int getFileDescriptor() {
        UsbDeviceConnection usbDeviceConnection = connection;
        if (usbDeviceConnection != null) {
            return usbDeviceConnection.getFileDescriptor();
        }
        return -1;
    }

    public int createNewDevice(UsbDevice usbDevice, String str) {
        connection = usbManager.openDevice(usbDevice);
        Log.d(this.LOG_TAG, "device create");
        int andIncrement = this.newDeviceIndex.getAndIncrement();
        instances.put(Integer.valueOf(andIncrement), new UposTransactionsRepositoryImpl(getApplication(), andIncrement));
        return andIncrement;
    }

    public void destroy(int i) {
        instances.remove(Integer.valueOf(i));
        Log.d(this.LOG_TAG, "device destroy");
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public UposTransactionsRepository getPaymentRepository(int i) {
        return instances.get(Integer.valueOf(i));
    }
}
