package ru.ip_fateev.lavka

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import ru.ip_fateev.lavka.Inventory.Product
import ru.ip_fateev.lavka.documents.*

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
        val receiptLive = localRepository?.newSellReceipt
        if (receiptLive != null) {
            receiptLive.observe(this) {
                receipt -> receipt.let {
                    if (it == null) {
                        val receipt = Receipt(id = 0, type = receiptType, state = ReceiptState.NEW)
                        lifecycleScope.launch {
                            if (localRepository != null) {
                                localRepository.insertReceipt(receipt)
                            }
                        }
                    } else {
                        adapter.clear()
                        receiptId = it.id
                        val positionsLive = localRepository?.getPositions(it.id)
                        if (positionsLive != null) {
                            positionsLive.observe(this) {
                                positions -> positions.let {
                                    if (it != null) {
                                        var pList = mutableListOf<Product>()
                                        for (i in positions) {
                                            val p = Product()
                                            p.id = i.productId
                                            p.name = i.productName
                                            pList.add(p)
                                        }
                                        adapter.updateList(pList)
                                    }
                                }
                            }
                        }
                    }
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
                    val position = Position(0, docId = receiptId!!, number = i, productId = product.id, productName = product.name)
                    lifecycleScope.launch {
                        localRepository?.insertPosition(position)
                    }
                }
                //adapter.AddProduct(product)
            }
        }

    fun onClickPay(view: View) {
        val intent = Intent()
        intent.putExtra("receiptId", receiptId)
        setResult(RESULT_OK, intent)
        finish()
    }
}