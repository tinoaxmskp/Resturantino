package com.example.myapplication_mainmenu1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map



class CartViewModel(
    private val repository: CartRepository
) : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartItem>>(repository.items)
    val cartItems: LiveData<List<CartItem>> = _cartItems

    private val _totalPrice = MutableLiveData<Double>(0.0)
    val totalPrice: LiveData<Double> = _totalPrice

    val cartItemCount: LiveData<Int> = cartItems.map { items ->
        items.sumOf { it.quantity }
    }


    fun getItemCount(): Int {
        return cartItems.value?.sumOf { it.quantity } ?: 0
    }

    fun addToCart(item: MenuItem) {
        val existing = repository.items.find { it.id == item.id }
        if (existing != null) {
            existing.quantity++
        } else {
            repository.items.add(
                CartItem(
                    id = item.id,
                    name = item.name,
                    price = item.price,
                    quantity = 1
                )
            )
        }
        notifyChanges()
    }

    fun updateQuantity(id: String, quantity: Int) {
        repository.items.find { it.id == id }?.quantity = quantity
        notifyChanges()
    }

    fun removeFromCart(id: String) {
        repository.items.removeAll { it.id == id }
        notifyChanges()
    }

    fun clearCart() {
        repository.items.clear()
        notifyChanges()
    }

    private fun notifyChanges() {
        _cartItems.value = repository.items.toList()
        _totalPrice.value = repository.items.sumOf { it.price * it.quantity }
    }
}

