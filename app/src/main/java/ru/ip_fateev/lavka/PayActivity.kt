package ru.ip_fateev.lavka

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import net.posprinter.posprinterface.IMyBinder
import net.posprinter.posprinterface.ProcessData
import net.posprinter.posprinterface.UiExecute
import net.posprinter.service.PosprinterService
import net.posprinter.utils.BitmapToByteData
import net.posprinter.utils.DataForSendToPrinterPos80
import net.posprinter.utils.PosPrinterDev
import ru.ip_fateev.lavka.documents.ReceiptDrawer

class PayActivity : AppCompatActivity() {
    var receiptId: Long? = null

    companion object {
        val EXTRA_RECEIPT_ID = "ReceiptId"
    }

    //IMyBinder interface，All methods that can be invoked to connect and send data are encapsulated within this interface
    var binder: IMyBinder? = null
    var usbList: MutableList<String>? = null

    var receiptBitmap: Bitmap? = null

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

        buttonPayCash = findViewById(R.id.payCash)
        buttonPayCard = findViewById(R.id.payCard)
        fabPrint = findViewById(R.id.fabPrint)

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
                if (receipt != null) {
                    localRepository?.getPositions(receipt.id).let {
                        it?.observe(this) { position ->
                            if (position != null) {
                                val receiptDrawer = ReceiptDrawer(receipt, position)
                                receiptBitmap = receiptDrawer.toBimap()
                                imageView.setImageBitmap(receiptBitmap)
                            }
                        }
                    }
                }
            }
        }

        imageView.setImageBitmap(ReceiptDrawer(null, null).toBimap())
    }

    private fun print() {
        val printBmp = receiptBitmap

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