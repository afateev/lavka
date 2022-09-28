package ru.ip_fateev.lavka

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import ru.ip_fateev.lavka.Inventory.Product

class InventoryActivity : AppCompatActivity() {
    private lateinit var products: LiveData<List<Product>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        val recyclerView = findViewById<RecyclerView>(R.id.inventory_list)
        val adapter = InventoryAdapter(this){ position -> onListItemClick(position) }
        recyclerView.adapter = adapter

        val app = application as? App
        products = app?.getInventory()!!
            .getProductListLive(
                this
            ) { products -> adapter.updateList(products) }
    }

    private fun onListItemClick(position: Int) {
        val product = products?.value?.get(position)

        val intent = Intent()
        intent.putExtra("product", product)
        setResult(RESULT_OK, intent)
        finish()
    }
}