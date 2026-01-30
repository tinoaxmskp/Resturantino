package com.example.myapplication_mainmenu1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.myapplication_mainmenu1.AdminMainActivity

class LauncherActivity : AppCompatActivity() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Simple empty content view; this activity only routes users
        setContentView(R.layout.activity_launcher)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // Not logged in – go to login screen
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            // Logged in – determine role and navigate
            firestore.collection("users")
                .document(currentUser.uid)
                .get()
                .addOnSuccessListener { snapshot ->
                    val role = snapshot.getString("role") ?: "user"
                    navigateToRole(role)
                }
                .addOnFailureListener {
                    // On failure, default to user home
                    navigateToRole("user")
                }
        }
    }

    private fun navigateToRole(role: String) {
        val target = if (role.equals("admin", ignoreCase = true)) {
            AdminDashboardActivity::class.java
        } else {
            HomeActivity::class.java
        }
        startActivity(Intent(this, target))
        finish()
    }
}


