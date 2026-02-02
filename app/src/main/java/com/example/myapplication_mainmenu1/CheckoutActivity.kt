package com.example.myapplication_mainmenu1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CheckoutActivity : AppCompatActivity() {

    private val cartViewModel: CartViewModel by viewModels {
        CartViewModelFactory((application as RecipeApp).cartRepository)
    }

    private lateinit var rvCheckoutItems: RecyclerView
    private lateinit var tvItemCount: TextView
    private lateinit var tvSubtotal: TextView
    private lateinit var tvTax: TextView
    private lateinit var tvTotal: TextView


    private lateinit var btnPlaceOrder: MaterialButton
    private lateinit var cardSuccess: View
    private lateinit var checkoutContent: View

    private val items = mutableListOf<CartItem>()
    private lateinit var adapter: CartAdapter

    private val TAX_RATE = 0.08

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // 🔹 Toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        // 🔹 Bind views
        rvCheckoutItems = findViewById(R.id.rvCheckoutItems)
        tvItemCount = findViewById(R.id.tvItemCount)
        tvSubtotal = findViewById(R.id.tvSubtotal)
        tvTax = findViewById(R.id.tvTax)
        tvTotal = findViewById(R.id.tvTotal)
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
        cardSuccess = findViewById(R.id.cardSuccess)
        checkoutContent = findViewById(R.id.checkoutContent)

        // 🔹 Adapter (editable checkout)
        adapter = CartAdapter(
            items,
            onQuantityChanged = { item ->
                cartViewModel.updateQuantity(item.id, item.quantity)
            },
            onRemove = { item ->
                cartViewModel.removeFromCart(item.id)
            }
        )

        rvCheckoutItems.layoutManager = LinearLayoutManager(this)
        rvCheckoutItems.adapter = adapter

        // 🔹 Observe cart items
        cartViewModel.cartItems.observe(this) { cartItems ->
            items.clear()
            items.addAll(cartItems)
            adapter.notifyDataSetChanged()

            val itemCount = cartItems.sumOf { it.quantity }
            val subtotal = cartItems.sumOf { it.price * it.quantity }
            val tax = subtotal * TAX_RATE
            val total = subtotal + tax

            tvItemCount.text = "$itemCount items"
            tvSubtotal.text = "$${"%.2f".format(subtotal)}"
            tvTax.text = "$${"%.2f".format(tax)}"
            tvTotal.text = "$${"%.2f".format(total)}"

            btnPlaceOrder.isEnabled = cartItems.isNotEmpty()
        }

        // 🔹 Place order → SUCCESS FLOW
        fun placeOrder() {
            val user = FirebaseAuth.getInstance().currentUser ?: return

            val order = Order(
                userId = user.uid,
                userEmail = user.email ?: "unknown",
                items = items.map {
                    OrderItem(
                        id = it.id,
                        name = it.name,
                        price = it.price,
                        quantity = it.quantity
                    )
                },
                totalPrice = items.sumOf { it.price * it.quantity },
                itemCount = items.sumOf { it.quantity },
                paymentMethod = "Cash",
                status = "Completed"
            )

            FirebaseFirestore.getInstance()
                .collection("orders")
                .add(order.toMap())
                .addOnSuccessListener {

                    // ✅ clear cart ONLY after order is saved
                    cartViewModel.clearCart()

                    // ✅ show success UI
                    checkoutContent.visibility = View.GONE
                    cardSuccess.visibility = View.VISIBLE

                    // ✅ wait 5 seconds, then go Home (not Cart)
                    cardSuccess.postDelayed({

                        val intent = Intent(this, HomeActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                        startActivity(intent)
                        finish()

                    }, 5000)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show()
                }
        }

// 🔹 Place Order button — ONLY calls placeOrder()
        btnPlaceOrder.setOnClickListener {
            placeOrder()
        }
    }
    }

