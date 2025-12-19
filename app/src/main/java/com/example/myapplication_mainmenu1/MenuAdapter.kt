package com.example.myapplication_mainmenu1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class MenuAdapter(
    private val items: List<MenuItem>,
    private val onAddToCart: (MenuItem) -> Unit
) : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ivMenuItem: ImageView = view.findViewById(R.id.ivMenuItem)
        private val tvMenuItemName: TextView = view.findViewById(R.id.tvMenuItemName)
        private val tvMenuItemDescription: TextView = view.findViewById(R.id.tvMenuItemDescription)
        private val tvMenuItemPrice: TextView = view.findViewById(R.id.tvMenuItemPrice)
        private val btnAddToCart: MaterialButton = view.findViewById(R.id.btnAddToCart)

        fun bind(menuItem: MenuItem) {
            ivMenuItem.setImageResource(menuItem.imageResId)
            tvMenuItemName.text = menuItem.name
            tvMenuItemDescription.text = menuItem.description
            tvMenuItemPrice.text = "$$%.2f".format(menuItem.price)

            btnAddToCart.setOnClickListener {
                onAddToCart(menuItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

