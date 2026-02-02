package com.example.myapplication_mainmenu1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class AdminMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)

        val btnRecipes = findViewById<android.view.View>(R.id.cardAdminRecipes)
        val btnLogout = findViewById<android.view.View>(R.id.cardAdminLogout)


        // Go to Admin Dashboard (recipe list)
        btnRecipes.setOnClickListener {
            startActivity(Intent(this, AdminDashboardActivity::class.java))
        }

        // Logout (same behavior as user home)
        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
