package ru.ip_fateev.lavka

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.ip_fateev.lavka.Inventory.Product

class ReceiptActivity : AppCompatActivity() {
    companion object {
        val EXTRA_RECEIPT_TYPE = "ReceiptType"
        val RECEIPT_TYPE_NONE = 0
        val RECEIPT_TYPE_SELL = 1
    }

    lateinit var fabAdd: FloatingActionButton
    lateinit var adapter: ReceiptAdapter
    var receiptType: Int = RECEIPT_TYPE_NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)

        receiptType = intent.getIntExtra(EXTRA_RECEIPT_TYPE, RECEIPT_TYPE_NONE)
        val viewReceiptType = findViewById<TextView>(R.id.receiptActivityReceiptType)
        when (receiptType) {
            RECEIPT_TYPE_SELL -> viewReceiptType.text = "ПРОДАЖА"
        }

        val recyclerView = findViewById<RecyclerView>(R.id.receipt_list)
        adapter = ReceiptAdapter(this)
        recyclerView.adapter = adapter

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
                adapter.AddProduct(product)
            }
        }
}