package com.example.myapplication_mainmenu1

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

class MenuActivity : AppCompatActivity() {

    private val cartViewModel: CartViewModel by lazy {
        (application as RecipeApp).cartViewModel
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
        toolbar.setNavigationOnClickListener { finish() }

        rvMenu = findViewById(R.id.rvMenu)
        menuAdapter = MenuAdapter(menuItems) { menuItem ->
            addToCart(menuItem)
        }

        rvMenu.layoutManager = LinearLayoutManager(this)
        rvMenu.adapter = menuAdapter

        loadMenuItems()
    }

    private fun loadMenuItems() {
        menuItems.clear()
        menuItems.add(
            MenuItem(
                id = "m1",
                name = "Olive Oil",
                description = "Extra virgin olive oil, premium quality",
                price = 8.50,
                imageResId = android.R.drawable.ic_menu_gallery,
                category = "Oils"
            )
        )
        menuItems.add(
            MenuItem(
                id = "m2",
                name = "Parmesan Cheese",
                description = "Aged Italian parmesan cheese",
                price = 4.20,
                imageResId = android.R.drawable.ic_menu_camera,
                category = "Dairy"
            )
        )
        menuItems.add(
            MenuItem(
                id = "m3",
                name = "Fresh Tomatoes",
                description = "Organic cherry tomatoes",
                price = 3.50,
                imageResId = android.R.drawable.ic_menu_gallery,
                category = "Vegetables"
            )
        )
        menuItems.add(
            MenuItem(
                id = "m4",
                name = "Whole Wheat Pasta",
                description = "Organic whole wheat spaghetti",
                price = 5.00,
                imageResId = android.R.drawable.ic_menu_camera,
                category = "Grains"
            )
        )
        menuItems.add(
            MenuItem(
                id = "m5",
                name = "Fresh Basil",
                description = "Organic fresh basil leaves",
                price = 2.75,
                imageResId = android.R.drawable.ic_menu_gallery,
                category = "Herbs"
            )
        )
        menuItems.add(
            MenuItem(
                id = "m6",
                name = "Garlic",
                description = "Fresh garlic bulbs",
                price = 1.50,
                imageResId = android.R.drawable.ic_menu_camera,
                category = "Vegetables"
            )
        )
        menuAdapter.notifyDataSetChanged()
    }

    private fun addToCart(menuItem: MenuItem) {
        val isNewItem = cartViewModel.addToCart(menuItem)
        
        if (isNewItem) {
            Toast.makeText(
                this,
                "${menuItem.name} added to cart",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val currentItems = cartViewModel.cartItems.value ?: mutableListOf()
            val item = currentItems.find { it.id == menuItem.id }
            val quantity = item?.quantity ?: 1
            Toast.makeText(
                this,
                "${menuItem.name} quantity increased to $quantity",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
