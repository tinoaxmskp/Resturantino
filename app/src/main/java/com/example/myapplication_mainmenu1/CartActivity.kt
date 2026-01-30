package com.example.myapplication_mainmenu1

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class CartActivity : AppCompatActivity() {

    private lateinit var adapter: CartAdapter
    private lateinit var rvCart: RecyclerView
    private lateinit var tvTotal: TextView
    private lateinit var btnCheckout: MaterialButton

    private val cartViewModel: CartViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)  
        setContentView(R.layout.activity_cart)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        rvCart = findViewById(R.id.rvCart)
        tvTotal = findViewById(R.id.tvTotal)
        btnCheckout = findViewById(R.id.btnCheckout)

        val items = mutableListOf<CartItem>()
        adapter = CartAdapter(items,
            onQuantityChanged = { 
                // Update quantity in ViewModel
                val item = items[it]
                cartViewModel.updateQuantity(item.id, item.quantity)
            },
            onRemove = { position ->
                // Remove from ViewModel
                val item = items[position]
                cartViewModel.removeFromCart(item.id)
            }
        )

        rvCart.layoutManager = LinearLayoutManager(this)
        rvCart.adapter = adapter

        // Observe cart items from ViewModel
        cartViewModel.cartItems.observe(this, Observer { cartItems ->
            items.clear()
            items.addAll(cartItems)
            adapter.notifyDataSetChanged()
            updateTotal()
        })

        // Observe total price from ViewModel
        cartViewModel.totalPrice.observe(this, Observer { total ->
            tvTotal.text = "$$%.2f".format(total)
        })

        btnCheckout.setOnClickListener {
            val itemCount = cartViewModel.cartItemCount.value ?: 0
            if (itemCount == 0) {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, CheckoutActivity::class.java))
            }
        }

        // Load initial cart state
        val currentItems = cartViewModel.cartItems.value
        if (currentItems != null && currentItems.isNotEmpty()) {
            items.clear()
            items.addAll(currentItems)
            adapter.notifyDataSetChanged()
        }
        updateTotal()
    }

    private fun updateTotal() {
        val total = cartViewModel.getTotalPrice()
        tvTotal.text = "$$%.2f".format(total)
    }
}
