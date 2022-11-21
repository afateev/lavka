package ru.ip_fateev.lavka

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import ru.ip_fateev.lavka.documents.Position
import ru.ip_fateev.lavka.documents.Receipt

class PayActivity : AppCompatActivity() {
    var receiptId: Long? = null
    lateinit var tempBitmap: Bitmap
    lateinit var tempCanvas: Canvas

    companion object {
        val EXTRA_RECEIPT_ID = "ReceiptId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)

        receiptId = intent.getLongExtra(EXTRA_RECEIPT_ID, 0)

        val localRepository = App.getInstance()?.getRepository()
        localRepository?.getReceipt(receiptId!!).let {
            it?.observe(this) { receipt ->
                if (receipt != null) {
                    localRepository?.getPositions(receipt.id).let {
                        it?.observe(this) { position ->
                            if (position != null) {
                                updateReceiptImage(receipt, position)
                            }
                        }
                    }
                }
            }
        }

        val imageView = findViewById<ImageView>(R.id.payImageView)
        tempBitmap = Bitmap.createBitmap(600, 2000, Bitmap.Config.ARGB_8888)
        tempCanvas = Canvas(tempBitmap)
        tempCanvas.drawColor(Color.rgb(200, 200, 200))
        imageView.setImageBitmap(tempBitmap)
    }

    private fun updateReceiptImage(receipt: Receipt, positions: List<Position>) {
        tempCanvas.drawColor(Color.rgb(200, 200, 200))

        val strHeight = 24F

        val paint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.BLACK
            isAntiAlias = true
            textSize = strHeight
            typeface = Typeface.MONOSPACE
        }

        var y = strHeight
        for(p in positions) {
            var str = (p.number + 1).toString() + ": " + p.productName
            tempCanvas.drawText(str, 1F, y, paint)

            y += strHeight
        }
    }
}