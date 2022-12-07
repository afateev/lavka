package ru.ip_fateev.lavka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.ip_fateev.lavka.documents.Receipt
import java.text.SimpleDateFormat
import java.util.*

class ReceiptEditActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_RECEIPT_ID = "ReceiptId"
    }

    lateinit var receiptEditId: TextView
    lateinit var receiptEditUuid: TextView
    lateinit var receiptEditDateEdit: ImageButton
    lateinit var receiptEditDate: TextView
    lateinit var receiptEditDatePicker: DatePicker
    lateinit var receiptEditDateApply: Button
    lateinit var receiptEditTimeEdit: ImageButton
    lateinit var receiptEditTime: TextView
    lateinit var receiptEditTimePicker: TimePicker
    lateinit var receiptEditTimeApply: Button


    var receiptId: Long = -1
    lateinit var localRepository: LocalRepository
    lateinit var receipt: LiveData<Receipt>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt_edit)

        receiptEditId = findViewById(R.id.receiptEditId)
        receiptEditUuid = findViewById(R.id.receiptEditUuid)
        receiptEditDateEdit = findViewById(R.id.receiptEditDateEdit)
        receiptEditDate = findViewById(R.id.receiptEditDate)
        receiptEditDatePicker = findViewById(R.id.receiptEditDatePicker)
        receiptEditDateApply = findViewById(R.id.receiptEditDateApply)
        receiptEditTimeEdit = findViewById(R.id.receiptEditTimeEdit)
        receiptEditTime = findViewById(R.id.receiptEditTime)
        receiptEditTimePicker = findViewById(R.id.receiptEditTimePicker)
        receiptEditTimeApply = findViewById(R.id.receiptEditTimeApply)

        receiptId = intent.getLongExtra(PayActivity.EXTRA_RECEIPT_ID, 0)
        localRepository = App.getInstance()?.getRepository()!!

        receipt = localRepository.getReceiptLive(receiptId!!)

        receipt.observe(this) {
            receiptEditId.text = it.id.toString()
            receiptEditUuid.text = it.uuid.toString()
            receiptEditDate.text = SimpleDateFormat("dd.MM.yyyy").format(it.dateTime)
            receiptEditTime.text = SimpleDateFormat("HH:mm").format(it.dateTime)
        }

        receiptEditDatePicker.isVisible = false
        receiptEditDateApply.isVisible = false

        receiptEditDateEdit.setOnClickListener {
            editDateVisibleToggle()
        }

        receiptEditDateApply.setOnClickListener {
            editDateApply()
        }

        receiptEditTimePicker.setIs24HourView(true)
        receiptEditTimePicker.isVisible = false
        receiptEditTimeApply.isVisible = false

        receiptEditTimeEdit.setOnClickListener {
            editTimeVisibleToggle()
        }

        receiptEditTimeApply.setOnClickListener {
            editTimeApply()
        }
    }

    private fun editDateVisibleToggle() {
        val visible = !receiptEditDatePicker.isVisible
        editDateVisibleSet(visible)
    }

    private fun editDateVisibleSet(visible: Boolean) {
        if (visible) {
            editTimeVisibleSet(false)
        }

        receiptEditDatePicker.isVisible = visible
        receiptEditDateApply.isVisible = visible

        if (visible) {
            val c = Calendar.getInstance()
            if (receipt.value != null) {
                c.timeInMillis = receipt.value!!.dateTime
            }
            receiptEditDatePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), null)
        }
    }

    private fun editDateApply() {
        val c = Calendar.getInstance()
        if (receipt.value != null) {
            c.timeInMillis = receipt.value!!.dateTime
        }
        c.set(Calendar.YEAR, receiptEditDatePicker.year)
        c.set(Calendar.MONTH, receiptEditDatePicker.month)
        c.set(Calendar.DAY_OF_MONTH, receiptEditDatePicker.dayOfMonth)

        val dateTime = c.timeInMillis

        lifecycleScope.launch {
            localRepository.setReceiptDateTime(receiptId, dateTime)
        }

        editDateVisibleSet(false)
    }

    private fun editTimeVisibleToggle() {
        val visible = !receiptEditTimePicker.isVisible
        editTimeVisibleSet(visible)
    }

    private fun editTimeVisibleSet(visible: Boolean) {
        if (visible) {
            editDateVisibleSet(false)
        }
        receiptEditTimePicker.isVisible = visible
        receiptEditTimeApply.isVisible = visible

        if (visible) {
            val c = Calendar.getInstance()
            if (receipt.value != null) {
                c.timeInMillis = receipt.value!!.dateTime
            }
            receiptEditTimePicker.hour = c.get(Calendar.HOUR)
            receiptEditTimePicker.minute = c.get(Calendar.MINUTE)
        }
    }

    private fun editTimeApply() {
        val c = Calendar.getInstance()
        if (receipt.value != null) {
            c.timeInMillis = receipt.value!!.dateTime
        }
        c.set(Calendar.HOUR, receiptEditTimePicker.hour)
        c.set(Calendar.MINUTE, receiptEditTimePicker.minute)

        val dateTime = c.timeInMillis

        lifecycleScope.launch {
            localRepository.setReceiptDateTime(receiptId, dateTime)
        }

        editTimeVisibleSet(false)
    }
}