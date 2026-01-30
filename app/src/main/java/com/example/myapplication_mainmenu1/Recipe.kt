package com.example.myapplication_mainmenu1

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val category: String = "",
    val ingredients: String = "",
    val steps: String = "",
    val imageUrl: String? = null   // Firebase Storage
) : Parcelable

