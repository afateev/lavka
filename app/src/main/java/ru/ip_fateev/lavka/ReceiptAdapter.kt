package ru.ip_fateev.lavka

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ip_fateev.lavka.data.Product

class ReceiptAdapter internal constructor(context: Context?) :
    RecyclerView.Adapter<ReceiptAdapter.ViewHolder>() {
    private val inflater: LayoutInflater
    private var productList: MutableList<Product> = arrayListOf()

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.list_item_receipt, parent, false)
        return ViewHolder(view)
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

    fun productList() : List<Product> {
        return productList.toList()
    }

    fun updateList(products: List<Product>) {
        productList.clear()
        productList.addAll(products)
        notifyDataSetChanged()
    }

    fun clear() {
        productList.clear()
    }

    fun AddProduct(product: Product) {
        productList.add(product)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val id: TextView
        val name: TextView
        val price: TextView

        init {
            id = itemView.findViewById(R.id.id)
            name = itemView.findViewById(R.id.name)
            price = itemView.findViewById(R.id.price)
        }
    }
}