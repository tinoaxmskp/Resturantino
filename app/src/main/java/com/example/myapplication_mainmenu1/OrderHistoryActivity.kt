package com.example.myapplication_mainmenu1

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class OrderHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val textView = TextView(this)
        textView.text = "Order History Activity\n\nThis is a placeholder for the Order History feature."
        textView.textSize = 18f
        textView.setPadding(32, 32, 32, 32)
        setContentView(textView)
    }
}

