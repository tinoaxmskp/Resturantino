package com.example.resturantino.models

data class Order(
    val id: String = "",
    val userId: String = "",
    val items: List<CartItem> = emptyList(),
    val total: Double = 0.0,
    val createdAt: Long = 0L
)
