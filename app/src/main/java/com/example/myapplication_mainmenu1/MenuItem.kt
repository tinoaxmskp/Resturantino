package com.example.myapplication_mainmenu1

data class MenuItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageResId: Int,
    val category: String = "General"
)

