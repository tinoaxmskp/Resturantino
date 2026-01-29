package com.example.myapplication_mainmenu1

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class AdminRecipeAdapter(
    private val recipes: List<Recipe>,
    private val onEdit: (Recipe) -> Unit,
    private val onDelete: (Recipe) -> Unit
) : RecyclerView.Adapter<AdminRecipeAdapter.VH>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VH {
        TODO("Not yet implemented")
    }

     class VH(view: View) : RecyclerView.ViewHolder(view) {
        val btnEdit = view.findViewById<Button>(R.id.btnEdit)
        val btnDelete = view.findViewById<Button>(R.id.btnDelete)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val recipe = recipes[position]
        holder.btnEdit.setOnClickListener { onEdit(recipe) }
        holder.btnDelete.setOnClickListener { onDelete(recipe) }
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}
