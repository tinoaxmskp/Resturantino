package com.example.myapplication_mainmenu1

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore



class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var ivRecipe: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvDesc: TextView
    private lateinit var tvIngredients: TextView
    private lateinit var tvSteps: TextView
    private lateinit var btnSave: MaterialButton

    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var recipeId: String
    private var isAdmin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        toolbar = findViewById(R.id.toolbar)
        ivRecipe = findViewById(R.id.ivRecipe)
        tvTitle = findViewById(R.id.tvTitle)
        tvDesc = findViewById(R.id.tvDesc)
        tvIngredients = findViewById(R.id.tvIngredients)
        tvSteps = findViewById(R.id.tvSteps)
        btnSave = findViewById(R.id.btnSave)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        recipeId = intent.getStringExtra("recipeId") ?: return
        isAdmin = (application as RecipeApp).isAdmin

        loadRecipe()

        if (isAdmin) {
            enableAdminMode()
        }
    }

    private fun loadRecipe() {
        firestore.collection("recipes")
            .document(recipeId)
            .get()
            .addOnSuccessListener { doc ->
                tvTitle.text = doc.getString("name")
                tvDesc.text = doc.getString("description")
                tvIngredients.text = doc.getString("ingredients") ?: "N/A"
                tvSteps.text = doc.getString("steps") ?: "N/A"
                toolbar.title = doc.getString("name")
            }
    }

    private fun enableAdminMode() {
        btnSave.visibility = View.VISIBLE

        tvTitle.isEnabled = true
        tvDesc.isEnabled = true
        tvIngredients.isEnabled = true
        tvSteps.isEnabled = true

        btnSave.setOnClickListener {
            saveChanges()
        }
    }

    private fun saveChanges() {
        val updates = mapOf(
            "name" to tvTitle.text.toString(),
            "description" to tvDesc.text.toString(),
            "ingredients" to tvIngredients.text.toString(),
            "steps" to tvSteps.text.toString()
        )

        firestore.collection("recipes")
            .document(recipeId)
            .update(updates)
            .addOnSuccessListener {
                Toast.makeText(this, "Recipe updated", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save changes", Toast.LENGTH_SHORT).show()
                finish()
            }
    }
}

