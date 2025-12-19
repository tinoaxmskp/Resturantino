package com.example.myapplication_mainmenu1

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val id: String,
    val title: String,
    val description: String,
    val imageResId: Int, // for sample local images; replace with URL when using Firestore
    val ingredients: List<String>,
    val steps: List<String>
) : Parcelable
