package ru.ip_fateev.lavka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import ru.ip_fateev.lavka.documents.ReceiptHelper
import java.text.SimpleDateFormat

class ReceiptEditActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_RECEIPT_ID = "ReceiptId"
    }

    lateinit var receiptEditId: TextView
    lateinit var receiptEditUuid: TextView
    lateinit var receiptDate: TextView
    lateinit var receiptTime: TextView

    var receiptId: Long = -1
    lateinit var localRepository: LocalRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt_edit)

        receiptEditId = findViewById(R.id.receiptEditId)
        receiptEditUuid = findViewById(R.id.receiptEditUuid)
        receiptDate = findViewById(R.id.receiptDate)
        receiptTime = findViewById(R.id.receiptTime)

        receiptId = intent.getLongExtra(PayActivity.EXTRA_RECEIPT_ID, 0)
        localRepository = App.getInstance()?.getRepository()!!

        localRepository.getReceiptLive(receiptId!!).observe(this) {
            receiptEditId.text = it.id.toString()
            receiptEditUuid.text = it.uuid.toString()
            receiptDate.text = SimpleDateFormat("dd.MM.yyyy").format(it.dateTime)
            receiptTime.text = SimpleDateFormat("HH:mm").format(it.dateTime)
        }

    }
}