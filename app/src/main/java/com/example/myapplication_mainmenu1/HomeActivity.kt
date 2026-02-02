// HomeActivity.kt
package com.example.myapplication_mainmenu1

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth


class HomeActivity : AppCompatActivity() {

    private val cartViewModel: CartViewModel by viewModels {
        CartViewModelFactory((application as RecipeApp).cartRepository)
    }


    private lateinit var tvCartBadge: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val menuCard = findViewById<MaterialCardView>(R.id.cardMenu)
        val recipesCard = findViewById<MaterialCardView>(R.id.cardRecipes)
        val cartCard = findViewById<MaterialCardView>(R.id.cardCart)
        val historyCard = findViewById<MaterialCardView>(R.id.cardHistory)
        val btnLogout = findViewById<MaterialButton>(R.id.btnLogout)
        tvCartBadge = findViewById(R.id.tvCartBadge)

        // Observe cart count and update badge
        cartViewModel.cartItemCount.observe(this, Observer { count ->
            updateCartBadge(count)
        })

        menuCard.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }

        recipesCard.setOnClickListener {
            startActivity(Intent(this, RecipeListActivity::class.java))
        }

        cartCard.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        historyCard.setOnClickListener {
            startActivity(Intent(this, OrderHistoryActivity::class.java))
        }
        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Update badge on resume to reflect current cart state
        updateCartBadge(cartViewModel.getItemCount())
    }

    override fun onResume() {
        super.onResume()
        // Refresh cart badge when returning to home
        updateCartBadge(cartViewModel.getItemCount())
    }

    private fun updateCartBadge(count: Int) {
        if (count > 0) {
            tvCartBadge.text = if (count > 99) "99+" else count.toString()
            tvCartBadge.visibility = TextView.VISIBLE
        } else {
            tvCartBadge.visibility = TextView.GONE
        }
    }
}
