package ru.ip_fateev.lavka

import android.app.Activity
import android.app.ProgressDialog
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
import android.widget.TextView
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
import ru.ip_fateev.lavka.documents.ReceiptHelper
import ru.ip_fateev.lavka.documents.ReceiptState
import ru.ip_fateev.lavka.documents.Transaction
import ru.ip_fateev.lavka.documents.TransactionType
import kotlin.collections.ArrayList

class PayActivity : AppCompatActivity() {
    var receiptId: Long? = null
    lateinit var localRepository: LocalRepository

    companion object {
        const val EXTRA_RECEIPT_ID = "ReceiptId"
        const val PAY_TYPE_UNKNOWN = 0
        const val PAY_TYPE_CASH = 1
        const val PAY_TYPE_CARD = 2
    }

    //IMyBinder interface，All methods that can be invoked to connect and send data are encapsulated within this interface
    var binder: IMyBinder? = null
    var usbList: MutableList<String>? = null

    val receiptHelper: MutableLiveData<ReceiptHelper> by lazy { MutableLiveData<ReceiptHelper>(ReceiptHelper()) }
    val receiptBitmap: MutableLiveData<Bitmap> by lazy { MutableLiveData<Bitmap>(receiptHelper.value?.toBimap()) }

    lateinit var payRemainder: TextView
    lateinit var buttonPayCash: Button
    lateinit var buttonPayCard: Button
    lateinit var fabPrint: FloatingActionButton
    lateinit var progressDialog: ProgressDialog

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

        localRepository = App.getInstance()?.getRepository()!!
        val imageView = findViewById<ImageView>(R.id.payImageView)
        payRemainder = findViewById(R.id.payRemainder)
        buttonPayCash = findViewById(R.id.payCash)
        buttonPayCard = findViewById(R.id.payCard)
        fabPrint = findViewById(R.id.fabPrint)
        progressDialog = ProgressDialog(this)

        receiptHelper.observe(this) {
            receiptBitmap.value = it.toBimap()
            payRemainder.text = it.getRemainder().toString()

            if (it.getAmount() > 0) {
                if (it.getRemainder().equals(0.0)) {
                    when (it.getState() ) {
                        ReceiptState.NEW -> {
                            lifecycleScope.launch {
                                localRepository.setReceiptState(receiptId!!, ReceiptState.PAID)
                            }
                        }
                        else -> {}
                    }
                }
            }
        }

        receiptBitmap.observe(this) {
            imageView.setImageBitmap(it)
        }

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

        localRepository.getReceiptLive(receiptId!!).observe(this) { receipt ->
            receiptHelper.value = ReceiptHelper(receipt)
            if (receipt != null) {
                localRepository.getPositionsLive(receipt.id).let {
                    it.observe(this) { positions ->
                        if (positions != null) {
                            receiptHelper.value = receiptHelper.value?.copy(positions = positions)
                        }
                    }
                }
                localRepository.getTransactionsLive(receipt.id).let {
                    it.observe(this) { transactions ->
                        if (transactions != null) {
                            receiptHelper.value = receiptHelper.value?.copy(transactions = transactions)
                        }
                    }
                }
            }
        }

        localRepository.getReceiptState(receiptId!!).observe(this) {
            when(it) {
                ReceiptState.NEW -> {
                    buttonPayCash.isEnabled = true
                    buttonPayCard.isEnabled = true
                }
                ReceiptState.PAID -> {
                    progressDialog.setTitle("Сохранение чека")
                    progressDialog.setMessage("Ожидание")
                    progressDialog.setCancelable(false)
                    progressDialog.show()

                    buttonPayCash.isEnabled = false
                    buttonPayCard.isEnabled = false
                }
                else -> {
                    progressDialog.hide()

                    buttonPayCash.isEnabled = false
                    buttonPayCard.isEnabled = false
                }
            }
        }
    }

    private fun pay(type: Int) {
        val amount = receiptHelper.value!!.getRemainder()

        if (type == PAY_TYPE_CASH) {
            payCash.launch(amount)
        }
    }

    class PayCash : ActivityResultContract<Double, Pair<Double, Double>?>() {
        override fun createIntent(context: Context, input: Double): Intent =
            Intent(context, PayCashActivity::class.java).apply {
                action = "ru.ip_fateev.lavka.ACTION_PAY_CASH"
                putExtra(PayCashActivity.EXTRA_AMOUNT, input)
            }

        override fun parseResult(resultCode: Int, intent: Intent?): Pair<Double, Double>? {
            if (resultCode != Activity.RESULT_OK) {
                return null
            }
            val a = intent?.getDoubleExtra(PayCashActivity.EXTRA_SUM, 0.0)
            val b = intent?.getDoubleExtra(PayCashActivity.EXTRA_CHANGE, 0.0)
            return Pair(a!!, b!!)
        }
    }

    private val payCash = registerForActivityResult(PayCash()) {
        if (it != null) {
            val amount = it.first
            val change = it.second

            if (amount > 0) {
                val transaction = Transaction(id = 0, docId = receiptId!!, type = TransactionType.CASH, amount = amount, rrn = "")
                lifecycleScope.launch {
                    localRepository.insertTransaction(transaction)
                }
            }

            if (change > 0) {
                val transaction = Transaction(id = 0, docId = receiptId!!, type = TransactionType.CASHCHANGE, amount = change, rrn = "")
                lifecycleScope.launch {
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