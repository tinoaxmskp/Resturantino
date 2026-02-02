package com.example.myapplication_mainmenu1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore


class RecipeListActivity : AppCompatActivity() {

    private lateinit var adapter: RecipeAdapter
    private val recipes = mutableListOf<Recipe>()
    private lateinit var rvRecipes: RecyclerView
    private lateinit var fabFavorites: ExtendedFloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        rvRecipes = findViewById(R.id.rvRecipes)
        fabFavorites = findViewById(R.id.fabFavorites)

        adapter = RecipeAdapter(recipes) { recipe ->
            val i = Intent(this, RecipeDetailActivity::class.java)
            i.putExtra("recipe", recipe)   // ✅ sample data path
            startActivity(i)
        }
        fun loadRecipes() {
            FirebaseFirestore.getInstance()
                .collection("recipes")
                .whereEqualTo("isActive", true)
                .get()
                .addOnSuccessListener { snapshot ->
                    recipes.clear()

                    for (doc in snapshot.documents) {
                        val recipe = Recipe(
                            id = doc.id,
                            title = doc.getString("name") ?: continue,
                            description = doc.getString("description") ?: "",
                            ingredients = doc.getString("ingredients") ?: "",
                            price = doc.getDouble("price") ?: 0.0,
                            category = doc.getString("category") ?: ""
                        )
                        recipes.add(recipe)
                    }

                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to load recipes", Toast.LENGTH_SHORT).show()
                }
        }

        rvRecipes.layoutManager = LinearLayoutManager(this)
        rvRecipes.adapter = adapter

        loadRecipes()

        fabFavorites.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }
    }

}
