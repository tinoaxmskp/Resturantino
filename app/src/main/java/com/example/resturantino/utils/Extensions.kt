package com.example.resturantino.utils

import android.widget.TextView

// Tiny extension example — useful for future small helpers
fun TextView.setTextIfNotEmpty(text: String?) {
    this.text = text ?: ""
}
