package com.example.myapplication_mainmenu1

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage



class AddRecipeActivity : AppCompatActivity() {
    private lateinit var ivRecipeImage: ImageView
    private lateinit var etName: EditText
    private lateinit var etDescription: EditText
    private lateinit var etPrice: EditText
    private lateinit var etCategory: EditText
    private lateinit var btnPickImage: Button
    private lateinit var btnSave: Button
    private lateinit var ivPreview: ImageView
    private lateinit var progressBar: ProgressBar
    private var imageUri: Uri? = null
    private var selectedImageUri: Uri? = null

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        etName = findViewById(R.id.etRecipeName)
        etDescription = findViewById(R.id.etRecipeDescription)
        etPrice = findViewById(R.id.etRecipePrice)
        etCategory = findViewById(R.id.etRecipeCategory)
        btnPickImage = findViewById(R.id.btnPickImage)
        btnSave = findViewById(R.id.btnSaveRecipe)
        ivPreview = findViewById(R.id.ivRecipeImage)
        progressBar = findViewById(R.id.progressBar)

        btnPickImage.setOnClickListener {
            imagePicker.launch("image/*")
        }

        btnSave.setOnClickListener {
            saveRecipe()
        }
    }

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                selectedImageUri = uri
                ivRecipeImage.setImageURI(uri)
            }
        }


    private fun saveRecipe() {
        val name = etName.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val priceText = etPrice.text.toString().trim()
        val category = etCategory.text.toString().trim()

        if (name.isEmpty() || description.isEmpty() || priceText.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (imageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        val price = priceText.toDoubleOrNull()
        if (price == null) {
            Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = ProgressBar.VISIBLE
        btnSave.isEnabled = false

        val imageRef = storage.reference
            .child("recipes/${System.currentTimeMillis()}.jpg")

        imageRef.putFile(imageUri!!)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                imageRef.downloadUrl
            }
            .addOnSuccessListener { downloadUrl ->

                val recipe = mapOf(
                    "name" to name,
                    "description" to description,
                    "price" to price,
                    "category" to category,
                    "imageUrl" to downloadUrl.toString()
                )

                firestore.collection("recipes")
                    .add(recipe)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Recipe added successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to save recipe", Toast.LENGTH_SHORT).show()
                        resetUi()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
                resetUi()
            }
    }

    private fun resetUi() {
        progressBar.visibility = ProgressBar.GONE
        btnSave.isEnabled = true
    }
}
