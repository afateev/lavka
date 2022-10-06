package ru.ip_fateev.lavka

import android.app.PendingIntent
import android.content.*
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ru.evotor.devices.drivers.IPaySystemDriverService
import ru.evotor.devices.drivers.IUsbDriverManagerService
import ru.evotor.devices.drivers.paysystem.PayInfo
import java.math.BigDecimal


class MainActivity : AppCompatActivity() {
    private var TAG = "Lavka"
    private val ACTION_USB_PERMISSION = "ru.evotor.devices.drivers.USB_PERMISSION"
    private val usbDevices: MutableList<UsbDevice> = arrayListOf()

    var evotorDriverManager: IUsbDriverManagerService? = null
    var terminalId = -1

    val evotorDriverManagerConnection = object : ServiceConnection {
        // Called when the connection with the service is established
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            evotorDriverManager = IUsbDriverManagerService.Stub.asInterface(service)
            Log.e(TAG, "Evotor DriverManagerService  connected")
        }

        // Called when the connection with the service disconnects unexpectedly
        override fun onServiceDisconnected(className: ComponentName) {
            Log.e(TAG, "Evotor DriverManagerService  disconnected")
            evotorDriverManager = null
        }
    }

    var evotorPaySystemService: IPaySystemDriverService? = null

    val evotorPaySystemServiceConnection = object : ServiceConnection {
        // Called when the connection with the service is established
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            evotorPaySystemService = IPaySystemDriverService.Stub.asInterface(service)
            Log.e(TAG, "Evotor PaySystemService  connected")

            val m = applicationContext.getSystemService(USB_SERVICE) as UsbManager
            val usbDevices = m.deviceList
            val ite: Collection<UsbDevice> = usbDevices.values
            val usbs: List<UsbDevice> = ite.toList<UsbDevice>()
            for (usb in usbs) {
                Log.d("Connected usb devices", "Connected usb devices are " + usb.deviceName)

                evotorRegisterDevice(usb)
            }
        }

        // Called when the connection with the service disconnects unexpectedly
        override fun onServiceDisconnected(className: ComponentName) {
            Log.e(TAG, "Evotor PaySystemService  disconnected")
            evotorPaySystemService = null
        }
    }

    private fun evotorRegisterDevice(device: UsbDevice) {
        /*
        // это работает, из сервиса сбера не хватает прав
        val m = applicationContext.getSystemService(USB_SERVICE) as UsbManager

        device.getInterface(0).also { intf ->
            intf.getEndpoint(0)?.also { endpoint ->
                m.openDevice(device)?.apply {
                    Log.e(TAG, this.toString())
                }
            }
        }*/

        if (evotorDriverManager != null) {
            terminalId = evotorDriverManager?.addUsbDevice(device, device.deviceName)!!
        }

        if (evotorPaySystemService != null) {
            if (terminalId >= 0) {
                evotorPaySystemService?.openServiceMenu(terminalId)
                evotorPaySystemService?.getBankName(terminalId)?.let { Log.e(TAG, it) }
                evotorPaySystemService?.getTerminalID(terminalId)?.let { Log.e(TAG, it) }
                evotorPaySystemService?.getMerchNumber(terminalId)?.let { Log.e(TAG, it) }
                evotorPaySystemService?.getMerchEngName(terminalId)?.let { Log.e(TAG, it) }
                evotorPaySystemService?.getServerIP(terminalId)?.let { Log.e(TAG, it) }
                val payInfo = PayInfo(BigDecimal(1))
                var payResult = evotorPaySystemService?.payment(terminalId, payInfo)
                Log.e(TAG, payResult.toString())
            }
        }
    }

    private val usbReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (ACTION_USB_PERMISSION == intent.action) {
                synchronized(this) {
                    val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        device?.apply {
                            //call method to set up device communication
                            usbDevices.add(device)
                            evotorRegisterDevice(device)
                        }
                    } else {
                        Log.d(TAG, "permission denied for device $device")
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        InitEvotorDriverManager()
        InitEvotorPaySystemService()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (evotorDriverManager != null) {
            unbindService(evotorDriverManagerConnection)
        }

        if (evotorPaySystemService != null) {
            unbindService(evotorPaySystemServiceConnection)
        }
    }

    private fun InitEvotorDriverManager() {
        val serviceIntent = Intent("ru.evotor.devices.drivers.DriverManager")
        val resolvedList = packageManager.queryIntentServices(serviceIntent, 0)
        Log.d(TAG,resolvedList.toString());

        for (i in resolvedList) {
            if (i.serviceInfo != null) {
                Log.d(TAG, i.serviceInfo.toString());
                val intent = Intent("ru.evotor.devices.drivers.DriverManager")
                intent.setPackage(i.serviceInfo.packageName)
                val res = bindService(intent, evotorDriverManagerConnection, BIND_AUTO_CREATE)
                Log.d(TAG, res.toString());
                break
            }
        }
    }

    private fun InitEvotorPaySystemService() {
        val serviceIntent = Intent("ru.evotor.devices.drivers.PaySystemService")
        val resolvedList = packageManager.queryIntentServices(serviceIntent, 0)
        Log.d(TAG,resolvedList.toString());

        for (i in resolvedList) {
            if (i.serviceInfo != null) {
                Log.d(TAG, i.serviceInfo.toString());
                val intent = Intent("ru.evotor.devices.drivers.PaySystemService")
                intent.setPackage(i.serviceInfo.packageName)
                val res = bindService(intent, evotorPaySystemServiceConnection, BIND_AUTO_CREATE)
                Log.d(TAG, res.toString());
                break
            }
        }
    }
}