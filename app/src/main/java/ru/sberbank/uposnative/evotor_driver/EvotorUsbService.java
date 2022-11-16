package ru.sberbank.uposnative.evotor_driver;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;

public class EvotorUsbService {
    private static UsbDeviceConnection connection;

    public static void setConnection(UsbDeviceConnection c) {
        if (connection == null) {
            connection = c;
        }
    }

    public static int getFileDescriptor() {
        UsbDeviceConnection usbDeviceConnection = connection;
        if (usbDeviceConnection != null) {
            return usbDeviceConnection.getFileDescriptor();
        }
        return -1;
    }
}
