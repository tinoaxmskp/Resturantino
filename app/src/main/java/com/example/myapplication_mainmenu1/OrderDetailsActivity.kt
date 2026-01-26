package com.example.myapplication_mainmenu1

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.firestore.FirebaseFirestore

class OrderDetailsActivity : AppCompatActivity() {

    private lateinit var rvItems: RecyclerView
    private lateinit var tvOrderId: TextView
    private lateinit var tvOrderDate: TextView
    private lateinit var tvOrderStatus: TextView
    private lateinit var tvTotal: TextView

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        rvItems = findViewById(R.id.rvOrderItems)
        tvOrderId = findViewById(R.id.tvOrderId)
        tvOrderDate = findViewById(R.id.tvOrderDate)
        tvOrderStatus = findViewById(R.id.tvOrderStatus)
        tvTotal = findViewById(R.id.tvOrderTotal)

        rvItems.layoutManager = LinearLayoutManager(this)

        val orderId = intent.getStringExtra("orderId")
        if (orderId == null) {
            Toast.makeText(this, "Invalid order", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        loadOrder(orderId)
    }

    private fun loadOrder(orderId: String) {
        firestore.collection("orders")
            .document(orderId)
            .get()
            .addOnSuccessListener { doc ->
                val order = Order.fromDocument(doc)
                if (order == null) {
                    Toast.makeText(this, "Order not found", Toast.LENGTH_LONG).show()
                    finish()
                    return@addOnSuccessListener
                }

                tvOrderId.text = "Order #${order.orderId.take(8)}"
                tvOrderDate.text = order.orderDate.toDate().toString()
                tvOrderStatus.text = order.status
                tvTotal.text = "$${"%.2f".format(order.totalPrice)}"

                rvItems.adapter = OrderItemsAdapter(order.items)
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Failed to load order: ${e.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}
