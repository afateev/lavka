package ru.ip_fateev.lavka

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import me.xdrop.fuzzywuzzy.FuzzySearch
import ru.ip_fateev.lavka.data.Product

class InventoryActivity : AppCompatActivity() {
    private lateinit var findInput: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InventoryAdapter
    private var productsAll: MutableLiveData<List<Product>> = MutableLiveData(arrayListOf())

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
                productsAll.value?.let { updateList(it) }
            }
        })

        adapter = InventoryAdapter(this){ position -> onListItemClick(position) }
        recyclerView.adapter = adapter

        productsAll.observe(this) {
            updateList(it)
        }

        App.getInstance().localRepository.getProductsLive().observe(this) {
            productsAll.postValue(it)
        }


    }

    private fun updateList(products: List<Product>) {
        adapter.updateList(applyFilter(products))
    }

    private fun applyFilter(products: List<Product>): MutableList<Product> {
        val s = findInput.text
        if (s.isEmpty())
        {
            return products.toMutableList()
        }

        val result = mutableListOf<Product>()

        //https://github.com/xdrop/fuzzywuzzy
        val filtered = FuzzySearch.extractSorted(s.toString(), products.toList(), { x: Product -> x.name }, 75)

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