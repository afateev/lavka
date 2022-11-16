package ru.ip_fateev.lavka

import android.content.*
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.os.IBinder
import android.util.ArrayMap
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ru.evotor.devices.drivers.IPaySystemDriverService
import ru.evotor.devices.drivers.IUsbDriverManagerService
import ru.evotor.devices.drivers.paysystem.PayInfo
import ru.sberbank.uposcore.*
import java.math.BigDecimal
import java.util.*


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
        /*
        // через сервис сбера

        if (evotorPaySystemService != null) {
            if (terminalId >= 0) {
                //evotorPaySystemService?.openServiceMenu(terminalId)
                val payInfo = PayInfo(BigDecimal(1))
                var payResult = evotorPaySystemService?.payment(terminalId, payInfo)
                Log.e(TAG, payResult.toString())
            }
        }*/
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

    class Listener: JNIListener {
        override fun onCreateScreen(i: Int, str: String?) {
            TODO("Not yet implemented")
        }

        override fun onDeleteKey(i: Int) {
            TODO("Not yet implemented")
        }

        override fun onDriverRequest(bArr: ByteArray?) {
            TODO("Not yet implemented")
        }

        override fun onGetHwStatus(i: Int): String {
            TODO("Not yet implemented")
        }

        override fun onLoadKey(i: Int, z: Boolean, i2: Int, bArr: ByteArray?): Boolean {
            TODO("Not yet implemented")
        }

        override fun onMasterCallData(bArr: ByteArray?) {
            TODO("Not yet implemented")
        }

        override fun onRelayCallData(bArr: ByteArray?) {
            TODO("Not yet implemented")
        }

        override fun onRunResult(i: Int, map: MutableMap<Int, String>?) {
            if (map != null) {
                ShowResult(map)
            }
            //TODO("Not yet implemented")
        }

        override fun onSecureCertificateRequest(): ByteArray {
            TODO("Not yet implemented")
        }

        override fun onUpdateScreen(i: Int, str: String?) {
            TODO("Not yet implemented")
        }

        private fun ShowResult(map: Map<Int, String>) {
            val length = Results.values().size
            for (i in 0 until length) {
                try {
                    Log.i("TAG", Results.fromInt(i).toString() + ":" + map[Integer.valueOf(i)])
                } catch (th: Throwable) {
                    Log.i("TAG", th.toString())
                }
            }
        }

    }

    var listener = Listener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //инициализация upos native
        val arrayMap: ArrayMap<Settings, String> = ArrayMap<Settings, String>()
        val absolutePath: String = applicationContext.getFilesDir().getAbsolutePath()
        arrayMap.put(Settings.CURRENT_DIR, absolutePath)
        arrayMap.put(Settings.EXTERN_DIR, absolutePath)
        arrayMap.put(Settings.CASH_REGISTER_NAME, "ru.sberbank.upos_driver_test")

        val rn = ""
        val sn = ""
        var fiscalActvationDate = ""
        arrayMap.put(Settings.CASH_REGISTER_SERIAL, rn);
        arrayMap.put(Settings.FISKAL_SERIAL, sn);
        arrayMap.put(Settings.FISKAL_ACTIVATION_DATE, fiscalActvationDate);

        var upos = NativeInterface.uposInit(arrayMap, applicationContext.assets, listener)
        Log.d(TAG, upos.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun onSellClick(view: View) {
        var intent = Intent(this, ReceiptActivity::class.java)
        intent.action = "ru.ip_fateev.lavka.ACTION_NEW_RECEIPT"
        intent.putExtra(ReceiptActivity.EXTRA_RECEIPT_TYPE, ReceiptActivity.RECEIPT_TYPE_SELL)
        startActivity(intent)
    }

    fun onPosClick(view: View) {
        // мимо сервиса
        val aMap: ArrayMap<Params, String> = ArrayMap<Params, String>()
        val valueOf: Int = Integer.valueOf(Random().nextInt(200000000) + 1)
        aMap.put(Params.OPERATION, "1");
        aMap.put(Params.AMOUNT, "100");
        aMap[Params.REQUEST_ID] = valueOf.toString()
        aMap[Params.JNI_PROCESS_CALL_ID] = valueOf.toString()

        val uposRun = NativeInterface.uposRun(aMap, listener)
        Log.d(TAG, uposRun.toString())
    }
}

