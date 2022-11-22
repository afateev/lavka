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
import ru.ip_fateev.lavka.documents.ReceiptDrawer

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

        val imageView = findViewById<ImageView>(R.id.payImageView)

        receiptId = intent.getLongExtra(EXTRA_RECEIPT_ID, 0)

        val localRepository = App.getInstance()?.getRepository()
        localRepository?.getReceipt(receiptId!!).let {
            it?.observe(this) { receipt ->
                if (receipt != null) {
                    localRepository?.getPositions(receipt.id).let {
                        it?.observe(this) { position ->
                            if (position != null) {
                                imageView.setImageBitmap(ReceiptDrawer(receipt, position).toBimap())
                            }
                        }
                    }
                }
            }
        }

        imageView.setImageBitmap(ReceiptDrawer(null, null).toBimap())
    }
}