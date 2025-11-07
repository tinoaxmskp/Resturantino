package com.example.resturantino.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.resturantino.models.MenuItem

// Minimal ViewModel scaffold — populate with real logic later
class MenuViewModel : ViewModel() {
    private val _menuItems = MutableLiveData<List<MenuItem>>(emptyList())
    val menuItems: LiveData<List<MenuItem>> = _menuItems

    // placeholder method to set items (used later)
    fun setMenu(items: List<MenuItem>) {
        _menuItems.value = items
    }
}
