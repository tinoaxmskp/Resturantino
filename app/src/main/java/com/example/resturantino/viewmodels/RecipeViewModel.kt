package com.example.resturantino.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.resturantino.models.Recipe

class RecipeViewModel : ViewModel() {
    private val _recipes = MutableLiveData<List<Recipe>>(emptyList())
    val recipes: LiveData<List<Recipe>> = _recipes

    fun setRecipes(list: List<Recipe>) {
        _recipes.value = list
    }
}
