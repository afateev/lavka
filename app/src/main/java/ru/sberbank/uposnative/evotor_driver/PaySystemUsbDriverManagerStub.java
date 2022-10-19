package ru.sberbank.uposnative.evotor_driver;

import android.hardware.usb.UsbDevice;
import android.os.RemoteException;
import ru.evotor.devices.drivers.IUsbDriverManagerService;

public class PaySystemUsbDriverManagerStub extends IUsbDriverManagerService.Stub {
    private final EvotorUsbService myDeviceService;

    public PaySystemUsbDriverManagerStub(EvotorUsbService evotorUsbService) {
        this.myDeviceService = evotorUsbService;
    }

    public int addUsbDevice(UsbDevice usbDevice, String str) throws RemoteException {
        return this.myDeviceService.createNewDevice(usbDevice, str);
    }

    public void destroy(int i) throws RemoteException {
        this.myDeviceService.destroy(i);
    }
}
