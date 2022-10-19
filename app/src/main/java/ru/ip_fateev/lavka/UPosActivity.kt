package ru.ip_fateev.lavka

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class UPosActivity : AppCompatActivity() {
    var TAG: String = "Lavka UPos Activity"
    var vid_pidList = listOf(Pair(39176, 36912))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upos)

        val supportedDevices = scanUsbDevices()
    }

    private fun scanUsbDevices(): MutableList<UsbDevice> {
        val supportedDevList = mutableListOf<UsbDevice>()

        val usbManager = applicationContext.getSystemService(USB_SERVICE) as UsbManager
        val usbDevices = usbManager.deviceList

        val usbs: List<UsbDevice> = usbDevices.values.toList<UsbDevice>()
        for (dev in usbs) {
            Log.d(TAG, "Connected usb device: " + dev.deviceName)

            if (vid_pidList.contains(Pair(dev.vendorId, dev.productId))) {
                supportedDevList.add(dev)
            }
        }

        return supportedDevList
    }
}