package com.example.myapplication_mainmenu1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderItemsAdapter(
    private val items: List<OrderItem>
) : RecyclerView.Adapter<OrderItemsAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(android.R.id.text1)
        private val tvPrice: TextView = itemView.findViewById(android.R.id.text2)

        fun bind(item: OrderItem) {
            tvName.text = "${item.name} x${item.quantity}"
            tvPrice.text = "$${"%.2f".format(item.price * item.quantity)}"
        }
    }
}
