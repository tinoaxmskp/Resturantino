package com.example.myapplication_mainmenu1

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var roleSpinner: Spinner
    private lateinit var registerButton: Button
    private lateinit var loginLink: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        emailInput = findViewById(R.id.inputEmail)
        passwordInput = findViewById(R.id.inputPassword)
        confirmPasswordInput = findViewById(R.id.inputConfirmPassword)
        roleSpinner = findViewById(R.id.spinnerRole)
        registerButton = findViewById(R.id.buttonRegister)
        loginLink = findViewById(R.id.textLogin)
        progressBar = findViewById(R.id.progressBar)

        // Simple role selector (User / Admin)
        val roles = listOf("User", "Admin")
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            roles
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        roleSpinner.adapter = adapter

        registerButton.setOnClickListener { attemptRegister() }
        loginLink.setOnClickListener {
            finish()
        }
    }

    private fun attemptRegister() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString()
        val confirmPassword = confirmPasswordInput.text.toString()
        val selectedRole = roleSpinner.selectedItem?.toString() ?: "User"

        if (!validateInputs(email, password, confirmPassword)) return

        setLoading(true)

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user
                if (user == null) {
                    showError("Registration failed. Please try again.")
                    setLoading(false)
                    return@addOnSuccessListener
                }

                val roleValue = if (selectedRole.equals("Admin", ignoreCase = true)) {
                    "admin"
                } else {
                    "user"
                }

                // Save basic profile (including account type) in Firestore
                val userProfile = hashMapOf(
                    "email" to email,
                    "role" to roleValue,
                    "createdAt" to System.currentTimeMillis()
                )

                firestore.collection("users")
                    .document(user.uid)
                    .set(userProfile)
                    .addOnSuccessListener {
                        setLoading(false)
                        Toast.makeText(
                            this,
                            "Account created successfully! Please log in.",
                            Toast.LENGTH_LONG
                        ).show()
                        // Go back to login screen
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener { e ->
                        setLoading(false)
                        showError(
                            "Account created, but failed to save profile: " +
                                (e.localizedMessage ?: "Unknown error")
                        )
                    }
            }
            .addOnFailureListener { e ->
                setLoading(false)
                val message = when {
                    e.localizedMessage?.contains("email address is already in use", true) == true ->
                        "This email is already registered. Try logging in instead."
                    else -> e.localizedMessage ?: "Registration failed. Please try again."
                }
                showError(message)
            }
    }

    private fun validateInputs(
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (email.isEmpty()) {
            emailInput.error = "Email is required"
            emailInput.requestFocus()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.error = "Please enter a valid email"
            emailInput.requestFocus()
            return false
        }
        if (!isStrongPassword(password)) {
            passwordInput.error = "Password must be at least 8 characters and contain letters and numbers"
            passwordInput.requestFocus()
            return false
        }
        if (password != confirmPassword) {
            confirmPasswordInput.error = "Passwords do not match"
            confirmPasswordInput.requestFocus()
            return false
        }
        return true
    }

    private fun isStrongPassword(password: String): Boolean {
        if (password.length < 8) return false
        var hasLetter = false
        var hasDigit = false
        password.forEach { ch ->
            if (ch.isLetter()) hasLetter = true
            if (ch.isDigit()) hasDigit = true
        }
        return hasLetter && hasDigit
    }

    private fun setLoading(loading: Boolean) {
        progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        registerButton.isEnabled = !loading
        emailInput.isEnabled = !loading
        passwordInput.isEnabled = !loading
        confirmPasswordInput.isEnabled = !loading
        roleSpinner.isEnabled = !loading
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}


