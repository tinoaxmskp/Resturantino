package com.example.myapplication_mainmenu1

import android.app.Application

class RecipeApp : Application() {
    
    // Shared ViewModel instance across activities
    val cartViewModel: CartViewModel by lazy {
        CartViewModel()
    }
    
    companion object {
        @Volatile
        private var instance: RecipeApp? = null
        
        fun getInstance(): RecipeApp {
            return instance ?: synchronized(this) {
                instance ?: RecipeApp().also { instance = it }
            }
        }
    }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        // Firebase is automatically initialized by google-services plugin
        // No need to call FirebaseApp.initializeApp() manually
    }
}

