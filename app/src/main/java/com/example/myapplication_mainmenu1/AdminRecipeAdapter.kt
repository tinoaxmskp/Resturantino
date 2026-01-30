package com.example.myapplication_mainmenu1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdminRecipeAdapter(
    private val recipes: List<Recipe>,
    private val onEdit: (Recipe) -> Unit,
    private val onDelete: (Recipe) -> Unit
) : RecyclerView.Adapter<AdminRecipeAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_recipe, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val recipe = recipes[position]

        holder.tvTitle.text = recipe.title
        holder.tvDesc.text = recipe.description

        holder.btnEdit.setOnClickListener { onEdit(recipe) }
        holder.btnDelete.setOnClickListener { onDelete(recipe) }
    }

    override fun getItemCount(): Int = recipes.size

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvRecipeTitle)
        val tvDesc: TextView = view.findViewById(R.id.tvRecipeDesc)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }
}

