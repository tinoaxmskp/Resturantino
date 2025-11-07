package com.example.resturantino.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.resturantino.models.CartItem

class CartViewModel : ViewModel() {
    private val _cartItems = MutableLiveData<List<CartItem>>(emptyList())
    val cartItems: LiveData<List<CartItem>> = _cartItems

    fun setCart(items: List<CartItem>) {
        _cartItems.value = items
    }
}
