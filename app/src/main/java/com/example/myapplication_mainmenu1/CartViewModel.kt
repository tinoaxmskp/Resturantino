package com.example.myapplication_mainmenu1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CartViewModel : ViewModel() {
    
    private val _cartItems = MutableLiveData<MutableList<CartItem>>(mutableListOf())
    val cartItems: LiveData<MutableList<CartItem>> = _cartItems
    
    private val _cartItemCount = MutableLiveData<Int>(0)
    val cartItemCount: LiveData<Int> = _cartItemCount
    
    private val _totalPrice = MutableLiveData<Double>(0.0)
    val totalPrice: LiveData<Double> = _totalPrice
    
    init {
        updateCartStats()
    }
    
    /**
     * Add item to cart or increase quantity if item already exists
     * Returns true if item was added, false if quantity was increased
     */
    fun addToCart(menuItem: MenuItem): Boolean {
        val currentItems = _cartItems.value ?: mutableListOf()
        val existingItemIndex = currentItems.indexOfFirst { it.id == menuItem.id }
        
        return if (existingItemIndex >= 0) {
            // Item exists, increase quantity
            currentItems[existingItemIndex].quantity++
            _cartItems.value = currentItems
            updateCartStats()
            false // Quantity increased
        } else {
            // New item, add to cart
            val cartItem = CartItem(
                id = menuItem.id,
                name = menuItem.name,
                price = menuItem.price,
                quantity = 1,
                imageResId = menuItem.imageResId
            )
            currentItems.add(cartItem)
            _cartItems.value = currentItems
            updateCartStats()
            true // New item added
        }
    }
    
    fun removeFromCart(itemId: String) {
        val currentItems = _cartItems.value ?: mutableListOf()
        currentItems.removeAll { it.id == itemId }
        _cartItems.value = currentItems
        updateCartStats()
    }
    
    fun updateQuantity(itemId: String, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(itemId)
            return
        }
        
        val currentItems = _cartItems.value ?: mutableListOf()
        val item = currentItems.find { it.id == itemId }
        item?.let {
            it.quantity = newQuantity
            _cartItems.value = currentItems
            updateCartStats()
        }
    }
    
    fun clearCart() {
        _cartItems.value = mutableListOf()
        updateCartStats()
    }
    
    private fun updateCartStats() {
        val items = _cartItems.value ?: mutableListOf()
        _cartItemCount.value = items.sumOf { it.quantity }
        _totalPrice.value = items.sumOf { it.price * it.quantity }
    }
    
    fun getItemCount(): Int {
        return _cartItemCount.value ?: 0
    }
    
    fun getTotalPrice(): Double {
        return _totalPrice.value ?: 0.0
    }
}

