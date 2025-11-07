package com.example.resturantino.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.resturantino.models.Order
import com.example.resturantino.R

private val recyclerview: Any
    get() {
        TODO()
    }

//  Adapter for Order items
class OrderAdapter(private var items: List<Order> = emptyList()) :
    RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text1: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // use a simple Android built-in layout to avoid needing a custom item layout
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = items[position]
        // show order id and total (very basic)
        holder.text1.text = "Order ${order.id} - ${order.total}"
    }

    fun setOrders(newList: List<Order>) {
        items = newList
        notifyDataSetChanged()
    }
}
