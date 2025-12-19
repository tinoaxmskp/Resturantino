package com.example.myapplication_mainmenu1

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FavoritesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val textView = TextView(this)
        textView.text = "Favorites Activity\n\nThis is a placeholder for the Favorites feature."
        textView.textSize = 18f
        textView.setPadding(32, 32, 32, 32)
        setContentView(textView)
    }
}

