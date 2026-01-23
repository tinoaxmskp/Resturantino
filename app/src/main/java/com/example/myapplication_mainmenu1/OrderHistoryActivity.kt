package com.example.myapplication_mainmenu1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class OrderHistoryActivity : AppCompatActivity() {

    private lateinit var rvOrders: RecyclerView
    private lateinit var tvEmpty: TextView

    private val orders = mutableListOf<Order>()
    private lateinit var adapter: OrderHistoryAdapter

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        rvOrders = findViewById(R.id.rvOrders)
        tvEmpty = findViewById(R.id.tvEmpty)

        adapter = OrderHistoryAdapter(orders) { order ->
            val intent = Intent(this, OrderDetailsActivity::class.java)
            intent.putExtra("orderId", order.orderId)
            startActivity(intent)
        }

        rvOrders.layoutManager = LinearLayoutManager(this)
        rvOrders.adapter = adapter

        loadOrders()
    }

    private fun loadOrders() {
        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(this, "Please log in to view orders", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        firestore.collection("orders")
            .whereEqualTo("userId", user.uid)
            .orderBy("orderDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                orders.clear()

                for (doc in snapshot.documents) {
                    val order = Order.fromDocument(doc)
                    if (order != null) {
                        orders.add(order)
                    }
                }

                adapter.notifyDataSetChanged()

                tvEmpty.visibility = if (orders.isEmpty()) View.VISIBLE else View.GONE
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Failed to load orders: ${e.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}

