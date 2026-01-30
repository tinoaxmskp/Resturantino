package com.example.myapplication_mainmenu1

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp

class CheckoutActivity : AppCompatActivity() {

    private lateinit var rvCheckoutItems: RecyclerView
    private lateinit var tvItemCount: TextView
    private lateinit var tvSubtotal: TextView
    private lateinit var tvTotal: TextView
    private lateinit var btnPlaceOrder: MaterialButton
    private lateinit var radioGroupPayment: RadioGroup
    private lateinit var cardSuccess: MaterialCardView
    private lateinit var tvSuccessMessage: TextView

    private val cartViewModel: CartViewModel by lazy {
        (application as RecipeApp).cartViewModel
    }
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private var checkoutAdapter: CartAdapter? = null
    private val checkoutItems = mutableListOf<CartItem>()
    private var selectedPaymentMethod: String = "Credit Card"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        rvCheckoutItems = findViewById(R.id.rvCheckoutItems)
        tvItemCount = findViewById(R.id.tvItemCount)
        tvSubtotal = findViewById(R.id.tvSubtotal)
        tvTotal = findViewById(R.id.tvTotal)
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
        radioGroupPayment = findViewById(R.id.radioGroupPayment)
        cardSuccess = findViewById(R.id.cardSuccess)
        tvSuccessMessage = findViewById(R.id.tvSuccessMessage)

        cardSuccess.visibility = View.GONE

        // Set up RecyclerView
        checkoutAdapter = CartAdapter(checkoutItems,
            onQuantityChanged = { position ->
                val item = checkoutItems[position]
                cartViewModel.updateQuantity(item.id, item.quantity)
            },
            onRemove = { position ->
                val item = checkoutItems[position]
                cartViewModel.removeFromCart(item.id)
            }
        )
        rvCheckoutItems.layoutManager = LinearLayoutManager(this)
        rvCheckoutItems.adapter = checkoutAdapter

        // Observe cart items
        cartViewModel.cartItems.observe(this, Observer { items ->
            checkoutItems.clear()
            checkoutItems.addAll(items)
            checkoutAdapter?.notifyDataSetChanged()
            updateTotals()
        })

        // Observe total price
        cartViewModel.totalPrice.observe(this, Observer { _ ->
            updateTotals()
        })

        // Payment method selection
        radioGroupPayment.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            selectedPaymentMethod = radioButton?.text?.toString() ?: "Credit Card"
        }

        // Place order button
        btnPlaceOrder.setOnClickListener {
            if (checkoutItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show()
            } else {
                placeOrder()
            }
        }

        // Initialize payment method
        val defaultRadio = findViewById<RadioButton>(R.id.radioCreditCard)
        defaultRadio?.isChecked = true

        updateTotals()
    }

    private fun updateTotals() {
        val itemCount = checkoutItems.sumOf { it.quantity }
        val subtotal = checkoutItems.sumOf { it.price * it.quantity }
        val tax = subtotal * 0.08 // 8% tax
        val total = subtotal + tax

        tvItemCount.text = getString(R.string.total_items, itemCount)
        tvSubtotal.text = "$$%.2f".format(subtotal)
        findViewById<TextView>(R.id.tvTax).text = "$$%.2f".format(tax)
        tvTotal.text = "$$%.2f".format(total)
    }

    private fun placeOrder() {
        btnPlaceOrder.isEnabled = false
        btnPlaceOrder.text = "Processing..."

        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Please log in to place an order", Toast.LENGTH_LONG).show()
            btnPlaceOrder.isEnabled = true
            btnPlaceOrder.text = "Place Order"
            return
        }

        val userId = currentUser.uid

        val items = checkoutItems.map { cartItem ->
            OrderItem(
                id = cartItem.id,
                name = cartItem.name,
                price = cartItem.price,
                quantity = cartItem.quantity,
                imageResId = cartItem.imageResId
            )
        }

        val order = Order(
            orderId = "",
            userId = userId,
            items = items,
            totalPrice = checkoutItems.sumOf { it.price * it.quantity },
            itemCount = checkoutItems.sumOf { it.quantity },
            paymentMethod = selectedPaymentMethod,
            orderDate = Timestamp.now(),
            status = "Confirmed"
        )

        firestore.collection("orders")
            .add(order.toMap())
            .addOnSuccessListener { doc ->
                showSuccessAnimation(doc.id)
                cartViewModel.clearCart()
            }
            .addOnFailureListener { e ->
                btnPlaceOrder.isEnabled = true
                btnPlaceOrder.text = "Place Order"

                Toast.makeText(
                    this,
                    "Failed to place order: ${e.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }




    private fun showSuccessAnimation(orderId: String) {
        cardSuccess.visibility = View.VISIBLE
        tvSuccessMessage.text = "Order #${orderId.take(8)} placed successfully!"

        // Animate success card appearance
        cardSuccess.alpha = 0f
        cardSuccess.scaleX = 0f
        cardSuccess.scaleY = 0f

        val scaleX = ObjectAnimator.ofFloat(cardSuccess, View.SCALE_X, 0f, 1.1f, 1f)
        val scaleY = ObjectAnimator.ofFloat(cardSuccess, View.SCALE_Y, 0f, 1.1f, 1f)
        val alpha = ObjectAnimator.ofFloat(cardSuccess, View.ALPHA, 0f, 1f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY, alpha)
        animatorSet.duration = 600
        animatorSet.start()

        // Hide checkout content
        findViewById<View>(R.id.checkoutContent).alpha = 1f
        val fadeOut = ObjectAnimator.ofFloat(findViewById<View>(R.id.checkoutContent), View.ALPHA, 1f, 0f)
        fadeOut.duration = 300
        fadeOut.start()
        
        fadeOut.addUpdateListener {
            if (it.animatedValue as Float <= 0.5f) {
                findViewById<View>(R.id.checkoutContent).visibility = View.GONE
            }
        }

        // Navigate to home after delay
        cardSuccess.postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }, 3000)
    }
}

