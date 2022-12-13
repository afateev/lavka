package ru.ip_fateev.lavka

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ip_fateev.lavka.Inventory.Product

class InventoryAdapter internal constructor(context: Context?, private val onItemClicked: (position: Int) -> Unit) :
    RecyclerView.Adapter<InventoryAdapter.ViewHolder>() {
    private val inflater: LayoutInflater
    var productList: MutableList<Product> = ArrayList<Product>()

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.list_item_inventory, parent, false)
        return ViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product: Product = productList[position]
        holder.id.setText(product.id.toString())
        holder.name.setText(product.name)
        holder.price.setText(product.price.toString())
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun updateList(products: MutableList<Product>) {
        productList.clear()
        productList = products
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View, private val onItemClicked: (position: Int) -> Unit) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val id: TextView
        val name: TextView
        val price: TextView

        init {
            id = itemView.findViewById(R.id.id)
            name = itemView.findViewById(R.id.name)
            price = itemView.findViewById(R.id.price)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            onItemClicked(position)
        }
    }
}