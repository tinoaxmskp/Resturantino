package com.example.myapplication_mainmenu1

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var registerLink: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        emailInput = findViewById(R.id.inputEmail)
        passwordInput = findViewById(R.id.inputPassword)
        loginButton = findViewById(R.id.buttonLogin)
        registerLink = findViewById(R.id.textRegister)
        progressBar = findViewById(R.id.progressBar)

        loginButton.setOnClickListener { attemptLogin() }
        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun attemptLogin() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString()

        if (!validateInputs(email, password)) return

        setLoading(true)

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user
                if (user == null) {
                    showError("Login failed. Please try again.")
                    setLoading(false)
                    return@addOnSuccessListener
                }

                // Fetch user role from Firestore
                firestore.collection("users")
                    .document(user.uid)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        val role = snapshot.getString("role") ?: "user"
                        Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show()
                        navigateToRole(role)
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this,
                            "Logged in, but could not load profile. Opening home.",
                            Toast.LENGTH_SHORT
                        ).show()
                        navigateToRole("user")
                    }
            }
            .addOnFailureListener { e ->
                setLoading(false)
                val message = when {
                    password.length < 6 ->
                        "Incorrect password or account does not exist."
                    else -> e.localizedMessage ?: "Login failed. Please check your credentials."
                }
                showError(message)
            }
    }

    private fun validateInputs(email: String, password: String): Boolean {
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
        if (password.isEmpty()) {
            passwordInput.error = "Password is required"
            passwordInput.requestFocus()
            return false
        }
        return true
    }

    private fun navigateToRole(role: String) {
        setLoading(false)
        val target = if (role.equals("admin", ignoreCase = true)) {
            AdminDashboardActivity::class.java
        } else {
            HomeActivity::class.java
        }
        startActivity(Intent(this, target))
        finish()
    }

    private fun setLoading(loading: Boolean) {
        progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        loginButton.isEnabled = !loading
        emailInput.isEnabled = !loading
        passwordInput.isEnabled = !loading
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}


