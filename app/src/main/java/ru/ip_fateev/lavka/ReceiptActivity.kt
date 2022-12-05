package ru.ip_fateev.lavka

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import ru.ip_fateev.lavka.Inventory.Product
import ru.ip_fateev.lavka.documents.*
import java.util.*

class ReceiptActivity : AppCompatActivity() {
    companion object {
        val EXTRA_RECEIPT_TYPE = "ReceiptType"
        val RECEIPT_TYPE_NONE = ReceiptType.NONE.ordinal
        val RECEIPT_TYPE_SELL = ReceiptType.SELL.ordinal
    }

    lateinit var fabAdd: FloatingActionButton
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

        val recyclerView = findViewById<RecyclerView>(R.id.receipt_list)
        adapter = ReceiptAdapter(this)
        recyclerView.adapter = adapter

        documents = LocalDatabase.instance(this)

        val localRepository = App.getInstance()?.getRepository()
        localRepository?.getActiveSellReceipt()?.observe(this) {
            if (it == null) {
                val receipt = Receipt(id = 0, uuid = UUID.randomUUID(), type = receiptType, state = ReceiptState.NEW)
                lifecycleScope.launch {
                    localRepository.insertReceipt(receipt)
                }
            } else {
                adapter.clear()
                receiptId = it.id
                localRepository.getPositionsLive(it.id).observe(this) { positions ->
                    val pList = mutableListOf<Product>()
                    for (i in positions) {
                        val p = Product()
                        p.id = i.productId
                        p.name = i.productName
                        p.price = i.price
                        pList.add(p)
                    }
                    adapter.updateList(pList)
                }
            }
        }

        fabAdd = findViewById(R.id.fabAdd)

        fabAdd.setOnClickListener{
            RequestItem()
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
                    val localRepository = App.getInstance()?.getRepository()
                    val i = adapter.itemCount
                    val position = Position(0, docId = receiptId!!, number = i, productId = product.id, productName = product.name, price = product.price, quantity = 1.0)
                    lifecycleScope.launch {
                        localRepository?.insertPosition(position)
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

    fun onClickPay(view: View) {
        payReceipt.launch(receiptId)
    }
}