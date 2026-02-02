package com.example.myapplication_mainmenu1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    private val items: MutableList<CartItem>,
    private val onQuantityChanged: (CartItem) -> Unit,
    private val onRemove: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvName)
        val price: TextView = view.findViewById(R.id.tvPrice)
        val qty: TextView = view.findViewById(R.id.tvQuantity)
        val add: Button = view.findViewById(R.id.btnAdd)
        val minus: Button = view.findViewById(R.id.btnMinus)
        val remove: Button = view.findViewById(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]

        holder.name.text = item.name
        holder.price.text = "$${"%.2f".format(item.price)}"
        holder.qty.text = item.quantity.toString()

        holder.add.setOnClickListener {
            onQuantityChanged(
                item.copy(quantity = item.quantity + 1)
            )
        }

        holder.minus.setOnClickListener {
            if (item.quantity > 1) {
                onQuantityChanged(
                    item.copy(quantity = item.quantity - 1)
                )
            }
        }

        holder.remove.setOnClickListener {
            onRemove(item)
        }
    }

    override fun getItemCount(): Int = items.size
}

