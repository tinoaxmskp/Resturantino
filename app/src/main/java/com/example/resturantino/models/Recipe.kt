package com.example.resturantino.models

data class Recipe(
    val id: String = "",
    val title: String = "",
    val ingredients: List<String> = emptyList(),
    val steps: List<String> = emptyList(),
    val imageUrl: String = ""
)
