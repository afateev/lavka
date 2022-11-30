package ru.ip_fateev.lavka

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import net.posprinter.posprinterface.IMyBinder
import net.posprinter.posprinterface.ProcessData
import net.posprinter.posprinterface.UiExecute
import net.posprinter.service.PosprinterService
import net.posprinter.utils.BitmapToByteData
import net.posprinter.utils.DataForSendToPrinterPos80
import net.posprinter.utils.PosPrinterDev
import ru.ip_fateev.lavka.documents.ReceiptDrawer
import ru.ip_fateev.lavka.documents.Transaction
import ru.ip_fateev.lavka.documents.TransactionType

class PayActivity : AppCompatActivity() {
    var receiptId: Long? = null

    companion object {
        const val EXTRA_RECEIPT_ID = "ReceiptId"
        const val PAY_TYPE_UNKNOWN = 0
        const val PAY_TYPE_CASH = 1
        const val PAY_TYPE_CARD = 2
    }

    //IMyBinder interface，All methods that can be invoked to connect and send data are encapsulated within this interface
    var binder: IMyBinder? = null
    var usbList: MutableList<String>? = null

    val receiptDrawer: MutableLiveData<ReceiptDrawer> by lazy { MutableLiveData<ReceiptDrawer>(ReceiptDrawer()) }
    val receiptBitmap: MutableLiveData<Bitmap> by lazy { MutableLiveData<Bitmap>(receiptDrawer.value?.toBimap()) }

    lateinit var buttonPayCash: Button
    lateinit var buttonPayCard: Button
    lateinit var fabPrint: FloatingActionButton

    //bindService connection
    var conn: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            //Bind successfully
            binder = iBinder as IMyBinder
            Log.e("binder", "connected")

            if (usbList != null) {
                if (usbList!!.size > 0) {
                    val usbAdrresss = usbList!!.get(0)
                    binder!!.connectUsbPort(
                        applicationContext,
                        usbAdrresss,
                        object : UiExecute {
                            override fun onsucess() {
                                Log.e("binder", "usb connected")
                            }

                            override fun onfailed() {
                                Log.e("binder", "usb connect fail")
                            }
                        })
                }
            }
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            Log.e("disbinder", "disconnected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)

        receiptId = intent.getLongExtra(EXTRA_RECEIPT_ID, 0)
        val imageView = findViewById<ImageView>(R.id.payImageView)

        receiptDrawer.observe(this) {
            receiptBitmap.value = it.toBimap()
        }
;
        receiptBitmap.observe(this) {
            imageView.setImageBitmap(it)
        }

        buttonPayCash = findViewById(R.id.payCash)
        buttonPayCard = findViewById(R.id.payCard)
        fabPrint = findViewById(R.id.fabPrint)

        buttonPayCash.setOnClickListener {
            pay(PAY_TYPE_CASH)
        }

        buttonPayCard.setOnClickListener {
            pay(PAY_TYPE_CARD)
        }

        fabPrint.setOnClickListener{
            print()
        }

        usbList = PosPrinterDev.GetUsbPathNames(this)
        if (usbList == null) {
            usbList = ArrayList<String>()
        }

        //bind service，get ImyBinder object
        //bind service，get ImyBinder object
        val intent = Intent(this, PosprinterService::class.java)
        bindService(intent, conn, BIND_AUTO_CREATE)


        val localRepository = App.getInstance()?.getRepository()
        localRepository?.getReceipt(receiptId!!).let {
            it?.observe(this) { receipt ->
                receiptDrawer.value = ReceiptDrawer(receipt)
                if (receipt != null) {
                    localRepository?.getPositions(receipt.id).let {
                        it?.observe(this) { positions ->
                            if (positions != null) {
                                receiptDrawer.value = receiptDrawer.value?.copy(positions = positions)
                            }
                        }
                    }
                    localRepository?.getTransactions(receipt.id).let {
                        it?.observe(this) { transactions ->
                            if (transactions != null) {
                                receiptDrawer.value = receiptDrawer.value?.copy(transactions = transactions)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun pay(type: Int) {
        if (type == PAY_TYPE_CASH) {
            payCash.launch(100.0)
        }

    }

    class PayCash : ActivityResultContract<Double, Double?>() {
        override fun createIntent(context: Context, input: Double): Intent =
            Intent(context, PayCashActivity::class.java).apply {
                action = "ru.ip_fateev.lavka.ACTION_PAY_CASH"
                putExtra(PayCashActivity.EXTRA_AMOUNT, input)
            }

        override fun parseResult(resultCode: Int, intent: Intent?): Double? {
            if (resultCode != Activity.RESULT_OK) {
                return null
            }
            return intent?.getDoubleExtra(PayCashActivity.EXTRA_SUM, 0.0)
        }
    }

    private val payCash = registerForActivityResult(PayCash()) {
        if (it != null) {
            val localRepository = App.getInstance()?.getRepository()

            val amount = (it * 100).toLong()
            val transaction = Transaction(id = 0, docId = receiptId!!, type = TransactionType.CASH, amount = amount, rrn = "")
            lifecycleScope.launch {
                if (localRepository != null) {
                    localRepository.insertTransaction(transaction)
                }
            }
        }
    }

    private fun done() {
        val intent = Intent()
        intent.putExtra("receiptId", receiptId)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun print() {
        val printBmp = receiptBitmap.value

        if (printBmp == null)
        {
            return
        }

        val height: Int = printBmp.getHeight()
        // if height > 200 cut the bitmap
        // if height > 200 cut the bitmap
        if (height > 200) {
            binder?.writeDataByYouself(object : UiExecute {
                override fun onsucess() {}
                override fun onfailed() {}
            }, ProcessData {
                val list: MutableList<ByteArray> = java.util.ArrayList()
                list.add(DataForSendToPrinterPos80.initializePrinter())
                var bitmaplist: List<Bitmap> = java.util.ArrayList()
                bitmaplist = cutBitmap(200, printBmp) //cut bitmap
                if (bitmaplist.size != 0) {
                    for (i in bitmaplist.indices) {
                        list.add(
                            DataForSendToPrinterPos80.printRasterBmp(
                                0,
                                bitmaplist[i],
                                BitmapToByteData.BmpType.Threshold,
                                BitmapToByteData.AlignType.Center,
                                576
                            )
                        )
                    }
                }
                list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(66, 1))
                list
            })
        } else {
            binder?.writeDataByYouself(object : UiExecute {
                override fun onsucess() {}
                override fun onfailed() {}
            }, ProcessData {
                val list: MutableList<ByteArray> = java.util.ArrayList()
                list.add(DataForSendToPrinterPos80.initializePrinter())
                list.add(
                    DataForSendToPrinterPos80.printRasterBmp(
                        0,
                        printBmp,
                        BitmapToByteData.BmpType.Threshold,
                        BitmapToByteData.AlignType.Center,
                        576
                    )
                )
                list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(66, 1))
                list
            })
        }
    }

    private fun cutBitmap(h: Int, bitmap: Bitmap): List<Bitmap> {
        val width = bitmap.width
        val height = bitmap.height
        val full = height % h == 0
        val n = if (height % h == 0) height / h else height / h + 1
        var b: Bitmap
        val bitmaps: MutableList<Bitmap> = java.util.ArrayList()
        for (i in 0 until n) {
            b = if (full) {
                Bitmap.createBitmap(bitmap, 0, i * h, width, h)
            } else {
                if (i == n - 1) {
                    Bitmap.createBitmap(bitmap, 0, i * h, width, height - i * h)
                } else {
                    Bitmap.createBitmap(bitmap, 0, i * h, width, h)
                }
            }
            bitmaps.add(b)
        }
        return bitmaps
    }
}