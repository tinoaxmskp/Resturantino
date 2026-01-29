package com.example.myapplication_mainmenu1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MenuAdapter(
    private val items: List<MenuItem>,
    private val onAddToCart: (MenuItem) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgFood)
        val name: TextView = view.findViewById(R.id.txtTitle)
        val price: TextView = view.findViewById(R.id.txtPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food_grid, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = items[position]
        holder.img.setImageResource(item.imageResId)
        holder.name.text = item.name
        holder.price.text = "${item.price.toInt()} zł"

        holder.itemView.setOnClickListener {
            onAddToCart(item)
        }
    }

    override fun getItemCount(): Int = items.size
}
