package com.example.myapplication_mainmenu1

import android.app.Application

class RecipeApp : Application() {
    val cartRepository by lazy { CartRepository() }
}




