package com.example.myapplication_mainmenu1

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class RecipeDetailActivity : AppCompatActivity() {

    private var recipe: Recipe? = null
    private val PREFS = "recipes_prefs"
    private val KEY_FAVS = "favorites" // store set of recipe ids
    private lateinit var ivRecipe: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvDesc: TextView
    private lateinit var tvIngredients: TextView
    private lateinit var tvSteps: TextView
    private lateinit var btnSave: MaterialButton
    private lateinit var collapsingToolbar: CollapsingToolbarLayout
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        toolbar = findViewById(R.id.toolbar)
        collapsingToolbar = findViewById(R.id.collapsingToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        ivRecipe = findViewById(R.id.ivRecipe)
        tvTitle = findViewById(R.id.tvTitle)
        tvDesc = findViewById(R.id.tvDesc)
        tvIngredients = findViewById(R.id.tvIngredients)
        tvSteps = findViewById(R.id.tvSteps)
        btnSave = findViewById(R.id.btnSave)

        recipe = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("recipe", Recipe::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("recipe")
        }
        recipe?.let { r ->
            ivRecipe.setImageResource(r.imageResId)
            collapsingToolbar.title = r.title
            tvTitle.text = r.title
            tvDesc.text = r.description
            tvIngredients.text = r.ingredients.joinToString("\n") { "• $it" }
            tvSteps.text = r.steps.mapIndexed { i, s -> "${i+1}. $s" }.joinToString("\n\n")
        }

        btnSave.setOnClickListener {
            recipe?.let { r ->
                val prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                val set = prefs.getStringSet(KEY_FAVS, mutableSetOf())?.toMutableSet() ?: mutableSetOf()
                if (set.contains(r.id)) {
                    set.remove(r.id)
                    btnSave.text = getString(R.string.save_recipe)
                    Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
                } else {
                    set.add(r.id)
                    btnSave.text = "Remove from Favorites"
                    Toast.makeText(this, "Saved to favorites", Toast.LENGTH_SHORT).show()
                }
                prefs.edit().putStringSet(KEY_FAVS, set).apply()
            }
        }
    }
}
