package com.example.myapplication_mainmenu1

import android.app.Application

class RecipeApp : Application() {

    var isAdmin: Boolean = false

    override fun onCreate() {
        super.onCreate()
        // Firebase auto-initialized
    }
}


