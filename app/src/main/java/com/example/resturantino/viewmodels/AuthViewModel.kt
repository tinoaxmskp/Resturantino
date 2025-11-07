package com.example.resturantino.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.resturantino.models.User

// Small auth ViewModel stub — can hold current user state later
class AuthViewModel : ViewModel() {
    private val _currentUser = MutableLiveData<User?>(null)
    val currentUser: LiveData<User?> = _currentUser

    fun setUser(user: User?) {
        _currentUser.value = user
    }
}
