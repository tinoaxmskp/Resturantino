package com.example.myapplication_mainmenu1

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore


class AddRecipeActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etDesc: EditText
    private lateinit var etPrice: EditText
    private lateinit var etCategory: EditText

    private lateinit var etIngredients: EditText
    private lateinit var btnSave: Button
    private lateinit var progress: ProgressBar

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        etName = findViewById(R.id.etRecipeName)
        etDesc = findViewById(R.id.etRecipeDescription)
        etPrice = findViewById(R.id.etRecipePrice)
        etCategory = findViewById(R.id.etRecipeCategory)
        etIngredients = findViewById(R.id.etRecipeIngredients)
        btnSave = findViewById(R.id.btnAddRecipe)
        progress = findViewById(R.id.progressBar)

        btnSave.setOnClickListener { saveRecipe() }
    }

    private fun saveRecipe() {
        val name = etName.text.toString()
        val desc = etDesc.text.toString()
        val price = etPrice.text.toString().toDoubleOrNull()
        val category = etCategory.text.toString()
        val ingredients = etIngredients.text.toString()


        if (name.isEmpty() || desc.isEmpty() || price == null) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        progress.visibility = View.VISIBLE

        val recipe = mapOf(
            "name" to name,
            "description" to desc,
            "ingredients" to ingredients,
            "price" to price,
            "category" to category,
            "isActive" to true,
            "createdAt" to Timestamp.now()
        )

        FirebaseFirestore.getInstance()
            .collection("recipes")
            .add(recipe)
            .addOnSuccessListener {
                Toast.makeText(this, "Recipe added", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                progress.visibility = View.GONE
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
    }
}
