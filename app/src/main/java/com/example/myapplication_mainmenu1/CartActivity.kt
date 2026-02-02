package com.example.myapplication_mainmenu1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {

    private val cartViewModel: CartViewModel by viewModels {
        CartViewModelFactory((application as RecipeApp).cartRepository)
    }

    private lateinit var rvCart: RecyclerView
    private lateinit var tvTotal: TextView
    private lateinit var btnCheckout: Button
    private lateinit var tvEmptyCart: TextView

    private val list = mutableListOf<CartItem>()
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        rvCart = findViewById(R.id.rvCart)
        btnCheckout = findViewById(R.id.btnCheckout)
        tvEmptyCart = findViewById(R.id.tvEmptyCart)

        adapter = CartAdapter(
            list,
            onQuantityChanged = {
                cartViewModel.updateQuantity(it.id, it.quantity)
            },
            onRemove = {
                cartViewModel.removeFromCart(it.id)
            }
        )

        rvCart.layoutManager = LinearLayoutManager(this)
        rvCart.adapter = adapter

        // 🔹 Observe cart items (EMPTY STATE LOGIC)
        cartViewModel.cartItems.observe(this) { items ->
            list.clear()
            list.addAll(items)
            adapter.notifyDataSetChanged()

            val isEmpty = items.isEmpty()

            tvEmptyCart.visibility = if (isEmpty) View.VISIBLE else View.GONE
            rvCart.visibility = if (isEmpty) View.GONE else View.VISIBLE
            btnCheckout.isEnabled = !isEmpty
        }

        // 🔹 Observe total price

        btnCheckout.setOnClickListener {
            startActivity(Intent(this, CheckoutActivity::class.java))
        }
    }
}

