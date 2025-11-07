package com.example.resturantino.ui.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.resturantino.R
import com.example.resturantino.models.MenuItem

//  Adapter for menu items
class MenuAdapter(private var items: List<MenuItem> = emptyList()) :
    RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    // 🔹 ViewHolder holds one menu item layout
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.itemTitle)
    }

    // 🔹 Inflate item_menu.xml for each row
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu, parent, false)
        return ViewHolder(view)
    }

    // 🔹 Return total number of items
    override fun getItemCount(): Int = items.size

    // 🔹 Bind each MenuItem’s title
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleText.text = items[position].title
    }

    // 🔹 Update data later
    fun setItems(newItems: List<MenuItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
