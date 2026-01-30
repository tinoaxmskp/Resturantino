package com.example.myapplication_mainmenu1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

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
            i.putExtra("recipe", recipe)
            startActivity(i)
        }

        rvRecipes.layoutManager = LinearLayoutManager(this)
        rvRecipes.adapter = adapter

        fabFavorites.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }

        loadSampleData()
    }


    private fun loadSampleData() {
        recipes.clear()

        recipes.add(
            Recipe(
                id = "r1",
                title = "Spaghetti Carbonara",
                description = "Classic Italian pasta",
                ingredients = listOf(
                    "Spaghetti",
                    "Eggs",
                    "Pancetta",
                    "Parmesan",
                    "Pepper"
                ).joinToString("\n• ", prefix = "• "),
                steps = listOf(
                    "Boil pasta",
                    "Cook pancetta",
                    "Mix eggs and cheese",
                    "Combine everything"
                ).joinToString("\n")
            )
        )

        recipes.add(
            Recipe(
                id = "r2",
                title = "Greek Salad",
                description = "Fresh and simple",
                ingredients = listOf(
                    "Tomatoes",
                    "Cucumber",
                    "Feta",
                    "Olives",
                    "Olive oil"
                ).joinToString("\n• ", prefix = "• "),
                steps = listOf(
                    "Chop vegetables",
                    "Mix together",
                    "Add feta and olive oil"
                ).joinToString("\n")
            )
        )

        adapter.notifyDataSetChanged()
    }
}
