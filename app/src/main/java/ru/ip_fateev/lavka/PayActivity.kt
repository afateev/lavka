package ru.ip_fateev.lavka

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.IBinder
import android.util.Log
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
import java.io.UnsupportedEncodingException

class PayActivity : AppCompatActivity() {
    var receiptId: Long? = null
    lateinit var tempBitmap: Bitmap
    lateinit var tempCanvas: Canvas

    companion object {
        val EXTRA_RECEIPT_ID = "ReceiptId"
    }

    //IMyBinder interface，All methods that can be invoked to connect and send data are encapsulated within this interface
    var binder: IMyBinder? = null
    var usbList: MutableList<String>? = null

    var receiptLines: List<String> = listOf()
    var receiptBitmap: Bitmap? = null

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
                                receiptLines = receiptDrawer.toStrings(40)
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

    private fun printText() {
        binder!!.writeDataByYouself(
            object : UiExecute {
                override fun onsucess() {
                    Log.e("binder", "usb printed")
                }
                override fun onfailed() {
                    Log.e("binder", "usb print fail")
                }
            }, ProcessData {
                val list: MutableList<ByteArray> = java.util.ArrayList()
                //creat a text ,and make it to byte[],
                val str: String = "test"

                //initialize the printer
//                            list.add( DataForSendToPrinterPos58.initializePrinter());
                list.add(DataForSendToPrinterPos80.initializePrinter())

                for (l in receiptLines) {
                    val data1: ByteArray? = StringUtils.strTobytes(l)
                    if (data1 != null) {
                        list.add(data1)
                    }
                    //should add the command of print and feed line,because print only when one line is complete, not one line, no print

                    list.add(DataForSendToPrinterPos80.printAndFeedLine())
                }
                //cut pager
                list.add(
                    DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(
                        66,
                        1
                    )
                )
                return@ProcessData list

            })
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

object StringUtils {
    /**
     * string to byte[]
     */
    fun strTobytes(str: String): ByteArray? {
        var b: ByteArray? = null
        var data: ByteArray? = null
        try {
            b = str.toByteArray(charset("utf-8"))
            data = String(b, Charsets.UTF_8).toByteArray(charset("gbk"))
        } catch (e: UnsupportedEncodingException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return data
    }

    /**
     * byte[] merger
     */
    fun byteMerger(byte_1: ByteArray, byte_2: ByteArray): ByteArray {
        val byte_3 = ByteArray(byte_1.size + byte_2.size)
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.size)
        System.arraycopy(byte_2, 0, byte_3, byte_1.size, byte_2.size)
        return byte_3
    }

    fun strTobytes(str: String, charset: String?): ByteArray? {
        var b: ByteArray? = null
        var data: ByteArray? = null
        try {
            b = str.toByteArray(charset("utf-8"))
            data = String(b, Charsets.UTF_8).toByteArray(charset(charset!!))
        } catch (e: UnsupportedEncodingException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return data
    }
}