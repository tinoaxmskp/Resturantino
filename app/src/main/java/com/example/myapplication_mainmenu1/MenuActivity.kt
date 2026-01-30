package com.example.myapplication_mainmenu1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

class MenuActivity : AppCompatActivity() {

    private val cartViewModel: CartViewModel by viewModels()


    private lateinit var rvMenu: RecyclerView
    private lateinit var menuAdapter: MenuAdapter
    private val menuItems = mutableListOf<MenuItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        // RecyclerView
        rvMenu = findViewById(R.id.rvMenu)
        menuAdapter = MenuAdapter(menuItems) { menuItem ->
            addToCart(menuItem)
        }

        // GRID layout (2 columns)
        rvMenu.layoutManager = GridLayoutManager(this, 2)
        rvMenu.adapter = menuAdapter

        loadMenuItems()
    }

    // Static menu data (UI ONLY, temporary)
    private fun loadMenuItems() {
        menuItems.clear()

        menuItems.add(
            MenuItem(
                id = "m1",
                name = "Classic Burger",
                description = "Juicy beef burger with cheese and lettuce",
                price = 42.0,   // zł
                imageResId = android.R.drawable.ic_menu_gallery,
                category = "Main"
            )
        )

        menuItems.add(
            MenuItem(
                id = "m2",
                name = "Pepperoni Pizza",
                description = "Stone baked pizza with pepperoni",
                price = 45.0,   // zł
                imageResId = android.R.drawable.ic_menu_camera,
                category = "Main"
            )
        )

        menuItems.add(
            MenuItem(
                id = "m3",
                name = "Grilled Chicken",
                description = "Grilled chicken with herbs",
                price = 48.0,   // zł
                imageResId = android.R.drawable.ic_menu_gallery,
                category = "Main"
            )
        )

        menuItems.add(
            MenuItem(
                id = "m4",
                name = "Pasta Carbonara",
                description = "Creamy pasta with bacon",
                price = 40.0,   // zł
                imageResId = android.R.drawable.ic_menu_camera,
                category = "Main"
            )
        )

        menuItems.add(
            MenuItem(
                id = "m5",
                name = "Chocolate Cake",
                description = "Rich chocolate dessert",
                price = 22.0,   // zł
                imageResId = android.R.drawable.ic_menu_gallery,
                category = "Dessert"
            )
        )

        menuItems.add(
            MenuItem(
                id = "m6",
                name = "Coffee",
                description = "Fresh brewed coffee",
                price = 10.0,   // zł
                imageResId = android.R.drawable.ic_menu_camera,
                category = "Drink"
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
