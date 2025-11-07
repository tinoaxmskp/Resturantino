package com.example.resturantino.ui.recipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.resturantino.R
import com.example.resturantino.models.Recipe

//  Adapter for recipes
class RecipeAdapter(private var items: List<Recipe> = emptyList()) :
    RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.itemRecipeTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleText.text = items[position].title
    }

    fun setItems(newItems: List<Recipe>) {
        items = newItems
        notifyDataSetChanged()
    }
}
