package com.example.myapplication_mainmenu1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class OrderHistoryAdapter(
    private val orders: List<Order>,
    private val onClick: (Order) -> Unit
) : RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder>() {

    private val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_history, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int = orders.size

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDate: TextView = itemView.findViewById(R.id.tvOrderDate)
        private val tvTotal: TextView = itemView.findViewById(R.id.tvOrderTotal)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvOrderStatus)

        fun bind(order: Order) {
            tvDate.text = dateFormat.format(order.orderDate.toDate())
            tvTotal.text = "$${"%.2f".format(order.totalPrice)}"
            tvStatus.text = order.status

            itemView.setOnClickListener {
                onClick(order)
            }
        }
    }
}
