package com.example.myapplication_mainmenu1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.firestore.FirebaseFirestore

class MenuActivity : AppCompatActivity() {

    private val cartViewModel: CartViewModel by viewModels {
        CartViewModelFactory((application as RecipeApp).cartRepository)
    }


    private lateinit var rvMenu: RecyclerView
    private lateinit var menuAdapter: MenuAdapter
    private val menuItems = mutableListOf<MenuItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rvMenu = findViewById(R.id.rvMenu)

        menuAdapter = MenuAdapter(menuItems) {
            cartViewModel.addToCart(it)
            Toast.makeText(this, "${it.name} added to cart", Toast.LENGTH_SHORT).show()
        }

        rvMenu.layoutManager = LinearLayoutManager(this)

        rvMenu.adapter = menuAdapter

        loadMenuItems()
    }

    private fun loadMenuItems() {
        FirebaseFirestore.getInstance()
            .collection("recipes")
            .whereEqualTo("isActive", true)
            .get()
            .addOnSuccessListener { snapshot ->
                menuItems.clear()

                for (doc in snapshot.documents) {
                    val item = MenuItem(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        price = doc.getDouble("price") ?: 0.0,
                        category = doc.getString("category") ?: "General"
                    )
                    menuItems.add(item)
                }

                menuAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load menu", Toast.LENGTH_SHORT).show()
            }
    }

}
