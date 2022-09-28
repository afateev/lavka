package ru.ip_fateev.lavka

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.ip_fateev.lavka.Inventory.Product

class ReceiptActivity : AppCompatActivity() {
    lateinit var fabAdd: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)

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
                Toast.makeText(this, product.name, Toast.LENGTH_SHORT).show()
            }
        }
}