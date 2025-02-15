package com.example.tms_thesis_moroz.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tms_thesis_moroz.databinding.ItemFavoriteBinding

class FavoriteAdapter(
    var favorites : List<FavoriteQuote>
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int){
        val favorite = favorites[position]
        holder.bind(favorite)
    }

    override fun getItemCount(): Int = favorites.size

    class FavoriteViewHolder(private val binding: ItemFavoriteBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(favorite:FavoriteQuote){
            binding.favoriteName.text = favorite.name
            binding.favoriteRate.text = favorite.rate.toString()
        }
    }


    data class FavoriteQuote(val name: String, val rate: Double)

}
