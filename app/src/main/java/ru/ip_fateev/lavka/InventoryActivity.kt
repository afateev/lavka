package ru.ip_fateev.lavka

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.xdrop.fuzzywuzzy.FuzzySearch
import ru.ip_fateev.lavka.data.Product

class InventoryActivity : AppCompatActivity() {
    private lateinit var findInput: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InventoryAdapter
    private val searchString = MutableStateFlow("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        findInput = findViewById(R.id.inventory_findInput)
        recyclerView = findViewById(R.id.inventory_list)

        findInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchString.value = s.toString()
            }
        })

        adapter = InventoryAdapter(this){ position -> onListItemClick(position) }
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            var tmp: String? = null
            var s: String? = null
            while (true) {
                val newS = searchString.value
                if (tmp != newS) {
                    tmp = newS
                    delay(500)
                    continue
                }
                if (s != tmp)
                {
                    s = tmp
                    Log.d("InventoryActivity", "Search $s")
                    val products = App.getInstance().localRepository.getProducts()
                    val list = filter(products, s)
                    adapter.updateList(list)
                }
                else
                {
                    delay(100)
                }
            }
        }
    }

    private fun filter(products: List<Product>, s: String): List<Product> {
        if (s.isEmpty())
        {
            return products.toMutableList()
        }

        val result = mutableListOf<Product>()

        //https://github.com/xdrop/fuzzywuzzy
        val filtered = FuzzySearch.extractSorted(s, products.toList(), { x: Product -> x.name }, 75)

        for (r in filtered) {
            result.add(products[r.index])
        }

        return result
    }

    private fun onListItemClick(position: Int) {
        val product = adapter.productList.get(position)

        val intent = Intent()
        intent.putExtra("product", product)
        setResult(RESULT_OK, intent)
        finish()
    }
}