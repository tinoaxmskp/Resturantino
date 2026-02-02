package com.example.myapplication_mainmenu1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore


class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddRecipe: FloatingActionButton
    private lateinit var adapter: AdminRecipeAdapter

    private val firestore = FirebaseFirestore.getInstance()
    private val recipes = mutableListOf<Recipe>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        recyclerView = findViewById(R.id.recyclerViewAdminRecipes)
        fabAddRecipe = findViewById(R.id.fabAddRecipe)

        adapter = AdminRecipeAdapter(
            recipes,
            onEdit = { recipe ->
                val intent = Intent(this, RecipeDetailActivity::class.java)
                intent.putExtra("recipeId", recipe.id)
                intent.putExtra("isEdit", true)
                startActivity(intent)
            },
            onDelete = { recipe ->
                confirmDelete(recipe)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fabAddRecipe.setOnClickListener {
            startActivity(Intent(this, AddRecipeActivity::class.java))
        }

        loadRecipes()
    }

    override fun onResume() {
        super.onResume()
        loadRecipes()
    }

    private fun loadRecipes() {
        firestore.collection("recipes")
            .get()
            .addOnSuccessListener { snapshot ->
                recipes.clear()

                for (doc in snapshot.documents) {
                    val recipe = Recipe(
                        id = doc.id,
                        title = doc.getString("name") ?: "",
                        description = doc.getString("description") ?: "",
                        price = doc.getDouble("price") ?: 0.0,
                        category = doc.getString("category") ?: "",
                        ingredients = doc.getString("ingredients") ?: ""
                    )
                    recipes.add(recipe)
                }

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load recipes", Toast.LENGTH_SHORT).show()
            }
    }


    private fun confirmDelete(recipe: Recipe) {
        AlertDialog.Builder(this)
            .setTitle("Delete Recipe")
            .setMessage("Are you sure you want to delete this recipe?")
            .setPositiveButton("Delete") { _, _ ->
                deleteRecipe(recipe)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteRecipe(recipe: Recipe) {
        FirebaseFirestore.getInstance()
            .collection("recipes")
            .document(recipe.id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Recipe deleted", Toast.LENGTH_SHORT).show()
                loadRecipes()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to delete recipe", Toast.LENGTH_SHORT).show()
            }
    }
}


