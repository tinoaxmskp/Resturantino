package com.example.myapplication_mainmenu1

import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException

class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var tvTitle: TextView
    private lateinit var tvDesc: TextView
    private lateinit var tvIngredients: TextView
    private lateinit var tvPrice: TextView
    private lateinit var btnSave: MaterialButton

    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var recipeId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        // 🔹 Bind views
        toolbar = findViewById(R.id.toolbar)
        tvTitle = findViewById(R.id.tvTitle)
        tvDesc = findViewById(R.id.tvDesc)
        tvIngredients = findViewById(R.id.tvIngredients)
        tvPrice = findViewById(R.id.tvPrice)
        btnSave = findViewById(R.id.btnSaveRecipe)

        // 🔹 Toolbar setup
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        // 🔒 LOCK FIELDS BY DEFAULT
        lockFields()

        // 🔹 Load recipe (Parcelable OR Firestore)
        val recipe: Recipe? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("recipe", Recipe::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("recipe")
        }

        if (recipe != null) {
            showRecipe(recipe)
        } else {
            recipeId = intent.getStringExtra("recipeId") ?: return
            loadRecipe()
        }

        // 🔹 Role check
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
            .addOnSuccessListener {
                val role = it.getString("role") ?: "user"
                if (role == "admin") {
                    enableAdminMode()
                } else {
                    enableUserMode()
                }
            }
    }

    // 🔒 LOCK EVERYTHING (DEFAULT)
    private fun lockFields() {
        listOf(tvTitle, tvDesc, tvIngredients, tvPrice).forEach {
            it.isEnabled = false
            it.isFocusable = false
            it.isFocusableInTouchMode = false
            it.isCursorVisible = false
        }
    }

    // 👤 USER MODE → TXT DOWNLOAD ONLY
    private fun enableUserMode() {
        btnSave.visibility = View.VISIBLE
        btnSave.text = getString(R.string.save_recipe)

        btnSave.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveRecipeAsTxt()
            } else {
                Toast.makeText(this, "Android version not supported", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 👑 ADMIN MODE → FIREBASE EDIT
    private fun enableAdminMode() {
        btnSave.visibility = View.VISIBLE
        btnSave.text = getString(R.string.save_changes)

        listOf(tvTitle, tvDesc, tvIngredients, tvPrice).forEach {
            it.isEnabled = true
            it.isFocusableInTouchMode = true
            it.isCursorVisible = true
        }

        btnSave.setOnClickListener {
            saveChanges()
        }
    }

    // 🔹 Display recipe (Parcelable)
    private fun showRecipe(recipe: Recipe) {
        tvTitle.text = recipe.title
        tvDesc.text = recipe.description
        tvIngredients.text = recipe.ingredients
        tvPrice.text = recipe.price.toString()
        toolbar.title = recipe.title
    }

    // 🔹 Load recipe from Firestore
    private fun loadRecipe() {
        firestore.collection("recipes")
            .document(recipeId)
            .get()
            .addOnSuccessListener { doc ->
                tvTitle.text = doc.getString("name")
                tvDesc.text = doc.getString("description") ?: ""
                tvIngredients.text = doc.getString("ingredients") ?: ""
                tvPrice.text = doc.getDouble("price")?.toString() ?: "0.0"
                toolbar.title = doc.getString("name")
            }
    }

    // 👑 ADMIN SAVE → FIRESTORE
    private fun saveChanges() {
        val updates = mapOf(
            "name" to tvTitle.text.toString(),
            "description" to tvDesc.text.toString(),
            "ingredients" to tvIngredients.text.toString(),
            "price" to tvPrice.text.toString().toDoubleOrNull()
        )

        firestore.collection("recipes")
            .document(recipeId)
            .update(updates)
            .addOnSuccessListener {
                Toast.makeText(this, "Recipe updated", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save changes", Toast.LENGTH_SHORT).show()
            }
    }

    // 👤 USER SAVE → TXT FILE
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveRecipeAsTxt() {
        val content = """
${tvTitle.text}

Price: ${tvPrice.text}

Ingredients:
${tvIngredients.text}

Description:
${tvDesc.text}
""".trimIndent()

        val fileName = "${tvTitle.text}.txt"

        try {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val uri = contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                values
            ) ?: throw IOException("File creation failed")

            contentResolver.openOutputStream(uri)?.use {
                it.write(content.toByteArray())
            }

            Toast.makeText(this, "Recipe saved to Downloads", Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
            Toast.makeText(this, "Failed to save recipe", Toast.LENGTH_SHORT).show()
        }
    }
}


