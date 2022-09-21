package ru.ip_fateev.lavka

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import ru.ip_fateev.lavka.Inventory.DownloadDataJobService

class InventoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        DownloadDataJobService.scheduleDownloadDataJob(applicationContext)

        val recyclerView = findViewById<RecyclerView>(R.id.list)
        val adapter = InventoryAdapter(this)
        recyclerView.adapter = adapter

        val app = application as? App
        val products = app?.getInventory()!!
            .getProductListLive(
                this
            ) { products -> adapter.updateList(products) }
    }
}