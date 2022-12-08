package ru.ip_fateev.lavka

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import ru.ip_fateev.lavka.Inventory.Product
import ru.ip_fateev.lavka.data.*
import java.text.SimpleDateFormat
import java.util.*

class ReceiptActivity : AppCompatActivity() {
    companion object {
        val EXTRA_RECEIPT_TYPE = "ReceiptType"
        val RECEIPT_TYPE_NONE = ReceiptType.NONE.ordinal
        val RECEIPT_TYPE_SELL = ReceiptType.SELL.ordinal
    }

    lateinit var receiptActivityReceiptInfo: TextView
    lateinit var receiptActivityReceiptEdit: ImageButton
    lateinit var receiptActivityReceiptTotal: TextView
    lateinit var fabAdd: FloatingActionButton
    lateinit var receiptPay: Button
    lateinit var adapter: ReceiptAdapter
    lateinit var documents: LocalDatabase
    var receiptType: ReceiptType = ReceiptType.NONE
    var receiptId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)

        receiptType = ReceiptType.getByValue(intent.getIntExtra(EXTRA_RECEIPT_TYPE, RECEIPT_TYPE_NONE))

        val viewReceiptType = findViewById<TextView>(R.id.receiptActivityReceiptType)
        when (receiptType) {
            ReceiptType.NONE -> viewReceiptType.text = "ТИП ЧЕКА НЕ ЗАДАН"
            ReceiptType.SELL -> viewReceiptType.text = "ПРОДАЖА"
        }

        receiptActivityReceiptInfo = findViewById(R.id.receiptActivityReceiptInfo)
        receiptActivityReceiptEdit = findViewById(R.id.receiptActivityReceiptEdit)

        receiptActivityReceiptEdit.setOnClickListener {
            editReceipt.launch(receiptId)
        }

        receiptActivityReceiptTotal = findViewById(R.id.receiptActivityReceiptTotal)

        val recyclerView = findViewById<RecyclerView>(R.id.receipt_list)
        adapter = ReceiptAdapter(this)
        recyclerView.adapter = adapter

        documents = LocalDatabase.instance(this)

        val localRepository = App.getInstance().getRepository()
        localRepository.getActiveSellReceipt().observe(this) {
            if (it == null) {
                val receipt = Receipt(id = 0, uuid = UUID.randomUUID(), dateTime = Calendar.getInstance().timeInMillis, type = receiptType, state = ReceiptState.NEW)
                lifecycleScope.launch {
                    localRepository.insertReceipt(receipt)
                }
            } else {
                adapter.clear()
                receiptId = it.id

                val dateTime = Calendar.getInstance()
                dateTime.timeInMillis = it.dateTime
                val dateTimeStr = SimpleDateFormat("dd.MM.yyyy HH:mm").format(dateTime.time)
                receiptActivityReceiptInfo.text = "№" + it.id.toString() + " от " + dateTimeStr



                localRepository.getPositionsLive(it.id).observe(this) { positions ->
                    val pList = mutableListOf<Product>()
                    var sum = 0.0

                    for (i in positions) {
                        val p = Product()
                        p.id = i.productId
                        p.name = i.productName
                        p.price = i.price
                        pList.add(p)

                        sum += 1.0 * p.price
                    }
                    adapter.updateList(pList)

                    receiptActivityReceiptTotal.text = sum.toString() + " Р"
                    receiptPay.isEnabled = positions.size > 0
                }
            }
        }

        fabAdd = findViewById(R.id.fabAdd)

        fabAdd.setOnClickListener{
            RequestItem()
        }

        receiptPay = findViewById(R.id.receiptPay)
        receiptPay.setOnClickListener {
            payReceipt.launch(receiptId)
        }
    }

    fun RequestItem(){
        var intent = Intent(this, InventoryActivity::class.java)
        requestItem.launch(intent)
    }

    private val requestItem =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val product = it.data?.getSerializableExtra("product") as Product
                if (receiptId != null) {
                    val localRepository = App.getInstance().getRepository()
                    val i = adapter.itemCount
                    val position = Position(0, docId = receiptId!!, number = i, productId = product.id, productName = product.name, price = product.price, quantity = 1.0)
                    lifecycleScope.launch {
                        localRepository.insertPosition(position)
                    }
                }
                //adapter.AddProduct(product)
            }
        }

    class PayReceipt : ActivityResultContract<Long, Long?>() {
        override fun createIntent(context: Context, input: Long): Intent =
            Intent(context, PayActivity::class.java).apply {
                action = "ru.ip_fateev.lavka.ACTION_PAY_RECEIPT"
                putExtra(PayActivity.EXTRA_RECEIPT_ID, input)
            }

        override fun parseResult(resultCode: Int, intent: Intent?): Long? {
            if (resultCode != Activity.RESULT_OK) {
                return null
            }
            return intent?.getLongExtra("receiptId", 0)
        }
    }

    private val payReceipt = registerForActivityResult(PayReceipt()) {
        if (it != null) {
            val intent = Intent()
            intent.putExtra("receiptId", receiptId)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    class EditReceipt : ActivityResultContract<Long, Boolean?>() {
        override fun createIntent(context: Context, input: Long): Intent =
            Intent(context, ReceiptEditActivity::class.java).apply {
                action = "ru.ip_fateev.lavka.ACTION_EDIT_RECEIPT"
                putExtra(ReceiptEditActivity.EXTRA_RECEIPT_ID, input)
            }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean? {
            if (resultCode != Activity.RESULT_OK) {
                return null
            }
            return true
        }
    }

    private val editReceipt = registerForActivityResult(EditReceipt()) {
        if (it != null) {

        }
    }
}