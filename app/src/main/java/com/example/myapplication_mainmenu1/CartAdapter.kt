package com.example.myapplication_mainmenu1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    private val items: MutableList<CartItem>,
    private val onQuantityChanged: (Int) -> Unit,
    private val onRemove: (Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val tvName: TextView = view.findViewById(R.id.tvName)
        private val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        private val tvQuantity: TextView = view.findViewById(R.id.tvQuantity)
        private val ivProduct: ImageView = view.findViewById(R.id.ivProduct)
        private val btnAdd: Button = view.findViewById(R.id.btnAdd)
        private val btnMinus: Button = view.findViewById(R.id.btnMinus)
        private val btnRemove: Button = view.findViewById(R.id.btnRemove)

        fun bind(ci: CartItem, position: Int) {
            tvName.text = ci.name
            tvPrice.text = "$$%.2f".format(ci.price)
            tvQuantity.text = ci.quantity.toString()
            ivProduct.setImageResource(ci.imageResId)

            btnAdd.setOnClickListener {
                ci.quantity += 1
                tvQuantity.text = ci.quantity.toString()
                onQuantityChanged(position)
            }

            btnMinus.setOnClickListener {
                if (ci.quantity > 1) {
                    ci.quantity -= 1
                    tvQuantity.text = ci.quantity.toString()
                    onQuantityChanged(position)
                }
            }

            btnRemove.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onRemove(pos)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position], position)
    override fun getItemCount(): Int = items.size
}
