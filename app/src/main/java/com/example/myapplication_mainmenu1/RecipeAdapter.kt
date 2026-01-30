package com.example.myapplication_mainmenu1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide




class RecipeAdapter(
    private val items: List<Recipe>,
    private val onClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val tvRecipeTitle: TextView = view.findViewById(R.id.tvRecipeTitle)
        private val tvRecipeDesc: TextView = view.findViewById(R.id.tvRecipeDesc)
        private val ivThumb: ImageView = view.findViewById(R.id.ivThumb)


        fun bind(r: Recipe) {
            tvRecipeTitle.text = r.title
            tvRecipeDesc.text = r.description

            when {
                // 🔥 Firebase image
                !r.imageUrl.isNullOrEmpty() -> {
                    Glide.with(ivThumb.context)
                        .load(r.imageUrl)
                        .placeholder(R.drawable.placeholder) // optional
                        .into(ivThumb)
                }

            }
            itemView.setOnClickListener { onClick(r) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])
    override fun getItemCount(): Int = items.size
}
