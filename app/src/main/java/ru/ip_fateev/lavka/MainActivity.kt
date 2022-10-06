package ru.ip_fateev.lavka

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.hardware.usb.UsbDevice
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

    var evotorDriverManager: IUsbDriverManagerService? = null
    var terminalId = -1

    val evotorDriverManagerConnection = object : ServiceConnection {
        // Called when the connection with the service is established
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            evotorDriverManager = IUsbDriverManagerService.Stub.asInterface(service)
            terminalId = evotorDriverManager?.addUsbDevice(null, "")!!
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
            if (terminalId >= 0) {
                evotorPaySystemService?.openServiceMenu(terminalId)
                evotorPaySystemService?.getBankName(terminalId)?.let { Log.e(TAG, it) }
                evotorPaySystemService?.getTerminalID(terminalId)?.let { Log.e(TAG, it) }
                evotorPaySystemService?.getMerchNumber(terminalId)?.let { Log.e(TAG, it) }
                evotorPaySystemService?.getMerchEngName(terminalId)?.let { Log.e(TAG, it) }
                evotorPaySystemService?.getServerIP(terminalId)?.let { Log.e(TAG, it) }
                //val payInfo = PayInfo(BigDecimal(1))
                //var payResult = evotorPaySystemService?.payment(terminalId, payInfo)
                //Log.e(TAG, payResult.toString())
            }

        }

        // Called when the connection with the service disconnects unexpectedly
        override fun onServiceDisconnected(className: ComponentName) {
            Log.e(TAG, "Evotor PaySystemService  disconnected")
            evotorPaySystemService = null
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