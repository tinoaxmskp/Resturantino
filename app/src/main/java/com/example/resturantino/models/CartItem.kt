package com.example.resturantino.models

data class CartItem(
    val menuItemId: String = "",
    val name: String = "",
    val quantity: Int = 0,
    val subtotal: Double = 0.0
)
